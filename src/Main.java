import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {
	
	/**
	 * All fixtures currently allocated for (<code>null</code> if they don't currently map to a fixture)
	 */
	public static Fixture[] fixtures;
	
	/**
	 * Values for each channel on DMX
	 * TODO: Refactor this to type byte
	 */
	public static int[] dmxChannels;
	
	/**
	 * List of all faders connected
	 */
	public static Fader[] faders;
	
	private static DMXWriter dmxWriter;
	private static FixtureManager fixtureManager;
	private static FaderManager faderManager;

	private static FixtureTemplate[] fixtureTemplates;
	
	public static void main(String[] args) {
		try {
			setPreferences();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		dmxWriter = new DMXWriter();
		
		Ini ini = null;
		try {
			ini = new Ini(new File(Constants.PREFERENCES_PATH));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		dmxChannels = new int[Constants.DMX_CHANNEL_COUNT]; //Auto initialised to 0
		faders = new Fader[Constants.FADER_COUNT];
		fixtures = new Fixture[Constants.FIXTURE_COUNT];
		setFixtures(ini);
		setFaders(ini);
		
		synchronized(dmxWriter) {
			dmxWriter = new DMXWriter();
			getDmxWriter().start();
			
			fixtureManager = new FixtureManager();
			fixtureManager.start();
			
			faderManager = new FaderManager();
			faderManager.start();
		}
		
		while(!Thread.interrupted()) {
			long start = System.nanoTime();
			
			
			
			long time = System.nanoTime() - start;
			if(time > Constants.MAIN_LOOP_PERIOD_NANOS)
				onError(ErrorType.MAIN_LOOP_NOT_UPDATED_FAST_ENOUGH, time + "ns > " + Constants.MAIN_LOOP_PERIOD_NANOS + "ns");
		}
	}

	private static void setFixtures(Ini ini) {
		JSONArray fixtureTemplatesJSON = null;
		JSONArray fixturesJSON = null;
		
		try {
			fixtureTemplatesJSON = (JSONArray) new JSONParser().parse(ini.get("fixture-templates").get("array"));
			fixturesJSON = (JSONArray) new JSONParser().parse(ini.get("fixtures").get("array"));
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int index = 0;
		
		for(Object fixtureTemplateJSONRaw: fixtureTemplatesJSON) {
			
			JSONObject fixtureTemplateJSON;
			
			if(fixtureTemplateJSONRaw instanceof JSONObject)
				fixtureTemplateJSON = (JSONObject) fixtureTemplateJSONRaw;
			else
				throw new IllegalArgumentException("Fixture template not a valid JSON object!");

			if(fixtureTemplates == null)
				fixtureTemplates = new FixtureTemplate[1];
			else
				fixtureTemplates = Arrays.copyOf(fixtureTemplates, fixtureTemplates.length + 1);
			
			fixtureTemplates[Math.toIntExact((long) fixtureTemplateJSON.get("uid"))]
					= new FixtureTemplate(
										(String) fixtureTemplateJSON.get("name"),
										Math.toIntExact((long) fixtureTemplateJSON.get("numChannels")),
										jsonArrayToStringArray((JSONArray) fixtureTemplateJSON.get("channelDescriptions")),
										Math.toIntExact((long) fixtureTemplateJSON.get("dimOffset"))
					);
			index++;
		}
		
		index = 0;
		
		for(Object fixtureJSONRaw: fixturesJSON) {
			
			JSONObject fixtureJSON;
			
			if(fixtureJSONRaw instanceof JSONObject)
				fixtureJSON = (JSONObject) fixtureJSONRaw;
			else
				throw new IllegalArgumentException("Fixture not a valid JSON object!");
			
			fixtures[index] = new Fixture(
											fixtureTemplates[Math.toIntExact((long) fixtureJSON.get("templateUid"))],
											Math.toIntExact((long) fixtureJSON.get("firstChannel")),
											jsonArrayToIntArray((JSONArray) fixtureJSON.get("channelValues"))
							);
			
			index++;
		}
		
	}

	private static String[] jsonArrayToStringArray(JSONArray in) {
		int size = in.size();
		String[] out = new String[size];
		Object[] arr = in.toArray();
		size--;
		while(size > 0) {
			out[size] = (String) arr[size--];
		}
		
		return out;
	}
	
	private static int[] jsonArrayToIntArray(JSONArray in) {
		int size = in.size();
		int[] out = new int[size];
		Object[] arr = in.toArray();
		size--;
		while(size > 0) {
			if(arr[size--] instanceof String)
				out[size] = Integer.parseInt((String) arr[size--]);
			else
				out[size] = Math.toIntExact((long) arr[size--]);
		}
		
		return out;
	}

	private static void setFaders(Ini ini) {
		JSONArray fadersJson = null;
		
		try {
			fadersJson = (JSONArray) new JSONParser().parse(ini.get("faders").get("array"));
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int index = 0;
		
		for(Object faderJsonRaw: fadersJson.toArray()) {
			JSONObject faderJson;
			
			if(faderJsonRaw instanceof JSONObject)
				faderJson = (JSONObject) faderJsonRaw;
			else
				throw new IllegalArgumentException("Fader not a valid JSON object!");
					
			
			
			faders[index] = new Fader(
								getFixtures(jsonArrayToIntArray((JSONArray) (faderJson.get("fixtureIds")))),
								jsonArrayToIntArray((JSONArray) (faderJson.get("fixtureChannels"))),
								jsonArrayToIntArray((JSONArray) (faderJson.get("fixtureValues"))),
								Math.toIntExact((long) faderJson.get("value"))
							);
			
			index++;
		}
		
	}

	public static void onError(ErrorType type, String message) {
		//TODO: Implement this in the GUI
		System.err.println(type.toString() + ": " + message);
	}
	
	private static void setPreferences() throws InvalidFileFormatException, IOException{
		InputStream is = Main.class.getClassLoader().getResource("preferencesPath.txt").openStream();
		Constants.PREFERENCES_PATH = "";
		while(is.available() > 0)
			Constants.PREFERENCES_PATH += (char) is.read();
		
		Ini ini = new Ini(new File(Constants.PREFERENCES_PATH));
		Section preferences = ini.get("preferences");
		Section constants = ini.get("constants");

		Constants.DMX_CHANNEL_COUNT = Integer.parseInt(preferences.get("DMX_CHANNEL_COUNT"));
		Constants.FADER_COUNT = Integer.parseInt(preferences.get("FADER_COUNT"));
		Constants.FIXTURE_COUNT = Integer.parseInt(preferences.get("FIXTURE_COUNT"));
		Constants.PREFERENCES_PATH = preferences.get("PREFERENCES_PATH");
		Constants.MAIN_LOOP_FREQUENCY = Integer.parseInt(preferences.get("MAIN_LOOP_FREQUENCY"));
		Constants.FADER_UPDATE_FREQUENCY = Integer.parseInt(preferences.get("FADER_UPDATE_FREQUENCY"));
		Constants.FIXTURE_UPDATE_FREQUENCY = Integer.parseInt(preferences.get("FIXTURE_UPDATE_FREQUENCY"));
		Constants.DMX_DATA_FREQUENCY = Integer.parseInt(preferences.get("DMX_DATA_FREQUENCY"));
		

		Constants.SERIAL_COMM_BAUD_RATE = Integer.parseInt(constants.get("SERIAL_COMM_BAUD_RATE"));
		Constants.SERIAL_COMM_PORT_NAME = constants.get("SERIAL_COMM_PORT_NAME");
		
		Constants.init();
		Constants.printAll();
	}
	
	/**
	 * @return the {@link #dmxWriter}
	 */
	public static DMXWriter getDmxWriter() {
		return dmxWriter;
	}
	
	public static Fixture[] getFixtures(int[] indexes) {
		Fixture[] out = new Fixture[indexes.length];
		for(int i = 0; i < indexes.length; i++)
			out[i] = fixtures[indexes[i]];
		
		return out;
	}
}
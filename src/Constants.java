import java.lang.reflect.Field;

public class Constants {
	public static int SERIAL_COMM_BAUD_RATE = 2000000;
	public static String SERIAL_COMM_PORT_NAME = "COM1";
	/**
	 * The number of times per second the DMX data is transmitted to the fixtures
	 */
	public static int DMX_DATA_FREQUENCY = 20;
	public static long DMX_DATA_PERIOD_NANOS;
	public static int DMX_CHANNEL_COUNT = 512;
	
	public static int FIXTURE_UPDATE_FREQUENCY = 20;
	public static long FIXTURE_UPDATE_PERIOD_NANOS;
	
	public static int FADER_UPDATE_FREQUENCY = 20;
	public static long FADER_UPDATE_PERIOD_NANOS;
	
	public static int MAIN_LOOP_FREQUENCY = 100;
	public static long MAIN_LOOP_PERIOD_NANOS;
	
	public static int FADER_COUNT = 12;
	public static int FIXTURE_COUNT = 12;
	
	public static String PREFERENCES_PATH;
	
	public static void init() {
		DMX_DATA_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) DMX_DATA_FREQUENCY));
		FIXTURE_UPDATE_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) DMX_DATA_FREQUENCY));
		FADER_UPDATE_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) FADER_UPDATE_FREQUENCY));
		MAIN_LOOP_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) MAIN_LOOP_FREQUENCY));
	}
	
	public static void printAll() {
		System.out.print("Printing preferences\n\n");
		
		Field[] fields = Constants.class.getDeclaredFields();
		for(Field field: fields) {
			try {
				System.out.println(field.getName() + ": " + field.get(null).toString());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				//Ignore
			} catch(NullPointerException e) {
				System.out.println(field.getName() + ": null");
			}
		}
	}
	
	
}

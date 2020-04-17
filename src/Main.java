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
	
	public static void main(String[] args) {
		dmxChannels = new int[Constants.DMX_CHANNEL_COUNT]; //Auto initialised to 0
		faders = new Fader[Constants.FADER_COUNT];
		setFaders();
		fixtures = new Fixture[Constants.FIXTURE_COUNT];
		setFixtures();
		
		dmxWriter = new DMXWriter();
		getDmxWriter().start();
		
		fixtureManager = new FixtureManager();
		fixtureManager.start();
		
		faderManager = new FaderManager();
		faderManager.start();
		
		while(!Thread.interrupted()) {
			long start = System.nanoTime();
			
			
			
			long time = System.nanoTime() - start;
			if(time > Constants.MAIN_LOOP_PERIOD_NANOS)
				onError(ErrorType.MAIN_LOOP_NOT_UPDATED_FAST_ENOUGH, time + "ns > " + Constants.MAIN_LOOP_PERIOD_NANOS + "ns");
		}
	}

	private static void setFixtures() {
		// TODO Auto-generated method stub
		throw new IllegalStateException("Unwritten method stub!");
	}

	private static void setFaders() {
		// TODO Auto-generated method stub
		throw new IllegalStateException("Unwritten method stub!");
	}

	public static void onError(ErrorType type, String message) {
		//TODO: Implement this in the GUI
		System.err.println(type.toString() + ": " + message);
	}

	/**
	 * @return the {@link #dmxWriter}
	 */
	public static DMXWriter getDmxWriter() {
		return dmxWriter;
	}
}
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
	 * Values for each fader connected
	 */
	public static int[] faders;
	
	private static DMXWriter dmxWriter;
	private static FixtureManager fixtureManager;
	
	public static void main(String[] args) {
		dmxChannels = new int[Constants.DMX_CHANNEL_COUNT]; //Auto initialised to 0
		faders = new int[Constants.FADER_COUNT]; //Auto initialised to 0
		fixtures = new Fixture[Constants.FIXTURE_COUNT];
		
		//TODO: Remove this debug code.
		fixtures[0] = new Fixture(new FixtureTemplate("Test", 5, new String[] {"lol", "lel", "brightness", "amk", "anan"}, 2), 0, new int[5]);
		
		dmxWriter = new DMXWriter();
		getDmxWriter().start();
		

		fixtureManager = new FixtureManager();
		fixtureManager.start();
		
		while(!Thread.interrupted()) {
			long start = System.nanoTime();
			
			
			
			long time = System.nanoTime() - start;
			if(time > Constants.MAIN_LOOP_PERIOD_NANOS)
				onError(ErrorType.MAIN_LOOP_NOT_UPDATED_FAST_ENOUGH, time + "ns > " + Constants.MAIN_LOOP_PERIOD_NANOS + "ns");
		}
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
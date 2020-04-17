public class Constants {
	public static final int SERIAL_COMM_BAUD_RATE = 2000000;
	public static final String SERIAL_COMM_PORT_NAME = "COM1";
	/**
	 * The number of times per second the DMX data is transmitted to the fixtures
	 */
	public static final int DMX_DATA_FREQUENCY = 20;
	public static final long DMX_DATA_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) DMX_DATA_FREQUENCY));
	public static final int DMX_CHANNEL_COUNT = 512;
	
	public static final int FIXTURE_UPDATE_FREQUENCY = 20;
	public static final long FIXTURE_UPDATE_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) DMX_DATA_FREQUENCY));
	
	public static final int FADER_UPDATE_FREQUENCY = 20;
	public static final long FADER_UPDATE_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) FADER_UPDATE_FREQUENCY));
	
	public static final int MAIN_LOOP_FREQUENCY = 100;
	public static final long MAIN_LOOP_PERIOD_NANOS = (long) (1000 * 1000 * 1000 * ((double) 1 / (double) MAIN_LOOP_FREQUENCY));
	
	public static final int FADER_COUNT = 12;
	public static final int FIXTURE_COUNT = 12;
}

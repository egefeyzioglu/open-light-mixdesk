/**
 * Stores all information related to a specific fader.
 * 
 * @author Ege Feyzioglu
 *
 */
public class Fader {
	/**
	 * Value for this fader. Value is between 0x00 and 0xFF
	 */
	private int value;
	
	/**
	 * List of fixtures attached to this fader. See {@link #channelsForFixtures} for the channels of each fixture this fader controls
	 */
	private Fixture[] fixtures;
	
	/**
	 * List of values for each channel. {@link #value} is multiplied with this for each channel. Indexes are the same as {@link #fixtures}
	 */
	private int[] channelsForFixtures;
	
	/**
	 * List of which channel to control for each fixture. Indexes are the same as {@link #fixtures}
	 */
	private int[] valuesForFixtures;
	
	public Fader(Fixture[] fixtures, int[] channelsForFixtures, int[] valuesForFixtures, int value) {
		this.fixtures = fixtures;
		this.channelsForFixtures = channelsForFixtures;
		this.setValue(value);
	}
	
	/**
	 * Create new fader with a value of 0.
	 * 
	 * @param fixtures
	 * @param channelsForFixtures
	 */
	public Fader(Fixture[] fixtures, int[] channelsForFixtures, int[] valuesForFixtures) {
		this(fixtures, channelsForFixtures, valuesForFixtures, 0);
	}
	
	/**
	 * Create new fader with a single fixture attached and a value of 0.
	 * 
	 * @param fixture
	 * @param channel
	 */
	public Fader(Fixture fixture, int channel, int valueForFixture) {
		this(new Fixture[]{fixture}, new int[] {channel}, new int[] {valueForFixture}, 0);
	}
	
	/**
	 * Create new fader with a single fixture attached.
	 * 
	 * @param fixture
	 * @param channel
	 * @param value
	 */
	public Fader(Fixture fixture, int channel, int valueForFixture, int value) {
		this(new Fixture[]{fixture}, new int[] {channel}, new int[] {valueForFixture}, value);
	}

	/**
	 * @return the {@link #value}
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the {@link #value} to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the {@link #fixtures}
	 */
	public Fixture[] getFixtures() {
		return fixtures;
	}

	/**
	 * @return the {@link #channelsForFixtures}
	 */
	public int[] getChannelsForFixtures() {
		return channelsForFixtures;
	}
	
	/**
	 * @return the {@link #valuesForFixtures}
	 */
	public int[] getValuesForFixtures() {
		return valuesForFixtures;
	}

	public Fixture getFixture(int index) {
		return fixtures[index];
	}
	

	public int getValueForFixture(int index) {
		return valuesForFixtures[index];
	}
	
	public int getChannelForFixture(int index) {
		return channelsForFixtures[index];
	}
	
}

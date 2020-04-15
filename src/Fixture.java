/**
 * 
 * Stores the information related to a single fixture. Does not transmit anything.
 * 
 * @author Ege Feyzioglu
 *
 */

public class Fixture{
	/**
	 * The template this fixture is based on
	 */
	FixtureTemplate template;
	
	/**
	 * The ID of the first channel the fixture occupies
	 */
	private int firstChannel;

	/**
	 * Values to be sent for each DMX channel occupied
	 * TODO: Refactor this to type byte
	 */
	private int[] channelValues;
	
	

	

	/**
	 * @return the {@link #firstChannel}
	 */
	public int getFirstChannel() {
		return firstChannel;
	}

	

	/**
	 * @return the {@link #channelValues}
	 */
	public int[] getChannelValues() {
		return channelValues;
	}



	/**
	 * @param {@link template}
	 * @param {@link #firstChannel}
	 * @param {@link #channelValues}
	 */
	public Fixture(FixtureTemplate template, int firstChannel, int[] channelValues) {
		super();
		this.template = template;
		this.firstChannel = firstChannel;
		this.channelValues = channelValues;
	}
	
	public void setChannelValue(int channel, int value) {
		channelValues[channel] = value;
	}
	
	public void setChannelValues(int[] values) {
		if(values.length != template.getNumChannels())
			throw new IllegalArgumentException("Incorrect number of values provided! Channel count = " + template.getNumChannels() + ", provided array item count = " + values.length);
		System.arraycopy(values, 0, channelValues, 0, values.length);
	}
	
}

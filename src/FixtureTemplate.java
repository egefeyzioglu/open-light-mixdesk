import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FixtureTemplate implements Serializable {
	private static final long serialVersionUID = 2484657643944683420L;

	/**
	 * Human-friendly name for the fixture template
	 */
	private String name;
	
	/**
	 * Number of DMX channels this fixture occupies
	 */
	private int numChannels;
	
	/**
	 * Human-readable explanation for each DMX channel
	 */
	private String[] channelDescriptions;
	
	/**
	 * Offset of the channel controlling the brightness of the fixture, starting from  {@link Fixture#firstChannel} 
	 */
	private int dimOffset;
	
	/**
	 * @return the {@link #dimOffset}
	 */
	public int getDimOffset() {
		return dimOffset;
	}
	
	/**
	 * @return the {@link #channelDescriptions}
	 */
	public String[] getChannelDescriptions() {
		return channelDescriptions;
	}
	
	/**
	 * @return the {@link #numChannels}
	 */
	public int getNumChannels() {
		return numChannels;
	}
	
	/**
	 * @return the {@link #name}
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param {@link #name}
	 * @param {@link #numChannels}
	 * @param {@link #channelDescriptions}
	 * @param {@link #dimOffset}
	 */
	public FixtureTemplate(String name, int numChannels, String[] channelDescriptions, int dimOffset) {
		this.name = name;
		this.numChannels = numChannels;
		this.channelDescriptions = channelDescriptions;
		this.dimOffset = dimOffset;
	}
	
	public void save(File path) throws FileNotFoundException, IOException {
		ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path));
		stream.writeObject(this);
		stream.close();
	}
	
	public static FixtureTemplate read(File path) throws FileNotFoundException, IOException {
		ObjectInputStream stream = new ObjectInputStream(new FileInputStream(path));
		try {
			FixtureTemplate template = (FixtureTemplate) stream.readObject();
			stream.close();
			return template;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			stream.close();
			return null;
		}
	}
}

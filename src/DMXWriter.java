import java.io.IOException;


/**
 * 
 * Writes changes in the channel values to DMX. HAS TO be started BEFORE any thread changes the DMX values.
 * 
 * @author Ege Feyzioglu
 *
 */
public class DMXWriter extends Thread{
	
	public static int[] dmxChannelsOld;
	public TwoWaySerialComm dmxComm;
	
	public DMXWriter() {
		dmxChannelsOld = new int[Constants.DMX_CHANNEL_COUNT];
	}
	
	@Override
	public void run() {
		dmxComm = new TwoWaySerialComm(); 
		try {
			dmxComm.connect(Constants.SERIAL_COMM_PORT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		while(!Thread.interrupted()) {
			long start = System.nanoTime();
			for(int i = 0; i < Constants.DMX_CHANNEL_COUNT; i++) {
				int temp = Main.dmxChannels[i];
				if(temp != dmxChannelsOld[i]) {
					try {
						dmxComm.write(i + ":" + temp + "\n");
						dmxChannelsOld[i] = temp;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			long time = System.nanoTime() - start;
			if(time > Constants.DMX_DATA_PERIOD_NANOS)
				Main.onError(ErrorType.DMX_NOT_UPDATED_FAST_ENOUGH, time + "ns > " + Constants.DMX_DATA_PERIOD_NANOS + "ns");
		}
	}
}

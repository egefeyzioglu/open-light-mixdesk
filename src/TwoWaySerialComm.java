import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

public class TwoWaySerialComm {
	
	private InputStream in;
	private OutputStream out;
	
	private ArrayList<TwoWaySerialCommSubscriber> subscribers;
	
	public TwoWaySerialComm() {
		subscribers = new ArrayList<TwoWaySerialCommSubscriber>();
	}
	
	public void subscribe(TwoWaySerialCommSubscriber newSub) {
		subscribers.add(newSub);
	}
	
    /**
     * Connects to a serial port
     * 
     * @param portName
     * @throws Exception
     */
    void connect ( String portName ) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() ) {
            System.out.println("Error: Port is currently in use");
        }
        else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),9600);
            
            if ( commPort instanceof SerialPort ) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(Constants.SERIAL_COMM_BAUD_RATE,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
                
                in = serialPort.getInputStream();
                out = serialPort.getOutputStream();
                
                (new Thread(new SerialReader(in))).start();

            }
            else {
                System.out.println("Error: Only serial ports are handled");
            }
        }     
    }
    
    public void write(String msg) throws IOException {
    	for(char c: msg.toCharArray())
    		out.write(c);
    }
    
    private class SerialReader implements Runnable {
        InputStream in;
        
        String msg = "";
        
        public SerialReader ( InputStream in ) {
            this.in = in;
        }
        
        public void run () {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ( ( len = this.in.read(buffer)) > -1 ) {
                    msg += new String(buffer,0,len);
                    
                    if(msg.endsWith("\n")) {
                    	for(TwoWaySerialCommSubscriber subscriber: subscribers) {
                    		subscriber.onSerialData(msg);
                    	}
                    	msg = "";
                    }
                    
                }
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
            
        }
    }
}
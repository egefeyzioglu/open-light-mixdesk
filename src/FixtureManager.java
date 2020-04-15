public class FixtureManager extends Thread implements TwoWaySerialCommSubscriber{
	
	@Override
	public void run() {
		
		Main.getDmxWriter().dmxComm.subscribe(this);
		
		while(!Thread.interrupted()) {
			for(int i = 0; i < Constants.FIXTURE_COUNT; i++) {
				long start = System.nanoTime();
				
				Fixture fixture = Main.fixtures[i];
				
				if(fixture == null)
					continue;
				
				int first = fixture.getFirstChannel();
				int[] vals = fixture.getChannelValues();
				for(int j = 0; j < vals.length; j++) {
					Main.dmxChannels[first + j] = vals[j];
				}
				
				long time = System.nanoTime() - start;
				if(time > Constants.FIXTURE_UPDATE_PERIOD_NANOS)
					Main.onError(ErrorType.FIXTURES_NOT_UPDATED_FAST_ENOUGH, time + "ns > " + Constants.FIXTURE_UPDATE_PERIOD_NANOS + "ns");
			}
		}
	}
	
	@Override
	public void onSerialData(String data) {
		//TODO: Remove this debug code
		String[] lel = data.substring(0, data.length() - 1).split(":");
		Fixture fixture = Main.fixtures[Integer.parseInt(lel[0])];
		fixture.setChannelValue(fixture.template.getDimOffset(), Integer.parseInt(lel[1]));
	}
	
}

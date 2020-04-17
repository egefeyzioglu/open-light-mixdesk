public class FaderManager extends Thread implements TwoWaySerialCommSubscriber{
	
	public FaderManager() {
		//TODO: Remove this debug code.
		Main.getDmxWriter().dmxComm.subscribe(this);
	}
	
	@Override
	public void run() {
		while(!Thread.interrupted()) {
			for(int i = 0; i < Constants.FIXTURE_COUNT; i++) {
				long start = System.nanoTime();
				
				Fader fader = Main.faders[i];
				
				if(fader == null)
					continue;
				
				Fixture[] fixtures = fader.getFixtures();
				
				for(int j = 0; j < fixtures.length; j++) {
					Main.fixtures[j].setChannelValue(fader.getChannelForFixture(j), fader.getValue() * fader.getValueForFixture(j));
				}
				
				long time = System.nanoTime() - start;
				if(time > Constants.FADER_UPDATE_PERIOD_NANOS)
					Main.onError(ErrorType.FADERS_NOT_UPDATED_FAST_ENOUGH, time + "ns > " + Constants.FADER_UPDATE_PERIOD_NANOS + "ns");
			}
		}
	}
	

	@Override
	public void onSerialData(String data) {
		//TODO: Remove this debug code
		String[] lel = data.substring(0, data.length() - 1).split(":");
		Fader fader = Main.faders[Integer.parseInt(lel[0])];
		fader.setValue(Integer.parseInt(lel[1]));
	}
}

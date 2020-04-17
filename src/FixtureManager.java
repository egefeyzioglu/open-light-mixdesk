public class FixtureManager extends Thread{
	
	@Override
	public void run() {
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
}

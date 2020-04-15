public class Matrix {
	int[] map;
	
	public Matrix(int[] in, int[] out) {
		if(in.length != out.length) {
			throw new IllegalArgumentException("Unequal array lengths! (" + in.length + " != " + out.length + ")");
		}
		this.map = new int[getMax(in) + 1];
		for(int i = 0; i < map.length; i++) {
			map[in[i]] = out[i];
		}
	}
	
	public int getDMXChannel(int faderChannel) {
		return map[faderChannel];
	}
	
	private int getMax(final int[] arr) {
		int max = Integer.MIN_VALUE;
		for(int i: arr) {
			if(i > max)
				max = i;
		}
		return max;
	}
	
}

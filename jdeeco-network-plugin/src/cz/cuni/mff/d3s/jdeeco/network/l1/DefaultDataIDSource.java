package cz.cuni.mff.d3s.jdeeco.network.l1;

public class DefaultDataIDSource implements DataIDSource {
	
	private static DefaultDataIDSource instance;
	private static int COUNTER = 0;
	
	public synchronized DefaultDataIDSource getInstance() {
		if (instance == null) {
			instance = new DefaultDataIDSource();
		}
		return instance;
	}
	
	@Override
	public int createDataID() {
		COUNTER++;
		return COUNTER;
	}

}

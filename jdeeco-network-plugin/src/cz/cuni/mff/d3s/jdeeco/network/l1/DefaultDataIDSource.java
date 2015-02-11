package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * Singleton class providing default data ID source.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class DefaultDataIDSource implements DataIDSource {

	private static DefaultDataIDSource instance;
	private static int COUNTER = 0;

	public synchronized DefaultDataIDSource getInstance() {
		if (instance == null) {
			instance = new DefaultDataIDSource();
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.jdeeco.network.l1.DataIDSource#createDataID()
	 */
	@Override
	public int createDataID() {
		COUNTER++;
		return COUNTER;
	}

}

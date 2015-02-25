package cz.cuni.mff.d3s.jdeeco.network.l1;

/**
 * 
 * Whenever L2 packet is created the data ID field needs to be filled in by some (locally) unique value. This interface
 * provides the single method from which obtain (locally) unique data ID.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface DataIDSource {
	/**
	 * Creates (locally) unique data ID.
	 * 
	 * @return data ID
	 */
	public int createDataID();
}

package cz.cuni.mff.d3s.jdeeco.network.omnet;

/**
 * Holds OMNeT node position information
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class Position {
	public final int x;
	public final int y;
	public final int z;
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

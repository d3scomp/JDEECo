package cz.cuni.mff.d3s.jdeeco.position;

/**
 * Interface used to obtain position information
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface PositionProvider {
	/**
	 * Gets current node position
	 *  
	 * @return Node position.
	 */
	public Position getPosition();
}

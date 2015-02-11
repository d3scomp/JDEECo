package cz.cuni.mff.d3s.jdeeco.network;

/**
 * Interface for network used by gossip layer
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface NetworkToGossip {
	/**
	 * Processes knowledge to be send to network from gossip
	 * 
	 * TODO: name is not nice
	 * 
	 * TODO: knowledge type
	 */
	public void processDataFromGossipLayer(Object knowledge);
}

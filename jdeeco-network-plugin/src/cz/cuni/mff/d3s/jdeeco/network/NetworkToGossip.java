package cz.cuni.mff.d3s.jdeeco.network;


// XXX TB: What is gossip layer? I thought this should be a strategy. I think this does not belong to ...network. It should be a separate package with gossip strategy.

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
	public void processKnowledgeFromGossip(Object knowledge);
}

package cz.cuni.mff.d3s.jdeeco.gossip;

/**
 * Interface for gossip implementation
 * 
 * Gossip implementation is expected to register itself as knowledge update listener with Knowledge manager and pass
 * received knowledge to knowledge manager.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface Gossip extends KnowledgeListener {
	/**
	 * Processes knowledge received from network
	 * 
	 * @param knowledge
	 *            Knowledge to process
	 */
	public void processReceivedKnowledge(Object knowledge);

	/**
	 * Registers a listener for knowledge updates to be send via network
	 * 
	 * @param consumer
	 *            Update listener (some network implementation)
	 */
	public void registerKnowledegeUpdateListener(KnowledgeListener consumer);
}

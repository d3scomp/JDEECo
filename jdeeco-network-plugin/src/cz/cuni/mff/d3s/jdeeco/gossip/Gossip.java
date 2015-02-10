package cz.cuni.mff.d3s.jdeeco.gossip;

/**
 * Interface for gossip implementation
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface Gossip {
	/**
	 * Processes knowledge received from network
	 * 
	 * TODO: Add proper knowledge type
	 * 
	 * @param knowledge
	 *            Knowledge to process
	 */
	public void processKnowledgeUpdate(Object knowledge);
}

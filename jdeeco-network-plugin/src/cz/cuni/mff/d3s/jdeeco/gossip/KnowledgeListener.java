package cz.cuni.mff.d3s.jdeeco.gossip;

/**
 * Interface for consuming knowledge updates
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface KnowledgeListener {
	/**
	 * Processes knowledge
	 * 
	 * @param knowledge
	 *            TODO: Determine type
	 */
	void processKnowledge(Object knowledge);
}

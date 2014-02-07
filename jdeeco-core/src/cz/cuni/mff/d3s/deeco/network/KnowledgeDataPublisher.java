package cz.cuni.mff.d3s.deeco.network;

/**
 * Convenience interface for separating the publishing logic from {@link PublisherTask}.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 * @see PublisherTask
 */
public interface KnowledgeDataPublisher {
	
	/**
	 * Publishes the local knowledge on the network.
	 */
	void publish();

	/**
	 * Republishes the received knowledge data with the given metadata
	 */
	void rebroacast(KnowledgeMetaData metadata);
}

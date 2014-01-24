package cz.cuni.mff.d3s.deeco.publisher;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface IncomingKnowledgeListener {

	public void knowledgeReceived(KnowledgeData knowledgeData);
	
}

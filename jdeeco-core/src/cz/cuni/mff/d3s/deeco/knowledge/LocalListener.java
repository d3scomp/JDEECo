package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface LocalListener {
	public void localCreated(KnowledgeManager km);
	public void localRemoved(KnowledgeManager km);
}

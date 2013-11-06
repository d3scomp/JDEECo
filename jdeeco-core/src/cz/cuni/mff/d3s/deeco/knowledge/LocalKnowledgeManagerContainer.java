package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface LocalKnowledgeManagerContainer {
	public KnowledgeManager createLocal();
	public KnowledgeManager removeLocal(KnowledgeManager km);
	public List<KnowledgeManager> getLocals();
	public void registerListener(LocalListener listener);
}

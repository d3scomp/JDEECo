package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This interface allows to create/remove or get the KnowledgeManagers of the local 
 * or remote components
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
import java.util.Collection;
import java.util.LinkedList;

public interface KnowledgeManagerRegistry {

	public void createLocal(); 
	public void createShadow();
	public void removeLocal(KnowledgeManager km);
	public void removeShadow(KnowledgeManager km);
	public Collection<KnowledgeManager> getLocals();
	public Collection<KnowledgeManager> getShadows();
	
}

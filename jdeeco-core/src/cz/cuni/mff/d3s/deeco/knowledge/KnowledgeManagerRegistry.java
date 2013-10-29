package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This class allows to create/remove or get the KnowledgeManagers of the local 
 * or remote components
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.task.Task;

public class KnowledgeManagerRegistry {

 	
	public KnowledgeManager createLocal(){
 		return null;
	}
	
	public KnowledgeManager createShadow(){
		return null;
	}
	
	public void removeLocal(KnowledgeManager km){
		
	}
	
	public void removeShadow(KnowledgeManager km){
		
	}
	
	public Collection<KnowledgeManager> getLocals(){
		return null;
	}
	
	public Collection<KnowledgeManager> getShadows(){
		return null;
	}
	
}

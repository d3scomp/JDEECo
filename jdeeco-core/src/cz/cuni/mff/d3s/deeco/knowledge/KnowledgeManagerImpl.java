package cz.cuni.mff.d3s.deeco.knowledge;
/**
 * This class implements the KnowledgeManager interface. It allows the user to 
 * add, update and read the values from KnowledgeSet. Also, the class allows to 
 * bind a trigger to tirggerListener or unbind it.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */

import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.task.TriggerListener;

public class KnowledgeManagerImpl implements KnowledgeManager {

	protected KnowledgeSet ks;
	
	public KnowledgeManagerImpl(){
		
		ks = new KnowledgeSet();
	}
	
	public KnowledgeManagerImpl(KnowledgeSet ks){
		this.ks = ks;
	}

	@Override
	public ValueSet get(Collection<KnowledgeReference> knowledgeReferenceList) {
		ValueSet vs = new ValueSet(ks);
		return vs;
	}

	@Override
	public void register(Trigger trigger, TriggerListener triggerListener) {
		
	}

	@Override
	public void unregister(Trigger trigger, TriggerListener triggerListener) {
		
	}

	@Override
	public void update(ChangeSet changeSet) {
		
	}

}

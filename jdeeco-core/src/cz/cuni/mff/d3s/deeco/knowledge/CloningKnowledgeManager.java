package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.Collection;

import com.rits.cloning.Cloner;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;

/**
 * 
 * A KnowledgeManager version that introduces cloning functionality for stored
 * data immutability.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class CloningKnowledgeManager extends BaseKnowledgeManager {

	private final Cloner cloner;
	
	public CloningKnowledgeManager(String id) {
		super(id);
		cloner = new Cloner();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#get(java.util.Collection
	 * )
	 */
	@Override
	public synchronized ValueSet get(Collection<KnowledgePath> knowledgePaths)
			throws KnowledgeNotFoundException {
		ValueSet values = super.get(knowledgePaths);
		ValueSet copy = new ValueSet();
		// only values need to be cloned (cloning KnowledgePaths in a full model
		// causes a loopback in the cloner)
		for (KnowledgePath p: values.getKnowledgePaths()) {
			copy.setValue(p, cloner.deepClone(values.getValue(p)));
		}
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#register(cz.cuni
	 * .mff.d3s.deeco.model.runtime.api.Trigger,
	 * cz.cuni.mff.d3s.deeco.knowledge.TriggerListener)
	 */
	@Override
	public synchronized void register(Trigger trigger,
			TriggerListener triggerListener) {
		super.register(trigger, triggerListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#unregister(cz.cuni
	 * .mff.d3s.deeco.model.runtime.api.Trigger,
	 * cz.cuni.mff.d3s.deeco.knowledge.TriggerListener)
	 */
	@Override
	public synchronized void unregister(Trigger trigger,
			TriggerListener triggerListener) {
		super.unregister(trigger, triggerListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#update(cz.cuni.mff
	 * .d3s.deeco.knowledge.ChangeSet)
	 */
	@Override
	public synchronized void update(ChangeSet changeSet) {
		ChangeSet copy = new ChangeSet();
		// only values need to be cloned (cloning KnowledgePaths in a full model
		// causes a loopback in the cloner)
		for (KnowledgePath p: changeSet.getUpdatedReferences()) {
			copy.setValue(p, cloner.deepClone(changeSet.getValue(p)));
		}
		for (KnowledgePath p: changeSet.getDeletedReferences()) {
			copy.setDeleted(p);
		}
		super.update(copy);
	}
}

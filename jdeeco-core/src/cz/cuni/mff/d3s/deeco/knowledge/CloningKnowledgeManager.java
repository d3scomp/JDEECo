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
	private final BaseKnowledgeManager bkm;

	public CloningKnowledgeManager(Object knowledge) {
		cloner = new Cloner();
		bkm = new BaseKnowledgeManager(cloner.deepClone(knowledge));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#get(java.util.Collection
	 * )
	 */
	@Override
	public ValueSet get(Collection<KnowledgePath> knowledgePaths)
			throws KnowledgeNotFoundException {
		return cloner.deepClone(bkm.get(knowledgePaths));
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
		bkm.register(trigger, triggerListener);
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
		bkm.unregister(trigger, triggerListener);
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
		bkm.update(cloner.deepClone(changeSet));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager#
	 * getOthersKnowledgeManagers()
	 */
	@Override
	public synchronized Collection<ReadOnlyKnowledgeManager> getOthersKnowledgeManagers() {
		return bkm.getOthersKnowledgeManagers();
	}
}

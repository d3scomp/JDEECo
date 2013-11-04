
package cz.cuni.mff.d3s.deeco.task;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class EnsembleTask extends Task {

	EnsembleController ensembleController;
	
	public EnsembleTask(EnsembleController ensembleController, Scheduler scheduler) {
		super(scheduler);
		
		this.ensembleController = ensembleController;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		assert(listener != null);
		
		ComponentInstance componentInstance = ensembleController.getComponentInstance(); 
		KnowledgeManager localKM = componentInstance.getKnowledgeManager();
		KnowledgeManagersView shadowsKM = componentInstance.getOtherKnowledgeManagersAccess();
		
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getSchedulingSpecification().getTriggers()) {
			
			// TODO: Here should come something which strips the change trigger knowledge path of the coord/member root
			
			localKM.register(trigger, listener);
			shadowsKM.register(trigger, listener);
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		ComponentInstance componentInstance = ensembleController.getComponentInstance(); 
		KnowledgeManager localKM = componentInstance.getKnowledgeManager();
		KnowledgeManagersView shadowsKM = componentInstance.getOtherKnowledgeManagersAccess();
		
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getSchedulingSpecification().getTriggers()) {
			
			// TODO: Here should come something which strips the change trigger knowledge path of the coord/member root
			
			localKM.unregister(trigger, listener);
			shadowsKM.unregister(trigger, listener);
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke() {
		// TODO Auto-generated method stub
		
	}

	public long getSchedulingPeriod() {
		return ensembleController.getEnsembleDefinition().getSchedulingSpecification().getPeriod();
	}
}

package cz.cuni.mff.d3s.deeco.task;

import java.util.Iterator;
import java.util.List;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class EnsembleTask extends Task {

	EnsembleController ensembleController;

	private class KnowledgeManagerTriggerListenerImpl implements TriggerListener {

		/* (non-Javadoc)
		 * @see cz.cuni.mff.d3s.deeco.knowledge.TriggerListener#triggered(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
		 */
		@Override
		public void triggered(Trigger trigger) {
			if (isForCoordinatorKM(trigger)) {
				// TODO: Schedule the execution of the ensemble (i.e. membership and possibly knowledge exchange) as the coordinator
				// This means that we have to know what to do once the scheduler calls us in the next round.
			} else if (isForMemberKM(trigger)) {
				// TODO: Schedule the execution of the ensemble (i.e. membership and possibly knowledge exchange) as the member
				// This means that we have to know what to do once the scheduler calls us in the next round.
			} else {
				assert(false); 
			}

			if (listener != null) {
				listener.triggered(EnsembleTask.this, trigger);
			}
		}
	}
	KnowledgeManagerTriggerListenerImpl knowledgeManagerTriggerListener = new KnowledgeManagerTriggerListenerImpl();

	private class ShadowsTriggerListenerImpl implements ShadowsTriggerListener {

		/* (non-Javadoc)
		 * @see cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener#triggered(cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager, cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
		 */
		@Override
		public void triggered(ReadOnlyKnowledgeManager knowledgeManager, Trigger trigger) {
			if (isForCoordinatorKM(trigger)) {
				// TODO: Schedule execution of the ensemble (i.e. membership and possibly knowledge exchange) as the member
				// This means that we have to know what to do once the scheduler calls us in the next round.
			} else if (isForMemberKM(trigger)) {
				// TODO: Schedule execution of the ensemble (i.e. membership and possibly knowledge exchange) as the coordinator
				// This means that we have to know what to do once the scheduler calls us in the next round.
			} else {
				assert(false); 
			}

			if (listener != null) {
				listener.triggered(EnsembleTask.this, trigger);
			}
		}
	}
	ShadowsTriggerListenerImpl shadowsTriggerListener = new ShadowsTriggerListenerImpl();

	public EnsembleTask(EnsembleController ensembleController, Scheduler scheduler) {
		super(scheduler);
		
		this.ensembleController = ensembleController;
	}

	// FIXME TB: The following four methods should probably go somewhere else, as the EnsembleTask is not really supposed 
	// to understand internals of triggers. Maybe it would make sense to put it either to the meta-model or to knowledge package.
	
	/**
	 * Helper method for methods isForCoordinatorKM and isForMemberKM.
	 * @param trigger Trigger to be checked. Currently only {@link KnowledgeChangeTrigger} is accepted.
	 * @param nodeType {@link PathNodeCoordinator}.class or {@link PathNodeMember}.class 
	 * @return True if the trigger is to be registered in coordinator's or member's knowledge manager respectively. 
	 */
	private boolean isForKM(Trigger trigger, Class<? extends PathNode> nodeType) {
		KnowledgeChangeTrigger knowledgeChangeTrigger = (KnowledgeChangeTrigger)trigger;
		
		List<PathNode> pathNodes = knowledgeChangeTrigger.getKnowledgePath().getNodes();
		
		return !pathNodes.isEmpty() && (nodeType.isInstance(pathNodes.get(0)));		
	}
	
	/**
	 * Returns true if the trigger is to be registered in the coordinator's knowledge manager.
	 * @param trigger
	 */
	private boolean isForCoordinatorKM(Trigger trigger) {
		return isForKM(trigger, PathNodeCoordinator.class);
	}
	
	/**
	 * Returns true if the trigger is to be registered in the member's knowledge manager.
	 * @param trigger
	 */
	private boolean isForMemberKM(Trigger trigger) {
		return isForKM(trigger, PathNodeMember.class);
	}
	
	/**
	 * Returns a trigger which can be understood by a knowledge manager. In particular this means that the knowledge path of the trigger (in case of
	 * the {@link KnowledgeChangeTrigger}) is striped of the coordinator/memeber prefix.
	 * 
	 * @param trigger The trigger to be adapted. Currently only {@link KnowledgeChangeTrigger} is supported.
	 * @return The adapted trigger or <code>null</code> if the trigger is invalid or can't be adapted.
	 */
	private Trigger adaptTriggerForKM(Trigger trigger) {
		KnowledgeChangeTrigger knowledgeChangeTrigger = (KnowledgeChangeTrigger)trigger;
		List<PathNode> origPathNodes = knowledgeChangeTrigger.getKnowledgePath().getNodes();
		
		if (origPathNodes.isEmpty()) {
			return null;
		}
		
		Iterator<PathNode> pathNodeIter = origPathNodes.iterator();
		PathNode firstPathNode = pathNodeIter.next();

		if ((firstPathNode instanceof PathNodeCoordinator) || (firstPathNode instanceof PathNodeMember)) {
			RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
			
			KnowledgePath knowledgePath = factory.createKnowledgePath();
			List<PathNode> newPathNodes = knowledgePath.getNodes();
						
			while (pathNodeIter.hasNext()) {
				PathNode origNode = pathNodeIter.next();
				
				if (origNode instanceof PathNodeField) {
					PathNodeField newNode = factory.createPathNodeField();
					newNode.setName(((PathNodeField) origNode).getName());
					
					newPathNodes.add(newNode);
				} else {
					return null;
				}
			}

			KnowledgeChangeTrigger result = factory.createKnowledgeChangeTrigger();
			result.setKnowledgePath(knowledgePath);
			
			return result;
		}
		
		return null;
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
		
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getTriggers()) {
			
			// TODO: Here should come something which strips the change trigger knowledge path of the coord/member root
			
			localKM.register(trigger, knowledgeManagerTriggerListener);
			shadowsKM.register(trigger, shadowsTriggerListener);
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
		
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getTriggers()) {
			
			// TODO: Here should come something which strips the change trigger knowledge path of the coord/member root
			
			localKM.unregister(trigger, knowledgeManagerTriggerListener);
			shadowsKM.unregister(trigger, shadowsTriggerListener);
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns the period associated with the ensemble in the in the meta-model as the {@link PeriodicTrigger}. Note that the {@link EnsembleTask} assumes that there is at most
	 * one instance of {@link PeriodicTrigger} associated with the ensemble in the meta-model.
	 * 
	 * @return Periodic trigger or null no period is associated with the task.
	 */
	@Override
	public PeriodicTrigger getPeriodicTrigger() {
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getTriggers()) {
			if (trigger instanceof PeriodicTrigger) {
				return ((PeriodicTrigger) trigger);
			}
		}
		
		return null;
	}
}

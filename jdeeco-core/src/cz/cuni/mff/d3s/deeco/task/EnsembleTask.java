package cz.cuni.mff.d3s.deeco.task;

import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getAbsoluteStrippedPath;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagersView;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class EnsembleTask extends Task {

	EnsembleController ensembleController;

	private static class LocalKMChangeTrigger extends TriggerImpl {
		public LocalKMChangeTrigger(KnowledgeChangeTrigger knowledgeChangeTrigger) {
			super();
			this.knowledgeChangeTrigger = knowledgeChangeTrigger;
		}

		KnowledgeChangeTrigger knowledgeChangeTrigger;
	}
	
	private static class ShadowKMChangeTrigger extends TriggerImpl {
		public ShadowKMChangeTrigger(ReadOnlyKnowledgeManager shadowKnowledgeManager, KnowledgeChangeTrigger knowledgeChangeTrigger) {
			super();
			this.knowledgeChangeTrigger = knowledgeChangeTrigger;
			this.shadowKnowledgeManager = shadowKnowledgeManager;
		}
		
		KnowledgeChangeTrigger knowledgeChangeTrigger;
		ReadOnlyKnowledgeManager shadowKnowledgeManager;
	}
	
	private class LocalKMTriggerListenerImpl implements TriggerListener {

		/* (non-Javadoc)
		 * @see cz.cuni.mff.d3s.deeco.knowledge.TriggerListener#triggered(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
		 */
		@Override
		public void triggered(Trigger trigger) {
			if (listener != null) {
				listener.triggered(EnsembleTask.this, new LocalKMChangeTrigger((KnowledgeChangeTrigger)trigger));
			}
		}
	}
	LocalKMTriggerListenerImpl knowledgeManagerTriggerListener = new LocalKMTriggerListenerImpl();

	private class ShadowsTriggerListenerImpl implements ShadowsTriggerListener {

		/* (non-Javadoc)
		 * @see cz.cuni.mff.d3s.deeco.knowledge.ShadowsTriggerListener#triggered(cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager, cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
		 */
		@Override
		public void triggered(ReadOnlyKnowledgeManager knowledgeManager, Trigger trigger) {
			if (listener != null) {
				listener.triggered(EnsembleTask.this, new ShadowKMChangeTrigger(knowledgeManager, (KnowledgeChangeTrigger)trigger));
			}
		}
	}
	ShadowsTriggerListenerImpl shadowsTriggerListener = new ShadowsTriggerListenerImpl();

	public EnsembleTask(EnsembleController ensembleController, Scheduler scheduler) {
		super(scheduler);
		
		this.ensembleController = ensembleController;
	}

	/**
	 * Returns a trigger which can be understood by a knowledge manager. In particular this means that the knowledge path of the trigger (in case of
	 * the {@link KnowledgeChangeTrigger}) is stripped of the coordinator/memeber prefix.
	 * 
	 * @param trigger The trigger to be adapted. Currently only {@link KnowledgeChangeTrigger} is supported.
	 * @return The adapted trigger or <code>null</code> if the trigger is invalid or can't be adapted.
	 */
	private Trigger adaptTriggerForKM(Trigger trigger) {
		KnowledgeChangeTrigger knowledgeChangeTrigger = (KnowledgeChangeTrigger)trigger;

		KnowledgePathAndRoot strippedKnowledgePathAndRoot = KnowledgePathHelper.getStrippedPath(knowledgeChangeTrigger.getKnowledgePath());
		
		if (strippedKnowledgePathAndRoot != null) {
			RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;

			KnowledgeChangeTrigger result = factory.createKnowledgeChangeTrigger();
			result.setKnowledgePath(strippedKnowledgePathAndRoot.knowledgePath);
			
			return result;
		} else {
			return null;
		}		
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
			if (trigger instanceof KnowledgeChangeTrigger) {
				Trigger strippedTrigger = adaptTriggerForKM(trigger);

				localKM.register(strippedTrigger, knowledgeManagerTriggerListener);
				shadowsKM.register(strippedTrigger, shadowsTriggerListener);
			}
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
			
			if (trigger instanceof KnowledgeChangeTrigger) {
				Trigger strippedTrigger = adaptTriggerForKM(trigger);
			
				localKM.unregister(strippedTrigger, knowledgeManagerTriggerListener);
				shadowsKM.unregister(strippedTrigger, shadowsTriggerListener);
			}
		}
	}

	private boolean checkMembership(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		// Obtain parameters from the local knowledge to evaluate the membership
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		Collection<Parameter> formalParams = ensembleController.getEnsembleDefinition().getMembership().getParameters();

		Collection<KnowledgePathAndRoot> allPathsWithRoots = new LinkedList<KnowledgePathAndRoot>();
		Collection<KnowledgePath> localPaths = new LinkedList<KnowledgePath>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<KnowledgePath>();
		
		for (Parameter formalParam : formalParams) {
			ParameterDirection paramDir = formalParam.getDirection();

			if (paramDir != ParameterDirection.IN) {
				throw new TaskInvocationException("Only IN params allowed in membership condition.");
			}
			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot;
			// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.
			if (localRole == PathRoot.COORDINATOR) {
				absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager);
			} else {
				absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), shadowKnowledgeManager, localKnowledgeManager);				
			}
			
			if (absoluteKnowledgePathAndRoot == null) {
				throw new TaskInvocationException("Member/Coordinator prefix required for membership paths.");
			} if (absoluteKnowledgePathAndRoot.root == localRole) {
				localPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
			} else {
				shadowPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
			}
			
			allPathsWithRoots.add(absoluteKnowledgePathAndRoot);
		}
		
		ValueSet localKnowledge;
		ValueSet shadowKnowledge;
		
		try {
			localKnowledge = localKnowledgeManager.get(localPaths);
			shadowKnowledge = shadowKnowledgeManager.get(shadowPaths);
		} catch (KnowledgeNotFoundException e) {
			// We were not able to find the knowledge, which means that the membership is false.
			return false;
		}

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[formalParams.size()];
		
		int paramIdx = 0;
		for (KnowledgePathAndRoot absoluKnowledgePathAndRoot : allPathsWithRoots) {
			
			if (absoluKnowledgePathAndRoot.root == localRole) {
				actualParams[paramIdx] = localKnowledge.getValue(absoluKnowledgePathAndRoot.knowledgePath);	
			} else {
				actualParams[paramIdx] = shadowKnowledge.getValue(absoluKnowledgePathAndRoot.knowledgePath);	
			}
			
			paramIdx++;
		}
		
		try {
			// Call the membership condition
			return (Boolean)ensembleController.getEnsembleDefinition().getMembership().getMethod().invoke(null, actualParams);
			
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new TaskInvocationException("Error when invoking a membership condition.", e);
		} catch (ClassCastException e) {
			throw new TaskInvocationException("Membership condition does not return a boolean.", e);			
		} catch (InvocationTargetException e) {
			Log.i("Membership condition returned an exception.", e.getTargetException());
			return false;
		}
	}
	
	private void performExchange(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		// Obtain parameters from the local knowledge to perform the exchange
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		Collection<Parameter> formalParams = ensembleController.getEnsembleDefinition().getKnowledgeExchange().getParameters();

		Collection<KnowledgePathAndRoot> allPathsWithRoots = new LinkedList<KnowledgePathAndRoot>();
		Collection<KnowledgePath> localPaths = new LinkedList<KnowledgePath>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<KnowledgePath>();
		
		for (Parameter formalParam : formalParams) {
			ParameterDirection paramDir = formalParam.getDirection();
			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot;

			// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.			
			if (localRole == PathRoot.COORDINATOR) {
				absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager);
			} else {
				absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), shadowKnowledgeManager, localKnowledgeManager);				
			}

			allPathsWithRoots.add(absoluteKnowledgePathAndRoot);

			if (paramDir == ParameterDirection.IN || paramDir == ParameterDirection.INOUT) {
				if (absoluteKnowledgePathAndRoot == null) {
					throw new TaskInvocationException("Member/Coordinator prefix required for knowledge exchange paths.");
				} if (absoluteKnowledgePathAndRoot.root == localRole) {
					localPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
				} else {
					shadowPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
				}				
			}
		}
		
		ValueSet localKnowledge;
		ValueSet shadowKnowledge;
		
		try {
			localKnowledge = localKnowledgeManager.get(localPaths);
			shadowKnowledge = shadowKnowledgeManager.get(shadowPaths);
		} catch (KnowledgeNotFoundException e) {
			throw new TaskInvocationException("Input knowledge of a knowledge exchange not found in the knowledge manager.", e);
		}

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[formalParams.size()];
		
		int paramIdx = 0;
		Iterator<KnowledgePathAndRoot> allPathsWithRootsIter = allPathsWithRoots.iterator(); 
		for (Parameter formalParam : formalParams) {
			ParameterDirection paramDir = formalParam.getDirection();
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();

			Object paramValue = null;
			
			if (paramDir == ParameterDirection.IN || paramDir == ParameterDirection.INOUT) {				
				if (absoluteKnowledgePathAndRoot.root == localRole) {
					paramValue = localKnowledge.getValue(absoluteKnowledgePathAndRoot.knowledgePath);	
				} else {
					paramValue = shadowKnowledge.getValue(absoluteKnowledgePathAndRoot.knowledgePath);	
				}
			}
			
			if (paramDir == ParameterDirection.IN) {
				actualParams[paramIdx] = paramValue;
				
			} else if (paramDir == ParameterDirection.OUT) {
				actualParams[paramIdx] = new ParamHolder<Object>();

			} else if (paramDir == ParameterDirection.INOUT) {
				actualParams[paramIdx] = new ParamHolder<Object>(paramValue);
			}
			// TODO: We could have an option of not creating the wrapper. That would make it easier to work with mutable out types.
			// TODO: We need some way of handling insertions/deletions in a hashmap.
			
			paramIdx++;
		}
		
		try {
			// Call the process method
			ensembleController.getEnsembleDefinition().getKnowledgeExchange().getMethod().invoke(null, actualParams);
			
			// Create a changeset
			ChangeSet localChangeSet = new ChangeSet();
			
			paramIdx = 0;
			allPathsWithRootsIter = allPathsWithRoots.iterator(); 
			for (Parameter formalParam : formalParams) {
				ParameterDirection paramDir = formalParam.getDirection();
				KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();

				if (absoluteKnowledgePathAndRoot.root == localRole) {
					if (paramDir == ParameterDirection.OUT || paramDir == ParameterDirection.INOUT) {
						localChangeSet.setValue(absoluteKnowledgePathAndRoot.knowledgePath, ((ParamHolder<Object>)actualParams[paramIdx]).value);
					}
				}
				
				paramIdx++;
			}
			
			// Write the changeset back to the knowledge
			localKnowledgeManager.update(localChangeSet);
			
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new TaskInvocationException("Error when invoking a knowledge exchange.", e);
		} catch (InvocationTargetException e) {
			Log.i("Knowledge exchange returned an exception.", e.getTargetException());
		}		
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		if (trigger instanceof ShadowKMChangeTrigger) {
			// If the trigger pertains to a shadow knowledge manager
			ReadOnlyKnowledgeManager shadowKnowledgeManager = ((ShadowKMChangeTrigger)trigger).shadowKnowledgeManager;

			// Invoke the membership condition and if the membership condition returned true, invoke the knowledge exchange
			if (checkMembership(PathRoot.COORDINATOR, shadowKnowledgeManager)) {
				performExchange(PathRoot.COORDINATOR, shadowKnowledgeManager);
			}

			// Do the same with the roles exchanged
			if (checkMembership(PathRoot.MEMBER, shadowKnowledgeManager)) {
				performExchange(PathRoot.MEMBER, shadowKnowledgeManager);
			}
			
		} else {
			// If the trigger is periodic trigger or pertains to the local knowledge manager, iterate over all shadow knowledge managers
			KnowledgeManagersView shadows = ensembleController.getComponentInstance().getOtherKnowledgeManagersAccess();

			for (ReadOnlyKnowledgeManager shadowKnowledgeManager : shadows.getOtherKnowledgeManagers()) {
				// Invoke the membership condition and if the membership condition returned true, invoke the knowledge exchange
				if (checkMembership(PathRoot.COORDINATOR, shadowKnowledgeManager)) {
					performExchange(PathRoot.COORDINATOR, shadowKnowledgeManager);
				}

				// Do the same with the roles exchanged
				if (checkMembership(PathRoot.MEMBER, shadowKnowledgeManager)) {
					performExchange(PathRoot.MEMBER, shadowKnowledgeManager);
				}
			}			
		}
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

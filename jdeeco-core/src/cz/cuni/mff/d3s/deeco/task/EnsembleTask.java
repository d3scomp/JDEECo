package cz.cuni.mff.d3s.deeco.task;

import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getAbsoluteStrippedPath;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.integrity.RatingsHolder;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;
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
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl;
import cz.cuni.mff.d3s.deeco.runtime.ArchitectureObserver;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.security.LocalSecurityChecker;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * The implementation of {@link Task} that corresponds to a ensemble controller at a component. This class is responsible for 
 * (a) registering triggers with the local knowledge of the component instance and the shadow knowledge manager registry, and 
 * (b) to execute the membership/knowledge exchange when invoked by the scheduler/executor.
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class EnsembleTask extends Task {

	/**
	 * Reference to the corresponding {@link EnsembleController} in the runtime metadata 
	 */
	EnsembleController ensembleController;

	/**
	 * Reference to the corresponding {@link ArchitectureObserver} 
	 */
	ArchitectureObserver architectureObserver;
	
	/**
	 * Reference to the corresponding {@link LocalSecurityChecker} 
	 */
	LocalSecurityChecker securityChecker;
	
	/**
	 * Reference to the ratings manager
	 */
	RatingsManager ratingsManager;
	
	/**
	 * A wrapper around a trigger obtained from the local knowledge manager. The sole purpose of this class is to be able to distinguish
	 * when invoked back by the scheduler/executor whether the trigger came originally from a local knowledge manager or a 
	 * shadow knowledge manager.
	 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
	 *
	 */
	private static class LocalKMChangeTrigger extends TriggerImpl {
		public LocalKMChangeTrigger(KnowledgeChangeTrigger knowledgeChangeTrigger) {
			super();
			this.knowledgeChangeTrigger = knowledgeChangeTrigger;
		}

		KnowledgeChangeTrigger knowledgeChangeTrigger;
	}
	
	/**
	 * A wrapper around a trigger obtained from a shadow knowledge manager. The sole purpose of this class is to be able to distinguish
	 * when invoked back by the scheduler/executor whether the trigger came originally from a local knowledge manager or a 
	 * shadow knowledge manager.
	 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
	 *
	 */
	private static class ShadowKMChangeTrigger extends TriggerImpl {
		public ShadowKMChangeTrigger(ReadOnlyKnowledgeManager shadowKnowledgeManager, KnowledgeChangeTrigger knowledgeChangeTrigger) {
			super();
			this.knowledgeChangeTrigger = knowledgeChangeTrigger;
			this.shadowKnowledgeManager = shadowKnowledgeManager;
		}
		
		KnowledgeChangeTrigger knowledgeChangeTrigger;
		ReadOnlyKnowledgeManager shadowKnowledgeManager;
	}
	
	/**
	 * Implementation of the trigger listener, which is registered in the local knowledge manager. When called, it calls the listener registered by
	 * {@link Task#setTriggerListener(TaskTriggerListener)}. As a parameter, it passes to the listener a new {@link LocalKMChangeTrigger} which
	 * wraps the trigger obtained from the local knowledge manager. This way, when the scheduler/executor call the ensemble task back with the trigger,
	 * it is possible to distinguish whether the trigger was originally from a local knowledge manager or a shadow knowledge manager.
	 * 
	 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
	 *
	 */
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

	/**
	 * Implementation of the trigger listener, which is registered in the shadow knowledge manager registry. When called, it calls the listener registered by
	 * {@link Task#setTriggerListener(TaskTriggerListener)}. As a parameter, it passes to the listener a new {@link ShadowKMChangeTrigger} which
	 * wraps the trigger obtained from the particular shadow knowledge manager. The reason is that same is in the case of the
	 * {@link LocalKMTriggerListenerImpl}.
	 * 
	 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
	 *
	 */
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

	public EnsembleTask(EnsembleController ensembleController, Scheduler scheduler, ArchitectureObserver architectureObserver, 
			KnowledgeManagerContainer kmContainer, RatingsManager ratingsManager) {
		super(scheduler);
		
		this.architectureObserver = architectureObserver;
		this.ensembleController = ensembleController;
		this.securityChecker = new LocalSecurityChecker(ensembleController, kmContainer);
		this.ratingsManager = ratingsManager;
	}

	/**
	 * Returns a trigger which can be understood by a knowledge manager. In particular this means that the knowledge path of the trigger (in case of
	 * the {@link KnowledgeChangeTrigger}) is stripped of the coordinator/memeber prefix.
	 * 
	 * @param trigger the trigger to be adapted. Currently only {@link KnowledgeChangeTrigger} is supported.
	 * @return the adapted trigger or <code>null</code> if the trigger is invalid or can't be adapted.
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
		ShadowKnowledgeManagerRegistry shadowsKM = componentInstance.getShadowKnowledgeManagerRegistry();
		
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
		ShadowKnowledgeManagerRegistry shadowsKM = componentInstance.getShadowKnowledgeManagerRegistry();
		
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getTriggers()) {
			
			if (trigger instanceof KnowledgeChangeTrigger) {
				Trigger strippedTrigger = adaptTriggerForKM(trigger);
			
				localKM.unregister(strippedTrigger, knowledgeManagerTriggerListener);
				shadowsKM.unregister(strippedTrigger, shadowsTriggerListener);
			}
		}
	}

	/**
	 * Checks membership condition on parameters of the local knowledge manager and parameters from a particular shadow knowledge manager.
	 * It fixes the coordinator/member roles based on the <code>localRole</code> parameter. If <code>localRole</code> is equal to {@link KnowledgePathHelper.PathRoot#COORDINATOR} it
	 * takes the coordinators parameters from the local knowledge manager and the member parameters from the shadow knowledge manager.
	 * If <code>localRole</code> is equal to {@link KnowledgePathHelper.PathRoot#MEMBER}, it does it vice-versa.
	 * 
	 * It essentially works in the following steps:
	 * <ol>
	 *   <li>It resolves the IN paths of the membership condition and retrieves the knowledge from the local knowledge manager and 
	 *   the shadow knowledge manager with the member/coordinator roles fixed according to the <code>localRole</code> parameter.
	 *   (Note that INOUT and OUT paths are not allowed. If they are present a {@link TaskInvocationException} is thrown.)</li>
	 *   <li>If the IN parameters were not found in the knowledge, <code>false</code> is returned.</li>
	 *   <li>Otherwise, it invokes the membership condition with the values obtained in the previous step and returns its return value.</li>
	 * </ol>
	 * 
	 * @param localRole specifies whether the membership condition is to be evaluated with assuming the local component as a coordinator or member.
	 * @param shadowKnowledgeManager specifies the shadow knowledge manager which contains data of the opposite component instance. 
	 * @return <code>true</code> if the membership condition was satisfied. <code>false</code> if it was not satisfied or if the
	 * IN parameters were not found in the knowledge.
	 * @throws TaskInvocationException thrown when the membership condition can't be executed (e.g. it does not exist, has another parameters, has INOUT/OUT parameters, etc.)
	 */
	private boolean checkMembership(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		// Obtain parameters from the local knowledge to evaluate the membership
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		Collection<Parameter> formalParams = ensembleController.getEnsembleDefinition().getMembership().getParameters();

		Collection<KnowledgePathAndRoot> allPathsWithRoots = new LinkedList<KnowledgePathAndRoot>();
		Collection<KnowledgePath> localPaths = new LinkedList<KnowledgePath>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<KnowledgePath>();
		
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();

			if (paramDir != ParameterKind.IN && paramDir != ParameterKind.RATING) {
				throw new TaskInvocationException("Only IN and RATING params allowed in membership condition.");
			}
			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot;
			// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.
			try {
				if (localRole == PathRoot.COORDINATOR) {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager);
				} else {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), shadowKnowledgeManager, localKnowledgeManager);				
				}
			} catch (KnowledgeNotFoundException e) {
				// We were not able to resolve the knowledge path, which means that the membership is false.
				return false;
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
		
		ValueSet localKnowledge = null;
		ValueSet shadowKnowledge = null;
		
		try {
			localKnowledge = localKnowledgeManager.get(localPaths);
			shadowKnowledge = shadowKnowledgeManager.get(shadowPaths);
		} catch (KnowledgeNotFoundException e) {
			
			// We were not able to find the knowledge, which means that the membership is false.
			ReadOnlyKnowledgeManager where = localKnowledge == null ? localKnowledgeManager : shadowKnowledgeManager;
			Log.d(String.format("Input knowledge (%s) of a membership in %s was not found in the knowledge manager %s.", 
					e.getNotFoundPath(), 
					ensembleController.getEnsembleDefinition().getName(),
					where.getId()
					));
			return false;
		}

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[formalParams.size()];
		
		int paramIdx = 0;
		Iterator<KnowledgePathAndRoot> allPathsWithRootsIter = allPathsWithRoots.iterator(); 
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();
			
			if (paramDir == ParameterKind.IN) {
				if (absoluteKnowledgePathAndRoot.root == localRole) {
					actualParams[paramIdx] = localKnowledge.getValue(absoluteKnowledgePathAndRoot.knowledgePath);						
				} else {
					actualParams[paramIdx] = shadowKnowledge.getValue(absoluteKnowledgePathAndRoot.knowledgePath);	
				}
			} else if (paramDir == ParameterKind.RATING) {		
				String knowledgeAuthor;
				if (absoluteKnowledgePathAndRoot.root == localRole) {
					knowledgeAuthor = localKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
				} else {
					knowledgeAuthor = shadowKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
				}
				actualParams[paramIdx] = ratingsManager.createReadonlyRatingsHolder(knowledgeAuthor, absoluteKnowledgePathAndRoot.knowledgePath); 			
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
			Log.d("Membership condition returned an exception.", e.getTargetException());
			return false;
		}
	}
	
	/**
	 * Performs the knowledge exchange between the local knowledge manager and a particular shadow knowledge manager. Afterwards, it writes new values to 
	 * the local knowledge manager. Similarly to {@link #checkMembership(PathRoot, ReadOnlyKnowledgeManager)}, it uses the <code>localRole</code>
	 * parameter to specify, whether the local component instance is assumed to be in member or coordinator role.
	 * 
	 * The method essentially works in the following steps:
	 * <ol>
	 *   <li>It resolves the IN/INOUT paths of the membership condition and retrieves the knowledge from the local knowledge manager and 
	 *   the shadow knowledge manager with the member/coordinator roles fixed according to the <code>localRole</code> parameter.
	 *   <li>It invokes the membership condition with the values obtained in the previous step.</li>
	 *   <li>It updates the local knowledge manager with the INOUT/OUT data that pertain to the local component.</li>
	 * </ol>
	 * 
	 * Similar to the {@link ProcessTask#invoke(Trigger)}, the INOUT/OUT parameters are wrapped by {@link ParamHolder}. 
	 * 
	 * @param localRole specifies whether the knowledge exchange is to be evaluated with assuming the local component as a coordinator or member.
	 * @param shadowKnowledgeManager specifies the shadow knowledge manager which contains data of the opposite component instance. 
	 * @return true if the exchange was performed successfully
	 * @throws TaskInvocationException thrown when the knowledge exchange can't be executed (e.g. it does not exist, has another parameters, has INOUT/OUT parameters, etc.).
	 * The exception is thrown also in the case when parameters cannot be retrieved from the local/shadow knowledge manager or when the output
	 * parameters can't be updated in the knowledge manager.
	 */
	private boolean performExchange(PathRoot localRole, ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		// Obtain parameters from the local knowledge to perform the exchange
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		Collection<Parameter> formalParams = ensembleController.getEnsembleDefinition().getKnowledgeExchange().getParameters();

		Collection<KnowledgePathAndRoot> allPathsWithRoots = new LinkedList<KnowledgePathAndRoot>();
		Collection<KnowledgePath> localPaths = new LinkedList<KnowledgePath>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<KnowledgePath>();
		
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();
			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot;

			// FIXME: The call to getAbsoluteStrippedPath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.	
			try {
				if (localRole == PathRoot.COORDINATOR) {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), localKnowledgeManager, shadowKnowledgeManager);
				} else {
					absoluteKnowledgePathAndRoot = getAbsoluteStrippedPath(formalParam.getKnowledgePath(), shadowKnowledgeManager, localKnowledgeManager);				
				}
			} catch (KnowledgeNotFoundException e) {
				Log.d(String.format(
						"Knowledge exchange of %s could not be performed: missing knowledge path (%s)", 
						ensembleController.getEnsembleDefinition().getName(), e.getNotFoundPath()));
				return false;
			}

			allPathsWithRoots.add(absoluteKnowledgePathAndRoot);

			if (paramDir == ParameterKind.IN || paramDir == ParameterKind.INOUT) {
				if (absoluteKnowledgePathAndRoot == null) {
					throw new TaskInvocationException("Member/Coordinator prefix required for knowledge exchange paths.");
				} if (absoluteKnowledgePathAndRoot.root == localRole) {
					localPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
				} else {
					shadowPaths.add(absoluteKnowledgePathAndRoot.knowledgePath);
				}				
			}		
		}
		
		ValueSet localKnowledge = null;
		ValueSet shadowKnowledge = null;
		
		try {
			localKnowledge = localKnowledgeManager.get(localPaths);	
			shadowKnowledge = shadowKnowledgeManager.get(shadowPaths);	
		} catch (KnowledgeNotFoundException e) {
			// We were not able to find the knowledge, which means that the membership is false.
			ReadOnlyKnowledgeManager where = localKnowledge == null ? localKnowledgeManager : shadowKnowledgeManager;
			Log.d(String.format("Input knowledge (%s) of a knowledge exchange in %s not found in the knowledge manager %s.", 
					e.getNotFoundPath(), 
					ensembleController.getEnsembleDefinition().getName(),
					where.getId()
					));
			return false;
		}

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[formalParams.size()];
		
		int paramIdx = 0;
		Iterator<KnowledgePathAndRoot> allPathsWithRootsIter = allPathsWithRoots.iterator(); 
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();			
			KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();
			String knowledgeAuthor = null;
			Object paramValue = null;
			
			if (paramDir == ParameterKind.IN || paramDir == ParameterKind.INOUT || paramDir == ParameterKind.RATING) {				
				if (absoluteKnowledgePathAndRoot.root == localRole) {
					paramValue = localKnowledge.getValue(absoluteKnowledgePathAndRoot.knowledgePath);
					knowledgeAuthor = localKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
				} else {
					paramValue = shadowKnowledge.getValue(absoluteKnowledgePathAndRoot.knowledgePath);
					knowledgeAuthor = shadowKnowledgeManager.getAuthor(absoluteKnowledgePathAndRoot.knowledgePath);
				}
			}
			
			if (paramDir == ParameterKind.IN) {
				actualParams[paramIdx] = paramValue;				
			} else if (paramDir == ParameterKind.OUT) {
				actualParams[paramIdx] = new ParamHolder<Object>();
			} else if (paramDir == ParameterKind.INOUT) {
				actualParams[paramIdx] = new ParamHolder<Object>(paramValue);
			} else if (paramDir == ParameterKind.RATING) {				
				actualParams[paramIdx] = ratingsManager.createReadonlyRatingsHolder(knowledgeAuthor, absoluteKnowledgePathAndRoot.knowledgePath); 			
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
				ParameterKind paramDir = formalParam.getKind();
				KnowledgePathAndRoot absoluteKnowledgePathAndRoot = allPathsWithRootsIter.next();

				if (absoluteKnowledgePathAndRoot.root == localRole) {
					if (paramDir == ParameterKind.OUT || paramDir == ParameterKind.INOUT) {
						localChangeSet.setValue(absoluteKnowledgePathAndRoot.knowledgePath, ((ParamHolder<Object>)actualParams[paramIdx]).value);
					}
				}
				
				paramIdx++;
			}
			
			// Write the changeset back to the knowledge
			localKnowledgeManager.update(localChangeSet, shadowKnowledgeManager.getId());			
		} catch (KnowledgeUpdateException | IllegalAccessException | IllegalArgumentException e) {
			throw new TaskInvocationException(String.format("Error when invoking a knowledge exchange for ensemble: %s", ensembleController.getEnsembleDefinition().getName()), e);			
		} catch (InvocationTargetException e) {
			Log.e("Knowledge exchange returned an exception.", e.getTargetException());
			return false;
		}		
		
		return true;
	}

	private void invokeRatingsProcess(String shadowComponentId) throws TaskInvocationException {
		ComponentInstance component = ensembleController.getComponentInstance();
		KnowledgeManager knowledgeManager = component.getKnowledgeManager();
		RatingsProcess process = component.getRatingsProcess();
		
		// check if component has rating process defined
		if (process == null) return;
		
		Collection<Parameter> formalParams = process.getParameters();
		Collection<KnowledgePath> inPaths = new LinkedList<KnowledgePath>();
		Collection<KnowledgePath> allPaths = new LinkedList<KnowledgePath>();
		
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();

			KnowledgePath absoluteKnowledgePath;
			// FIXME: The call to getAbsolutePath is in theory wrong, because this way we are not obtaining the
			// knowledge within one transaction. But fortunately this is not a problem with the single 
			// threaded scheduler we have at the moment, because once the invoke method starts there is no other
			// activity whatsoever in the system.
			try {  
				absoluteKnowledgePath = KnowledgePathHelper.getAbsolutePath(formalParam.getKnowledgePath(), knowledgeManager);
			} catch (KnowledgeNotFoundException e) {
				throw new TaskInvocationException(
						String.format("Knowledge path (%s) could not be resolved.", e.getNotFoundPath()), e);
			}
			
			if (paramDir == ParameterKind.IN) {
				inPaths.add(absoluteKnowledgePath);
			}
			
			allPaths.add(absoluteKnowledgePath);
		}
		
		ValueSet inKnowledge;
		
		try {
			inKnowledge = knowledgeManager.get(inPaths);
		} catch (KnowledgeNotFoundException e) {		
			throw new TaskInvocationException(
					String.format("Input knowledge (%s) of a component rating process not found in the knowledge manager %s.", 
							e.getNotFoundPath(), 							
							knowledgeManager.getId()
					), e);
		}

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[formalParams.size()];
		
		Map<KnowledgePath, RatingsHolder> ratingsHolders = new HashMap<>();
		int paramIdx = 0;
		Iterator<KnowledgePath> allPathsIter = allPaths.iterator();
		
		for (Parameter formalParam : formalParams) {
			ParameterKind paramDir = formalParam.getKind();
			Class paramType = formalParam.getType();
			KnowledgePath absoluteKnowledgePath = allPathsIter.next();			
			String knowledgeAuthor = knowledgeManager.getAuthor(absoluteKnowledgePath);
			
			if (paramDir == ParameterKind.IN) {
				actualParams[paramIdx] = inKnowledge.getValue(absoluteKnowledgePath);
				
			} else if (paramDir == ParameterKind.RATING) {				
				if (paramType.equals(ReadonlyRatingsHolder.class)) {					
					actualParams[paramIdx] = ratingsManager.createReadonlyRatingsHolder(knowledgeAuthor, absoluteKnowledgePath);
				} else {
					RatingsHolder ratingsHolder = ratingsManager.createRatingsHolder(component.getKnowledgeManager().getId(), knowledgeAuthor, absoluteKnowledgePath);
					ratingsHolders.put(absoluteKnowledgePath, ratingsHolder);
					actualParams[paramIdx] = ratingsHolder;
				}				 		
			}
			
			paramIdx++;
		}
		
		try {
			// Set the current process's context
			// FIXME is this necessary? The process context seems nowhere to be used.
			//ProcessContext.addContext(componentProcess, scheduler, architecture);
			
			// Call the rating process method
			process.getMethod().invoke(null, actualParams);
			
			ratingsManager.createRatingsChangeSet(ratingsHolders);
			ratingsManager.update(ratingsManager.getRatingsChangeSets());
		} catch (IllegalAccessException | IllegalArgumentException e) {
			Log.e("Can't invoke rating process method of component " + component.getName());
			throw new TaskInvocationException("Error when invoking a process method.", e);
		} catch (InvocationTargetException e) {
			Log.w("Rating process method returned an exception.", e.getTargetException());
		}		
	}
	
	/**
	 * Invokes the membership + knowledge exchange on each relevant pair in which the local component instance plays the role of member/coordinator.
	 * It optimizes the case when the trigger comes from the shadow knowledge manager. In such a case, it evaluates the membership + knowledge exchange only
	 * w.r.t. the particular shadow knowledge manager. 
	 * 
	 * Note that for each pair, it evaluates the membership + knowledge exchange twice. This is because assumes the local component instance
	 * to be a coordinator and then it assumes the local component instance to be a member.
	 * 
	 * @param trigger the trigger that caused the task invocation. It is either a {@link PeriodicTrigger} in case this triggering
	 * is because of new period or a trigger given by task to the scheduler when invoking the trigger listener.
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {

		EnsembleContext.addContext(scheduler);
		
		if (trigger instanceof ShadowKMChangeTrigger) {
			// If the trigger pertains to a shadow knowledge manager
			ReadOnlyKnowledgeManager shadowKnowledgeManager = ((ShadowKMChangeTrigger)trigger).shadowKnowledgeManager;

			evaluateMembershipAndPerformExchange(shadowKnowledgeManager);
			
		} else {
			// If the trigger is periodic trigger or pertains to the local knowledge manager, iterate over all shadow knowledge managers
			ShadowKnowledgeManagerRegistry shadows = ensembleController.getComponentInstance().getShadowKnowledgeManagerRegistry();

			for (ReadOnlyKnowledgeManager shadowKnowledgeManager : shadows.getShadowKnowledgeManagers()) {
				evaluateMembershipAndPerformExchange(shadowKnowledgeManager);
			}			
		}
	}

	private void evaluateMembershipAndPerformExchange(ReadOnlyKnowledgeManager shadowKnowledgeManager) throws TaskInvocationException {
		boolean coordinatorExchangePerformed = false;
		boolean memberExchangePerformed = false;
		
		// Invoke the membership condition and if the membership condition returned true, invoke the knowledge exchange
		if (checkMembership(PathRoot.COORDINATOR, shadowKnowledgeManager) && securityChecker.checkSecurity(PathRoot.COORDINATOR, shadowKnowledgeManager)) {
			architectureObserver.ensembleFormed(ensembleController.getEnsembleDefinition(), ensembleController.getComponentInstance(),
					ensembleController.getComponentInstance().getKnowledgeManager().getId(),shadowKnowledgeManager.getId());
			coordinatorExchangePerformed = performExchange(PathRoot.COORDINATOR, shadowKnowledgeManager);					
		}
		// Do the same with the roles exchanged
		if (checkMembership(PathRoot.MEMBER, shadowKnowledgeManager) && securityChecker.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager)) {
			architectureObserver.ensembleFormed(ensembleController.getEnsembleDefinition(), ensembleController.getComponentInstance(),
					shadowKnowledgeManager.getId(), ensembleController.getComponentInstance().getKnowledgeManager().getId());
			memberExchangePerformed = performExchange(PathRoot.MEMBER, shadowKnowledgeManager);			
		}
		
		if (coordinatorExchangePerformed || memberExchangePerformed) {
			invokeRatingsProcess(shadowKnowledgeManager.getId());
		}
	}


	/**
	 * Returns the period associated with the ensemble in the in the meta-model as the {@link TimeTrigger}. Note that the {@link EnsembleTask} assumes that there is at most
	 * one instance of {@link TimeTrigger} associated with the ensemble in the meta-model.
	 * 
	 * @return Periodic trigger or null no period is associated with the task.
	 */
	@Override
	public TimeTrigger getTimeTrigger() {
		for (Trigger trigger : ensembleController.getEnsembleDefinition().getTriggers()) {
			if (trigger instanceof TimeTrigger) {
				return ((TimeTrigger) trigger);
			}
		}
		
		return null;
	}
	
	public String toString() {
		return ensembleController.getEnsembleDefinition().getName();
	}
}

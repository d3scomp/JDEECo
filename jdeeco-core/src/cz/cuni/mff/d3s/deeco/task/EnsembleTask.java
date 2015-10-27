package cz.cuni.mff.d3s.deeco.task;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.integrity.RatingsChangeSet;
import cz.cuni.mff.d3s.deeco.integrity.RatingsHolder;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistry;
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
import cz.cuni.mff.d3s.deeco.model.runtime.impl.TriggerImpl;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.runtime.DEECoContainer;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogRecord;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogger;
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
	 * Reference to the corresponding {@link LocalSecurityChecker} 
	 */
	LocalSecurityChecker securityChecker;
	
	/**
	 * Reference to the ratings manager
	 */
	RatingsManager ratingsManager;

	/**
	 * The {@link DEECoContainer} this {@link EnsembleTask} belongs to.
	 */
	private DEECoContainer deecoContainer;
	
	/**
	 * Reference to the class that manages memberships and knowledge exchanges
	 */
	private EnsembleDataExchange ensembleDataExchange;
	
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

	/**
	 * The {@link RuntimeLogRecord) specific to the {@link EnsembleTask} ensemble status log message.
	 * 
	 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
	 */
	private class EnsembleLogRecord extends RuntimeLogRecord
	{
		/**
		 * Construct the {@link EnsembleLogRecord} instance.
		 */
		public EnsembleLogRecord() {
			super("EnsembleTask", new HashMap<String, Object>());
		}
		
	}
	
	ShadowsTriggerListenerImpl shadowsTriggerListener = new ShadowsTriggerListenerImpl();

	public EnsembleTask(EnsembleController ensembleController, Scheduler scheduler, 
			KnowledgeManagerContainer kmContainer, RatingsManager ratingsManager) {
		super(scheduler);
		
		this.ensembleController = ensembleController;
		this.securityChecker = new LocalSecurityChecker(ensembleController, kmContainer);
		this.ratingsManager = ratingsManager;
		this.ensembleDataExchange = new EnsembleDataExchange(ensembleController.getEnsembleDefinition(), ratingsManager);
	}
	
	/**
	 * Initialize the {@link EnsembleTask} with the given {@link DEECoContainer} instance.
	 * @param deecoContainer The {@link DEECoContainer} to initialize the {@link EnsembleTask} with.
	 * Shouldn't be null.
	 */
	public void init(DEECoContainer deecoContainer)
	{
		this.deecoContainer = deecoContainer;
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
		if(componentInstance == null) return;
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
	 * Invokes the method marked with the {@link RatingsProcess} annotation.
	 * @param shadowComponentId
	 * 			the ID of the shadow component
	 * @throws TaskInvocationException
	 */
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
			
			// only IN and RATING params are allowed
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
			
			// update the ratings container and prepare the change set for distribution
			List<RatingsChangeSet> changes = ratingsManager.createRatingsChangeSet(ratingsHolders);
			ratingsManager.update(changes);
			ratingsManager.addToPendingChangeSets(changes);
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
		if (deecoContainer == null)
			throw new IllegalStateException(String.format(
					"The %s class is not initialized.", "EnsembleTask"));
		
		EnsembleContext.addContext(scheduler.getTimer());
		
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
		
		KnowledgeManager localKnowledgeManager = ensembleController.getComponentInstance().getKnowledgeManager();
		
		// Invoke the membership condition and if the membership condition returned true, invoke the knowledge exchange
		if (ensembleDataExchange.checkMembership(PathRoot.COORDINATOR, localKnowledgeManager, shadowKnowledgeManager) 
				&& securityChecker.checkSecurity(PathRoot.COORDINATOR, shadowKnowledgeManager)) {
			ensembleDataExchange.performExchange(PathRoot.COORDINATOR, localKnowledgeManager, shadowKnowledgeManager);
			coordinatorExchangePerformed = true;

			logMembershipStatus(ensembleController.getEnsembleDefinition().getName(),
					shadowKnowledgeManager.getId(),
					ensembleController.getComponentInstance().getKnowledgeManager().getId(),
					true);
		}
		else
		{
			logMembershipStatus(ensembleController.getEnsembleDefinition().getName(),
					shadowKnowledgeManager.getId(),
					ensembleController.getComponentInstance().getKnowledgeManager().getId(),
					false);
		}
				
		// Do the same with the roles exchanged
		if (ensembleDataExchange.checkMembership(PathRoot.MEMBER, localKnowledgeManager, shadowKnowledgeManager)
				&& securityChecker.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager)) {
			ensembleDataExchange.performExchange(PathRoot.MEMBER, localKnowledgeManager, shadowKnowledgeManager);
			memberExchangePerformed = true;
		}
		else
		{
			logMembershipStatus(ensembleController.getEnsembleDefinition().getName(),
					ensembleController.getComponentInstance().getKnowledgeManager().getId(),
					shadowKnowledgeManager.getId(),
					false);	
		}
		
		if (coordinatorExchangePerformed || memberExchangePerformed) {
			invokeRatingsProcess(shadowKnowledgeManager.getId());
		}
	}

	/**
	 * Log the current ensemble membership status (ensemble exists/doesn't exist) using the {@link RuntimeLogger}.
	 * @param ensembleName is the name of tested ensemble.
	 * @param coordinatorID is the identifier of the coordinator in the tested ensemble.
	 * @param memberID is the identifier of the member in the tested ensemble.
	 * @param membership is the membership validity value (ensemble exists(true)/doesn't exist(false)).
	 * @throws TaskInvocationException Thrown if there is a problem writing into the log files.
	 */
	private void logMembershipStatus(String ensembleName, String coordinatorID, String memberID, boolean membership) throws TaskInvocationException
	{
		EnsembleLogRecord record = new EnsembleLogRecord();
		record.getValues().put("ensembleName", ensembleName);
		record.getValues().put("coordinatorID", coordinatorID);
		record.getValues().put("memberID", memberID);
		record.getValues().put("membership", membership);
		
		try
		{
			deecoContainer.getRuntimeLogger().log(record);
		}
		catch(IOException e)
		{
			throw new TaskInvocationException(e);
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

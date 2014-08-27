package cz.cuni.mff.d3s.deeco.task;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.TSParamHolder;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * The implementation of {@link Task} that corresponds to a component process. This class is responsible for (a) registering triggers with the
 * local knowledge of the component instance, and (b) to execute the process method when invoked by the scheduler/executor.
 * 
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class TransitionTask extends Task {
	
	/**
	 * Reference to the corresponding {@link ComponentProcess} in the runtime metadata 
	 */
	ComponentProcess componentProcess;
	
	/**
	 * Implementation of the trigger listener, which is registered in the local knowledge manager. When called, it calls the listener registered by
	 * {@link Task#setTriggerListener(TaskTriggerListener)}.
	 * 
	 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
	 *
	 */
	private class KnowledgeManagerTriggerListenerImpl implements TriggerListener {

		/* (non-Javadoc)
		 * @see cz.cuni.mff.d3s.deeco.knowledge.TriggerListener#triggered(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
		 */
		@Override
		public void triggered(Trigger trigger) {
			if (listener != null) {
				listener.triggered(TransitionTask.this, trigger);
			}
		}
	}
	KnowledgeManagerTriggerListenerImpl knowledgeManagerTriggerListener = new KnowledgeManagerTriggerListenerImpl();
	
	public TransitionTask(ComponentProcess componentProcess, Scheduler scheduler) {
		super(scheduler);
		this.componentProcess = componentProcess;
	}

	/**
	 * Invokes the component process. Essentially it works in these steps:
	 * <ol>
	 *   <li>It resolves the IN and INOUT paths of the process parameters and retrieves the knowledge from the local knowledge manager.</li>
	 *   <li>It invokes the process method with the values obtained in the previous step.</li>
	 *   <li>It updates the local knowledge manager with INOUT and OUT values gotten from the process.</li>
	 * </ol>
	 * 
	 * The INOUT and OUT values are wrapped in {@link ParamHolder}. OUT parameters are initialized with empty {@link ParamHolder}, 
	 * i.e. {@link ParamHolder#value} is set to <code>null</code>.
	 * 
	 * @param trigger the trigger that caused the task invocation. It is either a {@link PeriodicTrigger} in case this triggering
	 * is because of new period or a trigger given by task to the scheduler when invoking the trigger listener.
	 *  
	 * @throws TaskInvocationException signifies a problem in executing the task including the case when parameters cannot be retrieved from / updated in
	 * the knowledge manager.
	 */
	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {
		// Obtain parameters from the knowledge
		KnowledgeManager knowledgeManager = componentProcess.getComponentInstance().getKnowledgeManager();
		Collection<Parameter> formalParams = componentProcess.getParameters();

		Collection<KnowledgePath> inPaths = new LinkedList<KnowledgePath>();
		Collection<KnowledgePath> allPaths = new LinkedList<KnowledgePath>();
		for (Parameter formalParam : formalParams) {
			ParameterDirection paramDir = formalParam.getDirection();

			KnowledgePath absoluteKnowledgePath;
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		assert(listener != null);
		
		KnowledgeManager km = componentProcess.getComponentInstance().getKnowledgeManager();
		
		for (Trigger trigger : componentProcess.getTriggers()) {
			km.register(trigger, knowledgeManagerTriggerListener);
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		KnowledgeManager km = componentProcess.getComponentInstance().getKnowledgeManager();
		
		for (Trigger trigger : componentProcess.getTriggers()) {
			km.unregister(trigger, knowledgeManagerTriggerListener);
		}		
	}

	/**
	 * Returns the period associated with the process in the in the meta-model as the {@link TimeTrigger}. Note that the {@link TransitionTask} assumes that there is at most
	 * one instance of {@link TimeTrigger} associated with the process in the meta-model.
	 * 
	 * @return Periodic trigger or null no period is associated with the task.
	 */
	@Override
	public TimeTrigger getTimeTrigger() {
		for (Trigger trigger : componentProcess.getTriggers()) {
			if (trigger instanceof TimeTrigger) {
				return ((TimeTrigger) trigger);
			}
		}
		
		return null;
	}
}

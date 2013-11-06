package cz.cuni.mff.d3s.deeco.task;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PeriodicTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class ProcessTask extends Task {
	
	ComponentProcess componentProcess;
	
	private class KnowledgeManagerTriggerListenerImpl implements TriggerListener {

		/* (non-Javadoc)
		 * @see cz.cuni.mff.d3s.deeco.knowledge.TriggerListener#triggered(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
		 */
		@Override
		public void triggered(Trigger trigger) {
			listener.triggered(ProcessTask.this);
		}
	}
	KnowledgeManagerTriggerListenerImpl knowledgeManagerTriggerListener = new KnowledgeManagerTriggerListenerImpl();
	
	public ProcessTask(ComponentProcess componentProcess, Scheduler scheduler) {
		super(scheduler);
		this.componentProcess = componentProcess;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#invoke()
	 */
	@Override
	public void invoke() throws TaskInvocationException {
		// Obtain parameters from the knowledge
		KnowledgeManager knowledgeManager = componentProcess.getComponentInstance().getKnowledgeManager();
		Collection<Parameter> formalParams = componentProcess.getParameters();

		Collection<KnowledgePath> inPaths = new LinkedList<KnowledgePath>();
		for (Parameter formalParam : formalParams) {
			ParameterDirection paramDir = formalParam.getDirection();
			
			if (paramDir == ParameterDirection.IN || paramDir == ParameterDirection.INOUT) {
				// TODO: Expand the path here. At this point, this works only for absolute paths
				inPaths.add(formalParam.getKnowledgePath());
			}
		}
		
		ValueSet inKnowledge;
		
		try {
			inKnowledge = knowledgeManager.get(inPaths);
		} catch (KnowledgeNotFoundException e) {
			throw new TaskInvocationException("Input knowledge of a component process not found in the knowledge manager.", e);
		}

		// Construct the parameters for the process method invocation
		Object[] actualParams = new Object[formalParams.size()];
		
		int paramIdx = 0;
		for (Parameter formalParam : formalParams) {
			ParameterDirection paramDir = formalParam.getDirection();

			if (paramDir == ParameterDirection.IN) {
				actualParams[paramIdx] = inKnowledge.getValue(formalParam.getKnowledgePath());
				
			} else if (paramDir == ParameterDirection.OUT) {
				actualParams[paramIdx] = new ParamHolder<Object>();

			} else if (paramDir == ParameterDirection.INOUT) {
				actualParams[paramIdx] = new ParamHolder<Object>(inKnowledge.getValue(formalParam.getKnowledgePath()));
			}
			// TODO: We could have an option of not creating the wrapper. That would make it easier to work with mutable out types.
			// TODO: We need some way of handling insertions/deletions in a hashmap.
			
			paramIdx++;
		}
		
		try {
			// Call the process method
			componentProcess.getMethod().invoke(null, actualParams);
			
			// Create a changeset
			ChangeSet changeSet = new ChangeSet();
			
			paramIdx = 0;
			for (Parameter formalParam : formalParams) {
				ParameterDirection paramDir = formalParam.getDirection();

				if (paramDir == ParameterDirection.OUT || paramDir == ParameterDirection.INOUT) {
					changeSet.setValue(formalParam.getKnowledgePath(), ((ParamHolder<Object>)actualParams[paramIdx]).value);
				}

				paramIdx++;
			}
			
			// Write the changeset back to the knowledge
			knowledgeManager.update(changeSet);
			
		} catch (IllegalAccessException | IllegalArgumentException e) {
			throw new TaskInvocationException("Error when invoking a process method.", e);
		} catch (InvocationTargetException e) {
			Log.i("Process method returned an exception.", e.getTargetException());
		}		
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.knowledge.TriggerListener#triggered(cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger)
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
	 * Returns the period associated with the process in the in the meta-model as the {@link PeriodicTrigger}. Note that the {@link ProcessTask} assumes that there is at most
	 * one instance of {@link PeriodicTrigger} associated with the process in the meta-model.
	 * 
	 * @return Period in miliseconds or -1 if no period is associated with the task.
	 */
	@Override
	public long getSchedulingPeriod() {
		for (Trigger trigger : componentProcess.getTriggers()) {
			if (trigger instanceof PeriodicTrigger) {
				return ((PeriodicTrigger) trigger).getPeriod();
			}
		}
		
		return -1;
	}
}

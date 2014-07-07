package cz.cuni.mff.d3s.deeco.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValueDefinition;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;

import org.eclipse.emf.common.util.EList;

/**
 * The implementation of {@link Task} that corresponds to a component process. This class is responsible for (a) registering triggers with the
 * local knowledge of the component instance, and (b) to execute the process method when invoked by the scheduler/executor.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public class StateSpaceModelTask extends Task {
	
	/**
	 * Reference to the corresponding {@link ComponentProcess} in the runtime metadata 
	 */
	StateSpaceModelDefinition stateSpaceModel;

	private static double lastTime;
	private static final double SEC_NANOSECOND_FACTOR = 1000000000;

	
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
				listener.triggered(StateSpaceModelTask.this, trigger);
			}
		}
	}
	KnowledgeManagerTriggerListenerImpl knowledgeManagerTriggerListener = new KnowledgeManagerTriggerListenerImpl();
	
	public StateSpaceModelTask(StateSpaceModelDefinition stateSpaceModel, Scheduler scheduler) {
		super(scheduler);
		this.stateSpaceModel = stateSpaceModel;
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
		
//		KnowledgeManager knowledgeManager = stateSpaceModel.getComponentInstance().getKnowledgeManager();
//		List<KnowledgePath> kps = new ArrayList<KnowledgePath>();
//		kps.add(stateSpaceModel.getTriggerKowledgePath());
//		ValueSet values = knowledgeManager.get(kps);
//		if()
		
		
		double currentTime = System.nanoTime() / SEC_NANOSECOND_FACTOR;
		double[] boundaries = new double[1];
		double 	startTime = startTime();
		double dt = lastTime - startTime;
		// ---------------------- knowledge evaluation --------------------------------
			
		InaccurateValueDefinition inDerv = new InaccurateValueDefinition<>();
		inDerv = stateSpaceModel.getModel().getModelBoundaries(stateSpaceModel.getInStates());
		stateSpaceModel.getDerivationStates().set(0,inDerv);
		
		FirstOrderIntegrator integrator = new MidpointIntegrator(1);
		integrator.setMaxEvaluations((int) dt);
		FirstOrderDifferentialEquations f = new Derivation(); 
		//-----------------------  Boundaries    ----------------------------------
		for (int i = 0; i < stateSpaceModel.getInStates().size(); i++) {
			computeBoundary(boundaries, i, f, integrator, startTime, currentTime);
			System.out.println("boundaries : "+boundaries.length+"     "+stateSpaceModel.getDerivationStates().get(0).minBoundary+"  "+stateSpaceModel.getDerivationStates().get(0).maxBoundary);
		}

	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#registerTriggers()
	 */
	@Override
	protected void registerTriggers() {
		assert(listener != null);
		
		KnowledgeManager km = stateSpaceModel.getComponentInstance().getKnowledgeManager();
		
		for (Trigger trigger : stateSpaceModel.getTriggers()) {
			km.register(trigger, knowledgeManagerTriggerListener);
		}
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.task.Task#unregisterTriggers()
	 */
	@Override
	protected void unregisterTriggers() {
		KnowledgeManager km = stateSpaceModel.getComponentInstance().getKnowledgeManager();
		
		for (Trigger trigger : stateSpaceModel.getTriggers()) {
			km.unregister(trigger, knowledgeManagerTriggerListener);
		}		
	}

	/**
	 * Returns the period associated with the process in the in the meta-model as the {@link TimeTrigger}. Note that the {@link StateSpaceModelTask} assumes that there is at most
	 * one instance of {@link TimeTrigger} associated with the process in the meta-model.
	 * 
	 * @return Periodic trigger or null no period is associated with the task.
	 */
	@Override
	public TimeTrigger getTimeTrigger() {
		for (Trigger trigger : stateSpaceModel.getTriggers()) {
			if (trigger instanceof TimeTrigger) {
				return ((TimeTrigger) trigger);
			}
		}
		
		return null;
	}
	

	public void init(Double creationTime) {
		initBoundaries(stateSpaceModel.getInStates(), creationTime);
		initBoundaries(stateSpaceModel.getDerivationStates(), creationTime);
		lastTime = creationTime;
	}
	

	private void computeBoundary(double[] boundaries, int i, FirstOrderDifferentialEquations f,
			FirstOrderIntegrator integrator, double startTime, double currentTime) {
		
		double[] minBoundaries = new double[1];
		minBoundaries[0] = boundaries[0];
		integrator.integrate(f, startTime, minBoundaries, currentTime, minBoundaries);
		stateSpaceModel.getInStates().get(i).minBoundary = stateSpaceModel.getInStates().get(i).minBoundary.doubleValue() + minBoundaries[0];
		boundaries[0] = minBoundaries[0];
		
		double[] maxBoundaries = new double[1];
		maxBoundaries[0] = boundaries[1];
		integrator.integrate(f, startTime, maxBoundaries, currentTime, maxBoundaries);
		stateSpaceModel.getDerivationStates().get(i).maxBoundary = stateSpaceModel.getDerivationStates().get(i).maxBoundary.doubleValue() + maxBoundaries[0];
		boundaries[1] = maxBoundaries[0];
	}

	private void resetBoundaries(){
		for (int i = 0; i < stateSpaceModel.getInStates().size(); i++) {
			stateSpaceModel.getInStates().get(i).minBoundary = stateSpaceModel.getInStates().get(i).value; 
			stateSpaceModel.getInStates().get(i).maxBoundary = stateSpaceModel.getInStates().get(i).value; 
		}
	}
	
	private Double startTime(){
		
		Double startTime;
		if (stateSpaceModel.getInStates().get(0).creationTime <= lastTime) {
			startTime = lastTime;
		} else {
			startTime = stateSpaceModel.getInStates().get(0).creationTime;
			resetBoundaries();
		}
		return startTime;
	}
	
	private static class Derivation implements FirstOrderDifferentialEquations {

		@Override
		public int getDimension() {
			return 1;
		}

		@Override
		public void computeDerivatives(double t, double[] y, double[] yDot)
				throws MaxCountExceededException, DimensionMismatchException {
			int params = 1;
			int order = 1;
			DerivativeStructure x = new DerivativeStructure(params, order, 0,
					y[0]);
			DerivativeStructure f = x.divide(t);
			yDot[0] = f.getValue();
		}
	}
	
	
	private void initBoundaries(EList<InaccurateValueDefinition> eList, Double creationTime){
		for (int i = 0; i < eList.size(); i++) {
				eList.get(i).value = eList.get(i).value;
				eList.get(i).minBoundary = eList.get(i).value;
				eList.get(i).maxBoundary = eList.get(i).value;
				eList.get(i).creationTime = creationTime;
		}
	}

}

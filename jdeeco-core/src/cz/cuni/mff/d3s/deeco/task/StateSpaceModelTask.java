package cz.cuni.mff.d3s.deeco.task;

import java.util.ArrayList;
import java.util.Collection;
//
//import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
//import org.apache.commons.math3.exception.DimensionMismatchException;
//import org.apache.commons.math3.exception.MaxCountExceededException;
//import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
//import org.apache.commons.math3.ode.FirstOrderIntegrator;
//import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModeParamHolder;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;


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

	protected static double lastTime;
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
		init(stateSpaceModel.getComponentInstance().getKnowledgeManager(), 0.0);
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

		try {
					KnowledgeManager knowledgeManager = stateSpaceModel.getComponentInstance().getKnowledgeManager();
					ValueSet values = knowledgeManager.get(stateSpaceModel.getInStates());
					
					double currentTime = System.nanoTime() / SEC_NANOSECOND_FACTOR;
					double 	startTime = startTime(knowledgeManager,values);
					values = knowledgeManager.get(stateSpaceModel.getInStates());

					// ---------------------- knowledge evaluation --------------------------------

					ArrayList<InaccuracyParamHolder> in = new ArrayList<InaccuracyParamHolder>();
					for (KnowledgePath kp : stateSpaceModel.getInStates()) {
						Object v = values.getValue(kp);
						in.add(getValue(v));
					}
					
					InaccuracyParamHolder inDerv = stateSpaceModel.getModel().getModelBoundaries(in);
					stateSpaceModel.setModelValue(inDerv);					
					
					//-----------------------  Boundaries    ----------------------------------
					
					for (int i = 0; i < stateSpaceModel.getInStates().size(); i++) {
						values = knowledgeManager.get(stateSpaceModel.getInStates());
						computeBoundary(knowledgeManager, values, i, startTime, currentTime);
					}
					
					lastTime = currentTime;
			} catch (KnowledgeUpdateException|KnowledgeNotFoundException e) {
				e.printStackTrace();
			}
	}	
	

	
	public void init(KnowledgeManager km, Double creationTime) {
		try {
			initBoundaries(stateSpaceModel.getInStates(), km);
			initBoundaries(stateSpaceModel.getDerivationStates(), km);
			lastTime = creationTime;
		} catch (KnowledgeNotFoundException|KnowledgeUpdateException e) {
			e.printStackTrace();
		}
	}
	

	private void computeBoundary(KnowledgeManager km, ValueSet vs, int i,
			 double startTime, double currentTime) throws KnowledgeUpdateException {
		
		ChangeSet ch = new ChangeSet();
		ChangeSet dervCh = new ChangeSet();
		KnowledgePath inKp = null;
		KnowledgePath dervKp = null;
		InaccuracyParamHolder value = new InaccuracyParamHolder<>();
		InaccuracyParamHolder dervValue = new InaccuracyParamHolder<>();
		
		inKp = stateSpaceModel.getInStates().get(i);
		value = getValue(vs.getValue(inKp));
		
		if(i == 0){
			dervValue = stateSpaceModel.getModelValue();	
		}else{
			dervKp = stateSpaceModel.getDerivationStates().get(i-1);
			dervValue = getValue(vs.getValue(dervKp));
		}
		
		
//		MidpointIntegrator integrator = new MidpointIntegrator(1);
//		FirstOrderDifferentialEquations f = new Derivation(); 
	
		double[] minBoundaries = new double[1];
		minBoundaries[0] = (double) dervValue.minBoundary;
//		integrator.integrate(f, startTime, minBoundaries, currentTime, minBoundaries);
		value.minBoundary = (double)value.minBoundary + minBoundaries[0]*(currentTime-startTime);
		dervValue.minBoundary = minBoundaries[0];
		
		
		double[] maxBoundaries = new double[1];
		maxBoundaries[0] = (double)dervValue.maxBoundary;
//		integrator.integrate(f, startTime, maxBoundaries, currentTime, maxBoundaries);
		value.maxBoundary = (double)value.maxBoundary + maxBoundaries[0]*(currentTime - startTime);
		dervValue.maxBoundary = maxBoundaries[0];

		Object updatedValue = updateValue(value, vs , inKp);
		ch.setValue(inKp, updatedValue);
		km.update(ch);
		
		if(i == 0){
			stateSpaceModel.setModelValue(dervValue);
		}else{
			Object updatedValueDerv = updateValue(dervValue, vs, dervKp);
			dervCh.setValue(dervKp, updatedValueDerv );
			km.update(dervCh);
		}
	}

	
	private InaccuracyParamHolder getValue(Object v){
		InaccuracyParamHolder value = new InaccuracyParamHolder<>();
		if(v instanceof ModeParamHolder){
			ModeParamHolder<Number> mode = (ModeParamHolder)v;
			value.value = mode.value;
			value.creationTime = mode.creationTime;
			value.minBoundary = mode.minBoundary;
			value.maxBoundary = mode.maxBoundary;
		}else if(v instanceof InaccuracyParamHolder){
			value = (InaccuracyParamHolder)v;
		}else
			System.err.println("Wrong value for state space model : "+v);

		return value;
	}
	
	private Object updateValue(InaccuracyParamHolder val, ValueSet vs, KnowledgePath kp){
		if(vs.getValue(kp) instanceof ModeParamHolder){
			ModeParamHolder v = (ModeParamHolder)vs.getValue(kp);
			v.minBoundary = val.minBoundary;
			v.maxBoundary = val.maxBoundary;
			return v;
		}else if(vs.getValue(kp) instanceof InaccuracyParamHolder){
			InaccuracyParamHolder v = (InaccuracyParamHolder)vs.getValue(kp);
			v.minBoundary = val.minBoundary;
			v.maxBoundary = val.maxBoundary;
			return v;			
		}
		return null;
	}

	
	private void resetBoundaries(KnowledgeManager km, ValueSet values, Double ctime) throws KnowledgeUpdateException{
		ChangeSet ch = new ChangeSet();
		
		for (int i = 0; i < stateSpaceModel.getInStates().size(); i++) {
			Object val = values.getValue(stateSpaceModel.getInStates().get(i));
			if(val instanceof ModeParamHolder){
				ModeParamHolder v = (ModeParamHolder)val;
				v.minBoundary = v.value; 
				v.maxBoundary = v.value; 
				ch.setValue(stateSpaceModel.getInStates().get(i), v);
			}if(val instanceof InaccuracyParamHolder){
				InaccuracyParamHolder v = (InaccuracyParamHolder)val;
				v.minBoundary = v.value; 
				v.maxBoundary = v.value; 
				ch.setValue(stateSpaceModel.getInStates().get(i), v);
			}
		}
		km.update(ch);
		stateSpaceModel.setModelValue(new InaccuracyParamHolder<>());
	}
	
	
	private Double startTime(KnowledgeManager km, ValueSet values) throws KnowledgeUpdateException{
		
		Double startTime;
		Object obj = values.getValue(stateSpaceModel.getInStates().get(0));
		Double ctime = 0.0;
		if(obj instanceof ModeParamHolder)
			ctime = ((ModeParamHolder)obj).creationTime;
		else 
			ctime = ((InaccuracyParamHolder)obj).creationTime;
		
		if (ctime <= lastTime) {
			startTime = lastTime;
		} else {
			startTime = ctime;
			resetBoundaries(km,values,ctime);
		}
		return startTime;
	}
	
	
	private void initBoundaries(Collection<KnowledgePath> inStates, KnowledgeManager km) throws KnowledgeNotFoundException, KnowledgeUpdateException {
		ChangeSet ch = new ChangeSet();
		ValueSet vals = km.get(inStates);
		for (KnowledgePath knowledgePath : inStates) {
			Object obj = vals.getValue(knowledgePath);
			if(obj instanceof ModeParamHolder){
				ModeParamHolder newVal = new ModeParamHolder<>();
				ModeParamHolder oldVal = ((ModeParamHolder)obj);
				newVal.value = oldVal.value;
				newVal.minBoundary = oldVal.value;
				newVal.maxBoundary = oldVal.value;
				newVal.creationTime = oldVal.creationTime;
				newVal.trans.addAll(oldVal.trans);
				ch.setValue(knowledgePath, newVal);	
			}else {
				InaccuracyParamHolder newVal = new InaccuracyParamHolder<>();
				InaccuracyParamHolder oldVal = ((InaccuracyParamHolder)obj);
				newVal.value = oldVal.value;
				newVal.minBoundary = oldVal.value;
				newVal.maxBoundary = oldVal.value;
				newVal.creationTime = oldVal.creationTime;
				ch.setValue(knowledgePath, newVal);	
			}
		}
		km.update(ch);
		stateSpaceModel.setModelValue(new InaccuracyParamHolder<>());
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
	
//	private static class Derivation implements FirstOrderDifferentialEquations {
//
//		@Override
//		public int getDimension() {
//			return 1;
//		}
//
//		@Override
//		public void computeDerivatives(double t, double[] y, double[] yDot)
//				throws MaxCountExceededException, DimensionMismatchException {
//			int params = 1;
//			int order = 1;
//			DerivativeStructure x = new DerivativeStructure(params, order, 0,
//					y[0]);
//			DerivativeStructure f = x.divide(t);
//			yDot[0] = f.getValue();
//		}
//	}
}

package cz.cuni.mff.d3s.deeco.task;

import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.TriggerListener;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.StateSpaceModelDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.TimeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;


/**
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 *
 */
public class StateSpaceModelTask extends Task {
	
	StateSpaceModelDefinition stateSpaceModel;

	protected static double lastTime;
	protected static double lastCreationTime;
	private static final double SEC_NANOSECOND_FACTOR = 1000000000;

	
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


	@Override
	public void invoke(Trigger trigger) throws TaskInvocationException {

		try {
					KnowledgeManager knowledgeManager = stateSpaceModel.getComponentInstance().getKnowledgeManager();
					ValueSet values = knowledgeManager.get(stateSpaceModel.getStates());
					
					double currentTime = System.nanoTime() / SEC_NANOSECOND_FACTOR;
					double 	startTime = startTime(knowledgeManager,values);
					values = knowledgeManager.get(stateSpaceModel.getStates());

					// ---------------------- knowledge evaluation --------------------------------

					ArrayList<InaccuracyParamHolder> in = new ArrayList<InaccuracyParamHolder>();
					for (KnowledgePath kp : stateSpaceModel.getStates()) {
						Object v = values.getValue(kp);
						in.add(getValue(v));
					}
					
					InaccuracyParamHolder inDerv = stateSpaceModel.getModel().getModelBoundaries(in);
					stateSpaceModel.setModelValue(inDerv);					
					//-----------------------  Boundaries    ----------------------------------
					
					MidpointIntegrator integrator = new MidpointIntegrator(1);
					FirstOrderDifferentialEquations f = new Derivation();

					double[] v = new double[25];
					double[] vMin = new double[25];
					double[] vMax = new double[25];
					//what about the getValues
					values = knowledgeManager.get(stateSpaceModel.getStates());
					int index = stateSpaceModel.getStates().size();
					for (int i = 0; i < index; i++) {
						InaccuracyParamHolder elem = ((InaccuracyParamHolder)values.getValue(stateSpaceModel.getStates().get(i)));
						v[i] = (double) elem.minBoundary;
					}
					v[index] = (double)stateSpaceModel.getModelValue().minBoundary;
					integrator.integrate(f, 0, v, (currentTime-startTime), vMin);
					
					for (int i = 0; i < index; i++) {
						InaccuracyParamHolder elem = ((InaccuracyParamHolder)values.getValue(stateSpaceModel.getStates().get(i)));
						v[i] = (double) elem.maxBoundary;
					}
					v[index] = (double)stateSpaceModel.getModelValue().maxBoundary;
					integrator.integrate(f, 0, v, (currentTime-startTime), vMax);
//					System.out.println("curretn time : "+currentTime+"  , start time : "+startTime+" ,  - : "+(currentTime-startTime));
					setBoundaries(knowledgeManager, values, vMin, vMax);
					lastTime = currentTime;
					
			} catch (KnowledgeUpdateException|KnowledgeNotFoundException e) {
				e.printStackTrace();
			}
	}	
	

	
	public void init(KnowledgeManager km, Double creationTime) {
		try {
			initBoundaries(stateSpaceModel.getStates(), km);
			lastTime = creationTime;
		} catch (KnowledgeNotFoundException|KnowledgeUpdateException e) {
			e.printStackTrace();
		}
	}
	
	private void setBoundaries(KnowledgeManager km, ValueSet values, double[] vMin, double[] vMax) throws KnowledgeUpdateException {
		
		ChangeSet ch = new ChangeSet();
		InaccuracyParamHolder dervValue = new InaccuracyParamHolder<>();
		
		for (int i = 0; i < stateSpaceModel.getStates().size(); i++) {
			InaccuracyParamHolder value = new InaccuracyParamHolder<>();
			KnowledgePath inKp = stateSpaceModel.getStates().get(i);
			InaccuracyParamHolder elem = ((InaccuracyParamHolder)values.getValue(inKp));
			value.setWithInaccuracy(elem);
			value.minBoundary = vMin[i];
			value.maxBoundary = vMax[i];
			//put exception for inaccuracy type
			ch.setValue(inKp, value);
		}
		km.update(ch);
	
		dervValue.minBoundary = vMin[stateSpaceModel.getStates().size()];
		dervValue.maxBoundary = vMax[stateSpaceModel.getStates().size()];	
		stateSpaceModel.setModelValue(dervValue);
	}
	
	
	
	private InaccuracyParamHolder getValue(Object v){
		InaccuracyParamHolder value = new InaccuracyParamHolder<>();
		if(v instanceof InaccuracyParamHolder){
			value = (InaccuracyParamHolder)v;
		}else
			System.err.println("Wrong value for state space model : "+v);

		return value;
	}
	
	private void resetBoundaries(KnowledgeManager km, ValueSet values, Double ctime) throws KnowledgeUpdateException{
		ChangeSet ch = new ChangeSet();
		
		for (int i = 0; i < stateSpaceModel.getStates().size(); i++) {
			Object val = values.getValue(stateSpaceModel.getStates().get(i));
			if(val instanceof InaccuracyParamHolder){
				InaccuracyParamHolder v = (InaccuracyParamHolder)val;
				v.minBoundary = v.value; 
				v.maxBoundary = v.value; 
				ch.setValue(stateSpaceModel.getStates().get(i), v);
			}
		}
		km.update(ch);
		stateSpaceModel.setModelValue(new InaccuracyParamHolder<>());
	}
	
	
	private Double startTime(KnowledgeManager km, ValueSet values) throws KnowledgeUpdateException{
		
		Double startTime;
		Object obj = values.getValue(stateSpaceModel.getStates().get(0));
		Double ctime = ((InaccuracyParamHolder)obj).creationTime;
//		if(lastCreationTime == 0){
//			lastCreationTime = ctime;
//			lastTime = ctime;
//		}
		
		if(lastCreationTime == ctime){
			startTime = lastTime;
		}else{
			lastCreationTime = ctime;
			startTime = ctime;
			resetBoundaries(km,values,ctime);	
		}
//		System.out.println("creationtime : "+ctime+" , lastcreationtime : "+lastCreationTime+"   starttime : "+startTime);
		return startTime;
	}
	
	
	private void initBoundaries(Collection<KnowledgePath> inStates, KnowledgeManager km) throws KnowledgeNotFoundException, KnowledgeUpdateException {
		ChangeSet ch = new ChangeSet();
		ValueSet vals = km.get(inStates);
		for (KnowledgePath knowledgePath : inStates) {
			Object obj = vals.getValue(knowledgePath);
			InaccuracyParamHolder newVal = new InaccuracyParamHolder<>();
			InaccuracyParamHolder oldVal = ((InaccuracyParamHolder)obj);
			newVal.value = oldVal.value;
			newVal.minBoundary = oldVal.value;
			newVal.maxBoundary = oldVal.value;
			newVal.creationTime = oldVal.creationTime;
			ch.setValue(knowledgePath, newVal);	
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
	
	private static class Derivation implements FirstOrderDifferentialEquations {

		@Override
		public int getDimension() {
			return 25;
		}

		@Override
		public void computeDerivatives(double t, double[] y, double[] yDot)
				throws MaxCountExceededException, DimensionMismatchException {
			for (int i = 0; i < y.length-1; i++) {
				yDot[i] = y[i+1];
//				yDot[i] = y[i] + y[i+1]*t;
//				System.out.println(yDot[i]+" = "+y[i]+" + "+y[i+1]+" * "+t);
			}
		}
	}
}

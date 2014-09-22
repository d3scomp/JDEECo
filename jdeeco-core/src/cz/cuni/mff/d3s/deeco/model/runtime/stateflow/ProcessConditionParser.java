package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.mvel2.MVEL;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Transition;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;



public class ProcessConditionParser {
	
	public static boolean init = false;
	
	public static boolean checkTimeStampEquality(Object original, Object value) {
		InaccuracyParamHolder oldV = getInaccurateValue(original);
		InaccuracyParamHolder newV = getInaccurateValue(value);
		if(oldV.creationTime == newV.creationTime && oldV.value == newV.value)
			return true;
		return false;
	}

	//deterministic -> one return
	public static Transition returnTransitions(ComponentInstance componentInstance, KnowledgeManager km, ComponentProcess process) {

		ValueSet vs;
		try {
			vs = returnKnowledge(componentInstance,km,process);
			Map<String, Object> context = getContext(vs);
			Transition selectedTransitions = null;
			Boolean result = false;
			
			List<Boolean> index = isReachable(process.getTransitions());
			EList<Transition> transitions = process.getTransitions();
			for (int i = 0; i < index.size(); i++) {
				if(index.get(i)){
					if(transitions.get(i).getCondition().equals(""))
						result = true;
					else{
						Object eval = MVEL.eval(transitions.get(i).getCondition(), context);
						result = result || (Boolean)eval;
						if((Boolean)eval) {
							selectedTransitions = transitions.get(i);
						}
					}
				}
			}
				
			return selectedTransitions;
		} catch (KnowledgeNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}	

	private static ValueSet returnKnowledge(ComponentInstance componentInstance , KnowledgeManager km, ComponentProcess process) throws KnowledgeNotFoundException {
		ArrayList<KnowledgePath> kps = new ArrayList<KnowledgePath>();
		for (Parameter param : process.getParameters()) {
			kps.add(param.getKnowledgePath());
		}

		ValueSet values = km.get(kps);
		for (ComponentProcess cProcess : componentInstance.getComponentProcesses()) {
			KnowledgePath kp = RuntimeModelHelper.createKnowledgePath(cProcess.getName());
			values.setValue(kp, cProcess.isIsRunning());
		}
	
		return values;
	}

	public static Map<String, Object> getContext(ValueSet vs){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		
		for (KnowledgePath kp:vs.getKnowledgePaths()) {
			int size = kp.getNodes().toString().length();
			String name = kp.getNodes().toString().subSequence(1,size-1).toString();
			Object value = vs.getValue(kp);
			context.putAll(add(name, value));
		}
		return context;
	}
	
	public static Map<String, Object> add(String kpName, Object key){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		if(key instanceof InaccuracyParamHolder){
			return add(kpName, (InaccuracyParamHolder)key);
		}else if(key instanceof TSParamHolder){
			return add(kpName, (TSParamHolder)key);
		}else if(key instanceof ParamHolder){
			return add(kpName, (ParamHolder)key);
		}else{
			context.put(kpName, key);
			context.put(kpName+"_V", key);
			return context;
		}
	}

	public static Map<String, Object> add(String kpName, ParamHolder key){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		context.put(kpName, key.value);
		context.put(kpName+"_V", key.value);
		return context;
	}

	public static Map<String, Object> add(String kpName, TSParamHolder key){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		context.put(kpName, key.value);
		context.put(kpName+"_V", key.value);
		context.put(kpName+"_TS", key.creationTime);
		return context;
	}

	public static Map<String, Object> add(String kpName, InaccuracyParamHolder key){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		context.put(kpName, key.value);
		context.put(kpName+"_V", key.value);
		context.put(kpName+"_TS", key.creationTime);
		context.put(kpName+"_L", key.minBoundary);
		context.put(kpName+"_H", key.maxBoundary);
		context.put(kpName+"_LH", ((Double)key.maxBoundary - (Double)key.minBoundary));
		return context;
	}
	
	
	public static InaccuracyParamHolder getInaccurateValue(Object object){
		InaccuracyParamHolder key = new InaccuracyParamHolder();
		if(object instanceof InaccuracyParamHolder){
			key.setWithInaccuracy((InaccuracyParamHolder)object);
		}else if(object instanceof TSParamHolder){
			key.setWithTS((TSParamHolder)object);
			key.maxBoundary = 0.0;
			key.minBoundary = 0.0;
		}else{
			key.value = object;
			key.creationTime = 0.0;
			key.maxBoundary = 0.0;
			key.minBoundary = 0.0;
		}
		return key;
	}


	/*
	 * ---------------------------------------------modes-----------------------------------------------------
	 */
	public static List<Boolean> isReachable(List<Transition> transitions){
		List<Boolean> index = new ArrayList<Boolean>();
		for (Transition transition : transitions) {
			if(transition.isIsReachable())
				index.add(true);
			else
				index.add(false);
		}		
		return index;
	}

	public static void initStates(ComponentInstance componentInstance) {
		if(!init){
			for (ComponentProcess process : componentInstance.getComponentProcesses()) {
				for (Transition transition : process.getTransitions()) {
					transition.setIsReachable(false);
					process.setIsActive(false);
					process.setIsRunning(false);
				}
			}
			for (ComponentProcess process : componentInstance.getComponentProcesses()) {
				for (Transition transition : process.getTransitions()) {
					if(transition.getFrom().equals(process)){
						transition.setIsReachable(true);
						process.setIsActive(true);
						process.setIsRunning(true);
					}
				}
			}
			init = true;

		}
	}

	public static void resetTransitions(KnowledgeManager km, ComponentInstance componentInstance,
			ComponentProcess process, Transition transition) {
		transition.setIsReachable(false);
		process.setIsRunning(true);
		if(!transition.getFrom().equals(process)){
			transition.getFrom().setIsActive(false);
		}

		filterSilbing(componentInstance,process,transition);
		setNextTransitions(componentInstance,process, transition);

		setFromRunning(process);
		setKnowledge(km,componentInstance);
	}

	private static void setKnowledge(KnowledgeManager km,
			ComponentInstance componentInstance) {
		ChangeSet vs = new ChangeSet();
		for (ComponentProcess cProcess : componentInstance.getComponentProcesses()) {
			KnowledgePath kp = RuntimeModelHelper.createKnowledgePath(cProcess.getName());
			vs.setValue(kp, cProcess.isIsRunning());
		}
		try {
			km.update(vs);
		} catch (KnowledgeUpdateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void filterSilbing(ComponentInstance componentInstance,
			ComponentProcess process, Transition transition) {
		
		for (int i = 0; i < process.getTransitions().size(); i++) {
			process.getTransitions().get(i).setIsReachable(false);
		}
		
		for (ComponentProcess cProcess : componentInstance.getComponentProcesses()) {
			for (Transition cTransition : cProcess.getTransitions()) {
				if(cTransition.getFrom().equals(transition.getFrom()) && !cProcess.equals(process)){
					cTransition.setIsReachable(false);
					cProcess.setIsActive(false);
					cProcess.setIsRunning(false);
				}
			}
		}
		
	}

	private static void setNextTransitions(ComponentInstance componentInstance,
			ComponentProcess process, Transition transition) {
		for (ComponentProcess cProcess : componentInstance.getComponentProcesses()) {
			for (Transition cTransition : cProcess.getTransitions()) {
				if(cTransition.getFrom().equals(process) && !cTransition.equals(transition) && !cProcess.equals(process)){
					cTransition.setIsReachable(true);
					cProcess.setIsActive(true);
				}
			}
		}
		
	}
	
	private static void setFromRunning(ComponentProcess process){
		for (Transition transition : process.getTransitions()) {
			if(!transition.getFrom().equals(process))
				transition.getFrom().setIsRunning(false);
		}
	}
	
}
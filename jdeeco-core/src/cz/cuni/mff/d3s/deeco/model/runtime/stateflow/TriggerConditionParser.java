package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mvel2.MVEL;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Trigger;



public class TriggerConditionParser {
	
	public static boolean checkTimeStampEquality(Object original, Object value) {
		InaccuracyParamHolder oldV = getInaccurateValue(original);
		InaccuracyParamHolder newV = getInaccurateValue(value);
		if(oldV.creationTime == newV.creationTime && oldV.value == newV.value)
			return true;
		return false;
	}

	
	public static boolean checkCondition(Map<KnowledgePath, Object> knowledge, KnowledgeValueChangeTrigger kvct, Object object) {	
		InaccuracyParamHolder key = getInaccurateValue(object);
		Boolean result = false;
		Map<String, Object> context = getContext(knowledge, key);
		List<Boolean> index = isReachable(kvct.getConstraints(),kvct.getTo());
		List<ConditionType> conds = kvct.getConstraints();
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				if(conds.get(i).condition.equals(""))
					result = true;
				else{
					Object eval = MVEL.eval(conds.get(i).condition, context);
					result = result || (Boolean)eval;
				}
			}
		}
		setProcesses(result, kvct);
		return result;
	}
	

	public static boolean checkCondition(Map<KnowledgePath, Object> knowledge, KnowledgeValueUnchangeTrigger kct, Object object) {
		InaccuracyParamHolder key = getInaccurateValue(object);	
		Boolean result = false;
		Map<String, Object> context = getContext(knowledge, key);
		List<Boolean> index = isReachable(kct.getConstraints(),kct.getTo());
		List<ConditionType> conds = kct.getConstraints();
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				if(conds.get(i).condition.equals(""))
					result = true;
				else{
					Object eval = MVEL.eval(conds.get(i).condition, context);
					result = result || (Boolean)eval;
				}
			}
		}
		setProcesses(result,kct);
		return result;
	}

	
	public static Map<String, Object> getContext(Map<KnowledgePath, Object> knowledge, InaccuracyParamHolder key){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		context.put("V", key.value);
		context.put("TS", key.creationTime);
		context.put("L", key.minBoundary);
		context.put("H", key.maxBoundary);
		context.put("LH", ((Double)key.maxBoundary - (Double)key.minBoundary));
		
		for (KnowledgePath kp:knowledge.keySet()) {
			int size = kp.getNodes().toString().length();
			String name = kp.getNodes().toString().subSequence(1,size-1).toString();
			Object value = getValueKP(knowledge.get(kp));
			context.put(name, value);
		}
		return context;
	}
	
	
	public static Object getValueKP(Object value){
		if(value instanceof InaccuracyParamHolder){
			return ((InaccuracyParamHolder)value).value;
		}else if(value instanceof TSParamHolder){
			return ((TSParamHolder)value).value;
		}else return value;
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

	
	public static List<Boolean> isReachable(List<ConditionType> conds, ComponentProcess toProcess){
		List<Boolean> index = new ArrayList<Boolean>();
		for (ConditionType cond : conds) {
			if(cond.from.isIsActive())
				index.add(true);
			else if(toProcess.isIsActive())
					index.add(true);
				 else
					index.add(false);
		}		
		return index;
	}
	
	
	public static StatesType resetStates(List<ConditionType> conds, ComponentProcess toProcess){
		StatesType s = new StatesType();
		for (ConditionType cond : conds) {	
			if(!toProcess.getChildren().isEmpty() && toProcess.getState() == ModeState.RUNNING){
				for (ComponentProcess child : toProcess.getChildren()) {
					for (Trigger trigger : child.getTriggers()) {
						if(trigger instanceof KnowledgeValueChangeTrigger){
							KnowledgeValueChangeTrigger t  = (KnowledgeValueChangeTrigger)trigger; 
							for (ConditionType con : t.getConstraints()) {
								if(con.from.getName().equals(t.getTo().getName())){
									child.setState(ModeState.RUNNING);
									child.setIsActive(true);
									toProcess.setState(ModeState.RUNNING);
									toProcess.setIsActive(true);
								}
							}
						}
						if(trigger instanceof KnowledgeValueUnchangeTrigger){
							KnowledgeValueUnchangeTrigger t  = (KnowledgeValueUnchangeTrigger)trigger; 
							for (ConditionType con : t.getConstraints()) {
								if(con.from.getName().equals(t.getTo().getName())){
									child.setState(ModeState.RUNNING);
									child.setIsActive(true);
									toProcess.setState(ModeState.RUNNING);
									toProcess.setIsActive(true);
									}
							}
						}

					}
				}
			}
			if(cond.from.getState() == ModeState.RUNNING && toProcess.getState() != ModeState.RUNNING){
				cond.from.setIsActive(false);
				toProcess.setIsActive(true);
				cond.from.setState(ModeState.DEACTIVATED);
				for (ComponentProcess child : cond.from.getChildren()) {
					child.setIsActive(false);
					child.setState(ModeState.DEACTIVATED);
				}
				toProcess.setState(ModeState.RUNNING);
			}
		}
		s.conds.addAll(conds);
		s.toProcess = toProcess;
		return s;
	}
	
	
	public static void setProcesses(boolean result, KnowledgeValueChangeTrigger kct){
		if(result) {
			StatesType s = resetStates(kct.getConstraints(),kct.getTo());
			kct.setTo(s.toProcess);
			kct.getConstraints().clear();
			kct.getConstraints().addAll(s.conds);
		}
	}
	
	
	public static void setProcesses(boolean result, KnowledgeValueUnchangeTrigger kct){
		if(result) {
			StatesType s = resetStates(kct.getConstraints(),kct.getTo());
			kct.setTo(s.toProcess);
			kct.getConstraints().clear();
			kct.getConstraints().addAll(s.conds);
		}
	}
}

class StatesType{
	List<ConditionType> conds = new ArrayList<ConditionType>();
	ComponentProcess toProcess = null;
}
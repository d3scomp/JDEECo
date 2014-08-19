package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mvel2.MVEL;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ModeState;



public class TriggerConditionParser {
	
	public static boolean checkTimeStampEquality(Object original, Object value) {
		
		InaccuracyParamHolder oldV = getInaccurateValue(original);
		InaccuracyParamHolder newV = getInaccurateValue(value);
		
		if(oldV.creationTime == newV.creationTime && oldV.value == newV.value)
			return true;
		return false;
	}

	
	public static boolean checkCondition(KnowledgeValueChangeTrigger kct, Object object) {
		
		InaccuracyParamHolder key = getInaccurateValue(object);
		Boolean result = false;
		Map<String, Object> context = getContext(key);
		List<Boolean> index = isReachable(kct.getConstraints(),kct.getTo());
		List<ConditionType> conds = kct.getConstraints();
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				Object eval = MVEL.eval(conds.get(i).condition, context);
				result = result || (Boolean)eval;
			}
		}
		
		if(result) {
			StatesType s = resetStates(kct.getConstraints(),kct.getTo());
			kct.setTo(s.toProcess);
			kct.getConstraints().clear();
			kct.getConstraints().addAll(s.conds);
		}
		StatesType s = refresh(kct.getConstraints(),kct.getTo());
		kct.setTo(s.toProcess);
		kct.getConstraints().clear();
		kct.getConstraints().addAll(s.conds);

		return result;
	}
	

	public static boolean checkCondition(KnowledgeValueUnchangeTrigger kct, Object object) {
		
		InaccuracyParamHolder key = getInaccurateValue(object);	
		Boolean result = false;
		Map<String, Object> context = getContext(key);
		List<Boolean> index = isReachable(kct.getConstraints(),kct.getTo());
		List<ConditionType> conds = kct.getConstraints();
		
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				Object eval = MVEL.eval(conds.get(i).condition, context);
				result = result || (Boolean)eval;
			}
		}
		
		if(result) {
			StatesType s = resetStates(kct.getConstraints(),kct.getTo());
			kct.setTo(s.toProcess);
			kct.getConstraints().clear();
			kct.getConstraints().addAll(s.conds);
		}
		
		StatesType s = refresh(kct.getConstraints(),kct.getTo());
		kct.setTo(s.toProcess);
		kct.getConstraints().clear();
		kct.getConstraints().addAll(s.conds);

		
		return result;
	}

	public static Map<String, Object> getContext(InaccuracyParamHolder key){
		Map<String, Object> context = new java.util.HashMap<String, Object>();
		context.put("V", key.value);
		context.put("TS", key.creationTime);
		context.put("L", key.minBoundary);
		context.put("H", key.maxBoundary);
		context.put("LH", ((Double)key.maxBoundary - (Double)key.minBoundary));
		return context;
	}
	
	
	public static InaccuracyParamHolder getInaccurateValue(Object object){
		InaccuracyParamHolder key = new InaccuracyParamHolder();
		if(object instanceof InaccuracyParamHolder){
			key.setWithInaccuracy((InaccuracyParamHolder)object);
		}else if(object instanceof TSParamHolder){
			key.setWithTS((TSParamHolder)object);
		}else{
			key.value = object;
			key.creationTime = 0.0;
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
			if(cond.from.getState() == ModeState.RUNNING && toProcess.getState() != ModeState.RUNNING){
				cond.from.setIsActive(false);
				toProcess.setIsActive(true);
				cond.from.setState(ModeState.DEACTIVATED);
				toProcess.setState(ModeState.RUNNING);
			}
		}
		s.conds.addAll(conds);
		s.toProcess = toProcess;
		return s;
	}

	public static StatesType refresh(List<ConditionType> conds, ComponentProcess toProcess){
		StatesType s = new StatesType();
		for (ConditionType cond : conds) {			
			if(cond.from.getState() == ModeState.DEACTIVATED && toProcess.getState() != ModeState.RUNNING){
				cond.from.setIsActive(false);
				cond.from.setState(ModeState.IDLE);
			}
		}
		s.conds.addAll(conds);
		s.toProcess = toProcess;
		return s;
	}

}

class StatesType{
	List<ConditionType> conds = new ArrayList<ConditionType>();
	ComponentProcess toProcess = null;
}
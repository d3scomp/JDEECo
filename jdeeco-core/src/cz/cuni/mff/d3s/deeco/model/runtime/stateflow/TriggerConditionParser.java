package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.mvel2.MVEL;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
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

	
	public static boolean checkCondition(KnowledgeChangeTrigger kct, Object object) {
		if(kct instanceof KnowledgeValueChangeTrigger)
			return checkCondition((KnowledgeValueChangeTrigger)kct, object);
		else if(kct instanceof KnowledgeValueUnchangeTrigger)
			return checkCondition((KnowledgeValueUnchangeTrigger)kct, object);
		return false;
	}
	
	
	public static boolean checkCondition(KnowledgeValueChangeTrigger kct, Object object) {
		
		InaccuracyParamHolder key = getInaccurateValue(object);
		Boolean result = false;
		Map<String, Object> context = getContext(key);
		List<Boolean> index = isReachable(kct.getFrom(),kct.getTo());
		List<String> conds = kct.getCondition();
		
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				if(!conds.get(i).equals("")){
					Object eval = MVEL.eval(conds.get(i), context);
					result = result || (Boolean)eval;
				}else 
					result = true;
			}
		}
		return result;
	}
	

	public static boolean checkCondition(KnowledgeValueUnchangeTrigger kct, Object object) {
		
		InaccuracyParamHolder key = getInaccurateValue(object);	
		Boolean result = false;
		Map<String, Object> context = getContext(key);
		List<Boolean> index = isReachable(kct.getFrom(),kct.getTo());
		List<String> conds = kct.getCondition();
		
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				if(!conds.get(i).equals("")){
					Object eval = MVEL.eval(conds.get(i), context);
					result = result || (Boolean)eval;
				}else 
					result = true;
			}
		}
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

	
	public static List<Boolean> isReachable(List<ComponentProcess> processes, ComponentProcess toProcess){
		List<Boolean> index = new ArrayList<Boolean>();
		for (ComponentProcess process : processes) {
			if(process.getState() == ModeState.RUNNING)
				index.add(true);
			else if(process.getState() == ModeState.DEACTIVATED){
					if(toProcess.getState() == ModeState.RUNNING)
						index.add(true);
					else
						index.add(false);
			}else
				index.add(false);
		}
		return index;
		
	}
	
	
}

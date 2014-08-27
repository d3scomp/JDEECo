package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.mvel2.MVEL;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeTimeStampTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Transition;



public class TriggerConditionParser {
	
	public static boolean checkTimeStampEquality(Object original, Object value) {
		InaccuracyParamHolder oldV = getInaccurateValue(original);
		InaccuracyParamHolder newV = getInaccurateValue(value);
		if(oldV.creationTime == newV.creationTime && oldV.value == newV.value)
			return true;
		return false;
	}

	
	public static Transition returnTransition(Map<KnowledgePath, Object> knowledge, KnowledgeTimeStampTrigger kvct, Object object) {
		Transition selectedTransition = null;
		InaccuracyParamHolder key = getInaccurateValue(object);
		Boolean result = false;
		Map<String, Object> context = getContext(knowledge, key);
		List<Boolean> index = isReachable(kvct.getEvents());
		EList<Transition> conds = kvct.getEvents();
		for (int i = 0; i < index.size(); i++) {
			if(index.get(i)){
				if(conds.get(i).getCondition().equals(""))
					result = true;
				else{
					Object eval = MVEL.eval(conds.get(i).getCondition(), context);
					result = result || (Boolean)eval;
				}
//				System.out.println(conds.get(i).getFrom()+" => "+conds.get(i).getTo()+"  "+conds.get(i).getCondition()+"   "+result+"   "+kvct);
				if(result) selectedTransition = conds.get(i);
			}
		}
		return selectedTransition;
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

	
	public static List<Boolean> isReachable(List<Transition> conds){
		List<Boolean> index = new ArrayList<Boolean>();
		for (Transition cond : conds) {
			if(cond.isIsReachable())
				index.add(true);
			else
				index.add(false);
		}		
		return index;
	}
}
package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;

import org.eclipse.emf.common.util.EList;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComparisonType;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeValueUnchangeTrigger;

public class TriggerConditionParser {

	public static boolean checkTimeStampEquality(Object original, Object value) {
		
		InaccuracyParamHolder oldV = new InaccuracyParamHolder<>();
		InaccuracyParamHolder newV = new InaccuracyParamHolder<>();
		
		if(original instanceof ModeParamHolder){
			oldV = (ModeParamHolder)original;
			newV = (ModeParamHolder)value;
		}else if(original instanceof InaccuracyParamHolder){
			oldV = (InaccuracyParamHolder)original;
			newV = (InaccuracyParamHolder)value;
		}else if(original instanceof TSParamHolder){
			oldV.setWithTS((TSParamHolder)original);
			newV.setWithTS((TSParamHolder)value);			
		}else{
			oldV.value = original;
			newV.value = value;
		}
			
		if(oldV.creationTime.equals(newV.creationTime) && oldV.value.equals(newV.value))
			return true;
		
		return false;
	}

	
	public static boolean checkCondition(KnowledgeValueUnchangeTrigger kct, Object object) {
		
		ModeParamHolder<Double> key = new ModeParamHolder<Double>();
		if(object instanceof ModeParamHolder){
			key.setWithMode((ModeParamHolder)object);
		}else if(object instanceof InaccuracyParamHolder){
			key.setWithInaccuracy((InaccuracyParamHolder)object);
		}else if(object instanceof TSParamHolder){
			key.setWithTS((TSParamHolder)object);
		}else{
			key.value = (Double)object;
		}
				
		Double val =  new Double(0.0);
		switch(kct.getMeta()){
		case INACCURACY:
			val = key.maxBoundary - key.minBoundary;
			break;
		case TS:
			val = key.creationTime;
			break;
		case MIN_BOUNDARY:
			val = key.minBoundary;
			break;
		case MAX_BOUNDARY:
			val = key.maxBoundary;
			break;
		case EMPTY:
			val = key.value;
			break;
		default:
			val = key.value;
			break;				
		}
		
			EList<ComparisonType> comps = kct.getComparison();
			EList<Long> vals = kct.getValue();
			Boolean result = true;
			
			for (int i = 0; i < comps.size(); i++) {
				result = result && checkComparison(comps.get(i), val, vals.get(i).doubleValue());
			}
		return result;
	}
	
	
	public static boolean checkComparison(ComparisonType comp, double val1, double val2){
		switch(comp){
		case EQUAL:
			if(val1 == val2) return true; else return false;
		case EQUAL_LESS_THAN:
			if(val1 <= val2) return true; else return false;
		case EQUAL_MORE_THAN:
			if(val1 >= val2) return true; else return false;
		case LESS_THAN:
			if(val1 < val2) return true; else return false;
		case MORE_THAN:
			if(val1 > val2) return true; else return false;
		default:
			return false;
		}
	}
}

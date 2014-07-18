package cz.cuni.mff.d3s.deeco.model.runtime.stateflow;



public class ComponentModeTransition {

	public ComponentMode fromMode = null;
	public ComponentMode toMode = null;
	public String transitionCondition = new String(); 
	
	
//	boolean analyzeCondition(InaccurateValueDefinition inacc){
//		Number variable = 0;
//		String comparisonOperator = new String();
//		Number limit = 0;
//		
//		ArrayList<String> parts = new ArrayList<String>();
//		
//		transitionCondition = transitionCondition.trim();
//		String str = ""; 
//		int i =0;
//		int j = transitionCondition.indexOf(" ");
//		System.out.println(i+"    "+j+"   "+transitionCondition.length());
//
//		if(inacc.value.doubleValue() < 10000)
//			return true;
//		//TODO
//		return false;
//	}
	
}

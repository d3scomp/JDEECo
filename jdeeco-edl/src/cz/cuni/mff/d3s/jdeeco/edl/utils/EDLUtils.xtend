package cz.cuni.mff.d3s.jdeeco.edl.utils

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes
import cz.cuni.mff.d3s.jdeeco.edl.ContextSymbols

class EDLUtils {
	def static String stripSet(String setType) {
		if (setType.startsWith(ContextSymbols.SET_SYMBOL))
			setType.subSequence(4, setType.length-1).toString()
		else
			PrimitiveTypes.UNKNOWN;
	}
	
		
	def static boolean convertible(String src, String target) {
		if (src == null || target == null)
		 	return false
		
		if (src.equals(target))
			return true;
			
		if (target.startsWith(ContextSymbols.SET_SYMBOL)) {
			if (target.equals(ContextSymbols.SET_SYMBOL))
				return true;
			
			var String subType = stripSet(target) 			
			
			if (convertible(src, subType)) { 
				return true;				
			}
		}
			
		return false;
	}
	
	def static String getJavaTypeName(String type) {
		switch type {
			case PrimitiveTypes.STRING:
				"String"
			case PrimitiveTypes.BOOL:
				"Boolean"
			case PrimitiveTypes.INT:
				"Integer"
			case PrimitiveTypes.FLOAT:
				"Double"
			default:
				type
		}			
	}
	
	def static String getThriftTypeName(String type) {
		if (type.startsWith(ContextSymbols.SET_SYMBOL))
			return "set<" + getThriftTypeName(EDLUtils.stripSet(type)) + ">"
		
		switch type {
			case PrimitiveTypes.INT:
				"i32"
			case PrimitiveTypes.FLOAT:
				"double"
			case PrimitiveTypes.STRING:
				"string"
			case PrimitiveTypes.BOOL:
				"bool"
			default:
				type
		}			
	}		
}
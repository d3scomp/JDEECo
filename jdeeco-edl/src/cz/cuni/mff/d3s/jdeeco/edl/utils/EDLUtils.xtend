package cz.cuni.mff.d3s.jdeeco.edl.utils

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes

class EDLUtils {
	def static String stripSet(String setType) {
		if (setType.startsWith("set"))
			setType.subSequence(4, setType.length-1).toString()
		else
			PrimitiveTypes.UNKNOWN;
	}
	
		
	def static boolean convertible(String src, String target) {
		if (src == null || target == null)
		 	return false
		
		if (src.equals(target))
			return true;
			
		if (target.startsWith("set")) {
			if (target.equals("set"))
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
				"boolean"
			default:
				type
		}			
	}	
}
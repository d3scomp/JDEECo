package cz.cuni.mff.d3s.jdeeco.edl;

import cz.cuni.mff.d3s.jdeeco.edl.functions.IConstraintFunction;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunction;

public interface IFunctionRegistry {
	String getFunctionReturnType(String name);
	boolean containsFunction(String name);
	boolean containsConstraintFunction(String name);
	IFunction getFunction(String name);
	IConstraintFunction getConstraintFunction(String name);	
}
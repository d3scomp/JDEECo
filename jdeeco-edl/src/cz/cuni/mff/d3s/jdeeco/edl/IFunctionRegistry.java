package cz.cuni.mff.d3s.jdeeco.edl;

import cz.cuni.mff.d3s.jdeeco.edl.functions.IConstraintFunction;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunction;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

public interface IFunctionRegistry {
	String getFunctionReturnType(ITypeResolutionContext ctx, EnsembleDefinition ensemble, String name, Query... params);
	boolean containsFunction(String name);
	boolean containsConstraintFunction(String name);
	IFunction getFunction(String name);
	IConstraintFunction getConstraintFunction(String name);	
}
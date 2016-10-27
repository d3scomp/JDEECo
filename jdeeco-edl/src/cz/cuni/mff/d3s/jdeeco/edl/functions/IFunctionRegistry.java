package cz.cuni.mff.d3s.jdeeco.edl.functions;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.typing.ITypeInformationProvider;

public interface IFunctionRegistry {
	String getFunctionReturnType(ITypeInformationProvider typing, EnsembleDefinition ensemble, String name, Query... params);
	boolean containsFunction(String name);
	boolean containsConstraintFunction(String name);
	IFunction getFunction(String name);
	IConstraintFunction getConstraintFunction(String name);	
}
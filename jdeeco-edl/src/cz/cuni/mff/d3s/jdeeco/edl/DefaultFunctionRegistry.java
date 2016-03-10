package cz.cuni.mff.d3s.jdeeco.edl;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.jdeeco.edl.functions.Count;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IConstraintFunction;
import cz.cuni.mff.d3s.jdeeco.edl.functions.IFunction;
import cz.cuni.mff.d3s.jdeeco.edl.functions.OneOf;
import cz.cuni.mff.d3s.jdeeco.edl.functions.SetsEqual;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

public class DefaultFunctionRegistry implements IFunctionRegistry {
	private Map<String, IFunction> allFunctions;
	private Map<String, IConstraintFunction> constraintFunctions;	

	public DefaultFunctionRegistry() {
		allFunctions = new HashMap<String, IFunction>();
		constraintFunctions = new HashMap<String, IConstraintFunction>();
		initialize();
	}

	private void initialize() {		
		registerFunction("setsEqual", new SetsEqual());
		registerFunction("oneOf", new OneOf());
		registerFunction("count", new Count());
	}
	
	private void registerFunction(String name, IConstraintFunction f) {
		allFunctions.put(name, f);
		constraintFunctions.put(name, f);
	}
	
	private void registerFunction(String name, IFunction f) {
		allFunctions.put(name, f);		
	}
	
	@Override
	public String getFunctionReturnType(ITypeResolutionContext ctx, EnsembleDefinition ensemble, String name, Query... params) {
		return allFunctions.get(name).getReturnType(ctx, ensemble, params);	
	}

	@Override
	public boolean containsFunction(String name) {
		return allFunctions.containsKey(name);
	}
	
	@Override
	public boolean containsConstraintFunction(String name) {
		return constraintFunctions.containsKey(name);
	}
	
	@Override
	public IFunction getFunction(String name) {
		return allFunctions.get(name);
	}

	@Override
	public IConstraintFunction getConstraintFunction(String name) {
		return constraintFunctions.get(name);
	}
}

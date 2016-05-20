package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

public class Abs implements IFunction {

	private List<String> params;
	
	public Abs() {
		params = new ArrayList<String>();
		params.add(PrimitiveTypes.INT);
	}

	@Override
	public List<String> getParameterTypes() {
		return params;
	}

	@Override
	public Object evaluate(Object... params) {
		Integer arg = (Integer)params[0];
		return arg >= 0 ? arg : -arg;
	}

	@Override
	public String getReturnType(ITypeResolutionContext ctx,
			EnsembleDefinition ensemble, Query... params) {
		return PrimitiveTypes.INT;
	}
}

package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

public class Count implements IFunction {

	private List<String> params;

	public Count() {
		params = new ArrayList<String>();
		params.add("set");
	}

	@Override
	public List<String> getParameterTypes() {
		return params;
	}

	@Override
	public Object evaluate(Object... params) {
		// TODO Auto-generated method stub
		return ((List) params[0]).size();
	}

	@Override
	public String getReturnType(ITypeResolutionContext ctx,
			EnsembleDefinition ensemble, Query... params) {
		return PrimitiveTypes.INT;
	}

}

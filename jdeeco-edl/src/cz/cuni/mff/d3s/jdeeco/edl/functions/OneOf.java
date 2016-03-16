package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils;
import cz.cuni.mff.d3s.jdeeco.edl.utils.ITypeResolutionContext;

public class OneOf implements IFunction {
	
	private List<String> params;

	public OneOf() {
		params = new ArrayList<String>();
		params.add("set");
	}

	@Override
	public List<String> getParameterTypes() {
		return params;
		
	}

	@Override
	public Object evaluate(Object... params) {
		return params[0];
	}

	@Override
	public String getReturnType(ITypeResolutionContext ctx, EnsembleDefinition ensemble, Query... params) {		
		if(params.length == 0)
			return "unknown";
		
		return EDLUtils.stripSet(EDLUtils.getType(ctx, params[0], ensemble));
	}	
}

package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

import cz.cuni.mff.d3s.jdeeco.edl.ContextSymbols;
import cz.cuni.mff.d3s.jdeeco.edl.PrimitiveTypes;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.EnsembleDefinition;
import cz.cuni.mff.d3s.jdeeco.edl.model.edl.Query;
import cz.cuni.mff.d3s.jdeeco.edl.typing.ITypeInformationProvider;
import cz.cuni.mff.d3s.jdeeco.edl.utils.EDLUtils;

public class SetsEqual implements IConstraintFunction {
	
	private List<String> params;

	public SetsEqual() {
		params = new ArrayList<String>();
		params.add(ContextSymbols.SET_SYMBOL);
		params.add(ContextSymbols.SET_SYMBOL);
	}

	@Override
	public List<String> getParameterTypes() {
		return params;
	}


	@Override
	public Object evaluate(Object... params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getReturnType(ITypeInformationProvider typing, EnsembleDefinition ensemble, Query... params) {
		return PrimitiveTypes.BOOL;		
	}

	@Override
	public Expr generateFormula(Context ctx, Expr... params) {
		return ctx.mkEq(params[0], params[1]);
	}
}

package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.List;

import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;

public interface IConstraintFunction extends IFunction {
	Expr generateFormula(Context ctx, Expr... params);	
}

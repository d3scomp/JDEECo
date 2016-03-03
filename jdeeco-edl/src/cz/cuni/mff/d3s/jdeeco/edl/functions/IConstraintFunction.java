package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.List;

public interface IConstraintFunction extends IFunction {	
	// TODO: Signature of this method will probably depend on Z3
	void generateFormula(Object... params);	
}

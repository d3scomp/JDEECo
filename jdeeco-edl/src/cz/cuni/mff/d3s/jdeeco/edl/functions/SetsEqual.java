package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.ArrayList;
import java.util.List;

public class SetsEqual implements IConstraintFunction {
	
	private List<String> params;

	public SetsEqual() {
		params = new ArrayList<String>();
		params.add("set");
		params.add("set");
	}

	@Override
	public List<String> getParameterTypes() {
		return params;
	}

	@Override
	public void generateFormula(Object... params) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean evaluate(Object... params) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getReturnType() {
		return "bool";
	}
}

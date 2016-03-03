package cz.cuni.mff.d3s.jdeeco.edl.functions;

import java.util.List;

public interface IFunction {
	List<String> getParameterTypes();
	boolean evaluate(Object... params);
	String getReturnType();
}

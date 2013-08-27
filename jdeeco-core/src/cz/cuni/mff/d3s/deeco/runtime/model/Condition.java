package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.List;

public class Condition extends Invocable {

	public Condition(List<Parameter> parameters, Method method) {
		super(parameters, method, LockingMode.STRONG);
	}

	public Condition(Method method) {
		super(method, LockingMode.STRONG);
	}
}

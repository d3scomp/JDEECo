package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.List;

public class BooleanCondition extends Invocable {

	public BooleanCondition(List<Parameter> parameters, Method method) {
		super(parameters, method, LockingMode.STRONG);
	}

	public BooleanCondition(Method method) {
		super(method, LockingMode.STRONG);
	}
}

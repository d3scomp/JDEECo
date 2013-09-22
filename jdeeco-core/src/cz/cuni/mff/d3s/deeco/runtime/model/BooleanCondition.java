package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.List;

public class BooleanCondition extends Invocable {

	private final String id;
	
	public BooleanCondition(String id, List<Parameter> parameters, Method method) {
		super(parameters, method, LockingMode.STRONG);
		this.id = id;
	}

	public BooleanCondition(String id, Method method) {
		super(method, LockingMode.STRONG);
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
}

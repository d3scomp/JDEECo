package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public abstract class Invocable {
	private List<Parameter> parameters;
	private Method method;
	private LockingMode lockingMode;
	
	public Invocable(List<Parameter> parameters, Method method, LockingMode lockingMode) {
		super();
		this.parameters = parameters;
		this.method = method;
		this.lockingMode = lockingMode;
	}
	
	public Invocable(Method method, LockingMode lockingMode) {
		this(new LinkedList<Parameter>(), method, lockingMode);
	}

	public List<Parameter> getParameters() {
		return parameters;
	}
	
	public List<Parameter> getInputParameters() {
		List<Parameter> result = new LinkedList<Parameter>();
		for (Parameter p : parameters)
			if (p.getDirection().equals(ParameterDirection.IN))
				result.add(p);
		return result;
	}
	
	public List<Parameter> getOutputParameters() {
		List<Parameter> result = new LinkedList<Parameter>();
		for (Parameter p : parameters)
			if (p.getDirection().equals(ParameterDirection.OUT))
				result.add(p);
		return result;
	}

	public Method getMethod() {
		return method;
	}

	public LockingMode getLockingMode() {
		return lockingMode;
	}
		
}

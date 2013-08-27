package cz.cuni.mff.d3s.deeco.runtime.model;

import java.lang.reflect.Method;
import java.util.List;

public class Exchange extends Invocable {

	public Exchange(List<Parameter> parameters, Method method) {
		super(parameters, method, LockingMode.STRONG);
	}
	
	public Exchange(Method method) {
		super(method, LockingMode.STRONG);
	}
	
}

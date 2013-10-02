package cz.cuni.mff.d3s.deeco.model;

import java.lang.reflect.Method;
import java.util.List;

public class Exchange extends Invocable {

	private final String id;
	
	public Exchange(String id, List<Parameter> parameters, Method method) {
		super(parameters, method, LockingMode.STRONG);
		this.id = id;
	}
	
	public Exchange(String id, Method method) {
		super(method, LockingMode.STRONG);
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
}

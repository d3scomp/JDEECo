package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.util.List;



public abstract class MembershipMethod<T> implements Serializable {
	
	public final ParameterizedMethod method;
	
	public MembershipMethod(ParameterizedMethod method) {
		this.method = method;
	}
	
	public List<Parameter> getIn() {
		return method.in;
	}
	
	public List<Parameter> getOut() {
		return method.out;
	}
	
	public List<Parameter> getInOut() {
		return method.inOut;
	}
	
	public abstract T membership(Object [] parameters);
}

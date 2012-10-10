package cz.cuni.mff.d3s.deeco.invokable;

import java.util.List;



public abstract class Membership {
	
	public final ParameterizedMethod method;
	
	public Membership(ParameterizedMethod method) {
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
	
	public abstract boolean membership(Object [] parameters);
}

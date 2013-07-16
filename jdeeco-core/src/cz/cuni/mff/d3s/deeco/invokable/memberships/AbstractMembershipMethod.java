package cz.cuni.mff.d3s.deeco.invokable.memberships;

import java.io.Serializable;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;
import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;

public abstract class AbstractMembershipMethod<T extends ParameterizedMethod> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public final T method;
	
	public AbstractMembershipMethod(T method) {
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
	
	public abstract Boolean membership(Object [] parameters);
}

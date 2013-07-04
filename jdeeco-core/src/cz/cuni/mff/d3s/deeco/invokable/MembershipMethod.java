package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.util.List;


/**
 * This class contains the abstract definition of the membership method
 * and the ParameterizedMethod which contains the parameters of the defined method.
 */
public class MembershipMethod implements Serializable {
	
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
	
	/**
	 * the object must be either part of the package invokable.types or a boolean
	 * @param parameters the input parameter of the parameterized method
	 * @return an object of the package invokable.types wrapping the returning variable or a boolean
	 */
	public Object membership(Object [] parameters) {
		return (Object) method.invoke(parameters);
	}
}

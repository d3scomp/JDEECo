package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;

/**
 * The selector method has selector parameters to group parameters by their selector identifier.
 * This is used in the context of a {@link MembersMembershipMethod}
 * 
 * @author Julien Malvot
 *
 */
public class ParameterizedSelectorMethod extends ParameterizedMethod {

	private static final long serialVersionUID = 2233069281498445502L;
	
	/** list of selector parameters which are prior parsed in the MembersMembershipMethod */
	public final List<SelectorParameter> selectors;

	/**
	 * 
	 * @param in
	 * @param inOut
	 * @param out
	 * @param selectors
	 * @param method
	 */
	public ParameterizedSelectorMethod(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out,
			List<SelectorParameter> selectors, Method method) {
		super(in, inOut, out, method);
		this.selectors = selectors;
	}
}

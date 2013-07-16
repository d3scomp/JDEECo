package cz.cuni.mff.d3s.deeco.invokable;

import java.lang.reflect.Method;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;

public class ParameterizedSelectorMethod extends ParameterizedMethod {

	/**
	 * Selectors parameterTypes
	 */
	public final List<SelectorParameter> selectors;
	  
	/**
	 * 
	 */
	private static final long serialVersionUID = 2233069281498445502L;

	public ParameterizedSelectorMethod(List<Parameter> in,
			List<Parameter> inOut, List<Parameter> out,
			List<SelectorParameter> selectors, Method method) {
		super(in, inOut, out, method);
		this.selectors = selectors;
	}
}

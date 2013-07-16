package cz.cuni.mff.d3s.deeco.invokable.memberships;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedSelectorMethod;
import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;

public class MembersMembershipMethod extends AbstractMembershipMethod<ParameterizedSelectorMethod> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MembersMembershipMethod(ParameterizedSelectorMethod method) {
		super(method);
	}
	
	public List<SelectorParameter> getSelectors() {
		return method.selectors;
	}

	@Override
	public Boolean membership(Object[] parameters) {
		return (Boolean) method.invoke(parameters);
	}
}

package cz.cuni.mff.d3s.deeco.invokable.memberships;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedSelectorMethod;
import cz.cuni.mff.d3s.deeco.invokable.parameters.Parameter;
import cz.cuni.mff.d3s.deeco.invokable.parameters.SelectorParameter;

/**
 * The members-based membership method deals with different identified groups of members.
 * These groups need be defined via the selectors annotations.
 * The method is processed then differently than the standard member-based membership method,
 * as the selectors participate to implicit definitions for groups of parameters which are
 * used for the knowledge exchange method.
 * 
 * @author Julien Malvot
 *
 */
public class MembersMembershipMethod extends AbstractMembershipMethod<ParameterizedSelectorMethod> {

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

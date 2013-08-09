package cz.cuni.mff.d3s.deeco.invokable.memberships;

import cz.cuni.mff.d3s.deeco.invokable.ParameterizedMethod;

/**
 * extends purely the abstract membership method with no modifications
 * @author Julien Malvot
 *
 */
public class MemberMembershipMethod extends AbstractMembershipMethod<ParameterizedMethod> {

	private static final long serialVersionUID = 1L;

	public MemberMembershipMethod(ParameterizedMethod method) {
		super(method);
	}

	@Override
	public Boolean membership(Object[] parameters) {
		return (Boolean) method.invoke(parameters);
	}

}

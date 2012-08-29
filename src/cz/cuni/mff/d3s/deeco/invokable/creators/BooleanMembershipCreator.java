package cz.cuni.mff.d3s.deeco.invokable.creators;

import cz.cuni.mff.d3s.deeco.invokable.BooleanMembership;

public class BooleanMembershipCreator extends MembershipCreator {

	private static final long serialVersionUID = 8345533968775673991L;

	public BooleanMembershipCreator(ParametrizedMethodCreator method) {
		super(method);
	}

	@Override
	public BooleanMembership extract() {
		return new BooleanMembership(method.extract());
	}

}

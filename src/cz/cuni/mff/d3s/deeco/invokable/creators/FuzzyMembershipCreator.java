package cz.cuni.mff.d3s.deeco.invokable.creators;

import cz.cuni.mff.d3s.deeco.invokable.FuzzyMembership;
import cz.cuni.mff.d3s.deeco.invokable.Membership;

public class FuzzyMembershipCreator extends MembershipCreator {

	private static final long serialVersionUID = 8530746783734141682L;

	private final Double limit;

	public FuzzyMembershipCreator(ParametrizedMethodCreator method, Double limit) {
		super(method);
		this.limit = limit;
	}

	@Override
	public Membership extract() {
		return new FuzzyMembership(method.extract(), limit);
	}

}

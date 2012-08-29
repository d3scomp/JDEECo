package cz.cuni.mff.d3s.deeco.invokable.creators;

import java.io.Serializable;

import cz.cuni.mff.d3s.deeco.invokable.Membership;

public abstract class MembershipCreator implements Serializable {

	private static final long serialVersionUID = 8285113290370755928L;
	
	public final ParametrizedMethodCreator method;
	
	
	public MembershipCreator(ParametrizedMethodCreator method) {
		super();
		this.method = method;
	}


	public abstract Membership extract();

}

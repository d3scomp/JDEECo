package cz.cuni.mff.d3s.deeco.invokable;

public class BooleanMembership extends MembershipMethod<Boolean> {

	public BooleanMembership(ParameterizedMethod method) {
		super(method);
	}

	@Override
	public Boolean membership(Object[] parameters) {
		return (Boolean) method.invoke(parameters);
	}

}

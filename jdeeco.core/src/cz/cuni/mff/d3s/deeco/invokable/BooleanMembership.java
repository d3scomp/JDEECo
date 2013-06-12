package cz.cuni.mff.d3s.deeco.invokable;

public class BooleanMembership extends MembershipMethod {

	public BooleanMembership(ParameterizedMethod method) {
		super(method);
	}

	@Override
	public boolean membership(Object[] parameters) {
		return (Boolean) method.invoke(parameters);
	}

}

package cz.cuni.mff.d3s.deeco.invokable;

public class BooleanMembership extends Membership {

	public BooleanMembership(ParameterizedMethod method) {
		super(method);
	}

	@Override
	public boolean membership(Object[] parameters) {
		return (Boolean) method.invoke(parameters);
	}

}

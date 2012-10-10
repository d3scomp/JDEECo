package cz.cuni.mff.d3s.deeco.invokable;


public class FuzzyMembership extends Membership {

	private Double limit;

	public FuzzyMembership(ParameterizedMethod method, Double limit) {
		super(method);
		this.limit = limit;
	}

	@Override
	public boolean membership(Object[] parameters) {
		return (Double) method.invoke(parameters) <= limit;
	}
	

}

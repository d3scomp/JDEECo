package cz.cuni.mff.d3s.deeco.invokable;

/**
 * 
 * @author Julien Malvot
 *
 */
public class CandidateMembership extends MembershipMethod<String> {

	public CandidateMembership(ParameterizedMethod method) {
		super(method);
	}

	@Override
	public String membership(Object[] parameters) {
		return (String) method.invoke(parameters);
	}
}

package cz.cuni.mff.d3s.deeco.path.grammar;

public class PathGrammar {
	public static final String PATH_SEPARATOR = ".";
	
	/**
	 * Identifier used to describe an ensemble coordinator
	 */
	public static final String COORD = "coord";
	
	/**
	 * Identifier used to describe an ensemble member
	 */
	public static final String MEMBER = "member";
	
	/**
	 * Identifier used to describe an ensemble candidate
	 * when using an input list of members in the membership function
	 * resulting from a filtering applied from a given user metric 
	 * (e.g. network distance. the metric shall be supplied by a communication
	 * middleware in future versions of DEECo)
	 * in order to retrieve the best candidate after applying an heuristic
	 * on the list (e.g. minimum, maximum, ...)
	 */
	public static final String CANDIDATE = "candidate";
}

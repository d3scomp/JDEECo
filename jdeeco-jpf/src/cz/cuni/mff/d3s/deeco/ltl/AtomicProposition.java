package cz.cuni.mff.d3s.deeco.ltl;

import cz.cuni.mff.d3s.deeco.knowledge.local.KnowledgeJPF;

/**
 * Class representing an atomic proposition for LTL checking via JPF. 
 * 
 * @author Jaroslav Keznikl
 *
 */
public abstract class AtomicProposition {

	/**
	 * Returns the name of the proposition which is used in LTL formulas.
	 * @return
	 */
	public abstract String getName();

	/**
	 * Returns true iff the proposition is valid w.r.t. to the given knowledge. 
	 */
	public abstract Boolean evaluate(KnowledgeJPF knowledge);

	/**
	 * Creates an object of the corresponding type out of a knowledge subtree identified by the given id.
	 */
	protected Object deserialize(KnowledgeJPF knowledge, String knowledgeId) {
		// TODO: use InputParametersHelper and preprocessed map<Class, ParameterType>
		throw new RuntimeException("Not implemented.");
	}
	
	/**
	 * Creates a list of objects of the corresponding type out of a knowledge subtree identified by the given id.
	 */
	protected Object deserializeList(KnowledgeJPF knowledge, String knowledgeId) {
		throw new RuntimeException("Not implemented.");
	}
}

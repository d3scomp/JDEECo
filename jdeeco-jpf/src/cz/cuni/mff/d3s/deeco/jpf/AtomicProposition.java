package cz.cuni.mff.d3s.deeco.jpf;

import cz.cuni.mff.d3s.deeco.jpf.KnowledgeJPF;

/**
 * Interface representing an atomic proposition for LTL checking via JPF.
 * 
 * @author Jaroslav Keznikl
 *
 */
public interface AtomicProposition {

	/**
	 * Returns the name of the proposition which is used in LTL formulas.
	 * @return
	 */
	public abstract String getName();

	/**
	 * Returns true iff the proposition is valid w.r.t. to the given knowledge. 
	 */
	public abstract boolean evaluate(KnowledgeJPF knowledge);

}

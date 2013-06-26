package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;

/**
 * Interface used to access different kinds of data. Used by the knowledge
 * manager.
 * 
 * @author Michal
 * 
 */
public interface IObjectAccessor {
	/**
	 * Retrieves value for a given key.
	 * 
	 * @param propertyName
	 *            key for which value should be returned.
	 * @return value of the given key.
	 * @throws KMCastException
	 *             - thrown whenever there is a problem with the value
	 *             retrieval.
	 */
	public Object getValue(String propertyName) throws KMCastException;
}

package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

public interface IKnowledgeRepository {
	/**
	 * Reads a single entry from the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return object in the knowledge repository
	 * @throws KRExceptionUnavailableEntry
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object [] get(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError;

	/**
	 * Inserts an object to the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param value
	 *            inserted object
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public void put(String entryKey, Object value, ISession session)
			throws KRExceptionAccessError;

	/**
	 * Withdraws an object from the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return object from the knowledge repository
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object [] take(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError;
	
	/**
	 * Register a listener that should be notified by the knowledge repository
	 * whenever a specified properties are changing.
	 * 
	 * @param listener listening object
	 */
	public boolean registerListener(IKnowledgeChangeListener listener);
	
	/**
	 * Unregisters knowledge listener that has been registered earlier.
	 * 
	 * @param listener listening object
	 */
	public boolean unregisterListener(IKnowledgeChangeListener listener);
	
	
	/**
	 * Switching listening on or off
	 * Sets wether 
	 * 
	 * @param on if true the listening is on otherwise its off.
	 * 
	 */
	public void setListenersActive(boolean on);
	
	
	/**
	 * Checks if current knowledge change listening is on or off.
	 * 
	 * 
	 */
	public boolean isListenersActive();

	/**
	 * Creates a session object, which can be used for all the operations on the
	 * knowledge repository.
	 * 
	 * @return session object
	 */
	public ISession createSession();

	/**
	 * Reads a single entry from the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @return object in the knowledge repository
	 * @throws KRExceptionUnavailableEntry
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object [] get(String entryKey) throws KRExceptionUnavailableEntry,
			KRExceptionAccessError;

	/**
	 * Inserts an object to the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param value
	 *            inserted object
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public void put(String entryKey, Object value)
			throws KRExceptionAccessError;

	/**
	 * Withdraws an object from the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @return object from the knowledge repository
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object [] take(String entryKey) throws KRExceptionUnavailableEntry, KRExceptionAccessError;
}

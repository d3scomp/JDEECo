/*******************************************************************************
 * Copyright 2012 Charles University in Prague
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

/**
 * An abstract class defining basic operations on the knowledge
 * repository.
 * 
 * @author Michal Kit
 * 
 */
public abstract class KnowledgeRepository {

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
	public abstract Object [] get(String entryKey, ISession session)
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
	public abstract void put(String entryKey, Object value, ISession session)
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
	public abstract Object [] take(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError;
	
	/**
	 * Withdraws all objects related to the given id from the knowledge repository. 
	 * 
	 * The method is mainly used when there is a need to withdraw a full node from the repository.
	 * TODO: this has to be debugged mainly for consistency reasons. Not used in the demos yet.
	 * 
	 * This method is session oriented.
	 *  
	 * @param entryIdKey
	 * 			  key of the object in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return list of objects belonging to the provided id from the knowledge repository
	 * @throws KRExceptionAccessError
	 *			   thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object [] takeAll(String entryIdKey, ISession session) 
			throws KRExceptionUnavailableEntry, KRExceptionAccessError;
	
	/**
	 * Evaluates if the key is existing in the knowledge repository or not.
	 * 
	 * This is a faster alternative as it does only check the presence of the key
	 * without withdrawing/altering/... the repository.
	 * 
	 * Main use during the duck-typing when manipulating groups of members
	 * for the purpose of matching their identities to the given identified parameters.
	 * 
	 * This method is session oriented. 
	 * 
	 * @param entryIdKey
	 * 			  key of the object id in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return true if the key is existing in the knowledge repository
	 * @throws KRExceptionAccessError
	 *			   thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract boolean contains(String entryKey, ISession session) throws KRExceptionAccessError;
	
	/**
	 * Register a listener that should be notified by the knowledge repository
	 * whenever a specified properties are changing.
	 * 
	 * @param listener listening object
	 */
	public abstract boolean registerListener(IKnowledgeChangeListener listener);
	
	/**
	 * Unregisters knowledge listener that has been registered earlier.
	 * 
	 * @param listener listening object
	 */
	public abstract boolean unregisterListener(IKnowledgeChangeListener listener);
	
	
	/**
	 * Switching listening on or off
	 * Sets wether 
	 * 
	 * @param on if true the listening is on otherwise its off.
	 * 
	 */
	public abstract void setListenersActive(boolean on);
	
	
	/**
	 * Checks if current knowledge change listening is on or off.
	 * 
	 */
	public abstract boolean isListenersActive();

	/**
	 * Creates a session object, which can be used for all the operations on the
	 * knowledge repository.
	 * 
	 * @return session object
	 */
	public abstract ISession createSession();
	
	
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
			KRExceptionAccessError {
		return get(entryKey, null);
	}

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
			throws KRExceptionAccessError {
		put(entryKey, value, null);
	}

	/**
	 * Withdraws an object from the knowledge repository.
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
	public Object [] take(String entryKey) throws KRExceptionUnavailableEntry, KRExceptionAccessError {
		return take(entryKey, null);
	}
	
	/**
	 * Withdraws all objects related to the given id from the knowledge repository. 
	 * 
	 * @param entryKey
	 *            key of the object id in the knowledge repository
	 * @return list of objects belonging to the provided id from the knowledge repository
	 * @throws KRExceptionUnavailableEntry
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object [] takeAll(String entryIdKey) throws KRExceptionUnavailableEntry, KRExceptionAccessError {
		return takeAll(entryIdKey, null);
	}
	
	/**
	 * Evaluates if the key is existing in the knowledge repository or not.
	 * 
	 * @param entryKey
	 * 			key of the object id in the knowledge repository
	 * @return true 
	 * 			if the key is existing in the knowledge repository
	 * @throws KRExceptionAccessError
	 */
	public boolean contains(String entryKey) throws KRExceptionAccessError {
		return contains(entryKey, null);
	}
}

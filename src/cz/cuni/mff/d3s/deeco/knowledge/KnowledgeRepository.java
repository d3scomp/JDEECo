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

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;

/**
 * An abstract class specifing and defining basic operations on the knowledge
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
	 * @throws UnavailableEntryException
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object get(String entryKey, ISession session)
			throws UnavailableEntryException, KnowledgeRepositoryException;

	/**
	 * Reads all the objects for the specified key. This method is session
	 * oriented.
	 * 
	 * @param entryKey
	 *            key of the objects in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return an array of the matched objects
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object[] getAll(String entryKey, ISession session)
			throws KnowledgeRepositoryException;

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
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract void put(String entryKey, Object value, ISession session)
			throws KnowledgeRepositoryException;

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
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object take(String entryKey, ISession session)
			throws UnavailableEntryException, KnowledgeRepositoryException;

	/**
	 * Withdraws all the objects (matching the specified key) from the knowledge
	 * repository. This method is session oriented.
	 * 
	 * @param entryKey
	 *            key of the objects in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return an array of objects from the knowledge repository
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object[] takeAll(String entryKey, ISession session)
			throws KnowledgeRepositoryException;

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
	 * @throws UnavailableEntryException
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object get(String entryKey) throws UnavailableEntryException,
			KnowledgeRepositoryException {
		return get(entryKey, null);
	}

	/**
	 * Inserts an object to the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param value
	 *            inserted object
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public void put(String entryKey, Object value)
			throws KnowledgeRepositoryException {
		put(entryKey, value, null);
	}

	/**
	 * Withdraws an object from the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @return object from the knowledge repository
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object take(String entryKey) throws UnavailableEntryException, KnowledgeRepositoryException {
		return take(entryKey, null);
	}

	/**
	 * Withdraws all the objects (matching the specified key) from the knowledge
	 * repository.
	 * 
	 * @param entryKey
	 *            key of the objects in the knowledge repository
	 * @return an array of objects from the knowledge repository
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object[] takeAll(String entryKey)
			throws KnowledgeRepositoryException {
		return takeAll(entryKey, null);
	}

	/**
	 * Reads all the objects for the specified key.
	 * 
	 * @param entryKey
	 *            key of the objects in the knowledge repository
	 * @return an array of the matched objects
	 * @throws KnowledgeRepositoryException
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object[] findAll(String entryKey)
			throws KnowledgeRepositoryException {
		return getAll(entryKey, null);
	}
}

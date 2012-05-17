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

import java.lang.reflect.Type;

import cz.cuni.mff.d3s.deeco.exceptions.KMException;

/**
 * An abstract class providing higher level interface for accessing the
 * knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public abstract class KnowledgeManager {

	/**
	 * Retrieves knowledge from the knowledge repository, defined by both
	 * <code>knowledgePath</code> and <code>structure</code>. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the retrieval should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public abstract Object getKnowledge(String knowledgePath, Type type,
			ISession session) throws KMException;

	/**
	 * Puts knowledge object to the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @param session
	 *            a session object within which all the put operations should be
	 *            performed
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public abstract void putKnowledge(String knowledgePath, Object value, Type type,
			ISession session, boolean replace) throws KMException;

	/**
	 * Withdraws the knowledge from the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the withdrawals should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public abstract Object takeKnowledge(String knowledgePath, Type type,
			ISession session) throws KMException;

	/**
	 * Finds all the flat (not hierarchical structures) properties in the
	 * knowledge repository. This method is session oriented.
	 * 
	 * @param knowledgePath
	 *            absolute name (knowledge path) of the property
	 * @param session
	 *            a session object within which finding should be performed
	 * @return an array of matched objects
	 */
	public abstract Object[] findAllProperties(String knowledgePath,
			ISession session);

	/**
	 * Creates {@link ISession} instance used for enclosing all knowledge
	 * operations within a single session.
	 * 
	 * @return {@link ISession} object instance.
	 */
	public abstract ISession createSession();

	/**
	 * Retrieves knowledge from the knowledge repository, defined by both
	 * <code>knowledgePath</code> and <code>structure</code>.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object getKnowledge(String knowledgePath, Type type)
			throws KMException {
		return getKnowledge(knowledgePath, type, null);
	}

	/**
	 * Puts knowledge object to the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public void putKnowledge(String knowledgePath, Object value, Type type, ISession session)
			throws KMException {
		putKnowledge(knowledgePath, value, type, session, true);
	}

	/**
	 * Withdraws the knowledge from the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param structure
	 *            Knowledge structure (Knowledge Interface)
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object takeKnowledge(String knowledgePath, Type type)
			throws KMException {
		return takeKnowledge(knowledgePath, type, null);
	}

	/**
	 * Retrieves knowledge from the knowledge repository, defined by
	 * <code>knowledgePath</code>.
	 * 
	 * @param absolute
	 *            name (knowledge path) of the property to be retrieved
	 * @return retrieved knowledge object
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object getKnowledge(String knowledgePath) throws KMException {
		return getKnowledge(knowledgePath, null);
	}
	
	
	public void putKnowledge(String knowledgePath, Object value, Type type)
			throws KMException {
		putKnowledge(knowledgePath, value, type, null, true);
	}

	public Object takeKnowledge(String knowledgePath) throws KMException {
		return takeKnowledge(knowledgePath, null);
	}

	/**
	 * Finds all the flat (not hierarchical structures) properties in the
	 * knowledge repository.
	 * 
	 * @param knowledgePath
	 *            absolute name (knowledge path) of the property
	 * @return an array of matched objects
	 */
	public Object[] findAllProperties(String knowledgePath) {
		return findAllProperties(knowledgePath, null);
	}
}

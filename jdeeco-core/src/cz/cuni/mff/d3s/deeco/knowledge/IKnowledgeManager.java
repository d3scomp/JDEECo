/*******************************************************************************
 * Copyright 2012-2013 Charles University in Prague
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

import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.invokable.TypeDescription;
import cz.cuni.mff.d3s.deeco.runtime.IRuntime;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

public interface IKnowledgeManager {

	/**
	 * Retrieves knowledge from the knowledge repository, defined by
	 * <code>knowledgePath. The result is either map or flat element(s). This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param session
	 *            a session object within which all the retrieval should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object getKnowledge(String knowledgePath, ISession session)
			throws KMException;

	/**
	 * Retrieves knowledge from the knowledge repository, defined by both
	 * <code>knowledgePath</code> and <code>expectedType</code>. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param session
	 *            a session object within which all the retrieval should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object getKnowledge(String knowledgePath,
			TypeDescription expectedType, ISession session) throws KMException;

	/**
	 * Withdraws the knowledge from the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param type
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the withdrawals should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object takeKnowledge(String knowledgePath, ISession session)
			throws KMException;
	
	/**
	 * Withdraws the knowledge from the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @param type
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the withdrawals should be
	 *            performed
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object[] takeAllKnowledge(String knowledgeId, ISession session)
			throws KMException;

	/**
	 * Alters knowledge object in the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @param type
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the put operations should be
	 *            performed
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public void alterKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException;

	/**
	 * Puts knowledge object to the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @param type
	 *            Knowledge structure (Knowledge Interface)
	 * @param session
	 *            a session object within which all the put operations should be
	 *            performed
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException;
	
	/**
	 * Checks if the array of knowledge paths can exist altogether in the knowledge repository.
	 * e.g. in order to check the pattern of an input object.
	 * This method is session oriented.
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
	 * TODO: does it require any session as no change is done ?
	 */
	public boolean containsKnowledge(String knowledgePath, ISession session) throws KMException;

	/**
	 * Creates {@link ISession} instance used for enclosing all knowledge
	 * operations within a single session.
	 * 
	 * @return {@link ISession} object instance.
	 */
	public ISession createSession();

	/**
	 * Register a listener that should be notified by the knowledge manager
	 * whenever a specified properties are changing.
	 * 
	 * @param listener
	 *            listening object
	 */
	public boolean registerListener(IKnowledgeChangeListener listener);

	/**
	 * Unregisters the listener that has been registered earlier.
	 * 
	 * @param listener
	 *            listening object
	 */
	public boolean unregisterListener(IKnowledgeChangeListener listener);

	public void setListenersActive(boolean on);

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
	public Object getKnowledge(String knowledgePath) throws KMException;

	/**
	 * Withdraws the knowledge from the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object takeKnowledge(String knowledgePath) throws KMException;
	
	/**
	 * Withdraws the knowledge from the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge structure should be rooted
	 * @return retrieved knowledge object of type structure
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public Object[] takeAllKnowledge(String knowledgeId) throws KMException;

	/**
	 * Alters knowledge object in the knowledge repository.
	 * 
	 * @param knowledgePath
	 *            nesting at which the knowledge object should be stored
	 * @param value
	 *            knowledge to be stored
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public void alterKnowledge(String knowledgePath, Object value)
			throws KMException;

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
	public void putKnowledge(String knowledgePath, Object value)
			throws KMException;
	
	/**
	 * Checks if the array of knowledge paths can exist altogether in the knowledge repository.
	 * e.g. in order to check the pattern of an input object.
	 * This method is session oriented.
	 * 
	 * @param knowledgePaths
	 *            nesting set at which the knowledge object should be stored
	 * @throws KMException
	 *             thrown whenever there is a problem accessing the knowledge
	 *             repository
	 */
	public boolean containsKnowledge(String knowledgePath) throws KMException;
        
        /** 
         * Sets the runtime to the scheduler (without a runtime, the scheduler will not work).
         * 
         * @param rt runtime
         */
        public void setRuntime(IRuntime rt);
        
        /**
         * Returns a runtime reference. It might be <code>null</code> if the knowledge manager has not been set to any runtime.
         * 
         * @return a runtime reference
         */
        public IRuntime getRuntime();
        
        /**
         * Unsets the runtime.
         * 
         * @see IKnowledgeManager#setRuntime(cz.cuni.mff.d3s.deeco.runtime.IRuntime) 
         */
        public void unsetRuntime();
}

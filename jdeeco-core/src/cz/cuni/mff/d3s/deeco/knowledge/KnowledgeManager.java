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
import cz.cuni.mff.d3s.deeco.runtime.IRuntime;

/**
 * An abstract class providing higher level interface for accessing the
 * knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public abstract class KnowledgeManager implements IKnowledgeManager {
  
        protected IRuntime runtime;
	
	@Override
	public Object getKnowledge(String knowledgePath) throws KMException {
		return getKnowledge(knowledgePath, null);
	}

	@Override
	public Object takeKnowledge(String knowledgePath) throws KMException {
		return takeKnowledge(knowledgePath, null);
	}
	
	@Override
	public void alterKnowledge(String knowledgePath, Object value)
			throws KMException {
		alterKnowledge(knowledgePath, value, null);
	}
	
	@Override
	public void putKnowledge(String knowledgePath, Object value)
			throws KMException {
		putKnowledge(knowledgePath, value, null);
	}
	
	/**
	 * Checks if the array of knowledge entries are existing altogether in the knowledge repository
	 * This method is session oriented.
	 * 
	 * @param entryKeys
	 *            keys of the object in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return object from the knowledge repository
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 * TODO: does it require any session as no change is done ?
	 */
	@Override
	public boolean containsKnowledge(String knowledgePath) throws KMException {
		return containsKnowledge(knowledgePath, null);
	}
        
        @Override
        public void setRuntime(IRuntime rt) {
          runtime = rt;
        }
        
        @Override
        public void unsetRuntime() {
          runtime = null;
        }
        
        @Override
        public IRuntime getRuntime() {
          return runtime;
        }
}

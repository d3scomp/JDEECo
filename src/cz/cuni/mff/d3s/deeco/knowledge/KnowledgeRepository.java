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


public abstract class KnowledgeRepository {

	public abstract Object get(String entryKey, ISession session) throws UnavailableEntryException, KnowledgeRepositoryException;
	
	public abstract Object [] findAll(String entryKey, ISession session) throws KnowledgeRepositoryException;

	public abstract void put(String entryKey, Object value, ISession session) throws KnowledgeRepositoryException;

	public abstract Object take(String entryKey, ISession session) throws KnowledgeRepositoryException;
	
	public abstract Object [] takeAll(String entryKey, ISession session) throws KnowledgeRepositoryException;

	public abstract ISession createSession();
	
	public Object get(String entryKey) throws UnavailableEntryException, KnowledgeRepositoryException {
		return get(entryKey, null);
	}
	
	public void put(String entryKey, Object value) throws KnowledgeRepositoryException {
		put(entryKey, value, null);
	}
	
	public Object take(String entryKey) throws KnowledgeRepositoryException {
		return take(entryKey, null);
	}
	
	public Object [] takeAll(String entryKey) throws KnowledgeRepositoryException {
		return takeAll(entryKey, null);
	}
	
	public Object [] findAll(String entryKey) throws KnowledgeRepositoryException {
		return findAll(entryKey, null);
	}
}

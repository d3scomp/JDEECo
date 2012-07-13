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
package cz.cuni.mff.d3s.deeco.knowledge.local;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

/**
 * Implementation of the knowledge repository using a hashmap. This implementation allows only for local execution.
 * 
 * It uses a big global lock to protect a sessions. Thus it serializes all sessions working on the repository
 * (for the whole duration of the session). This means that with the current implementation, there is no benefit
 * on multicores. This repository is thus aimed to be used e.g. for JPF-based verification.  
 * 
 * @author Tomas Bures
 *
 */
public class LocalKnowledgeRepository extends KnowledgeRepository {

	final ReentrantLock lock = new ReentrantLock();
	final HashMap<String, List<Object>> ts = new HashMap<String, List<Object>>(); 
	
	@Override
	public Object get(String entryKey, ISession session)
			throws UnavailableEntryException, KnowledgeRepositoryException {
		
		List<Object> vals = ts.get(entryKey);
		
		if (vals == null) {
			throw new UnavailableEntryException("Key " + entryKey + " is not in the local knowledge repository.");
		}
		
		// TODO: Here we should create a deep copy.
		return vals.get(0);
	}

	@Override
	public Object[] getAll(String entryKey, ISession session)
			throws KnowledgeRepositoryException {
		
		List<Object> vals = ts.get(entryKey);
		
		if (vals == null) {
			return new Object[0];
		}
		
		// TODO: Here we should create a deep copy.
		return vals.toArray();
	}

	@Override
	public void put(String entryKey, Object value, ISession session)
			throws KnowledgeRepositoryException {

		List<Object> vals = ts.get(entryKey);
		
		if (vals == null) {
			vals = new LinkedList<Object>();
			ts.put(entryKey, vals);
		}
		
		// TODO: Here we should create a deep copy.
		vals.add(value);		
	}

	@Override
	public Object take(String entryKey, ISession session)
			throws UnavailableEntryException, KnowledgeRepositoryException {

		List<Object> vals = ts.get(entryKey);
		
		if (vals == null) {
			throw new UnavailableEntryException("Key " + entryKey + " is not in the local knowledge repository.");
		}
		
		if (vals.size() <= 1) {
			ts.remove(entryKey);
		}
		
		// TODO: Here we should create a deep copy.
		return vals.remove(0);
	}

	@Override
	public Object[] takeAll(String entryKey, ISession session)
			throws KnowledgeRepositoryException {
		
		List<Object> vals = ts.get(entryKey);
		
		if (vals == null) {
			return new Object[0];
		}
		
		ts.remove(entryKey);
		
		// TODO: Here we should create a deep copy.
		return vals.toArray();
	}

	@Override
	public ISession createSession() {
		return new LocalSession(this);
	}

	@Override
	public boolean listenForChange(IKnowledgeChangeListener listener) {
		return false;
	}

}

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
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

import com.rits.cloning.Cloner;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeChangeCollector;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryChangeNotifier;
import cz.cuni.mff.d3s.deeco.knowledge.TimeProvider;

/**
 * Implementation of the knowledge repository using a hashmap. This
 * implementation allows only for local execution.
 * 
 * It uses a big global lock to protect a sessions. Thus it serializes all
 * sessions working on the repository (for the whole duration of the session).
 * This means that with the current implementation, there is no benefit on
 * multicores. This repository is thus aimed to be used e.g. for JPF-based
 * verification.
 * 
 * @author Tomas Bures
 * 
 */
public class LocalKnowledgeRepository extends KnowledgeRepository {

	protected final ReentrantLock lock = new ReentrantLock();
	protected final HashMap<String, List<Object>> ts = new HashMap<String, List<Object>>();
	
	protected final Map<String, Long> timestamps = new HashMap<>();
	private final Map<String, LocalRepositoryChangeNotifier> internalNotifiers = new HashMap<>();
	private final Cloner cloner = new Cloner();
	
	@Override
	public Long getKnowledgeTimeStamp(String entryKey, ISession session) {
		if (session == null)
			lock.lock();
		Long result = timestamps.get(entryKey);
		if (result == null)
			result = -1L;
		if (session == null)
			lock.unlock();
		return result;
	}

	public LocalKnowledgeRepository(TimeProvider tp) {
		super(tp);
		// JPF Optimization - class initialization if lock is used causes state
		// space explosion
		// here we intentionally use the lock -> it will hopefully
		// "execute all class initializer"
		// here in single threaded code and reduce number of program states
		// Problematic class
		// java.util.concurrent.locks.AbstractQueuedSynchronizer$Node
		// Problematic class java.util.concurrent.locks.LockSupport
		Condition c = lock.newCondition();
		try {
			lock.lock();
			c.awaitNanos(-1); // Not sleep at all
			lock.unlock();
		} catch (InterruptedException e) {
		}
		LockSupport.unpark(null);
	}

	@Override
	public Object[] get(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError {

		if (session == null)
			lock.lock();
		List<Object> vals = ts.get(entryKey);

		if (vals == null) {
			throw new KRExceptionUnavailableEntry("Key " + entryKey
					+ " is not in the local knowledge repository.");
		}

		vals = cloner.deepClone(vals);
		if (session == null)
			lock.unlock();
		return vals.toArray();
	}

	@Override
	public void put(String entryKey, Object value, ISession session)
			throws KRExceptionAccessError {
		insert(entryKey, value, session);
		if (session != null) {
			KnowledgeChangeCollector kcc = (KnowledgeChangeCollector) session;
			if (!kcc.isKnowledgeRepositoryRegistered())
				kcc.registerKnowledgeRepository(this);
			kcc.knowledgeChanges(entryKey);
		}
		LocalRepositoryChangeNotifier notifier = internalNotifiers
				.get(entryKey);
		if (notifier != null)
			notifier.notify(entryKey);
	}

	@Override
	public Object[] take(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError {

		if (session == null)
			lock.lock();

		List<Object> vals = ts.get(entryKey);

		if (vals == null) {
			throw new KRExceptionUnavailableEntry("Key " + entryKey
					+ " is not in the local knowledge repository.");
		}

		if (vals.size() <= 1) {
			ts.remove(entryKey);
		}

		vals = cloner.deepClone(vals);

		if (session == null)
			lock.unlock();

		return vals.toArray();
	}

	@Override
	public ISession createSession() {
		return new LocalSession(this);
	}

	@Override
	public RepositoryChangeNotifier listenForChange(String entryKey)
			throws KRExceptionAccessError {
		String fullListenPath = KnowledgePathHelper.prependToRoot(entryKey,
				ConstantKeys.LISTEN_ID);
		LocalRepositoryChangeNotifier localNotifier = new LocalRepositoryChangeNotifier(
				this);
		lock.lock();
		insert(fullListenPath, "1", null);
		internalNotifiers.put(fullListenPath, localNotifier);
		lock.unlock();
		return localNotifier;
	}

	private void insert(String entryKey, Object value, ISession session)
			throws KRExceptionAccessError {
		if (session == null)
			lock.lock();
		List<Object> vals = ts.get(entryKey);

		if (vals == null) {
			vals = new LinkedList<Object>();
			ts.put(entryKey, vals);
		}
		Object newValue = cloner.deepClone(value);
		vals.add(newValue);
		timestamps.put(entryKey, tp.getCurrentTime());
		if (session == null)
			lock.unlock();
	}
}

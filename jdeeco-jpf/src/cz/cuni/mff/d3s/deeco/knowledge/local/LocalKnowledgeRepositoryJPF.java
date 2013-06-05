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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.ltl.AtomicProposition;

import cz.cuni.mff.d3s.deeco.ltl.CommlinkDEECoJPF;



/**
 * Implementation of the knowledge repository using a hashmap optimized for JPF
 * 
 * This implementation allows only for local execution.
 * It is based on the {@link LocalKnowledgeRepository}.
 * It allows for evaluating atomic propositions over the component knowledge after each change.
 * 
 * @author Jaroslav Keznikl
 * 
 */
public class LocalKnowledgeRepositoryJPF extends LocalKnowledgeRepository implements KnowledgeJPF {
	
	public HashMap<String, Boolean> propositionValues = new HashMap<>();	
	public HashMap<String, Boolean> propositionToEvaluate = new HashMap<>();
	
	List<AtomicProposition> propositions = new ArrayList<>();

	public LocalKnowledgeRepositoryJPF(List<AtomicProposition> propositions) {
		this.propositions = propositions;
		for (AtomicProposition ap : propositions) {
			propositionToEvaluate.put(ap.getName(), true);
			propositionValues.put(ap.getName(), false);
		}
		
		// JPF optimization
		// class initialization if the lock is used causes state space explosion
		// here we intentionally use the lock -> it will hopefully execute all class 
		// initializers in single threaded code and reduce number of program states
		// problematic class: java.util.concurrent.locks.AbstractQueuedSynchronizer$Node
		// problematic class java.util.concurrent.locks.LockSupport
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
	public void put(String entryKey, Object value, ISession session) throws KRExceptionAccessError {		
		// make sure that when outside of a session, put + evaluation of propositions is done atomically
		lock.lock();
		
		super.put(entryKey, value, session);
		
		for (AtomicProposition ap : propositions) {
			// propositionToEvaluate.get(...) might return null 
			if (propositionToEvaluate.get(ap.getName()) == true)
				propositionValues.put(ap.getName(), ap.evaluate(this));
		}

		// send names of atomic propositions into JPF
		// we consider only propositions that evaluate to "true" in the current state
		CommlinkDEECoJPF.notifyEventProcessingStart();		
		for (AtomicProposition ap : propositions) 
		{
			Boolean apVal = propositionValues.get(ap.getName());
			if ((apVal != null) && apVal.booleanValue()) CommlinkDEECoJPF.addTrueAtomicProposition(ap.getName());
		}
		CommlinkDEECoJPF.notifyAtomicPropositionsComplete();		
		
		lock.unlock();
	}

	@Override
	public Object [] get(String knowledgeId) {
		return ts.get(knowledgeId).toArray();
	}
	
	@Override
	public Object getSingle(String knowledgeId) {
		List<Object> v = ts.get(knowledgeId);
		if (v.isEmpty())
			return null;
		else
			return v.get(0);
	}
}

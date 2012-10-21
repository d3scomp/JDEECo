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
package cz.cuni.mff.d3s.deeco.knowledge.jini;

import java.rmi.RMISecurityManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KPBuilder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

/**
 * Class implementing <code>KnowledgeRepository</code> with use of tuple spaces
 * and transactions.
 * 
 * @author Michal Kit
 * 
 */
public class TSKnowledgeRepository extends KnowledgeRepository {

	private Map<String, TSRemoteEventListener> tsListeners;
	private boolean triggeringOn;

	public TSKnowledgeRepository() {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		tsListeners = new HashMap<String, TSRemoteEventListener>();
		triggeringOn = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#takeAll(java.lang
	 * .String, cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object[] take(String entryKey, ISession session)
			throws KRExceptionAccessError, KRExceptionUnavailableEntry {
		List<Object> resultList = new LinkedList<Object>();
		try {
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
//			Collection<?> tuples = space.take(Arrays
//					.asList(new Tuple[] { TSUtils.createTemplate(entryKey) }),
//					tx, 5, Long.MAX_VALUE);
//			if (tuples.size() > 0) {
//				Iterator<?> iterator = tuples.iterator();
//				for (int i = 0; i < tuples.size(); i++) {
//					resultList.add(((Tuple)iterator.next()).value);
//				}
//			}			
			Tuple tuple = null;
			do {
				tuple = (Tuple) space.takeIfExists(TSUtils.createTemplate(entryKey), tx, Lease.FOREVER);
				if (tuple != null)
					resultList.add(tuple.value);
				else
					break;
			} while (true);
		} catch (Exception e) {
			throw new KRExceptionAccessError(
					"TSKnowledgeRepository error when taking properties: "
							+ entryKey + " - " + e.getMessage());
		}
		if (resultList.size() == 0)
			throw new KRExceptionUnavailableEntry("Entry " + entryKey
					+ " unavailable!");
		//System.out.println("Taking: " + entryKey);
		return resultList.toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#findAll(java.lang
	 * .String, cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object[] get(String entryKey, ISession session)
			throws KRExceptionAccessError, KRExceptionUnavailableEntry {
		List<Object> resultList = new LinkedList<Object>();
		try {
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
			MatchSet tuples = space.contents(Arrays
					.asList(new Tuple[] { TSUtils.createTemplate(entryKey) }),
					tx, Lease.FOREVER, Long.MAX_VALUE);
			Tuple tuple = (Tuple) tuples.next();
			while (tuple != null) {
				resultList.add(tuple.value);
				tuple = (Tuple) tuples.next();
			}
		} catch (Exception e) {
			throw new KRExceptionAccessError(
					"TSKnowledgeRepository error when reading properties: "
							+ entryKey + " - " + e.getMessage());
		}
		if (resultList.size() == 0)
			throw new KRExceptionUnavailableEntry("Entry " + entryKey
					+ " unavailable!");
		
		return resultList.toArray();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#put(java.lang.String,
	 * java.lang.Object, cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public void put(String entryKey, Object value, ISession session)
			throws KRExceptionAccessError {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
			space.write(TSUtils.createTuple(entryKey, value), tx, Lease.FOREVER);
			//System.out.println("Writing entry: " + entryKey);
			if (session != null)
				((TransactionalSession) session).propertyChanged(entryKey, this);
		} catch (Exception e) {
			throw new KRExceptionAccessError(
					"TSKnowledgeRepository error when writing property: "
							+ entryKey + " - " + e.getMessage());
		}
		//System.out.println("Putting: " + entryKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#createSession()
	 */
	@Override
	public ISession createSession() {
		return new TransactionalSession();
	}
	
	@Override
	public synchronized void setListenersActive(boolean on) {
		triggeringOn = on;
	}
	
	@Override
	public synchronized boolean isListenersActive() {
		return triggeringOn;
	}

	@Override
	public boolean registerListener(IKnowledgeChangeListener kcListener) {
		if (kcListener != null) {
			TSRemoteEventListener tsListener;
			for (String kp : kcListener.getKnowledgePaths()) {
				tsListener = tsListeners.get(kp);
				if (tsListener == null) {
					tsListener = TSRemoteEventListener.getRemoteEventListener(this);
					tsListeners.put(kp, tsListener);
					try {
						addTSNotifier(kp, tsListener);
					} catch (Exception e) {
						return false;
					}
				}
				tsListener.addKCListener(kcListener);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean unregisterListener(IKnowledgeChangeListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	private void addTSNotifier(String kp, TSRemoteEventListener tsListener)
			throws KRExceptionAccessError {
		try {
			JavaSpace05 space = TSUtils.getSpace();
			String fullListenPath = KPBuilder.prependToRoot(kp,
					ConstantKeys.LISTEN_ID);
			Tuple t;
			TransactionalSession ts = (TransactionalSession) createSession();
			ts.begin();
			while (ts.repeat()) {
				t = (Tuple) space.readIfExists(
						TSUtils.createTemplate(fullListenPath),
						ts.getTransaction(), Lease.FOREVER);
				if (t == null) {
					space.write(TSUtils.createTuple(fullListenPath, "1"),
							ts.getTransaction(), Lease.FOREVER);
				}
				ts.end();
			}
			space.registerForAvailabilityEvent(
					Arrays.asList(new Tuple[] { TSUtils.createTemplate(fullListenPath) }), null, true,
					tsListener.getStub(), Lease.FOREVER, null);
			System.out.println("Listener added: " + fullListenPath);
		} catch (Exception e) {
			throw new KRExceptionAccessError(
					"TSKnowledgeRepository error when adding a listener for the property: "
							+ kp + " - " + e.getMessage());
		}
	}
}

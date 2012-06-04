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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;
import net.jini.space.JavaSpace;
import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;
import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;

/**
 * Class implementing <code>KnowledgeRepository</code> with use of tuple spaces
 * and transactions.
 * 
 * @author Michal Kit
 * 
 */
public class TSKnowledgeRepository extends KnowledgeRepository {

	public TSKnowledgeRepository() {
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#get(java.lang.String,
	 * cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object get(String entryKey, ISession session)
			throws UnavailableEntryException, KnowledgeRepositoryException {

		Tuple tuple = null;

		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
			tuple = (Tuple) space.readIfExists(
					TSUtils.createTemplate(entryKey), tx, Lease.FOREVER);
		} catch (Exception e) {
			throw new KnowledgeRepositoryException(
					"TSKnowledgeRepository error when reading property: "
							+ entryKey + " - " + e.getMessage());
		}
		
		if (tuple == null)
			throw new UnavailableEntryException("Entry " + entryKey
					+ " unavailable!");
		return tuple.value;
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
			throws KnowledgeRepositoryException {
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
			space.write(TSUtils.createTuple(entryKey, value), tx, Lease.FOREVER);
		} catch (Exception e) {
			throw new KnowledgeRepositoryException(
					"TSKnowledgeRepository error when writing property: "
							+ entryKey + " - " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#take(java.lang.String
	 * , cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object take(String entryKey, ISession session)
			throws KnowledgeRepositoryException, UnavailableEntryException {

		Tuple tuple = null;
		
		try {
			JavaSpace space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
			tuple = (Tuple) space.takeIfExists(
					TSUtils.createTemplate(entryKey), tx, Lease.FOREVER);

		} catch (Exception e) {
			throw new KnowledgeRepositoryException(
					"TSKnowledgeRepository error when taking property: "
							+ entryKey + " - " + e.getMessage());
		}

		if (tuple == null)
			throw new UnavailableEntryException("Entry " + entryKey
					+ " unavailable!");

		return tuple.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#takeAll(java.lang
	 * .String, cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object[] takeAll(String entryKey, ISession session)
			throws KnowledgeRepositoryException {
		try {
			JavaSpace05 space = TSUtils.getSpace();
			Transaction tx = (session != null) ? ((TransactionalSession) session)
					.getTransaction() : null;
			Collection<Tuple> tuples = space.take(Arrays
					.asList(new Tuple[] { TSUtils.createTemplate(entryKey) }),
					tx, Lease.FOREVER, Long.MAX_VALUE);
			if (tuples.size() > 0) {
				Object[] result = new Object[tuples.size()];
				Iterator<Tuple> iterator = tuples.iterator();
				for (int i = 0; i < tuples.size(); i++) {
					result[i] = iterator.next().value;
				}
				return result;
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new KnowledgeRepositoryException(
					"TSKnowledgeRepository error when takingAll properties: "
							+ entryKey + " - " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository#findAll(java.lang
	 * .String, cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object[] getAll(String entryKey, ISession session)
			throws KnowledgeRepositoryException {
		try {
			List<Object> resultList = new LinkedList<Object>();
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
			return (resultList.size() > 0) ? resultList.toArray() : null;
		} catch (Exception e) {
			throw new KnowledgeRepositoryException(
					"TSKnowledgeRepository error when readingAll properties: "
							+ entryKey + " - " + e.getMessage());
		}
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
}

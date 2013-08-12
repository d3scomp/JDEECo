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

import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeChangeCollector;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * Class implementing session functionalities, using transaction.
 * 
 * @author Michal Kit
 * 
 */
public class TransactionalSession extends KnowledgeChangeCollector {
	protected final static Long DEFAULT_TRANSACTION_TIMEOUT = 1500L; // .5 sec
	protected final static int MAX_REPETITIONS = 3;
	
	// TODO: check that this can be non-static (since it was static before)
	private TransactionManager txManager = null;

	private Transaction tx;
	private boolean succeeded = false;
	private int count = MAX_REPETITIONS;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#begin()
	 */
	@Override
	public void begin() {
		//LoggerFactory.getLogger().fine("Session starts - " + this.toString());
		if (tx != null) {
			cancel();
		}
		tx = createTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#end()
	 */
	@Override
	public void end() {
		//LoggerFactory.getLogger().fine("Session ends - " + this.toString());
		if (tx != null)
			try {
				count--;
				notifyAboutKnowledgeChange();
				tx.commit();
				tx = null;
				succeeded = true;
			} catch (Exception e) {
				if (repeat())
					tx = createTransaction();
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#cancel()
	 */
	@Override
	public void cancel() {
		//LoggerFactory.getLogger().fine("Session cancel - " + this.toString());
		if (tx != null && !succeeded)
				try {
					count = 0;
					tx.abort();
				} catch (Exception e) {
				} finally {
					tx = null;
				}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#repeat()
	 */
	@Override
	public boolean repeat() {
		return !succeeded && count > 0;
	}

	/**
	 * Returns the transaction object, used as this session implementation.
	 * 
	 * @return transaction object wrapped by this session
	 */
	public Transaction getTransaction() {
		return tx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#hasSucceeded()
	 */
	@Override
	public boolean hasSucceeded() {
		return succeeded;
	}

	
	/**
	 * Returns transaction instance retrieved from the transaction manager.
	 * 
	 * @return transaction instance
	 */
	private synchronized Transaction createTransaction() {
		try {
			if (txManager == null) {
				Lookup transactionLookup = new Lookup(TransactionManager.class);
				txManager = (TransactionManager) transactionLookup.getService();
			}
			Transaction.Created trc = TransactionFactory.create(txManager,
					DEFAULT_TRANSACTION_TIMEOUT);
			return trc.transaction;
		} catch (Exception e) {
			Log.e("Transaction retrieval error",e);
			return null;
		}
	}

}

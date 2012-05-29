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

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import net.jini.core.transaction.Transaction;

/**
 * Class implementing session functionalities, using transaction.
 * 
 * @author Michal Kit
 * 
 */
public class TransactionalSession implements ISession {

	private static final int MAX_REPETITIONS = 5;

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
		if (tx != null) {
			try {
				cancel();
			} catch (SessionException se) {
				
			}
		}
		tx = TransactionUtils.createTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#end()
	 */
	@Override
	public void end() throws SessionException {
		if (tx != null)
			try {
				count--;
				tx.commit();
				tx = null;
				succeeded = true;
			} catch (Exception e) {
				if (repeat())
					tx = TransactionUtils.createTransaction();
			}
		else
			throw new SessionException("Session not started!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#cancel()
	 */
	@Override
	public void cancel() throws SessionException {
		if (tx != null)
			if (!succeeded)
				try {
					tx.abort();
				} catch (Exception e) {
				} finally {
					tx = null;
				}
			else
				throw new SessionException("Session has already ended!");
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

}

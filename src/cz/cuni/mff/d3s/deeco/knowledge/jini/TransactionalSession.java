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
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;

/**
 * Class implementing session functionalities, using transaction.
 * 
 * @author Michal Kit
 * 
 */
public class TransactionalSession implements ISession {

	private static final int MAX_REPETITIONS = 3;

	private Transaction tx;
	private boolean succeeded = false;
	private int count = MAX_REPETITIONS;
	private ChangeNotifier cn = null;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#begin()
	 */
	@Override
	public void begin() {
		//System.out.println("Session starts - " + this.toString());
		if (tx != null) {
			cancel();
		}
		tx = TransactionUtils.createTransaction();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#end()
	 */
	@Override
	public void end() {
		//System.out.println("Session ends - " + this.toString());
		if (tx != null)
			try {
				count--;
				if (cn != null)
					cn.notifyAboutChanges(this);
				tx.commit();
				tx = null;
				succeeded = true;
			} catch (Exception e) {
				if (repeat())
					tx = TransactionUtils.createTransaction();
			}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.ISession#cancel()
	 */
	@Override
	public void cancel() {
		//System.out.println("Session cancel - " + this.toString());
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
	
	public void propertyChanged(String knowledgePath, KnowledgeRepository kr) {
		if (cn == null) {
			cn = new ChangeNotifier(kr);
		}
		cn.knowledgeWritten(knowledgePath);
	}

}

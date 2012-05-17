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

import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import net.jini.core.transaction.Transaction;

public class TransactionalSession implements ISession {

	private static final int MAX_REPETITIONS = 5;

	private Transaction tx;
	private boolean succeeded = false;
	private int count = MAX_REPETITIONS;

	@Override
	public void begin() {
		if (tx == null)
			tx = TransactionUtils.createTransaction();
	}

	@Override
	public void end() throws SessionException {
		if (tx != null)
			try {
				count--;
				tx.commit();
				tx = null;
				succeeded = true;
			} catch (Exception e) {
			}
		else
			throw new SessionException("Session not started!");
	}

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

	@Override
	public boolean repeat() {
		return !succeeded && count > 0;
	}

	public Transaction getTransaction() {
		return tx;
	}

	@Override
	public boolean hasSucceeded() {
		return succeeded;
	}

}

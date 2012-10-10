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

/**
 * Utility class, used for accessing the transaction manager.
 * 
 * @author Michal Kit
 * 
 */
public class TransactionUtils {
	protected final static Long DEFAULT_TRANSACTION_TIMEOUT = 150000L; // .5 sec
	private volatile static TransactionManager txManager = null;

	/**
	 * Returns transaction instance retrieved from the transaction manager.
	 * 
	 * @return transaction instance
	 */
	public synchronized static Transaction createTransaction() {
		try {
			if (txManager == null) {
				Lookup transactionLookup = new Lookup(TransactionManager.class);
				txManager = (TransactionManager) transactionLookup.getService();
			}
			Transaction.Created trc = TransactionFactory.create(txManager,
					DEFAULT_TRANSACTION_TIMEOUT);
			return trc.transaction;
		} catch (Exception e) {
			System.out.println("ERROR - Transaction retrieval error: "
					+ e.getMessage());
			return null;
		}
	}
}

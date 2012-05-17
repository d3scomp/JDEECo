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

import net.jini.core.transaction.Transaction;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.server.TransactionManager;

public class TransactionUtils {
	protected final static Long DEFAULT_LEASE_TIMEOUT = 100000L; // 100 sec
	private static TransactionManager txManager = null;
	
	private static Object transactionLock = new Object();

	public static Transaction createTransaction() {
		synchronized (transactionLock) {
			try {
				if (txManager == null) {
					Lookup transactionLookup = new Lookup(
							TransactionManager.class);
					txManager = (TransactionManager) transactionLookup
							.getService();
				}
				Transaction.Created trc = TransactionFactory.create(
						txManager, DEFAULT_LEASE_TIMEOUT);
				return trc.transaction;
			} catch (Exception e) {
				System.out.println("ERROR - Transaction retrieval error: "
						+ e.getMessage());
				return null;
			}
		}
	}
}

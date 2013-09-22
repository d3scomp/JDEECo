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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.logging.Log;

/**
 * An abstract class defining basic operations on the knowledge repository.
 * 
 * @author Michal Kit
 * 
 */
public abstract class KnowledgeRepository {

	public static final int THREAD_POOL_SIZE = 10;

	private boolean triggeringActive = false;
	private ExecutorService executor;
	
	protected final TimeProvider tp;

	public KnowledgeRepository(TimeProvider tp, int threadPoolSize) {
		this.executor = Executors.newFixedThreadPool(threadPoolSize);
		this.tp = tp;
	}
	
	public KnowledgeRepository(TimeProvider tp) {
		this(tp, THREAD_POOL_SIZE);
	}

	public boolean isTriggeringActive() {
		return triggeringActive;
	}

	public void setTriggeringActive(boolean triggeringActive) {
		this.triggeringActive = triggeringActive;
	}

	public String notify(String knowledgePath, String lastProcessed,
			List<IKnowledgeChangeListener> listeners) {
		String result = null;
		ISession session = null;
		try {
			String stringVersionAndOwner;
			String[] versionOwner;
			Object[] tObjects;
			session = createSession();
			session.begin();
			while (session.repeat()) {
				tObjects = get(knowledgePath, session);
				stringVersionAndOwner = (String) tObjects[0];
				versionOwner = extractVersionOwner(stringVersionAndOwner);
				if (versionOwner != null) {
					final String version = versionOwner[0];
					final String owner = versionOwner[1];
					if (!version.equals(lastProcessed)) {
						final TriggerType triggerType = getTriggerRecipient(knowledgePath);
						for (final IKnowledgeChangeListener listener : listeners)
							executor.submit(new Runnable() {
								@Override
								public void run() {
									listener.knowledgeChanged(owner,
											triggerType);
								}
							});
						result = version;
					}
				}
				session.end();
			}
			return result;
		} catch (Exception e) {
			if (session != null)
				session.cancel();
			Log.e("Notification exception", e);
		}
		return null;

	}

	public static String[] extractVersionOwner(String string) {
		String[] dString = KnowledgePathHelper.decomposePath(string);
		if (dString.length == 2) { // correct format
			return dString;
		}
		return null;
	}

	public static TriggerType getTriggerRecipient(String listenKey) {
		String[] dString = KnowledgePathHelper.decomposePath(listenKey);
		if (dString.length > 2) { // correct format
			return TriggerType.fromString(dString[1]);
		}
		return null;
	}

	/**
	 * Reads a single entry from the knowledge repository. This method is
	 * session oriented.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return object in the knowledge repository
	 * @throws KRExceptionUnavailableEntry
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object[] get(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError;

	/**
	 * Inserts an object to the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param value
	 *            inserted object
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract void put(String entryKey, Object value, ISession session)
			throws KRExceptionAccessError;

	/**
	 * Withdraws an object from the knowledge repository. This method is session
	 * oriented.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param session
	 *            a session object within which the operation should be
	 *            performed
	 * @return object from the knowledge repository
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public abstract Object[] take(String entryKey, ISession session)
			throws KRExceptionUnavailableEntry, KRExceptionAccessError;
	
	/**
	 * Returns the most recent time stamp for this tuple.
	 * 
	 * @param entryKey - tuple key
	 * @return Time stamp of the last update.
	 */
	public abstract Long getKnowledgeTimeStamp(String entryKey, ISession session);

	/**
	 * Creates a session object, which can be used for all the operations on the
	 * knowledge repository.
	 * 
	 * @return session object
	 */
	public abstract ISession createSession();

	/**
	 * Reads a single entry from the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @return object in the knowledge repository
	 * @throws KRExceptionUnavailableEntry
	 *             thrown whenever the object for the specified
	 *             <code>entryKey</code> does not exists
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object[] get(String entryKey) throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		return get(entryKey, null);
	}

	/**
	 * Inserts an object to the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @param value
	 *            inserted object
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public void put(String entryKey, Object value)
			throws KRExceptionAccessError {
		put(entryKey, value, null);
	}

	/**
	 * Withdraws an object from the knowledge repository.
	 * 
	 * @param entryKey
	 *            key of the object in the knowledge repository
	 * @return object from the knowledge repository
	 * @throws KRExceptionAccessError
	 *             thrown whenever there is a knowledge repository access
	 *             problem
	 */
	public Object[] take(String entryKey) throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		return take(entryKey, null);
	}
	
	

	public abstract RepositoryChangeNotifier listenForChange(String entryKey)
			throws KRExceptionAccessError;
}

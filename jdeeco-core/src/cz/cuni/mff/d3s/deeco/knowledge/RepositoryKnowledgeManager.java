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

import java.util.HashMap;

import cz.cuni.mff.d3s.deeco.exceptions.KMAccessException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

/*
 * Requires refactoring
 * 
 * @author Michal Kit
 *
 */
public class RepositoryKnowledgeManager extends KnowledgeManager {

	private RepositoryKnowledgeManagerHelper rkmh;

	public RepositoryKnowledgeManager() {
		unsetKnowledgeRepository(null);
	}

	public RepositoryKnowledgeManager(KnowledgeRepository kr) {
		setKnowledgeRepository(kr);
	}

	/**
	 * This method is required by the OSGi framework.
	 * 
	 * @param kr
	 */
	public synchronized void setKnowledgeRepository(Object kr) {
		this.rkmh = new RepositoryKnowledgeManagerHelper(
				(KnowledgeRepository) kr, this);
	}

	/**
	 * This method is required by the OSGi framewrok.
	 * 
	 * @param kr
	 */
	public synchronized void unsetKnowledgeRepository(Object kr) {
		this.rkmh = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#createSession()
	 */
	@Override
	public ISession createSession() {
		return rkmh.createSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#getKnowledge(java.lang
	 * .String, java.lang.reflect.Type,
	 * cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object getKnowledge(String knowledgePath, ISession session)
			throws KMException {
		return getKnowledge(false, knowledgePath, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#listenForChange(cz.cuni
	 * .mff.d3s.deeco.scheduling.IKnowledgeChangeListener)
	 */
	@Override
	public boolean registerListener(IKnowledgeChangeListener listener) {
		return rkmh.registerListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#unregisterListener(cz
	 * .cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener)
	 */
	@Override
	public boolean unregisterListener(IKnowledgeChangeListener listener) {
		return rkmh.unregisterListener(listener);
	}

	@Override
	public void setListenersActive(boolean on) {
		rkmh.setListenersActive(on);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#takeKnowledge(java.lang
	 * .String, java.lang.reflect.Type,
	 * cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object takeKnowledge(String knowledgePath, ISession session)
			throws KMException {
		return getKnowledge(true, knowledgePath, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#putKnowledge(java.lang
	 * .String, java.lang.Object, java.lang.reflect.Type,
	 * cz.cuni.mff.d3s.deeco.knowledge.ISession, boolean)
	 */
	@Override
	public void alterKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		storeKnowledge(knowledgePath, value, session, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#putKnowledge(java.lang
	 * .String, java.lang.Object, cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		storeKnowledge(knowledgePath, value, session, false);

	}

	private void storeKnowledge(String knowledgePath, Object value,
			ISession session, boolean modify) throws KMAccessException {
		if (rkmh != null) {
			ISession localSession;
			if (session == null) {
				localSession = rkmh.createSession();
				localSession.begin();
			} else
				localSession = session;
			String tPath, tString;
			IObjectAccessor accessor;
			Object[] structure;
			try {
				while (localSession.repeat()) {
					structure = rkmh.putStructure(knowledgePath, value,
							localSession, modify);
					if (structure == null)
						rkmh.putFlat(knowledgePath, value, localSession, modify);
					else if (structure.length > 0) {
						accessor = KMHelper.getObjectAccessor(value);
						for (Object s : structure) {
							tString = (String) s;
							tPath = KPBuilder.appendToRoot(knowledgePath,
									tString);
							alterKnowledge(tPath, accessor.getValue(tString),
									localSession);
						}
					}
					if (session == null)
						localSession.end();
					else
						break;
				}
			} catch (KRExceptionAccessError kre) {
				if (session == null)
					localSession.cancel();
				throw new KMAccessException(kre.getMessage());
			} catch (Exception e) {
				localSession.cancel();
				System.out.println(e.getMessage());
			}
		} else
			new KMAccessException("Knowledge repository unavailable");
	}

	private Object getKnowledge(boolean withdrawal, String knowledgePath,
			ISession session) throws KMException {
		if (rkmh != null) {
			Object result = null;
			ISession locSession = null;
			if (session == null) {
				locSession = rkmh.createSession();
				locSession.begin();
			}

			final ISession localSession = (session == null ? locSession
					: session);

			try {
				while (localSession.repeat()) {
					Object[] structure = rkmh.getStructure(withdrawal,
							knowledgePath, localSession);
					if (structure == null) // flat
						result = rkmh.getFlat(withdrawal, knowledgePath,
								localSession);
					else {
						String tPath, tString;
						HashMap<String, Object> map = new HashMap<String, Object>();
						for (Object s : structure) {
							tString = (String) s;
							tPath = KPBuilder.appendToRoot(knowledgePath,
									tString);
							map.put(tString,
									getKnowledge(withdrawal, tPath,
											localSession));
						}
						result = map;
					}
					if (session == null)
						localSession.end();
					else
						break;
				}
				return result;
			} catch (KRExceptionUnavailableEntry uee) {
				if (session == null)
					localSession.cancel();
				throw new KMNotExistentException(uee.getMessage());
			} catch (KRExceptionAccessError kre) {
				if (session == null)
					localSession.cancel();
				throw new KMAccessException(kre.getMessage());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				localSession.cancel();
				return null;
			}
		} else
			throw new KMAccessException("Knowledge repository unavailable");
	}
}
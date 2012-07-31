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

import java.lang.reflect.Field;

import cz.cuni.mff.d3s.deeco.exceptions.KMAccessException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

/*
 * Requires refactoring
 * 
 * @author Michal Kit
 *
 */
public class RepositoryKnowledgeManager extends KnowledgeManager {

	private RepositoryKnowledgeManagerHelper rkmh;
	private TraversableRKMHelper trkmh;

	public RepositoryKnowledgeManager(KnowledgeRepository kr) {
		this.rkmh = new RepositoryKnowledgeManagerHelper(kr, this);
		this.trkmh = new TraversableRKMHelper(rkmh, this);
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
	public void putKnowledge(String knowledgePath, Object value,
			ISession session) throws KMException {
		ISession localSession;
		if (session == null) {
			localSession = rkmh.createSession();
			localSession.begin();
		} else
			localSession = session;
		String tempPath;
		try {
			while (localSession.repeat()) {
				if (value == null) {
					rkmh.putFlat(knowledgePath, null, localSession);// put null value
				} else {
					Class<?> structure = value.getClass();
					if (KMHelper.isOutputWrapper(structure)) {
						putKnowledge(knowledgePath,
								((OutWrapper<?>) value).item, localSession);
					} else if (KMHelper.isTraversable(structure)) {
						trkmh.putTraversable(knowledgePath, value, structure,
								localSession);
					} else if (KMHelper.isKnowledge(structure)) {
						rkmh.putStructure(knowledgePath, structure, localSession);
						Field[] fields = structure.getFields();
						for (Field f : fields) {
							tempPath = KPBuilder.appendToRoot(knowledgePath,
									f.getName());
							putKnowledge(tempPath, f.get(value), localSession);
						}
					} else {
						rkmh.putFlat(knowledgePath, value, localSession);
					}
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
		} catch (KnowledgeRepositoryException kre) {
			throw new KMAccessException(kre.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			localSession.cancel();
		}
	}

	private Object getKnowledge(boolean withdrawal, String knowledgePath,
			ISession session) throws KMException {
		Object result = null;
		ISession localSession;
		if (session == null) {
			localSession = rkmh.createSession();
			localSession.begin();
		} else
			localSession = session;
		try {
			while (localSession.repeat()) {
				Object structure = rkmh.getStructure(withdrawal, knowledgePath, localSession);
				if (structure == null) // flat
					result = rkmh.getFlat(withdrawal, knowledgePath, localSession);
				else {
					Class<?> classStructure = (Class<?>) structure;
					if (KMHelper.isTraversable(classStructure)) {
						result = trkmh.getTraversable(withdrawal, knowledgePath, localSession);
					} else {// knowledge
						String tempPath;
						result = classStructure.newInstance();
						for (Field f : classStructure.getFields()) {
							tempPath = KPBuilder.appendToRoot(knowledgePath,
									f.getName());
							f.set(result,
									getKnowledge(withdrawal, tempPath,
											localSession));
						}
					}
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			return result;
		} catch (IllegalArgumentException iae) {
			throw new KMIllegalArgumentException(iae.getMessage());
		} catch (UnavailableEntryException uee) {
			throw new KMNotExistentException(uee.getMessage());
		} catch (KnowledgeRepositoryException kre) {
			throw new KMAccessException(kre.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
			localSession.cancel();
			return null;
		}
	}

	@Override
	public boolean listenForChange(IKnowledgeChangeListener listener) {
		return rkmh.listenForChange(listener);
	}
}

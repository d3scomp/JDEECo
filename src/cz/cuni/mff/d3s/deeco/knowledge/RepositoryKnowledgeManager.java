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
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.exceptions.KMAccessException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMIllegalArgumentException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeRepositoryException;
import cz.cuni.mff.d3s.deeco.exceptions.SessionException;
import cz.cuni.mff.d3s.deeco.exceptions.UnavailableEntryException;

public class RepositoryKnowledgeManager extends KnowledgeManager {

	private KnowledgeRepository kr;

	public RepositoryKnowledgeManager(KnowledgeRepository kr) {
		this.kr = kr;
	}

	@Override
	public ISession createSession() {
		return kr.createSession();
	}

	@Override
	public Object getKnowledge(String knowledgePath, Type type, ISession session)
			throws KMException {
		return retrieveKnowledge(false, knowledgePath, type, session);
	}

	@Override
	public Object takeKnowledge(String knowledgePath, Type type,
			ISession session) throws KMException {
		return retrieveKnowledge(true, knowledgePath, type, session);
	}

	@Override
	public void putKnowledge(String knowledgePath, Object value, Type type,
			ISession session, boolean replace) throws KMException {
		ISession localSession = (session == null) ? kr.createSession()
				: session;
		try {
			while (localSession.repeat()) {
				localSession.begin();
				if (KMHelper.isOutputWrapper(type)) {
					putKnowledge(knowledgePath,
							(value != null) ? ((OutWrapper) value).item : null,
							KMHelper.getOutWrapperParamType(type),
							localSession, replace);
				} else {
					if (KMHelper.isKnowledge(type)) {
						Class structure = KMHelper.getClass(type);
						String innerPath;
						Field[] fields = structure.getFields();
						for (Field f : fields) {
							innerPath = KPBuilder.appendToRoot(knowledgePath,
									f.getName());
							if (value != null)
								putKnowledge(innerPath, f.get(value),
										f.getGenericType(), localSession,
										replace);
							else
								takeKnowledge(innerPath, f.getGenericType(),
										localSession);
						}
						if (value == null)
							putFlat(knowledgePath, null, localSession, replace);
					} else if (KMHelper.isTraversable(type)) {
						putTraversable(knowledgePath, value, type,
								localSession, replace);
					} else {// flat property
						putFlat(knowledgePath, value, localSession, replace);
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
			try {
				localSession.cancel();
			} catch (SessionException se) {
			}
		}
	}

	@Override
	public Object[] findAllProperties(String knowledgePath, ISession session) {
		ISession localSession = (session == null) ? kr.createSession()
				: session;
		try {
			Object[] result = null;
			while (localSession.repeat()) {
				localSession.begin();
				result = kr.findAll(knowledgePath, localSession);
				if (session == null)
					localSession.end();
				else
					break;
			}
			return (result == null || result.length == 0) ? null : result;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {
				session.cancel();
			} catch (SessionException se) {
			}
			return null;
		}
	}

	private Object retrieveKnowledge(boolean withdrawal, String knowledgePath,
			Type type, ISession session) throws KMException {
		Object result = null;
		ISession localSession = (session == null) ? kr.createSession()
				: session;
		try {
			Class structure = KMHelper.getClass(type);
			while (localSession.repeat()) {
				localSession.begin();
				if (KMHelper.isOutputWrapper(structure)) {
					OutWrapper owi = (OutWrapper) KMHelper.getInstance(type);
					owi.item = retrieveKnowledge(withdrawal, knowledgePath,
							KMHelper.getOutWrapperParamType(type), localSession);
					return owi;
				} else if (KMHelper.isKnowledge(structure)) {
					result = structure.newInstance();
					String innerPath;
					Class innerStructure;
					for (Field f : structure.getFields()) {
						innerPath = KPBuilder.appendToRoot(knowledgePath,
								f.getName());
						innerStructure = f.getType();
						f.set(result,
								retrieveKnowledge(withdrawal, innerPath,
										innerStructure, localSession));
					}
				} else if (KMHelper.isTraversable(structure)) {
					result = getTraversable(withdrawal, knowledgePath,
							localSession);
				} else {
					result = (withdrawal) ? kr
							.take(knowledgePath, localSession) : kr.get(
							knowledgePath, localSession);
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
			try {
				localSession.cancel();
			} catch (SessionException se) {
			}
			return null;
		}
	}

	private void putFlat(String knowledgePath, Object newValue,
			ISession session, boolean replace)
			throws KnowledgeRepositoryException {
		Object currentValue = null;
		try {
			if (replace) {
				currentValue = kr.get(knowledgePath, session);
				if ((newValue != null && !newValue.equals(currentValue))
						|| (currentValue != null && !currentValue
								.equals(newValue))) {
					kr.take(knowledgePath, session);
					kr.put(knowledgePath, newValue, session);
				}
			} else {
				kr.put(knowledgePath, newValue, session);
			}
		} catch (UnavailableEntryException uee) {
			System.out.println("Unavailable entry: " + knowledgePath);
			kr.put(knowledgePath, newValue, session);
		}
	}

	private void putTraversable(String knowledgePath, Object value, Type type,
			ISession session, boolean replace)
			throws KnowledgeRepositoryException, KMException {
		Class structure = null;
		Map<String, Object> traversable = null;
		Set<String> keys = null;
		if (value != null) {
			structure = value.getClass();
			traversable = KMHelper.translateToMap(value);
			keys = traversable.keySet();
			Set<Map.Entry<String, Object>> entries = traversable.entrySet();
			for (Map.Entry<String, Object> entry : entries)
				putKnowledge(
						KPBuilder.appendToRoot(knowledgePath, entry.getKey()),
						entry.getValue(), null, session, replace);
		}
		removeRedundantTraversable(knowledgePath, keys, session);
		putKnowledge(KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.TRAVERSABLE_KEYS_ID),
				(keys != null) ? keys.toArray(new String[keys.size()]) : null, null, session, replace);
		putKnowledge(KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.TRAVERSABLE_CLASS_ID), structure, null, session,
				replace);
		putKnowledge(KPBuilder.appendToRoot(knowledgePath,
				ConstantKeys.TRAVERSABLE_ELEMENT_CLASS_ID),
				KMHelper.getTraversableElementType(type), null, session,
				replace);
	}

	private Object getTraversable(boolean withdrawal, String knowledgePath,
			ISession session) throws UnavailableEntryException,
			KnowledgeRepositoryException, InstantiationException,
			IllegalAccessException, KMException {
		Object tResult = null;
		Class resultClass = null;
		if (withdrawal)
			resultClass = (Class) kr.take(KPBuilder.appendToRoot(knowledgePath,
					ConstantKeys.TRAVERSABLE_CLASS_ID), session);
		else
			resultClass = (Class) kr.get(KPBuilder.appendToRoot(knowledgePath,
					ConstantKeys.TRAVERSABLE_CLASS_ID), session);
		if (resultClass != null) {
			String[] currentKeys = null;
			if (withdrawal)
				currentKeys = (String[]) kr.take(KPBuilder.appendToRoot(
						knowledgePath, ConstantKeys.TRAVERSABLE_KEYS_ID),
						session);
			else
				currentKeys = (String[]) kr.get(KPBuilder.appendToRoot(
						knowledgePath, ConstantKeys.TRAVERSABLE_KEYS_ID),
						session);
			Type tElementType = null;
			if (withdrawal)
				tElementType = (Type) kr.take(KPBuilder.appendToRoot(
						knowledgePath, ConstantKeys.TRAVERSABLE_CLASS_ID),
						session);
			else
				tElementType = (Type) kr.get(KPBuilder.appendToRoot(
						knowledgePath, ConstantKeys.TRAVERSABLE_CLASS_ID),
						session);
			resultClass.newInstance();
			Object currentElement;
			for (String k : currentKeys) {
				currentElement = retrieveKnowledge(false,
						KPBuilder.appendToRoot(knowledgePath, k), tElementType,
						session);
				KMHelper.addElementToTraversable(currentElement, tResult, k);
			}
		}
		return tResult;

	}

	private void removeRedundantTraversable(String knowledgePath,
			Set<String> keys, ISession session)
			throws KnowledgeRepositoryException {
		String[] currentKeys = null;
		try {
			currentKeys = (String[]) kr.get(KPBuilder.appendToRoot(
					knowledgePath, ConstantKeys.TRAVERSABLE_KEYS_ID));
		} catch (UnavailableEntryException uee) {
		}
		if (currentKeys != null) {
			List<String> listOfCurrentKeys = Arrays.asList(currentKeys);
			if (listOfCurrentKeys != null) {
				if (keys != null)
					listOfCurrentKeys.removeAll(keys);
				for (String s : listOfCurrentKeys)
					kr.take(KPBuilder.appendToRoot(knowledgePath, s), session);
			}
		}
	}
}

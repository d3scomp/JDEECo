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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMAccessException;
import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.exceptions.KMNotExistentException;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionAccessError;
import cz.cuni.mff.d3s.deeco.exceptions.KRExceptionUnavailableEntry;
import cz.cuni.mff.d3s.deeco.invokable.TypeDescription;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.scheduling.IKnowledgeChangeListener;

/**
 * KnowledgeManager version using the JavaSpaces implementation for the
 * knowledge storage.
 * 
 * @author Michal Kit
 * 
 */
public class RepositoryKnowledgeManager extends KnowledgeManager {

	public final static long serialVersionUID = 1L;

	private KnowledgeRepository kr;

	public RepositoryKnowledgeManager() {
		unsetKnowledgeRepository(null);
	}

	public RepositoryKnowledgeManager(KnowledgeRepository kr) {
		this.kr = kr;
	}

	/**
	 * This method is required by the OSGi framework.
	 * 
	 * @param kr
	 */
	public synchronized void setKnowledgeRepository(Object kr) {
		this.kr = (KnowledgeRepository) kr;
	}

	/**
	 * This method is required by the OSGi framewrok.
	 * 
	 * @param kr
	 */
	public synchronized void unsetKnowledgeRepository(Object kr) {
		this.kr = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager#createSession()
	 */
	@Override
	public ISession createSession() {
		return kr.createSession();
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
	 * cz.cuni.mff.d3s.deeco.knowledge.IKnowledgeManager#getKnowledge(java.lang
	 * .String, cz.cuni.mff.d3s.deeco.invokable.TypeDescription,
	 * cz.cuni.mff.d3s.deeco.knowledge.ISession)
	 */
	@Override
	public Object getKnowledge(String knowledgePath,
			TypeDescription expectedType, ISession session) throws KMException {
		Object value = getKnowledge(knowledgePath, session);
		return getInstance(expectedType, value);
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
		return kr.registerListener(listener);
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
		return kr.unregisterListener(listener);
	}

	@Override
	public void setListenersActive(boolean on) {
		kr.setListenersActive(on);
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
	
	@Override
	public Object[] takeAllKnowledge(String knowledgeId, ISession session)
			throws KMException {
		if (kr == null)
			throw new KMAccessException("Knowledge repository unavailable");

		Object[] result = null;
		ISession locSession = null;
		if (session == null) {
			locSession = createSession();
			locSession.begin();
		}

		final ISession localSession = (session == null ? locSession : session);

		try {
			while (localSession.repeat()) {
				// get any entry starting by knowledgeId
				result = kr.takeAll(knowledgeId, localSession);
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
			Log.e("", e);
			localSession.cancel();
			return null;
		}
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
	
	@Override
	public boolean containsKnowledge(String knowledgePath, ISession session) throws KMException {
		if (kr == null)
			throw new KMAccessException("Knowledge repository unavailable");
		
		boolean result = false;
		ISession locSession = null;
		if (session == null) {
			locSession = createSession();
			locSession.begin();
		}
		final ISession localSession = (session == null ? locSession : session);
		try {
			while (localSession.repeat()) {
				// we try the default path with no additional keys
				result = kr.contains(knowledgePath, localSession);
				// if the knowledge is not found in the knowledge repository,
				// we try to append the structure key to the knowledge and try to check again with this
				if (!result){
					String structuredKnowledgePath = KnowledgePathHelper.appendToRoot(knowledgePath, ConstantKeys.STRUCTURE_ID); 
					result = kr.contains(structuredKnowledgePath, localSession);
				}
				if (session == null)
					localSession.end();
				else
					break;
			}
			return result;
		} catch (KRExceptionAccessError kre) {
			if (session == null)
				localSession.cancel();
			throw new KMAccessException(kre.getMessage());
		} catch (Exception e) {
			Log.e("", e);
			localSession.cancel();
			return false;
		}
	}

	private void storeKnowledge(String knowledgePath, Object value,
			ISession session, boolean modify) throws KMAccessException {
		if (kr == null)
			throw new KMAccessException("Knowledge repository unavailable");

		ISession localSession;
		if (session == null) {
			localSession = createSession();
			localSession.begin();
		} else
			localSession = session;
		String tPath, tString;
		IObjectAccessor accessor;
		Object[] structure;
		try {
			while (localSession.repeat()) {
				structure = putStructure(knowledgePath, value, localSession,
						modify);
				if (structure == null)
					putFlat(knowledgePath, value, localSession, modify);
				else if (structure.length > 0) {
					accessor = getObjectAccessor(value);
					for (Object s : structure) {
						tString = (String) s;
						tPath = KnowledgePathHelper.appendToRoot(knowledgePath,
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
			Log.e("", e);
		}
	}

	private IObjectAccessor getObjectAccessor(Object value) {
		if (value == null)
			return null;
		Class<?> structure = value.getClass();
		if (TypeUtils.isList(structure))
			return new ListAccessor((List<?>) value);
		else if (TypeUtils.isMap(structure))
			return new MapAccessor((Map<String, ?>) value);
		else if (TypeUtils.isKnowledge(structure))
			return new KnowledgeAccessor((Knowledge) value);
		return null;
	}

	private Object getKnowledge(boolean withdrawal, String knowledgePath,
			ISession session) throws KMException {
		if (kr == null)
			throw new KMAccessException("Knowledge repository unavailable");

		Object result = null;
		ISession locSession = null;
		if (session == null) {
			locSession = createSession();
			locSession.begin();
		}

		final ISession localSession = (session == null ? locSession : session);

		try {
			while (localSession.repeat()) {
				Object[] structure = getStructure(withdrawal, knowledgePath,
						localSession);
				if (structure == null) // flat
					result = getFlat(withdrawal, knowledgePath, localSession);
				else {
					String tPath, tString;
					HashMap<String, Object> map = new HashMap<String, Object>();
					for (Object s : structure) {
						tString = (String) s;
						tPath = KnowledgePathHelper.appendToRoot(knowledgePath,
								tString);
						map.put(tString,
								getKnowledge(withdrawal, tPath, localSession));
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
			Log.e("", e);
			localSession.cancel();
			return null;
		}
	}

	private void putFlat(String knowledgePath, Object newValue,
			ISession session, boolean modify) throws KRExceptionAccessError {
		Object currentValue = null;
		putStructure(knowledgePath, null, session, modify);
		if (modify) {
			try {
				currentValue = kr.get(knowledgePath, session);
				if ((newValue != null || currentValue != null)
						&& !Arrays.deepEquals((Object[]) currentValue,
								new Object[] { newValue })) {
					kr.take(knowledgePath, session);
					kr.put(knowledgePath, newValue, session);
				}
			} catch (KRExceptionUnavailableEntry uee) {
				kr.put(knowledgePath, newValue, session);
			}
		} else {
			kr.put(knowledgePath, newValue, session);
		}
	}

	private Object getFlat(boolean withdrawal, String knowledgePath,
			ISession session) throws KRExceptionUnavailableEntry,
			KRExceptionAccessError {
		return (withdrawal) ? kr.take(knowledgePath, session) : kr.get(
				knowledgePath, session);
	}

	private Object[] getStructure(boolean withdrawal, String knowledgePath,
			ISession session) throws KRExceptionAccessError {
		String tempPath = KnowledgePathHelper.appendToRoot(knowledgePath,
				ConstantKeys.STRUCTURE_ID);
		try {
			Object[] result = (Object[]) getFlat(withdrawal, tempPath, session);
			if (result.length == 1)
				return (Object[]) result[0];
			else
				return null;
		} catch (KRExceptionUnavailableEntry uee) {
			return null;
		}
	}

	private Object[] putStructure(String knowledgePath, Object value,
			ISession session, boolean modify) throws KRExceptionAccessError {
		Object[] oldStructure, newStructure = StructureHelper
				.getStructureFromObject(value);
		boolean store = value != null && newStructure != null;
		String structurePath = KnowledgePathHelper.appendToRoot(knowledgePath,
				ConstantKeys.STRUCTURE_ID);
		try {
			if (modify) {
				Object[] tObjects = (Object[]) kr.get(structurePath, session);
				oldStructure = (Object[]) tObjects[0];
				if ((newStructure != null || oldStructure != null)
						&& !Arrays.deepEquals(oldStructure, newStructure)) {
					kr.take(structurePath, session);
					String tempPath;
					List<?> nsList = Arrays.asList(newStructure);
					for (Object s : oldStructure) {
						if (nsList.contains(s))
							continue;
						tempPath = KnowledgePathHelper.appendToRoot(
								knowledgePath, (String) s);
						try {
							takeKnowledge(tempPath, session);
						} catch (KMNotExistentException kmnee) {
						} catch (KMException kmae) {
							throw new KRExceptionAccessError(
									"Knowledge repository error!");
						}
					}
					if (store)
						kr.put(structurePath, newStructure, session);
				}
			} else if (store)
				kr.put(structurePath, newStructure, session);
		} catch (KRExceptionUnavailableEntry uee) {
			if (store)
				kr.put(structurePath, newStructure, session);
		}
		return newStructure;
	}

	private Object getInstance(TypeDescription expectedType, Object value)
			throws KMCastException {
		try {
			if (value == null)
				throw new KMCastException(
						"Parameter Instantiation Exception: Incompatible types");
			else if (TypeUtils.isMap(value.getClass())) {
				if (expectedType.isKnowledge()) {
					return getKnowledgeInstance(expectedType, value);
				} else if (expectedType.isMap()) {
					return getMapInstance(expectedType, value);
				} else if (expectedType.isList()) {
					return getListInstance(expectedType, value);
				} else if (TypeUtils.isInstanceOf(expectedType.clazz, value))
					return value;
				else
					throw new KMCastException(
							"Parameter Instantiation Exception: Incompatible types");
			} else {
				if (expectedType.isOutWrapper()) {
					if (isMap(value)) // OutWrapper cannot take such structures
						throw new KMCastException(
								"Parameter Instantiation Exception: Wrong value for OutWrapper");
					OutWrapper ow = (OutWrapper) expectedType.newInstance();
					ow.value = extractValue((Object[]) value);
					return ow;
				} else {
					return extractValue((Object[]) value);
				}
			}
		} catch (Exception e) {
			throw new KMCastException("Parameter Instantiation Exception: "
					+ e.getMessage());
		}
	}

	private Object getKnowledgeInstance(TypeDescription expectedParamType,
			Object value) throws KMCastException, InstantiationException,
			IllegalAccessException, IllegalArgumentException {
		Map<String, ?> mValue;
		if (!isMap(value))
			throw new KMCastException(
					"Parameter Instantiation Exception: Wrong value for Knowledge");
		Field[] fields = TypeUtils.getClassFields(expectedParamType.clazz);
		if (fields == null)
			throw new KMCastException(
					"Parameter Instantiation Exception: Empty structure");
		Object result = expectedParamType.newInstance();
		mValue = (Map<String, ?>) value;
		String fName;
		for (Field f : fields) {
			fName = f.getName();
			if (mValue.containsKey(fName)) {
				f.set(result,
						getInstance(
								expectedParamType.getKnowledgeFieldType(fName),
								mValue.get(fName)));
			} else
				throw new KMCastException(
						"Parameter Instantiation Exception: Knowledge structure confilict");
		}
		return result;
	}

	private Object getMapInstance(TypeDescription expectedType, Object value)
			throws KMCastException, InstantiationException,
			IllegalAccessException {
		Map<String, ?> mValue;
		if (!isMap(value))
			throw new KMCastException(
					"Parameter Instantiation Exception: Wrong value for Map");
		mValue = (Map<String, ?>) value;
		Map<String, Object> mResult;
		if (expectedType.isInterface())
			mResult = new HashMap<String, Object>();
		else
			mResult = (Map<String, Object>) expectedType.newInstance();
		TypeDescription elementType = expectedType.getParametricTypeAt(1);
		Object element;
		for (String s : mValue.keySet()) {
			try {
				element = getInstance(elementType, mValue.get(s));
				mResult.put(s, element);
			} catch (KMCastException kmce) {
			}
		}
		return mResult;
	}

	private Object getListInstance(TypeDescription expectedType, Object value)
			throws KMCastException, InstantiationException,
			IllegalAccessException {
		Map<String, ?> mValue;
		if (!isMap(value))
			throw new KMCastException(
					"Parameter Instantiation Exception: Wrong value for List");
		mValue = (Map<String, ?>) value;
		List<Object> lResult;
		if (expectedType.isInterface())
			lResult = new ArrayList<Object>();
		else
			lResult = (List<Object>) expectedType.newInstance();
		TypeDescription elementType = expectedType.getParametricTypeAt(0);
		Object element;
		String sIndex;
		for (int i = 0; i < mValue.size(); i++) {
			sIndex = Integer.toString(i);
			if (!mValue.containsKey(sIndex))
				throw new KMCastException(
						"Parameter Instantiation Exception: Error when parsing list");
			element = getInstance(elementType, mValue.get(sIndex));
			lResult.add(element);
		}
		return lResult;
	}

	private Object extractValue(Object[] value) throws KMCastException {
		if (value == null || value.length != 1)
			throw new KMCastException(
					"Parameter Instantiation Exception: Error when extracting value");
		return value[0];

	}

	private boolean isMap(Object value) {
		return TypeUtils.isMap(value.getClass());
	}
}
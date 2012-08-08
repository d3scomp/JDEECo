package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class InputParametersHelper implements Serializable {

	public Object getParameterInstance(ParameterType expectedParamType,
			Object value) throws KMCastException {
		try {
			if (value == null)
				throw new KMCastException(
						"Parameter Instantiation Exception: Incompatible types");
			else if (TypeUtils.isMap(value.getClass())) {
				if (expectedParamType.isKnowledge()) {
					return getKnowledgeInstance(expectedParamType, value);
				} else if (expectedParamType.isMap()) {
					return getMapInstance(expectedParamType, value);
				} else if (expectedParamType.isList()) {
					return getListInstance(expectedParamType, value);
				} else if (TypeUtils.isInstanceOf(expectedParamType.clazz,
						value))
					return value;
				else
					throw new KMCastException(
							"Parameter Instantiation Exception: Incompatible types");
			} else {
				if (expectedParamType.isOutWrapper()) {
					if (isMap(value)) // OutWrapper cannot take such structures
						throw new KMCastException(
								"Parameter Instantiation Exception: Wrong value for OutWrapper");
					OutWrapper ow = (OutWrapper) expectedParamType
							.newInstance();
					ow.item = extractValue((Object[]) value);
					return ow;
				} else {
					return extractValue((Object[]) value);
				}
			}
		} catch (KMCastException kmce) {
			throw kmce;
		} catch (Exception e) {
			throw new KMCastException("Parameter Instantiation Exception: "
					+ e.getMessage());
		}
	}

	/**
	 * @param expectedParamType
	 * @param value
	 * @return
	 * @throws KMCastException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private Object getKnowledgeInstance(ParameterType expectedParamType,
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
						getParameterInstance(
								expectedParamType.getKnowledgeFieldType(fName),
								mValue.get(fName)));
			} else
				throw new KMCastException(
						"Parameter Instantiation Exception: Knowledge structure confilict");
		}
		return result;
	}

	/**
	 * @param expectedParamType
	 * @param value
	 * @return
	 * @throws KMCastException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getMapInstance(ParameterType expectedParamType, Object value)
			throws KMCastException, InstantiationException,
			IllegalAccessException {
		Map<String, ?> mValue;
		if (!isMap(value))
			throw new KMCastException(
					"Parameter Instantiation Exception: Wrong value for Map");
		mValue = (Map<String, ?>) value;
		Map<String, Object> mResult;
		if (expectedParamType.isInterface())
			mResult = new HashMap<String, Object>();
		else
			mResult = (Map<String, Object>) expectedParamType.newInstance();
		ParameterType elementType = expectedParamType.getParametricTypeAt(1);
		Object element;
		for (String s : mValue.keySet()) {
			try {
				element = getParameterInstance(elementType, mValue.get(s));
				mResult.put(s, element);
			} catch (KMCastException kmce) {
			}
		}
		return mResult;
	}

	/**
	 * @param expectedParamType
	 * @param value
	 * @return
	 * @throws KMCastException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getListInstance(ParameterType expectedParamType, Object value)
			throws KMCastException, InstantiationException,
			IllegalAccessException {
		Map<String, ?> mValue;
		if (!isMap(value))
			throw new KMCastException(
					"Parameter Instantiation Exception: Wrong value for List");
		mValue = (Map<String, ?>) value;
		List<Object> lResult;
		if (expectedParamType.isInterface())
			lResult = new ArrayList<Object>();
		else
			lResult = (List<Object>) expectedParamType.newInstance();
		ParameterType elementType = expectedParamType.getParametricTypeAt(0);
		Object element;
		String sIndex;
		for (int i = 0; i < mValue.size(); i++) {
			sIndex = Integer.toString(i);
			if (!mValue.containsKey(sIndex))
				throw new KMCastException(
						"Parameter Instantiation Exception: Error when parsing list");
			element = getParameterInstance(elementType, mValue.get(sIndex));
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

package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.exceptions.KMCastException;
import cz.cuni.mff.d3s.deeco.exceptions.KMException;
import cz.cuni.mff.d3s.deeco.knowledge.ISession;
import cz.cuni.mff.d3s.deeco.knowledge.KPBuilder;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.OutWrapper;
import cz.cuni.mff.d3s.deeco.knowledge.StructureHelper;
import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class OutputParametersHelper implements Serializable {
	
	public Object getParameterInstance(ParameterType expectedParamType)
			throws KMCastException {
		try {
			if (expectedParamType.isMap()) {
				if (expectedParamType.isInterface())
					return new HashMap<String, Object>();
				else
					return expectedParamType.newInstance();
			} else if (expectedParamType.isList()) {
				if (expectedParamType.isInterface())
					return new ArrayList<Object>();
				else
					return expectedParamType.newInstance();
			} else
				return expectedParamType.newInstance();
		} catch (Exception e) {
			throw new KMCastException("Out parameter instantiation exception");
		}
	}

	public void storeOutValue(String knowledgePath, Object orgValue,
			Object newValue, KnowledgeManager km, ISession session)
			throws KMException {
		if (newValue == null) {
			if (orgValue != null)
				km.alterKnowledge(knowledgePath, newValue, session);
		} else if (orgValue == null) {
			km.alterKnowledge(knowledgePath, newValue, session);
		} else {
			Class<?> nvClass = newValue.getClass(), ovClass = orgValue
					.getClass();
			if (TypeUtils.isOutWrapper(nvClass)) {
				if (TypeUtils.isOutWrapper(ovClass))
					storeOutValue(knowledgePath, ((OutWrapper<?>) orgValue).item,
							((OutWrapper<?>) newValue).item, km, session);
				else
					System.out.println("Something is wrong!");
			} else if (!(TypeUtils.isList(nvClass) || TypeUtils.isList(ovClass))) {
				Object [] nvStructure = StructureHelper
						.getStructureFromObject(newValue), ovStructure = StructureHelper
						.getStructureFromObject(orgValue);
				if (ovStructure == null || nvStructure == null
						|| !Arrays.deepEquals(nvStructure, ovStructure))
					km.alterKnowledge(knowledgePath, newValue, session);
				else {
					String property;
					for (Object o : nvStructure) {
						property = (String) o;
						storeOutValue(
								KPBuilder.appendToRoot(knowledgePath, property),
								getValue(ovClass, orgValue, property),
								getValue(nvClass, newValue, property), km,
								session);
					}
				}
			} else
				km.alterKnowledge(knowledgePath, newValue, session);
		}
	}

	private Object getValue(Class<?> tClass, Object target, String propertyName)
			throws KMCastException {
		if (TypeUtils.isMap(tClass)) {
			Map<String, ?> mTarget = (Map<String, ?>) target;
			if (mTarget.containsKey(propertyName))
				return mTarget.get(propertyName);
			else
				throw new KMCastException("Store exception: No such property!");
		} else if (TypeUtils.isKnowledge(tClass)) {
			try {
				Field field = tClass.getField(propertyName);
				return field.get(target);
			} catch (Exception e) {
				throw new KMCastException("Store exception: No such property!");
			}

		} else
			throw new KMCastException("Store exception: Unexpected type!");

	}
}

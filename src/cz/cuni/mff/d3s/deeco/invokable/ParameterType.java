package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class ParameterType implements Serializable {
	public Class<?> clazz = null;
	public ParameterType[] parametricTypes = null;
	public Map<String, ParameterType> knowledgeStructure = null;

	public Object newInstance() throws InstantiationException,
			IllegalAccessException {
		return clazz.newInstance();
	}

	public boolean isInterface() {
		return clazz.isInterface();
	}

	public boolean isKnowledge() {
		return TypeUtils.isKnowledge(clazz);
	}

	public boolean isList() {
		return TypeUtils.isList(clazz);
	}

	public boolean isMap() {
		return TypeUtils.isMap(clazz);
	}

	public boolean isOutWrapper() {
		return TypeUtils.isOutWrapper(clazz);
	}

	public ParameterType getKnowledgeFieldType(String fieldName) {
		if (knowledgeStructure == null
				|| !knowledgeStructure.containsKey(fieldName))
			return null;
		return knowledgeStructure.get(fieldName);
	}
	
	public ParameterType getParametricTypeAt(int index) {
		if (parametricTypes == null || index < 0 || parametricTypes.length <= index)
			return null;
		return parametricTypes[index];
	}
	
	public Class<?> getParametricClassAt(int index) {
		ParameterType pt = getParametricTypeAt(index);
		if (pt == null)
			return null;
		return pt.clazz;
	}

}

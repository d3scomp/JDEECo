package cz.cuni.mff.d3s.deeco.invokable;

import java.io.Serializable;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.knowledge.TypeUtils;

public class TypeDescription implements Serializable {

	private static final long serialVersionUID = 1L;

	public final Class<?> clazz;
	public final TypeDescription[] parametricTypes;
	public final Map<String, TypeDescription> knowledgeStructure;

	
	
	public TypeDescription(Class<?> clazz, TypeDescription[] parametricTypes, Map<String, TypeDescription> knowledgeStructure) {
		this.clazz = clazz;
		this.parametricTypes = parametricTypes;
		this.knowledgeStructure = knowledgeStructure;
	}

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

	public TypeDescription getKnowledgeFieldType(String fieldName) {
		if (knowledgeStructure == null
				|| !knowledgeStructure.containsKey(fieldName))
			return null;
		return knowledgeStructure.get(fieldName);
	}
	
	public TypeDescription getParametricTypeAt(int index) {
		if (parametricTypes == null || index < 0 || parametricTypes.length <= index)
			return null;
		return parametricTypes[index];
	}
	
	public Class<?> getParametricClassAt(int index) {
		TypeDescription pt = getParametricTypeAt(index);
		if (pt == null)
			return null;
		return pt.clazz;
	}
	
	public String toString() {
		return clazz.toString();
	}

}

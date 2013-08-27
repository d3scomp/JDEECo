package cz.cuni.mff.d3s.deeco.runtime.model;

import java.util.LinkedList;
import java.util.List;

public class StructuredKnowledgeValueType extends KnowledgeType {
	private List<NestedKnowledgeDefinition> children;
	
	public StructuredKnowledgeValueType(Class<?> clazz, List<NestedKnowledgeDefinition> children) {
		super(clazz);
		this.children = children;
	}
	
	public StructuredKnowledgeValueType(Class<?> clazz) {
		this(clazz, new LinkedList<NestedKnowledgeDefinition>());
	}

	public List<NestedKnowledgeDefinition> getChildren() {
		return children;
	}
	
	public NestedKnowledgeDefinition getChild(String childName) {
		for (NestedKnowledgeDefinition nkd : children)
			if (nkd.getName().equals(childName))
				return nkd;
		return null;
	}

}

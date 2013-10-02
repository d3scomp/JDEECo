package cz.cuni.mff.d3s.deeco.model;

public class ListValueType extends KnowledgeType {
	private KnowledgeType elementType;

	public ListValueType(KnowledgeType elementType, Class<?> clazz) {
		super(clazz);
		this.elementType = elementType;
	}

	public KnowledgeType getElementType() {
		return elementType;
	}

}

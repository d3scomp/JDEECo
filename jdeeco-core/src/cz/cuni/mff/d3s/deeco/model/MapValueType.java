package cz.cuni.mff.d3s.deeco.model;

public class MapValueType extends KnowledgeType {
	private KnowledgeType valueType;

	public MapValueType(KnowledgeType valueType, Class<?> clazz) {
		super(clazz);
		this.valueType = valueType;
	}

	public KnowledgeType getValueType() {
		return valueType;
	}

}

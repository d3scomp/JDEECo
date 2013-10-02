package cz.cuni.mff.d3s.deeco.model;

public class OutWrapperValueType extends KnowledgeType {
	private KnowledgeType wrappedType;
	
	public OutWrapperValueType(KnowledgeType wrappedType, Class<?> clazz) {
		super(clazz);
		this.wrappedType = wrappedType;
	}

	public KnowledgeType getWrappedType() {
		return wrappedType;
	}
}

package cz.cuni.mff.d3s.deeco.model;

public class NestedKnowledgeDefinition {
	private String name;
	private boolean isLocal;
	private KnowledgeType type;
	
	public NestedKnowledgeDefinition(String name, boolean isLocal,
			KnowledgeType type) {
		super();
		this.name = name;
		this.isLocal = isLocal;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public KnowledgeType getType() {
		return type;
	}
	
	
}

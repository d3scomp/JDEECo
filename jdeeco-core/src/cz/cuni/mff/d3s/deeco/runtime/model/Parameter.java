package cz.cuni.mff.d3s.deeco.runtime.model;

public class Parameter {
	private ParameterDirection direction;
	private KnowledgePath knowledgePath;
	private KnowledgeType type;
	
	public Parameter(ParameterDirection direction, KnowledgePath knowledgePath,
			KnowledgeType type) {
		super();
		this.direction = direction;
		this.knowledgePath = knowledgePath;
		this.type = type;
	}

	public ParameterDirection getDirection() {
		return direction;
	}

	public KnowledgePath getKnowledgePath() {
		return knowledgePath;
	}

	public KnowledgeType getType() {
		return type;
	}

}

package cz.cuni.mff.d3s.deeco.runtime.model;

public class KnowledgeChangeTrigger extends Trigger {
	private KnowledgePath knowledgePath;

	public KnowledgeChangeTrigger(KnowledgePath knowledgePath) {
		super();
		this.knowledgePath = knowledgePath;
	}

	public KnowledgePath getKnowledgePath() {
		return knowledgePath;
	}
}

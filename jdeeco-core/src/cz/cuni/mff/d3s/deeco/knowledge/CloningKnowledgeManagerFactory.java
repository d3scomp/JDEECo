package cz.cuni.mff.d3s.deeco.knowledge;

public class CloningKnowledgeManagerFactory implements KnowledgeManagerFactory {

	@Override
	public KnowledgeManager create(String id) {
		return new CloningKnowledgeManager(id);
	}

}

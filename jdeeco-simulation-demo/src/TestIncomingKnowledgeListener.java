import cz.cuni.mff.d3s.deeco.publisher.IncomingKnowledgeListener;
import cz.cuni.mff.d3s.deeco.publisher.KnowledgeData;


public class TestIncomingKnowledgeListener implements IncomingKnowledgeListener {	
	@Override
	public void knowledgeReceived(KnowledgeData knowledge) {
		System.out.println("Received knowledge from " + knowledge.getOwnerId());
	}

}

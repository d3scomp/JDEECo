package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

import com.rits.cloning.Cloner;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeNotExistentException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;

/**
 * 
 * A KnowledgeManager version that introduces cloning functionality for stored
 * data immutability.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class CloningKnowledgeManager extends BaseKnowledgeManager {

	private final Cloner cloner;

	public CloningKnowledgeManager(Object knowledge) {
		this.cloner = new Cloner();
	}
	
	@Override
	protected Object getKnowledge(List<PathNode> knowledgePath)
			throws KnowledgeNotExistentException {
		return cloner.deepClone(super.getKnowledge(knowledgePath));
	}

	@Override
	protected void updateKnowledge(KnowledgePath knowledgePath, Object value) {
		super.updateKnowledge(cloner.deepClone(knowledgePath), cloner.deepClone(value));
	}

}

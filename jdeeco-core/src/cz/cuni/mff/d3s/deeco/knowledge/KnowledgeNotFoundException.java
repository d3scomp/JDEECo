package cz.cuni.mff.d3s.deeco.knowledge;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeNotFoundException extends Exception {
	private static final long serialVersionUID = -999702030030889941L;
	
	KnowledgePath notFoundPath;
	
	KnowledgeNotFoundException() {}
	
	public KnowledgeNotFoundException(KnowledgePath notFoundPath) {
		this.notFoundPath = notFoundPath;
	}

	public KnowledgePath getNotFoundPath() {
		return notFoundPath;
	}
	
	
	
}

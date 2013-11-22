package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * Thrown by the {@link KnowledgeManager} when the knowledge update fails.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class KnowledgeUpdateException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public KnowledgeUpdateException(String message) {
		super(message);
	}

}

package cz.cuni.mff.d3s.deeco.knowledge.container;

/**
 * Thrown when a knowledge container is unable to read some knowledge.
 * 
 * @author Zbyněk Jiráček
 *
 * @see ReadOnlyKnowledgeWrapper#getUntrackedRoleKnowledge(Class)
 */
public class KnowledgeAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1874118337683034543L;

	public KnowledgeAccessException() {
	}

	public KnowledgeAccessException(String message) {
		super(message);
	}

	public KnowledgeAccessException(Throwable cause) {
		super(cause);
	}

	public KnowledgeAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public KnowledgeAccessException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

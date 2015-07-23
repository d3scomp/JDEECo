package cz.cuni.mff.d3s.deeco.knowledge.container;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

/**
 * General exception thrown by the {@link TrackingKnowledgeContainer} class.
 * The exception message and the inner exception contain the details.
 * 
 * Possible causes are: {@link KnowledgeAccessException}, {@link KnowledgeCommitException},
 * {@link RoleClassException}
 * 
 * @author Zbyněk Jiráček
 *
 * @see TrackingKnowledgeContainer
 */
public class KnowledgeContainerException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9219992641061413076L;
	
	public KnowledgeContainerException(String message) {
		super(message);
	}

	public KnowledgeContainerException(Throwable cause) {
		super(cause);
	}

	public KnowledgeContainerException(String message, Throwable cause) {
		super(message, cause);
	}	
}

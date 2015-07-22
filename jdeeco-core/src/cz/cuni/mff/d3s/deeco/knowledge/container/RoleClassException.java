package cz.cuni.mff.d3s.deeco.knowledge.container;

/**
 * Thrown when a role is not implemented by the component knowledge, or when a role class
 * can't be instantiated or accessed via introspection. 
 * 
 * @author Zbyněk Jiráček
 *
 * @see ReadOnlyKnowledgeWrapper#getUntrackedRoleKnowledge(Class)
 * @see TrackingKnowledgeWrapper#commitChanges()
 */
public class RoleClassException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7039246181045851157L;

	public RoleClassException() {
	}

	public RoleClassException(String message) {
		super(message);
	}

	public RoleClassException(Throwable cause) {
		super(cause);
	}

	public RoleClassException(String message, Throwable cause) {
		super(message, cause);
	}

	public RoleClassException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

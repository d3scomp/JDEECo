package cz.cuni.mff.d3s.deeco.knowledge.container;

public class KnowledgeCommitException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3266548012554156432L;

	public KnowledgeCommitException() {
	}

	public KnowledgeCommitException(String arg0) {
		super(arg0);
	}

	public KnowledgeCommitException(Throwable cause) {
		super(cause);
	}

	public KnowledgeCommitException(String message, Throwable cause) {
		super(message, cause);
	}

	public KnowledgeCommitException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

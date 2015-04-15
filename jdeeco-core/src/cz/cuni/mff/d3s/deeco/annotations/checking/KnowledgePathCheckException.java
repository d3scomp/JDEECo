package cz.cuni.mff.d3s.deeco.annotations.checking;

/**
 * An exception that is used by the {@link KnowledgePathChecker} class when a given pair (type, field sequence)
 * is invalid or does not exist in the given class.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class KnowledgePathCheckException extends Exception {
	
	private static final long serialVersionUID = -8720858480071032369L;

	public KnowledgePathCheckException() {
	}

	public KnowledgePathCheckException(String message) {
		super(message);
	}

	public KnowledgePathCheckException(Throwable cause) {
		super(cause);
	}

	public KnowledgePathCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public KnowledgePathCheckException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

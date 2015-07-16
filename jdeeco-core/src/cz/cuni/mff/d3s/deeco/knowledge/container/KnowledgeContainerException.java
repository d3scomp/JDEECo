/**
 * 
 */
package cz.cuni.mff.d3s.deeco.knowledge.container;

/**
 * @author Zbyněk Jiráček
 *
 */
public class KnowledgeContainerException extends Exception {

	public KnowledgeContainerException() {
	}

	public KnowledgeContainerException(String message) {
		super(message);
	}

	public KnowledgeContainerException(Throwable cause) {
		super(cause);
	}

	public KnowledgeContainerException(String message, Throwable cause) {
		super(message, cause);
	}

	public KnowledgeContainerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

package cz.cuni.mff.d3s.deeco.annotations.checking;

/**
 * Thrown when validating a component/ensemble definition. Used by classes that implement {@link AnnotationChecker}.
 * 
 * @author Zbyněk Jiráček
 *
 * @see AnnotationChecker
 */
public class AnnotationCheckerException extends Exception {

	private static final long serialVersionUID = 1L;

	public AnnotationCheckerException() {
	}

	public AnnotationCheckerException(String message) {
		super(message);
	}

	public AnnotationCheckerException(Throwable cause) {
		super(cause);
	}

	public AnnotationCheckerException(String message, Throwable cause) {
		super(message, cause);
	}

	public AnnotationCheckerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}

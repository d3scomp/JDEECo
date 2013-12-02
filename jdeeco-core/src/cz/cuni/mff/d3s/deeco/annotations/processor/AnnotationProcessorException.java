package cz.cuni.mff.d3s.deeco.annotations.processor;

/**
 * Thrown when processing of DEECo-annotated Java object(s)/class(es) fails.
 * <p>
 * The message should encode all relevant information (class name, method name, parameter index, ...) to pinpoint the problem. 
 * </p>
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class AnnotationProcessorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnnotationProcessorException(final String msg) {
		super(msg);
	}
	
	public AnnotationProcessorException(final String msg, final Throwable t) {
		super(msg, t);
	}
	
}

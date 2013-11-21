package cz.cuni.mff.d3s.deeco.annotations.processor;

public class AnnotationProcessorException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnnotationProcessorException(final String msg) {
		super(msg);
	}
	
	public AnnotationProcessorException(final String msg, final Throwable t) {
		super(msg, t);
	}
	
}

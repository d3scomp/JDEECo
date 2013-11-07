package cz.cuni.mff.d3s.deeco.annotations.processor;

public class AnnotationParsingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AnnotationParsingException(String msg) {
		super(msg);
	}
	
	public AnnotationParsingException(String msg, Throwable t) {
		super(msg, t);
	}
	
}

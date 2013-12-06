package cz.cuni.mff.d3s.deeco.annotations.processor;

/**
 * 	
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ModelValidationError {

	public enum Severity {ERROR, WARNING}
	
	private String msg;
	private Severity severity;
	
	public ModelValidationError(Severity severity, String msg) {
		this.msg = msg;
		this.severity = severity;
	}
	
	public String getMsg() {
		return msg;
	}

	public Severity getSeverity() {
		return severity;
	}
	
	
}

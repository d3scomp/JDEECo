package cz.cuni.mff.d3s.deeco.annotations.processor;

import org.eclipse.emf.ecore.EObject;

/**
 * 	
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ModelValidationError {

	public enum Severity {ERROR, WARNING}
	
	private String msg;
	private Severity severity;
	private EObject where;
	
	public ModelValidationError(EObject where, Severity severity, String msg) {
		this.where = where;
		this.msg = msg;
		this.severity = severity;
	}
	
	public String getMsg() {
		return msg;
	}

	public Severity getSeverity() {
		return severity;
	}

	public EObject getWhere() {
		return where;
	}
	
}

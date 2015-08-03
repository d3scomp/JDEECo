package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

/**
 * Used by {@link ScriptOutputVariableRegistry} when a variable has a different type than expected.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class OutputVariableParseException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3372224239114525697L;
	
	private String variableName;
	private String desiredType;
	
	public String getVariableName() {
		return variableName;
	}
	
	public String getDesiredType() {
		return desiredType;
	}
	
	private static String defaultMessage(String variableName, String desiredType) {
		return String.format("Error parsing variable %s. The given value cannot be parsed as %s. ", variableName, desiredType);
	}
	
	public OutputVariableParseException(String variableName, String desiredType) {
		super(defaultMessage(variableName, desiredType));
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, String desiredType, String additionalMessage) {
		super(defaultMessage(variableName, desiredType) + additionalMessage);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, String desiredType, Throwable cause) {
		super(cause);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, String desiredType, String additionalMessage, Throwable cause) {
		super(defaultMessage(variableName, desiredType) + additionalMessage, cause);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

}

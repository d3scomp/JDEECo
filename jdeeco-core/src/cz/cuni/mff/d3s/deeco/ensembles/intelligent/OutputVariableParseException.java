package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

public class OutputVariableParseException extends Exception {

	private String variableName;
	private Class<?> desiredType;
	
	public String getVariableName() {
		return variableName;
	}
	
	public Class<?> getDesiredType() {
		return desiredType;
	}
	
	private static String defaultMessage(String variableName, Class<?> desiredType) {
		return String.format("Error parsing variable %s of a desired type %s. ", variableName, desiredType.getName());
	}
	
	public OutputVariableParseException(String variableName, Class<?> desiredType) {
		super(defaultMessage(variableName, desiredType));
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, Class<?> desiredType, String additionalMessage) {
		super(defaultMessage(variableName, desiredType) + additionalMessage);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, Class<?> desiredType, Throwable cause) {
		super(cause);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, Class<?> desiredType, String additionalMessage, Throwable cause) {
		super(defaultMessage(variableName, desiredType) + additionalMessage, cause);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

	public OutputVariableParseException(String variableName, Class<?> desiredType, String additionalMessage, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(defaultMessage(variableName, desiredType) + additionalMessage, cause, enableSuppression, writableStackTrace);
		this.variableName = variableName;
		this.desiredType = desiredType;
	}

}

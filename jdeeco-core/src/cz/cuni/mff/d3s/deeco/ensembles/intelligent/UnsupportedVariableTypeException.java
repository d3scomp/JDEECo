package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class UnsupportedVariableTypeException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2151595521034738200L;
	
	private Class<?> usedType;
	
	public Class<?> getUsedType() {
		return usedType;
	}
	
	private static String defaultMessage(Class<?> usedType) {
		return String.format("The type %s is not supported. See documentation of the %s class for the current list of supported types. ",
				usedType, ScriptInputVariableRegistry.class.getName());
	}
	
	public UnsupportedVariableTypeException(Class<?> usedType) {
		super(defaultMessage(usedType));
		this.usedType = usedType;
	}

	public UnsupportedVariableTypeException(Class<?> usedType, String descriptionMessage) {
		super(descriptionMessage + " " + defaultMessage(usedType));
		this.usedType = usedType;
	}

	public UnsupportedVariableTypeException(Class<?> usedType, Throwable cause) {
		super(defaultMessage(usedType), cause);
		this.usedType = usedType;
	}

	public UnsupportedVariableTypeException(Class<?> usedType, String descriptionMessage, Throwable cause) {
		super(descriptionMessage + " " + defaultMessage(usedType), cause);
		this.usedType = usedType;
	}

}

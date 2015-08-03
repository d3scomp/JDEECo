package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import cz.cuni.mff.d3s.deeco.runtime.DEECoException;

public class ScriptExecutionException extends DEECoException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4153928800901402094L;
	
	private String scriptPath;
	//private String command;
	
	public String getScriptPath() {
		return scriptPath;
	}
	
	//public String getCommand() {
	//	 return command;
	//}
	
	public ScriptExecutionException(String message, String scriptPath/*, String command*/) {
		super(message);
		this.scriptPath = scriptPath;
		//this.command = command;
	}

	public ScriptExecutionException(Throwable cause, String scriptPath/*, String command*/) {
		super(cause);
		this.scriptPath = scriptPath;
		//this.command = command;
	}

	public ScriptExecutionException(String message, Throwable cause, String scriptPath/*, String command*/) {
		super(message, cause);
		this.scriptPath = scriptPath;
		//this.command = command;
	}

}

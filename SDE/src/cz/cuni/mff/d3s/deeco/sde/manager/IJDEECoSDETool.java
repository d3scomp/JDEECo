package cz.cuni.mff.d3s.deeco.sde.manager;

import java.io.File;
import java.util.List;

import eu.sensoria_ist.casetool.core.ext.ISensoriaTool;
import eu.sensoria_ist.casetool.core.ext.SensoriaTool;
import eu.sensoria_ist.casetool.core.ext.SensoriaToolFunction;
import eu.sensoria_ist.casetool.core.ext.SensoriaToolFunctionParameter;
import eu.sensoria_ist.casetool.core.ext.SensoriaToolFunctionReturns;

@SensoriaTool(name = "JDEECo runtime manager tool", description = "The tool controls the JDEECo runtime", categories = "JDEECo")
public interface IJDEECoSDETool extends ISensoriaTool {

	@SensoriaToolFunction(description = "Starts JDEECo runtime")
	@SensoriaToolFunctionReturns(description = "Returns confirmation message")
	public String start();

	@SensoriaToolFunction(description = "Stops JDEECo runtime")
	@SensoriaToolFunctionReturns(description = "Returns confirmation message")
	public String stop();

	@SensoriaToolFunction(description = "Lists all registered DEECo processes")
	@SensoriaToolFunctionReturns(description = "Returns list of all registered DEECo processes")
	public String listProcesses();

	@SensoriaToolFunction(description = "Lists all DEECo component knowledges currently available in the runtime")
	@SensoriaToolFunctionReturns(description = "Returns list of all registered DEECo component knowledges")
	public String listKnowledges();

	@SensoriaToolFunction(description = "Adds component and ensemble definitions")
	@SensoriaToolFunctionReturns(description = "Returns textual feedback on execution result")
	public String addDefinitions(
			@SensoriaToolFunctionParameter(description = "Path to a JAR file or root directory with properly packeged class files") String path);

//	@SensoriaToolFunction(description = "Packages input files into JDEECo and OSGi compliant bundles")
//	@SensoriaToolFunctionReturns(description = "Returns textual feedback on execution result")
//	public String packageToOSGiBundle(
//			@SensoriaToolFunctionParameter(description = "Resources that need to be packaged") List<File> input,
//			@SensoriaToolFunctionParameter(description = "Output localization") String target);
	
	@SensoriaToolFunction(description = "Creates JDEECo and OSGi compliant bundle with use of wizard")
	@SensoriaToolFunctionReturns(description = "Returns textual feedback on execution result")
	public String packageToOSGiBundle();

	@SensoriaToolFunction(description = "Prints information about JDEECo runtime environment")
	@SensoriaToolFunctionReturns(description = "Returns Runtime information")
	public String getRuntimeInfo();

}

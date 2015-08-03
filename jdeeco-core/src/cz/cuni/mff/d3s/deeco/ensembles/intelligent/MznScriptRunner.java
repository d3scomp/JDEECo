package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.cuni.mff.d3s.deeco.ensembles.intelligent.ScriptInputVariableRegistry.Entry;


/**
 * Executes MiniZinc script and returns the result.
 * 
 * A MiniZinc script is executed in two steps:
 * First, the mzn2fzn application is executed, that transforms the input MiniZinc file into FlatZinc format.
 * Then, the FlatZinc file is solved by the Gecode solver.
 * 
 * Internally, this class does not execute the applications; it uses {@link ExternalAppRunner} class to do so.
 * 
 * The location of the mzn2fzn application and Gecode solver is obtained from minizinc.properties file located
 * in the jdeeco core project root.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class MznScriptRunner {

	private String scriptPath;
	private ExternalAppRunner appRunner;
	private String tempFilePath;
	
	/**
	 * A key in minizinc.properties file containing path to the mzn2fzn program.
	 */
	public static String MZN2FZN_PATH_PROPERTY = "mzn2fznPath";
	
	/**
	 * A key in minizinc.properties file containing path to the Gecode solver (can be replaced by a different one).
	 */
	public static String FZN_SOLVER_PATH_PROPERTY = "fznSolverPath";
	
	/**
	 * Not used (TODO)
	 */
	public static String TEMP_FOLDER_PROPERTY = "tempPath";
	
	/**
	 * Creates runner instance.
	 * @param scriptPath The script file path (without any enclosing quotes!)
	 */
	public MznScriptRunner(String scriptPath) {
		this(scriptPath, new ExternalAppRunner(), generateTempFilePath());
	}
	
	/**
	 * Creates runner instance (used for tests only)
	 * @param scriptPath The script file path (without any enclosing quotes!)
	 * @param appRunner An instance of the {@link ExternalAppRunner} (can be replaced by a mock)
	 * @param tempFilePath
	 */
	public MznScriptRunner(String scriptPath, ExternalAppRunner appRunner, String tempFilePath) {
		this.scriptPath = scriptPath;
		this.appRunner = appRunner;
		this.tempFilePath = tempFilePath;
	}
	
	/**
	 * Gets the script file path.
	 * @return The script file path.
	 */
	public String getScriptPath() {
		return scriptPath;
	}
	
	/**
	 * Reads the necessary properties from the minizinc.properties file.
	 * @return The properties.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties getProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("minizinc.properties"));
		return properties;
	}
	
	/**
	 * Executes MiniZinc script and returns the output variables with their respective values.
	 * 
	 * @param inputParams Value registry that contains input variable values.
	 * @return A map containing all output variables (keys) and their respective values.
	 * @throws ScriptExecutionException
	 */
	public Map<String, String> runScript(ScriptInputVariableRegistry inputParams) throws ScriptExecutionException {
				
		Properties properties;
		try {
			properties = getProperties();
		} catch (IOException e) {
			throw new ScriptExecutionException("Error reading MiniZinc properties (minizinc.properties file)", e, scriptPath);
		}
		
		try {
			String mzn2fznResult = mzn2fzn(inputParams, properties);
			if (!mzn2fznResult.trim().equals("")) {
				throw new ScriptExecutionException("MZN2FZN returned a message (though the output should be empty): " + mzn2fznResult, scriptPath);
			}
		} catch (IOException e) {
			throw new ScriptExecutionException("Error running MZN2FZN.", e, scriptPath);
		}
	
		try {
			String[] result = fznSolve(properties);
			
			// parse the result into Map
			Pattern pattern = Pattern.compile("^([A-Za-z0-9_]+)=(.*)$");
			Map<String, String> resultMap = new HashMap<>();
			for (String line : result) {
				String trimmedLine = line.replace(" ", "");
				if (trimmedLine.equals("") || trimmedLine.startsWith("--") || trimmedLine.startsWith("==")) {
					continue;
				}
				
				Matcher matcher = pattern.matcher(trimmedLine);
				if (!matcher.matches()) {
					throw new ScriptExecutionException("FZN solver output line is not correctly formatted: " + line, scriptPath);
				}
				
				String identifierName = matcher.group(1);
				String identifierValue = matcher.group(2);
				resultMap.put(identifierName, identifierValue);
			}
			
			return resultMap;
			
		} catch (IOException e) {
			throw new ScriptExecutionException("Error running FZN solver.", e, scriptPath);
		}

	}
	
	private static String generateTempFilePath() {
		// TODO
		return "temp.fzn";
	}
	
	/**
	 * Runs the mzn2fzn application with following arguments
	 *  - the input MiniZinc file
	 *  - a temporary file for FlatZinc output
	 *  - all input variable values
	 *  
	 * @param inputParams The input variable values.
	 * @param properties Properties containing the mzn2fzn file path.
	 * @return Output of the mzn2fzn application (should be empty).
	 * @throws IOException
	 */
	private String mzn2fzn(ScriptInputVariableRegistry inputParams, Properties properties) throws IOException {
		List<Entry> inputVariables = inputParams.getInputVariables();
		String[] parameterList = new String[2 + inputVariables.size()];
		parameterList[0] = String.format("\"%s\"", scriptPath);
		parameterList[1] = String.format("-o %s", tempFilePath);
		for (int i = 0; i < inputVariables.size(); i++) {
			parameterList[2 + i] = String.format("-D %s", inputVariables.get(i).toString());
		}
		
		String appPath = properties.getProperty(MZN2FZN_PATH_PROPERTY);
		String[] result = appRunner.run(appPath, Arrays.asList(parameterList));
		return String.join("\n", result);
	}
	
	/**
	 * Runs the Gecode solver providing it the FlatZinc file created by mzn2fzn.
	 * @param properties Properties containing the solver file path.
	 * @return Solver output (split by lines).
	 * @throws IOException
	 */
	private String[] fznSolve(Properties properties) throws IOException {
		String[] parameterList = new String[1];
		parameterList[0] = tempFilePath;		
		String appPath = properties.getProperty(FZN_SOLVER_PATH_PROPERTY);
		
		return appRunner.run(appPath, Arrays.asList(parameterList));
	}

}

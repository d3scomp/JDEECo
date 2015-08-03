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


public class MznScriptRunner {

	private String scriptPath;
	private ExternalAppRunner appRunner;
	private String tempFilePath;
	
	public static String MZN2FZN_PATH_PROPERTY = "mzn2fznPath";
	public static String FZN_SOLVER_PATH_PROPERTY = "fznSolverPath";
	public static String TEMP_FOLDER_PROPERTY = "tempPath";
	
	// scriptPath without quotes!
	public MznScriptRunner(String scriptPath) {
		this(scriptPath, new ExternalAppRunner(), generateTempFilePath());
	}
	
	// scriptPath without quotes! tempPath with quotes if necessary!
	public MznScriptRunner(String scriptPath, ExternalAppRunner appRunner, String tempFilePath) {
		this.scriptPath = scriptPath;
		this.appRunner = appRunner;
		this.tempFilePath = tempFilePath;
	}
	
	public String getScriptPath() {
		return scriptPath;
	}
	
	// file paths are expected to contain quotes if necessary
	public static Properties getProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream("minizinc.properties"));
		return properties;
	}
	
	public Map<String, String> runScript(ScriptInputVariableRegistry inputParams) throws ScriptExecutionException {
		/*
		ExternalAppRunner ear = new ExternalAppRunner("\"c:\\Program Files (x86)\\MiniZinc IDE (bundled)\\mzn2fzn.exe "
				+ " -o temp.fzn " + scriptPath);
		*/
				
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
			Pattern pattern = Pattern.compile("^([A-Za-z0-9]+)=(.*)$");
			Map<String, String> resultMap = new HashMap<>();
			for (String line : result) {
				String trimmedLine = line.replace(" ", "");
				if (trimmedLine.equals("")) {
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
	
	private String[] fznSolve(Properties properties) throws IOException {
		String[] parameterList = new String[1];
		parameterList[0] = tempFilePath;		
		String appPath = properties.getProperty(FZN_SOLVER_PATH_PROPERTY);
		
		return appRunner.run(appPath, Arrays.asList(parameterList));
	}

}

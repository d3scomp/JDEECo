package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Runs native applications. Returns output.
 * 
 * @author Zbyněk Jiráček
 *
 */
public class ExternalAppRunner {
	
	/**
	 * Runs the given command.
	 * 
	 * @param appPath Command (application). If the command contains spaces it should be wrapped in quote marks.
	 * @param appArguments Arguments (will be joined and separated by spaces).
	 * @return Output of the application, separated by lines (includes also error stream output).
	 * @throws IOException
	 */
	public String[] run(String appPath, Collection<String> appArguments) throws IOException {
		String command = String.format("%s %s", appPath, String.join(" ", appArguments));
		
		Process process = Runtime.getRuntime().exec(command);
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		BufferedReader brError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		
		String line;
		List<String> output = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			output.add(line);
		}
		
		while ((line = brError.readLine()) != null) {
			output.add(line);
		}
		
		return Arrays.copyOf(output.toArray(), output.size(), String[].class);
	}

}

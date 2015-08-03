package cz.cuni.mff.d3s.deeco.ensembles.intelligent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExternalAppRunner {
	
	public String[] run(String appPath, Collection<String> appArguments) throws IOException {
		String command = String.format("\"%s\" %s", appPath, String.join(" ", appArguments));
		
		Process process = new ProcessBuilder(command).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		
		String line;
		List<String> output = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			output.add(line);
		}
		
		return (String[]) output.toArray();
	}

}

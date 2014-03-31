package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class ComponentConfigParser {
	BufferedReader in;
	ComponentConfigParser(String filename) throws FileNotFoundException {
		in = new BufferedReader(new FileReader(filename));
	}
	public PositionAwareComponent parseComponent() {
		if (in == null)
			return null;
		String line;
		try {
			line = in.readLine();
		} catch (IOException e) {				
			e.printStackTrace();
			return null;
		}
		if (line == null)
			return null;
		
		String[] parts = line.split(" ");
		if (parts.length < 5)
			return null;
		
		
		switch (parts[0]) {
		case "M":
			if (parts.length < 6)
				return null;
			return new Member(parts[1], parts[2], 
					new Position(Integer.parseInt(parts[3]), Integer.parseInt(parts[4])), Boolean.parseBoolean(parts[5]));
		case "L":
			if (parts.length < 6)
				return null;
			return new Leader(parts[1], parts[2], 
					new Position(Integer.parseInt(parts[3]), Integer.parseInt(parts[4])), Boolean.parseBoolean(parts[5]));
		case "O":				
			return new OtherComponent(parts[1], 
					new Position(Integer.parseInt(parts[2]), Integer.parseInt(parts[3])), Boolean.parseBoolean(parts[4]));
		default:
			return null;
		}			
	}
}
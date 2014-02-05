package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

class SiteConfigParser {
	BufferedReader in;
	SiteConfigParser(String filename) throws FileNotFoundException {
		in = new BufferedReader(new FileReader(filename));
	}
	public Area parseArea() {
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
		
		String[] teams = null;
		switch (parts[0]) {
		case "C":			
			if (parts.length > 5)
				teams = Arrays.copyOfRange(parts, 5, parts.length);
			return new CircularArea(
					parts[1], 
					Integer.parseInt(parts[2]), 
					Integer.parseInt(parts[3]), 
					Integer.parseInt(parts[4]),
					teams);
		case "R":
			if (parts.length < 6)
				return null;
			if (parts.length > 6)
				teams = Arrays.copyOfRange(parts, 6, parts.length);
			return new RectangularArea(
					parts[1], 
					Integer.parseInt(parts[2]), 
					Integer.parseInt(parts[3]), 
					Integer.parseInt(parts[4]),
					Integer.parseInt(parts[5]),
					teams);
		
		default:
			return null;
		}			
	}
}
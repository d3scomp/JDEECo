package cz.cuni.mff.d3s.deeco.runtime;

import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;
import cz.cuni.mff.d3s.deeco.processor.ParsedObjectWriter;
import cz.cuni.mff.d3s.deeco.provider.ClassDEECoObjectProvider;

public class PreLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println("Wrong parameterTypes!");
			return;
		}
		
		ClassFinder cf = new ClassFinder();
		cf.resolve(args);
		ClassDEECoObjectProvider cdop = ClassProcessor.processClasses(cf.getClasses(), cf.getDirURLs());
		ParsedObjectWriter pow = new ParsedObjectWriter();
		pow.write(cdop);
	}	
}

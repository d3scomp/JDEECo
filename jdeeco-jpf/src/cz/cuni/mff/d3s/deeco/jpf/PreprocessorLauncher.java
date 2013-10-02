package cz.cuni.mff.d3s.deeco.jpf;

import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.provider.RuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;

/**
 * Class responsible for parsing and serializing both component and ensemble
 * definitions into a file. It is used before proper runtime execution in case
 * of JPF verification.
 * 
 * @author Michal Kit
 * 
 */
public class PreprocessorLauncher {

	/**
	 * Parses and serializes components and ensembles into a file.
	 * 
	 * @param args
	 *            paths to jars or directories containing component and ensemble
	 *            .class files.
	 */
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			Log.w("Wrong parameter number");
			return;
		}

		ClassFinder cf = new ClassFinder();
		cf.resolve(args);
		RuntimeMetadataProvider provider = ClassProcessor.processClasses(
				cf.getClasses(), cf.getDirURLs());
		ParsedObjectWriter pow = new ParsedObjectWriter();
		pow.write(provider);
	}
}

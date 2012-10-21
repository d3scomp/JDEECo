package cz.cuni.mff.d3s.deeco.provider;

import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;

public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private final String fileName;
	private final ClassLoader parentClassLoader;

	public FileDEECoObjectProvider(String fileName) {
		this(fileName, null);
	}

	public FileDEECoObjectProvider(String fileName,
			ClassLoader parentClassLoader) {
		this.fileName = fileName;
		this.parentClassLoader = parentClassLoader;
	}

	@Override
	protected void processComponents() {
		processAll();
	}

	@Override
	protected void processEnsembles() {
		processAll();
	}

	private void processAll() {
		if (fileName == null || fileName.length() == 0) {
			System.out.println("Wrong path");
			return;
		}
		ClassFinder cf = new ClassFinder();
		cf.resolve(new String[] { fileName });
		AbstractDEECoObjectProvider dop = ClassProcessor.processClasses(
				cf.getClasses(), cf.getDirURLs(), parentClassLoader);
		components = dop.getComponents();
		ensembles = dop.getEnsembles();
	}

}

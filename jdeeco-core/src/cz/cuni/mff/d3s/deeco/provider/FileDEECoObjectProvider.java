package cz.cuni.mff.d3s.deeco.provider;

import cz.cuni.mff.d3s.deeco.logging.LoggerFactory;
import cz.cuni.mff.d3s.deeco.processor.ClassFinder;
import cz.cuni.mff.d3s.deeco.processor.ClassProcessor;

/**
 * Provider class used to retrieve components and ensemble definitions after
 * serialization.
 * 
 * @author Michal Kit
 * 
 */
public class FileDEECoObjectProvider extends AbstractDEECoObjectProvider {


	private static final long serialVersionUID = 6765016395278637455L;
	
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider#processComponents
	 * ()
	 */
	@Override
	protected void processComponents() {
		processAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider#processEnsembles
	 * ()
	 */
	@Override
	protected void processEnsembles() {
		processAll();
	}

	private void processAll() {
		if (fileName == null || fileName.length() == 0) {
			LoggerFactory.getLogger().info("Wrong path");
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

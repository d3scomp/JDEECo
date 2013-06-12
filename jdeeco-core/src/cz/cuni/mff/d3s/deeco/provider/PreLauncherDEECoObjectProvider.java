package cz.cuni.mff.d3s.deeco.provider;

import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;

/**
 * Provider class used to retrieve components and ensemble definitions after
 * serialization.
 * 
 * @author Michal Kit
 * 
 */
public class PreLauncherDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private static final long serialVersionUID = 548154110219388270L;
	
	private AbstractDEECoObjectProvider provider;

	public PreLauncherDEECoObjectProvider(String fileName) {
		provider = new ParsedObjectReader(fileName).read();

	}

	public PreLauncherDEECoObjectProvider() {
		this(null);
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
		components = provider.getComponents();
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
		ensembles = provider.getEnsembles();
	}

}

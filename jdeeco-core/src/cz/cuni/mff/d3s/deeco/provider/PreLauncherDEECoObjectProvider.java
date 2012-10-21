package cz.cuni.mff.d3s.deeco.provider;

import cz.cuni.mff.d3s.deeco.processor.ParsedObjectReader;

public class PreLauncherDEECoObjectProvider extends AbstractDEECoObjectProvider {

	private AbstractDEECoObjectProvider provider;
	
	
	public PreLauncherDEECoObjectProvider(String fileName) {
		provider = new ParsedObjectReader(fileName).read();

	}
	
	public PreLauncherDEECoObjectProvider() {
		this(null);
	}

	@Override
	protected void processComponents() {
		components = provider.getComponents();
	}

	@Override
	protected void processEnsembles() {
		ensembles = provider.getEnsembles();
	}

}

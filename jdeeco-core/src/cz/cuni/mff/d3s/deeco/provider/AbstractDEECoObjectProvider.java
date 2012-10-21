package cz.cuni.mff.d3s.deeco.provider;

import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.creators.SchedulableEnsembleProcessCreator;

 
public abstract class AbstractDEECoObjectProvider {

	protected List<ParsedComponent> components;
	protected List<SchedulableEnsembleProcessCreator> ensembles;

	public List<ParsedComponent> getComponents() {
		if (components == null)
			processComponents();
		return components;
	}

	public List<SchedulableEnsembleProcessCreator> getEnsembles() {
		if (ensembles == null)
			processEnsembles();
		return ensembles;
	}

	abstract protected void processComponents();

	abstract protected void processEnsembles();

}

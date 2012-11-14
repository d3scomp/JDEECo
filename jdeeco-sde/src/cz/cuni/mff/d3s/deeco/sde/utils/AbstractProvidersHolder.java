package cz.cuni.mff.d3s.deeco.sde.utils;

import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableComponentProcess;
import cz.cuni.mff.d3s.deeco.invokable.SchedulableEnsembleProcess;
import cz.cuni.mff.d3s.deeco.knowledge.ComponentKnowledge;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ParsedComponent;
import cz.cuni.mff.d3s.deeco.provider.ProcessInstantiator;
import cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer;

/**
 * Provider object aggregator class.
 * 
 * @author Michal Kit
 * 
 */
public class AbstractProvidersHolder implements IEnsembleComponentInformer {

	private List<AbstractDEECoObjectProvider> providers;

	public AbstractProvidersHolder() {
		providers = new LinkedList<AbstractDEECoObjectProvider>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer#getComponentsIds
	 * ()
	 */
	@Override
	public List<String> getComponentsIds() {
		List<String> result = new LinkedList<String>();
		for (AbstractDEECoObjectProvider adop : providers) {
			for (ParsedComponent pc : adop.getComponents()) {
				result.add(pc.getInitialKnowledge().id);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer#
	 * getComponentKnowledge(java.lang.String)
	 */
	@Override
	public Object getComponentKnowledge(String componentId) {
		ComponentKnowledge ck;
		for (AbstractDEECoObjectProvider adop : providers) {
			for (ParsedComponent pc : adop.getComponents()) {
				ck = pc.getInitialKnowledge();
				if (ck.id.equals(componentId))
					return ck;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer#
	 * getComponentProcesses(java.lang.String)
	 */
	@Override
	public List<SchedulableComponentProcess> getComponentProcesses(
			String componentId) {
		ComponentKnowledge ck;
		for (AbstractDEECoObjectProvider adop : providers) {
			for (ParsedComponent pc : adop.getComponents()) {
				ck = pc.getInitialKnowledge();
				if (ck.id.equals(componentId))
					return ProcessInstantiator.createComponentProcesses(
							pc.getProcesses(), null, adop.getContextClassLoader());
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.runtime.IEnsembleComponentInformer#getEnsembleProcesses
	 * ()
	 */
	@Override
	public List<SchedulableEnsembleProcess> getEnsembleProcesses() {
		List<SchedulableEnsembleProcess> result = new LinkedList<SchedulableEnsembleProcess>();
		for (AbstractDEECoObjectProvider adop : providers) {
			result.addAll(ProcessInstantiator.createEnsembleProcesses(
					adop.getEnsembles(), null, adop.getContextClassLoader()));
		}
		return result;
	}

	/**
	 * Retrieves all the providers available in this holder.
	 * 
	 * @return All holder providers.
	 */
	public List<AbstractDEECoObjectProvider> getProviders() {
		return providers;
	}

	/**
	 * Checks whether the holder contains the specified provider.
	 * 
	 * @param adop
	 *            Provider to be checked.
	 * @return Result of the search. True in case of a success or false
	 *         otherwise.
	 */
	public boolean contains(AbstractDEECoObjectProvider adop) {
		return providers.contains(adop);
	}

	/**
	 * Adds a provider to this holder.
	 * 
	 * @param adop
	 *            Provider instance to be added.
	 * @return Result of the addition. True in case of a success or false
	 *         otherwise.
	 */
	public boolean add(AbstractDEECoObjectProvider adop) {
		return providers.add(adop);
	}

	/**
	 * Removes the particular provider instance from this holder.
	 * 
	 * @param adop
	 *            Provider to be removed.
	 * @return Result of the removal. True in case of a success or false
	 *         otherwise.
	 */
	public boolean remove(AbstractDEECoObjectProvider adop) {
		return providers.remove(adop);
	}
}

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

public class AbstractProvidersHolder implements IEnsembleComponentInformer {

	private List<AbstractDEECoObjectProvider> providers;

	public AbstractProvidersHolder() {
		providers = new LinkedList<AbstractDEECoObjectProvider>();
	}

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

	@Override
	public List<SchedulableComponentProcess> getComponentProcesses(
			String componentId) {
		ComponentKnowledge ck;
		for (AbstractDEECoObjectProvider adop : providers) {
			for (ParsedComponent pc : adop.getComponents()) {
				ck = pc.getInitialKnowledge();
				if (ck.id.equals(componentId))
					return ProcessInstantiator.createComponentProcesses(
							pc.getProcesses(), null);
			}
		}
		return null;
	}

	@Override
	public List<SchedulableEnsembleProcess> getEnsembleProcesses() {
		List<SchedulableEnsembleProcess> result = new LinkedList<SchedulableEnsembleProcess>();
		for (AbstractDEECoObjectProvider adop : providers) {
			result.addAll(ProcessInstantiator.createEnsembleProcesses(
					adop.getEnsembles(), null));
		}
		return result;
	}

	public List<AbstractDEECoObjectProvider> getProviders() {
		return providers;
	}

	public boolean contains(AbstractDEECoObjectProvider adop) {
		return providers.contains(adop);
	}

	public boolean add(AbstractDEECoObjectProvider adop) {
		return providers.add(adop);
	}

	public boolean remove(AbstractDEECoObjectProvider adop) {
		return providers.remove(adop);
	}
}

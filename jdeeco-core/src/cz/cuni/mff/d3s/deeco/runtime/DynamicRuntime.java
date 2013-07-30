package cz.cuni.mff.d3s.deeco.runtime;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.provider.ComponentInstance;
import cz.cuni.mff.d3s.deeco.provider.DEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.scheduling.IScheduler;

/**
 * the dynamic runtime makes it feasible for the developer to be intentionally capable of
 * removing a node at runtime for demo purposes (e.g. high load)
 * @author Julien
 *
 */
public class DynamicRuntime extends Runtime implements IRuntime {

	private DEECoObjectProvider provider = null;
	
	public DynamicRuntime(KnowledgeManager km, IScheduler scheduler) {
		super(km, scheduler);
	}

	@Override
	public void registerComponentsAndEnsembles(
			DEECoObjectProvider provider) {
		super.registerComponentsAndEnsembles(provider);
		this.provider = provider;
	}
	
	/**
	 * Registers a component dynamically from the runtime.
	 * @param component
	 */
	public void registerComponent(Component component) throws Exception{
		ClassLoader contextClassLoader = provider.getContextClassLoader();
		if (component != null){
			// add the component into the provider
			provider.addInitialKnowledge(component);
			// process the component from the provider (which has prior parsed the component)
			try {
				initComponentKnowledge(component, km);
			} catch (Exception e) {
				Log.e(String.format(
						"Error when initializing knowledge of component %s",
						component.getClass()), e);
			}
			List<? extends SchedulableProcess> componentProcesses = provider.getComponentInstances().get(provider.getComponentInstances().size()-1).getProcesses();
			// the component can have no processes
			if (componentProcesses != null) {
				setUpProcesses(componentProcesses, km, contextClassLoader);
				addSchedulableProcesses(componentProcesses);
			}
		}
	}
	
	/**
	 * Remove the component from the repository.
	 * 
	 * @param id id of the component to remove
	 */
	// FIXME: needs be debugged at runtime for consistency reasons
	public boolean removeComponent(String id) {
		List<ComponentInstance> cis = provider.getComponentInstances();
		// get the component
		ComponentInstance ci = null;
		Integer i = 0;
		while (ci == null){
			if (provider.getInitialKnowledgeForComponentInstance(cis.get(i)).id.equals(id))
				ci = cis.get(i);
			i++;
		}
		// remove the processes
		List<? extends SchedulableProcess> componentProcesses = ci.getProcesses();
		if (componentProcesses != null) {
			scheduler.remove((List<SchedulableProcess>) componentProcesses);
		}
		// remove the knowledge
		Component c = provider.getInitialKnowledgeForComponentInstance(ci);
		try {
			// FIXME : must lock or excluse the node to be called in the ensemble to avoid unconsistency or unexpected behaviors !!!
			// on simulating, some knowledge removed implies some knowledge existing, 
			// and can affect the loading of parameters of the methods using the knowledge manager
			// how to remove a set of knowledge in the knowledge manager?
			removeComponentKnowledge(c, km);
		} catch (Exception e) {
			Log.e(String.format(
					"Error when removing knowledge of component %s",
					c.getClass()), e);
		}
		// remove the component
		return provider.getComponentInstances().remove(ci);
	}
	
	private synchronized void removeComponentKnowledge(Component knowledge,
			KnowledgeManager km) throws Exception {
		if ((knowledge == null) || (km == null))
			throw new NullPointerException();
		
		Object[] currentIds = (Object[]) km
				.getKnowledge(ConstantKeys.ROOT_KNOWLEDGE_ID);
		if (!Arrays.asList(currentIds).contains(knowledge.id)){
			throw new Exception(String.format(
					"Knowledge of a component with id '%s' does not exists",
					knowledge.id));
		}
		// TODO: this is not working perfectly yet
		km.takeAllKnowledge(knowledge.id);
	}
}

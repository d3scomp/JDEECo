package cz.cuni.mff.d3s.deeco.runtime;

import static cz.cuni.mff.d3s.deeco.processor.ComponentParser.extractComponentProcess;

import java.util.Arrays;
import java.util.List;

import cz.cuni.mff.d3s.deeco.invokable.SchedulableProcess;
import cz.cuni.mff.d3s.deeco.knowledge.Component;
import cz.cuni.mff.d3s.deeco.knowledge.ConstantKeys;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.provider.AbstractDEECoObjectProvider;
import cz.cuni.mff.d3s.deeco.provider.ParsedComponent;
import cz.cuni.mff.d3s.deeco.scheduling.IScheduler;

/**
 * the dynamic runtime makes it feasible for the developer to be intentionally capable of dynamically
 * removing a node at runtime for a demo purpose
 * @author Julien
 *
 */
public class DynamicRuntime extends Runtime implements IRuntime {

	private AbstractDEECoObjectProvider provider = null;
	
	public DynamicRuntime(KnowledgeManager km, IScheduler scheduler) {
		super(km, scheduler);
	}

	@Override
	public void registerComponentsAndEnsembles(
			AbstractDEECoObjectProvider provider) {
		super.registerComponentsAndEnsembles(provider);
		this.provider = provider;
	}
	
	/**
	 * register a component dynamically from the runtime
	 * @param component
	 */
	public void registerComponent(Component component) throws Exception{
		ClassLoader contextClassLoader = provider.getContextClassLoader();
		if (component != null){
			ParsedComponent parsedComponent = new ParsedComponent(extractComponentProcess(
					component.getClass(), component.id), component);
			// add the component into the provider
			provider.getComponents().add(parsedComponent);
			// process the component from the provider (which has prior parsed the component)
			try {
				initComponentKnowledge(parsedComponent.getInitialKnowledge(), km);
			} catch (Exception e) {
				Log.e(String.format(
						"Error when initializing knowledge of component %s",
						parsedComponent.getInitialKnowledge().getClass()), e);
				throw e;
			}
			List<? extends SchedulableProcess> componentProcesses = parsedComponent.getProcesses();
			// the component can have no processes
			if (componentProcesses != null) {
				setUpProcesses(componentProcesses, km, contextClassLoader);
				addSchedulableProcesses(componentProcesses);
			}
		}
	}
	
	/**
	 * 
	 * @param id id of the component to remove
	 */
	public boolean unregisterComponent(String id) {
		List<ParsedComponent> parsedComponents = provider.getComponents();
		// get the component
		ParsedComponent component = null;
		Integer i = 0;
		while (component == null){
			if (parsedComponents.get(i).getInitialKnowledge().id.equals(id))
				component = provider.getComponents().get(i);
			i++;
		}
		// remove the processes
		List<? extends SchedulableProcess> componentProcesses = component.getProcesses();
		if (componentProcesses != null) {
			scheduler.remove((List<SchedulableProcess>) componentProcesses);
		}
		// remove the knowledge
		try {
			// FIXME : must lock or excluse the node to be called in the ensemble to avoid unconsistency or unexpected behaviors !!!
			// on simulating, some knowledge removed implies some knowledge existing, 
			// and can affect the loading of parameters of the methods using the knowledge manager
			// how to remove a set of knowledge in the knowledge manager?
			removeComponentKnowledge(component.getInitialKnowledge(), km);
		} catch (Exception e) {
			Log.e(String.format(
					"Error when removing knowledge of component %s",
					component.getInitialKnowledge().getClass()), e);
		}
		// remove the component
		return provider.getComponents().remove(id);
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

package cz.cuni.mff.d3s.deeco.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.task.EnsembleFormationTask;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Container holding {@link Task}s corresponding to a single
 * {@link ComponentInstance} within {@link RuntimeFramework}.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
class ComponentInstanceRecord {
	/**
	 * @see ComponentInstanceRecord#getInstance()
	 */
	ComponentInstance componentInstance;
	
	/**
	 * @see ComponentInstanceRecord#getProcessTasks()
	 */
	Map<ComponentProcess , Task> processTasks = new HashMap<>();

	/**
	 * @see ComponentInstanceRecord#getEnsembleTasks()
	 */
	Map<EnsembleController, Task> ensembleTasks = new HashMap<>();
	
	/**
	 * @see ComponentInstanceRecord#getEnsembleFormationTasks()
	 */
	List<EnsembleFormationTask> ensembleFormationTasks = new ArrayList<>();	

		
	public ComponentInstanceRecord(ComponentInstance componentInstance) {
		this.componentInstance = componentInstance;
	}
	
	/**
	 * The corresponding component instance.
	 */
	public ComponentInstance getInstance() {
		return componentInstance;
	}

	/**
	 * The tasks corresponding to processes of the instance.
	 */
	public Map<ComponentProcess , Task> getProcessTasks() {
		return processTasks;
	}		
	
	
	/**
	 * The tasks corresponding to ensemble controllers of the instance.
	 */
	public Map<EnsembleController, Task> getEnsembleTasks() {
		return ensembleTasks;
	}
	
	public List<EnsembleFormationTask> getEnsembleFormationTasks() {
		return ensembleFormationTasks;
	}


	/**
	 * Returns all tasks associated with the corresponding component
	 * instance.
	 */
	public Collection<Task> getAllTasks() {
		Set<Task> all = new HashSet<>();
		all.addAll(processTasks.values());
		all.addAll(ensembleTasks.values());	
		all.addAll(ensembleFormationTasks);
		
		return all;
	}	
	
}

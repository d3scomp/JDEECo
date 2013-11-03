package cz.cuni.mff.d3s.deeco.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * Container holding {@link Task}s corresponding to a single {@link ComponentInstance}.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
class ComponentInstanceRecord {
	ComponentInstance componentInstance;
	
	Map<ComponentProcess , Task> processTasks = new HashMap<>();
	Map<EnsembleController, Task> ensembleControllerTasks = new HashMap<>();
	
	public ComponentInstanceRecord(ComponentInstance componentInstance) {
		this.componentInstance = componentInstance;
	}

	/**
	 * Returns tasks associated with the processes of the corresponding
	 * component instance.
	 */
	public Map<ComponentProcess , Task> getProcessTasks() {
		return processTasks;
	}		
	
	/**
	 * Returns tasks associated with the ensemble controllers of the
	 * corresponding component instance.
	 */
	public Map<EnsembleController, Task> getEnsembleControllerTasks() {
		return ensembleControllerTasks;
	}	
	
	/**
	 * Returns all tasks associated with the corresponding component
	 * instance.
	 */
	public Collection<Task> getAllTasks() {
		Set<Task> all = new HashSet<>();
		all.addAll(processTasks.values());
		all.addAll(ensembleControllerTasks.values());
		return all;
	}
	
	
}
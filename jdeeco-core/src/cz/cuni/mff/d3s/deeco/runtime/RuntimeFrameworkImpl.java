package cz.cuni.mff.d3s.deeco.runtime;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;
import cz.cuni.mff.d3s.deeco.task.ProcessTask;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeFrameworkImpl implements RuntimeFramework {
	
	protected Scheduler scheduler;
	protected RuntimeMetadata model;
	protected Executor executor;
	protected KnowledgeManagerContainer kmContainer;
	
	protected Map<ComponentInstance, ComponentInstanceRecord> componentRecords = new HashMap<>();
		
	protected Map<ComponentInstance, Adapter> componentInstanceAdapters = new HashMap<>();
	protected Map<ComponentProcess, Adapter> componentProcessAdapters = new HashMap<>();

	

	/**
	 * Initializes the runtime with the given runtime services and prepares the
	 * model for execution.
	 * 
	 * It also registers adaptors for reacting to changes in the model.
	 * 
	 * @param model
	 *            model of the application to be executed.
	 * @param scheduler
	 * 			  the scheduler to be used for scheduling the tasks
	 * @param executor
	 * 			  the scheduler to be used for executing the tasks
	 * @param kmRegistry
	 *            the KM registry to be used for management of knowledge repositories.
	 * 
	 * FIXME: add synchronizer/network container in the constructor
	 */
	public RuntimeFrameworkImpl(RuntimeMetadata model, Scheduler scheduler,
			Executor executor, KnowledgeManagerContainer kmContainer) {
		
		if (model == null)
			throw new IllegalArgumentException("Model cannot be null");
		if (scheduler == null)
			throw new IllegalArgumentException("Scheduler cannot be null");
		if (executor == null)
			throw new IllegalArgumentException("Executor cannot be null");
		if (kmContainer == null)
			throw new IllegalArgumentException("KnowledgeManagerContainer cannot be null");
		
		this.scheduler = scheduler;
		this.model = model;
		this.executor = executor;
		this.kmContainer = kmContainer;
		
		init();
	}

	/**
	 * Creates and initializes all the internal runtime objects based on the
	 * {@link #model} and {@link #configuration}.
	 */
	void init() {		
		// initialize the components
		for (ComponentInstance ci: model.getComponentInstances()) {
			componentInstanceAdded(ci);
		}
		
		// register adapters to listen for model changes
		// listen to ADD/REMOVE in RuntimeMetadata.getComponentInstances()
		Adapter componentInstancesAdapter = new AdapterImpl() {
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if (notification.getFeature() == model.getComponentInstances()) {
					// new component instance added
					if (notification.getEventType() == Notification.ADD) {
						componentInstanceAdded((ComponentInstance) notification.getNewValue());
					// a component instance removed
					} else if (notification.getEventType() == Notification.REMOVE) {
						componentInstanceRemoved((ComponentInstance) notification.getOldValue());
					}
				}
			}
		};
		model.eAdapters().add(componentInstancesAdapter);	
	}
	


	/**
	 * Implementation of a notification indicating that a new component instance
	 * has been added to the model.
	 */
	void componentInstanceAdded(final ComponentInstance instance) {
		if ((instance == null) || (componentRecords.containsKey(instance))) {
			return;
		}
		
				
		ComponentInstanceRecord ciRecord = new ComponentInstanceRecord(instance);
		componentRecords.put(instance, ciRecord);
		
		for (ComponentProcess p: instance.getComponentProcesses()) {			
			componentProcessAdded(instance, p);
		}
		
		// for now, we do not assume that the EnsembleControllers will change,
		// thus they are scheduled from the beginning and have no adapters
		for (EnsembleController ec: instance.getEnsembleControllers()) {
			Task task = new EnsembleTask(ec, scheduler);
			ciRecord.getEnsembleTasks().put(ec, task);
			scheduler.addTask(task);						
		}
				
		// register adapters to listen for model changes
		// listen to ADD/REMOVE in ComponentInstance.getComponentProcesses()
		Adapter componentInstanceAdapter = new AdapterImpl() {
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if (notification.getFeature() == instance.getComponentProcesses()) {
					// if new task added
					if (notification.getEventType() == Notification.ADD) {
						componentProcessAdded(instance, (ComponentProcess) notification.getNewValue());
					// if a task removed
					} else if (notification.getEventType() == Notification.REMOVE) {
						componentProcessRemoved(instance, (ComponentProcess) notification.getOldValue());
					}
				}
			}
		};
		instance.eAdapters().add(componentInstanceAdapter);	
		componentInstanceAdapters.put(instance, componentInstanceAdapter);
	}
	
	void componentProcessAdded(final ComponentInstance instance,
			final ComponentProcess process) {
		ComponentInstanceRecord ciRecord = componentRecords.get(instance);
		final Task newTask = new ProcessTask(process, scheduler);
		ciRecord.getProcessTasks().put(process, newTask);
		
		componentProcessActiveChanged(process, newTask, true);
		
		// register adapters to listen for model changes
		// listen to change in ComponentProcess.isActive
		Adapter componentProcessAdapter = new AdapterImpl() {
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if ((notification.getFeatureID(ComponentProcess.class) == RuntimeMetadataPackage.COMPONENT_PROCESS__IS_ACTIVE)
						&& (notification.getEventType() == Notification.SET)){
					componentProcessActiveChanged(process, newTask, notification.getNewBooleanValue());
				}
			}
		};
		process.eAdapters().add(componentProcessAdapter);	
		componentProcessAdapters.put(process, componentProcessAdapter);
	} 
	
	void componentProcessActiveChanged(ComponentProcess process, Task processTask, boolean active) {
		if (active) {
			scheduler.addTask(processTask);
		} else {
			scheduler.removeTask(processTask);
		}
	}


	/**
	 * Implementation of a notification indicating that an existing component
	 * instance has been removed from the model.
	 */
	void componentInstanceRemoved(ComponentInstance instance) {
		if ((instance == null) || (!componentRecords.containsKey(instance))) {
			return;
		} 
		
		ComponentInstanceRecord ciRecord = componentRecords.get(instance);
		
		for (ComponentProcess p: ciRecord.getProcessTasks().keySet()) {
			componentProcessRemoved(instance, p);
		}	
		
		for (Task t: ciRecord.getEnsembleTasks().values()) {
			scheduler.removeTask(t);
		}
		
		componentRecords.remove(instance);
		
		if (componentInstanceAdapters.containsKey(instance)) { 
			instance.eAdapters().remove(componentInstanceAdapters.get(instance));
			componentInstanceAdapters.remove(instance);
		}		
	}
	
	void componentProcessRemoved(ComponentInstance instance,
			ComponentProcess process) {
		
		ComponentInstanceRecord ciRecord = componentRecords.get(instance);
		
		componentProcessActiveChanged(process, ciRecord.getProcessTasks().get(process), false);
		
		ciRecord.getProcessTasks().remove(process);
		
		if (componentProcessAdapters.containsKey(process)) { 
			process.eAdapters().remove(componentProcessAdapters.get(process));
			componentProcessAdapters.remove(process);
		}	
	}
	
	
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework#start()
	 */
	@Override
	public void start() {
		scheduler.start();
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework#stop()
	 */
	@Override
	public void stop() {
		scheduler.stop();
	}
	
	

}

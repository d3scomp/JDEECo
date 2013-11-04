package cz.cuni.mff.d3s.deeco.runtime;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerRegistry;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.EnsembleCoordinatorTask;
import cz.cuni.mff.d3s.deeco.task.EnsembleMemberTask;
import cz.cuni.mff.d3s.deeco.task.ProcessTask;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeFrameworkImpl implements RuntimeFramework {
	protected RuntimeConfiguration configuration;
	protected Scheduler scheduler;
	protected RuntimeMetadata model;
	protected Executor executor;
	protected KnowledgeManagerRegistry kmRegistry;
	
	protected Map<ComponentInstance, ComponentInstanceRecord> componentRecords = new HashMap<>();
		
	protected Map<ComponentInstance, Adapter> componentInstanceAdapters = new HashMap<>();
	protected Map<ComponentProcess, Adapter> componentProcessAdapters = new HashMap<>();

	
	/**
	 * Creates and initializes all the internal runtime objects.
	 * 
	 * @param model	model of the application to be deployed. 
	 * @param configuration technical configuration of the runtime framework
	 */
	public RuntimeFrameworkImpl(RuntimeMetadata model, RuntimeConfiguration configuration) {
		if ((model == null) || (configuration == null)) {
			throw new IllegalArgumentException("model and configuration must not be null");
		}
		
		this.model = model;
		this.configuration = configuration;
				
		init();
	}

	/**
	 * Creates and initializes all the internal runtime objects based on the
	 * {@link #model} and {@link #configuration}.
	 */
	void init() {		
		// wire things together
		scheduler = configuration.getScheduler();
		executor = configuration.getExecutor();
		scheduler.setExecutor(executor);
		executor.setExecutionListener(scheduler);
		
		kmRegistry = configuration.getKnowledgeManagerRegistry();
		
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
	
	
	void registerModelAdapters() {
		
		
	
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
			Task tCoordinator = new EnsembleCoordinatorTask(ec, scheduler);
			ciRecord.getEnsembleCoordinatorTasks().put(ec, tCoordinator);
			scheduler.addTask(tCoordinator);
			
			Task tMember = new EnsembleMemberTask(ec, scheduler);
			ciRecord.getEnsembleMemberTasks().put(ec, tMember);			
			scheduler.addTask(tMember);
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
		
		for (Task t: ciRecord.getAllEnsembleTasks()) {
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

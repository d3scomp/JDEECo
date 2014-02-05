package cz.cuni.mff.d3s.deeco.runtime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ShadowKnowledgeManagerRegistryImpl;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.logging.Log;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataPackage;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.EnsembleTask;
import cz.cuni.mff.d3s.deeco.task.ProcessTask;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * JDEECo runtime framework implementation.
 * 
 * <p>
 * The class acts as a management container over the internal jDEECo services (
 * {@link Scheduler}, {@link Executor}, {@link KnowledgeManagerContainer}
 * ) and the {@link RuntimeMetadata} model.
 * </p> 
 * 
 * <p>
 * The main purpose is to observe the changes in the model and control the
 * {@link Scheduler} and {@link Executor} accordingly. In particular, the
 * runtime framework observes the following:
 * - whether a component instance was added/removed</br>
 * - whether a process/ensemble controller was added/removed from an existing instance</br>
 * - whether {@link ComponentProcess#isIsActive()} for an existing process changed</br> 
 * </p>
 * 
 * <p>
 * All the internal services are passed to the runtime explicitly, which allows
 * for sharing of these services among multiple runtime framework instances.
 * </p>
 * 
 * <p>
 * The class is not thread safe (modifying the model from multiple threads can 
 * result in race conditions in model change listeners of the runtime framework).
 * </p>
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * 
 */
public class RuntimeFrameworkImpl implements RuntimeFramework {
	
	/**
	 * The model corresponding to the running application.
	 */
	protected RuntimeMetadata model;
	
	
	/** 
	 * The scheduler used by the runtime.
	 */
	protected Scheduler scheduler;	
	
	/**
	 * The executor used by the runtime.
	 */
	protected Executor executor;	
	/**
	 * The KM container used by the runtime.
	 */
	protected KnowledgeManagerContainer kmContainer;
	
	/**
	 * Keeps track of each instance's tasks.
	 */
	protected Map<ComponentInstance, ComponentInstanceRecord> componentRecords = new HashMap<>();
		
	
	/**
	 * Keeps track of ecore adapters for component instances.
	 */
	protected Map<ComponentInstance, Adapter> componentInstanceAdapters = new HashMap<>();	
	/**
	 * Keeps track of ecore adapters for component processes.
	 */
	protected Map<ComponentProcess, Adapter> componentProcessAdapters = new HashMap<>();

	

	/**
	 * Initializes the runtime with the given internal services and prepares the
	 * model for execution.
	 * 
	 * It also registers adaptors for observing the changes in the model.
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
	 * TODO: add synchronizer/network container in the constructor
	 * 
	 * @throws IllegalArgumentException if either of the arguments is null.
	 */
	public RuntimeFrameworkImpl(RuntimeMetadata model, Scheduler scheduler,
			Executor executor, KnowledgeManagerContainer kmContainer) {
		this(model, scheduler, executor, kmContainer, true);
	}
	
	/**
	 * Convenience constructor to make the initialization of the runtime upon
	 * creation optional. The public constructor has it always mandatory.
	 * 
	 * @see RuntimeFrameworkImpl#RuntimeFrameworkImpl(RuntimeMetadata, Scheduler, Executor, KnowledgeManagerContainer)
	 * @see RuntimeFrameworkImpl#init()
	 */
	RuntimeFrameworkImpl(RuntimeMetadata model, Scheduler scheduler,
			Executor executor, KnowledgeManagerContainer kmContainer, boolean autoInit) {
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
		
		if (autoInit)
			init();
	}

	/**
	 * Initializes the internal services based on the {@link #model}. It also
	 * registers adaptors for listening to changes in the model.
	 * <p>
	 * Logs errors but does no throw any exceptions.
	 * </p>
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
				if (notification.getFeature() == model.eClass().getEStructuralFeature(RuntimeMetadataPackage.RUNTIME_METADATA__COMPONENT_INSTANCES)) {
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
	 * 
	 * <p>
	 * Creates and schedules new tasks for existing component processes and ensemble controllers.
	 * Registers adaptors for listening to changes in the {@code instance}. 
	 * </p>
	 * 
	 * <p>
	 * Logs errors but does not throw any exceptions.
	 * TODO: throw exceptions and catch them in the callbacks (if this method is
	 * used from init() the exceptions will propagate to the caller)
	 * </p>
	 * 
	 * @see RuntimeFrameworkImpl#componentProcessAdded(ComponentInstance, ComponentProcess)
	 * 
	 */
	void componentInstanceAdded(final ComponentInstance instance) {
		if (instance == null) {
			Log.w("Attempting to add null ComponentInstance");
			return;
		}
	
		if (componentRecords.containsKey(instance)) {
			Log.w(String.format("Attempting to add an already-registered ComponentInstance (%s)", instance));		
			return;
		}		
				
		ComponentInstanceRecord ciRecord = new ComponentInstanceRecord(instance);
		componentRecords.put(instance, ciRecord);
		
		// replace the KM with one created via kmContainer
		replaceKnowledgeManager(instance);
		
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
				if (notification.getFeature() == instance.eClass().getEStructuralFeature(RuntimeMetadataPackage.COMPONENT_INSTANCE__COMPONENT_PROCESSES)) {
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
	
	/** 
	 * Replaces the KM in the component instance with a KM created via {@link #kmContainer}.
	 */
	void replaceKnowledgeManager(ComponentInstance ci) {			
		ValueSet initialKnowledge = null;
		try {
			KnowledgePath empty = RuntimeMetadataFactoryExt.eINSTANCE.createKnowledgePath();
			// get all the knowledge (corresponding to an empty knowledge path)
			initialKnowledge = ci.getKnowledgeManager().get(Arrays.asList(empty));
		} catch (KnowledgeNotFoundException e) {
			Log.w("No initial knowledge value cor component " + ci.getKnowledgeManager().getId());
		}
		
		// copy all the knowledge values into a ChangeSet
		ChangeSet cs = new ChangeSet();
		if (initialKnowledge != null) {
			for (KnowledgePath p: initialKnowledge.getKnowledgePaths()) {
				cs.setValue(p, initialKnowledge.getValue(p));
			}			
		}
		
		// create a new KM with the same id and knowledge values
		KnowledgeManager km = kmContainer.createLocal(ci.getKnowledgeManager().getId());
		try {
			km.update(cs);
		} catch (KnowledgeUpdateException e) {
			Log.e("bindKnowledgeManagerContainer: exception when updating the knowledge manager", e);
		}
		// replace the KM and the KMView references
		ci.setKnowledgeManager(km);	
		ci.setShadowKnowledgeManagerRegistry(new ShadowKnowledgeManagerRegistryImpl(km, kmContainer));			
	}
	
	/**
	 * Implementation of a notification indicating that a new component process
	 * has been added to the model. 
	 * 
	 * <p>
	 * Creates a new task for the process and schedules it if the process is active.
	 * Registers an adaptor for listening to changes in the {@code process}. 
	 * </p> 
	 * <p>
	 * Logs errors but does not throw any exceptions.
	 * </p>
	 * 
	 * @see RuntimeFrameworkImpl#componentProcessActiveChanged(ComponentProcess, boolean) 
	 * 
	 */
	void componentProcessAdded(final ComponentInstance instance,
			final ComponentProcess process) {
		if ((instance == null) || (process == null)) {
			Log.w(String.format("Attempting to add an invalid process (%s) to an invalid component instance (%s)", process, instance));
			return;
		}
		
		ComponentInstanceRecord cir = componentRecords.get(instance);		
		if (cir == null) {
			Log.w(String.format("Attempting to add a process (%s) to an unregistered instance (%s)", process, instance));
			return;
		}
		
		if (cir.getProcessTasks().containsKey(process)) {
			Log.w(String.format("Attempting to add an already existing process (%s) to instance (%s)", process, instance));
			return;
		}
		
		final Task newTask = new ProcessTask(process, scheduler);
		cir.getProcessTasks().put(process, newTask);
		
		componentProcessActiveChanged(instance, process, process.isIsActive());
		
		// register adapters to listen for model changes
		// listen to change in ComponentProcess.isActive
		Adapter componentProcessAdapter = new AdapterImpl() {
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				if ((notification.getFeatureID(ComponentProcess.class) == RuntimeMetadataPackage.COMPONENT_PROCESS__IS_ACTIVE)
						&& (notification.getEventType() == Notification.SET)){
					componentProcessActiveChanged(instance, process, notification.getNewBooleanValue());
				}
			}
		};
		process.eAdapters().add(componentProcessAdapter);	
		componentProcessAdapters.put(process, componentProcessAdapter);
	} 
	
	/**
	 * Implementation of a notification indicating that a process became (in)active.
	 * 
	 * <p>
	 * Schedules the corresponding task accordingly.   
	 * </p> 
	 * <p>
	 * Logs errors but does not throw any exceptions.
	 * </p>
	 * 
	 * @see Scheduler#addTask(Task)
	 * @see Scheduler#removeTask(Task) 
	 * 
	 */
	void componentProcessActiveChanged(ComponentInstance instance, ComponentProcess process, boolean active) {		
		if (process == null) {
			Log.w("Attempting to to change the activity of a null process.");
			return;
		}
		// the instance is not registered 
		if ((!componentRecords.containsKey(instance) 
			// OR the process is not registered
			|| (!componentRecords.get(instance).getProcessTasks().containsKey(process)))) {
			Log.w(String.format("Attempting to change the activity of an unregistered process (%s).", process));
			return;
		}
		
		Task t = componentRecords.get(instance).getProcessTasks().get(process);
		
		Log.i(String.format("Changing the activity of task %s corresponding to process %s to %s.", t, process, active));
		
		if (active) {
			scheduler.addTask(t);
		} else {
			scheduler.removeTask(t);
		}
	}


	/**
	 * Implementation of a notification indicating that an existing component
	 * instance has been removed from the model.
	 * 
	 * <p>
	 * Removes and de-schedules the corresponding tasks for the removed instance.
	 * Unregisters the ecore adaptors from the instance. 
	 * </p>
	 * 
	 * <p>
	 * Logs errors but does not throw any exceptions.	
	 * </p>
	 * 
	 * @see RuntimeFrameworkImpl#componentProcessRemoved(ComponentInstance, ComponentProcess)
	 */
	void componentInstanceRemoved(ComponentInstance instance) {
		if (instance == null) {
			Log.w("Attempting to remove a null ComponentInstance");
			return;
		}
	
		if (!componentRecords.containsKey(instance)) {
			Log.w(String.format("Attempting to remove a non-registered ComponentInstance (%s)", instance));		
			return;
		}	
						
		ComponentInstanceRecord ciRecord = componentRecords.get(instance);
		
		while (!ciRecord.getProcessTasks().isEmpty()) {
			ComponentProcess p = ciRecord.getProcessTasks().keySet().iterator().next();
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
	
	/**
	 * Implementation of a notification indicating that an existing component
	 * process has been removed from the model.
	 * 
	 * <p>
	 * De-schedules and discards the task corresponding to the removed process.
	 * Unregisters the ecore adaptors from the process. 
	 * </p>
	 * 
	 * <p>
	 * Logs errors but does not throw any exceptions.	
	 * </p>
	 * 
	 * @see RuntimeFrameworkImpl#componentProcessActiveChanged(ComponentProcess, boolean)
	 */
	void componentProcessRemoved(ComponentInstance instance,
			ComponentProcess process) {
		if ((instance == null) || (process == null)) {
			Log.w(String.format("Attempting to remove an invalid process (%s) from an invalid component instance (%s)", process, instance));
			return;
		}
		
		ComponentInstanceRecord cir = componentRecords.get(instance);		
		if (cir == null) {
			Log.w(String.format("Attempting to remove a process (%s) from an unregistered instance (%s)", process, instance));
			return;
		}
		
		if (!cir.getProcessTasks().containsKey(process)) {
			Log.w(String.format("Attempting to remove an unregistered process (%s) from instance (%s)", process, instance));
			return;
		}
		
		// unregister the adapter before the task is discarded 
		if (componentProcessAdapters.containsKey(process)) { 
			process.eAdapters().remove(componentProcessAdapters.get(process));
			componentProcessAdapters.remove(process);
		}	
		
		componentProcessActiveChanged(instance, process, false);
		
		cir.getProcessTasks().remove(process);		
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
	
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.runtime.RuntimeFramework#invokeAndWait(java.lang.Runnable)
	 */
	@Override
	public void invokeAndWait(Runnable r) throws InterruptedException {
		scheduler.invokeAndWait(r);		
	}
	

}

package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.executor.SameThreadExecutor;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Distribution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Execution;
import cz.cuni.mff.d3s.deeco.runtime.RuntimeConfiguration.Scheduling;
import cz.cuni.mff.d3s.deeco.scheduler.LocalTimeScheduler;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeFrameworkImplTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	Scheduler scheduler;
	Executor executor;
	KnowledgeManagerContainer kmContainer;
	
	RuntimeMetadata model;	
	
	ComponentInstance component;
	ComponentProcess process;
	EnsembleController econtroller;
	
	RuntimeFrameworkImpl spy;

	
	@Before
	public void setUp() throws Exception {
		scheduler = mock(Scheduler.class);
		executor = mock(Executor.class);
		kmContainer = mock(KnowledgeManagerContainer.class);
				
		RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
		process = factory.createComponentProcess();
		econtroller = factory.createEnsembleController();
		
		component = factory.createComponentInstance();
		component.getComponentProcesses().add(process);
		component.getEnsembleControllers().add(econtroller);
		
		model = factory.createRuntimeMetadata();
		model.getComponentInstances().add(component);
		
		spy = mock(RuntimeFrameworkImpl.class);
		
	}

	
	
	@Test
	public void testRuntimeFrameworkImplNotNull() {
		// GIVEN valid model, scheduler, and executor	
		// WHEN a new RuntimeFrameworkImpl is created
		RuntimeFrameworkImpl result = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer);
		// THEN the constructor finishes successfully
		assertNotNull(result);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullModel() {		
		// WHEN a new RuntimeFrameworkImpl is created with a null model
		new RuntimeFrameworkImpl(null, scheduler, executor, kmContainer);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}

	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullScheduler() {
		// WHEN a new RuntimeFrameworkImpl is created with a null scheduler
		new RuntimeFrameworkImpl(model, null, executor, kmContainer);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}

	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullExecutor() {
		// WHEN a new RuntimeFrameworkImpl is created with a null executor
		new RuntimeFrameworkImpl(model, scheduler, null, kmContainer);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}

	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullKnowledgeRepositoryContainer() {
		// WHEN a new RuntimeFrameworkImpl is created with a null KM container
		new RuntimeFrameworkImpl(model, scheduler, executor, null);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}
	
	@Test
	public void testRuntimeFrameworkImplInitCalled() {
		// GIVEN valid model, scheduler, and executor	
		// WHEN a new RuntimeFrameworkImpl is created via the public constructor 
		RuntimeFrameworkImpl result = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer) {
			@Override
			void init() {
				spy.init();
			}
		};
		// THEN the init() gets called
		verify(spy).init();
	}
	
	@Test
	public void testInitComponentInstancesAdapterPresent() {
		// GIVEN a model with no adapters  and a non-initialized runtime 	
		assertEquals(0, model.eAdapters().size());			
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false);

		// WHEN init() is called on the runtime
		tested.init();
		
		// THEN the runtime sets up an adapter to observe changes of the list of
		// component instances
		assertEquals(1, model.eAdapters().size());				
	}
	
	
	
	@Test
	public void testInit0ComponentInstanceAdded() {
		// GIVEN a model with no component instances and a non-initialized runtime 		
		model.getComponentInstances().clear();		
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		
		// WHEN when init is called() on the runtime		
		tested.init();
		
		// THEN the callback componentInstanceAdded is not called 
		verify(tested, never()).componentInstanceAdded(any(ComponentInstance.class));		
	}
	
	@Test
	public void testInit1ComponentInstanceAdded() {
		// GIVEN a model with one component instance and a non-initialized runtime 	
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		
		// WHEN when init is called() on the runtime
		tested.init();
		
		// THEN the component is added via the callback componentInstanceAdded 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verify(tested).componentInstanceAdded(component);
	}
	
	@Test
	public void testInit2ComponentInstanceAdded() {
		// GIVEN a model with two component instances and a non-initialized runtime 	
		ComponentInstance component2 = EcoreUtil.copy(component);
		model.getComponentInstances().add(component2);
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
	
		// WHEN when init is called() on the runtime
		tested.init();		
		
		// THEN the components are all added via the callback componentInstanceAdded 
		verify(tested, times(2)).componentInstanceAdded(any(ComponentInstance.class));
		verify(tested).componentInstanceAdded(component);
		verify(tested).componentInstanceAdded(component2);
	}
	
	/*
	 * componentInstanceAdded
	 */	
	
	@Test
	public void testComponentInstanceAddedNull() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		
		// WHEN adding a null component instance
		tested.componentInstanceAdded(null);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentInstanceAddedExisting() {
		// GIVEN a runtime initialized with a component instance
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
		
		// WHEN adding the already added component instance
		tested.componentInstanceAdded(component);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}	
	
	
	
	@Test
	public void testComponentInstanceAddedCreatesNewComponentRecord() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false);
		
		// WHEN a valid component instance is added
		tested.componentInstanceAdded(component);		
		
		// THEN a new record will be created in componentRecords collection
		assertNotNull(tested.componentRecords.get(component));		
	}
	
	@Test
	public void testComponentInstanceAdded0ProcessesAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		
		// WHEN adding a component instance with zero processes
		component.getComponentProcesses().clear();
		tested.componentInstanceAdded(component);
		
		// THEN the componentProcessAdded is not called
		verify(tested, never()).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
	}
	
	@Test
	public void testComponentInstanceAdded2ProcessesAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		
		// WHEN adding a component instance with three processes
		ComponentProcess process2 = EcoreUtil.copy(process);
		component.getComponentProcesses().add(process2);
		tested.componentInstanceAdded(component);
		
		// THEN the componentProcessAdded is called for each of the processes
		verify(tested, times(2)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));;
		verify(tested).componentProcessAdded(component, process);
		verify(tested).componentProcessAdded(component, process2);
	}
	
	@Test
	public void testComponentInstanceAdded0ControllersAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false);
		
		// WHEN adding a component instance with zero ensemble controllers and zero processes
		component.getComponentProcesses().clear();
		component.getEnsembleControllers().clear();
		tested.componentInstanceAdded(component);
		
		// THEN no ensembling tasks are created/scheduled
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		assertEquals(0, cir.getEnsembleTasks().size());		
		verify(scheduler, never()).addTask(any(Task.class));
	}
	
	@Test
	public void testComponentInstanceAdded2ControllersAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false);
		
		// WHEN adding a component instance with three ensemble controllers and zero processes
		component.getComponentProcesses().clear();
		EnsembleController econtroller2 = EcoreUtil.copy(econtroller);
		component.getEnsembleControllers().add(econtroller2);
		tested.componentInstanceAdded(component);
		
		// THEN a task is created and scheduled for each of the controllers
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		assertEquals(2, cir.getEnsembleTasks().size());	
		Task t = cir.getEnsembleTasks().get(econtroller);
		Task t2 = cir.getEnsembleTasks().get(econtroller2);
		assertNotNull(t);
		assertNotNull(t2);
		verify(scheduler, times(2)).addTask(any(Task.class));
		verify(scheduler).addTask(t);
		verify(scheduler).addTask(t2);
	}
	
	@Test
	public void testComponentInstanceAddedAdapterPresent() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false);		
		
		// WHEN adding a component instance		
		tested.componentInstanceAdded(component);
		
		// THEN the runtime registers an adapter observing changes of the instance
		Adapter a = tested.componentInstanceAdapters.get(component);
		assertNotNull(a);
		assertTrue(component.eAdapters().contains(a));
	}
	
	/*
	 * componentInstanceRemoved
	 */	
	
	@Test
	public void testComponentInstanceRemovedNull() {
		// GIVEN a runtime that uses a model with one component instance
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		
		// WHEN removing a null component instance
		tested.componentInstanceRemoved(null);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceRemoved(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentInstanceRemovedNonexisting() {
		// GIVEN a runtime that uses a model with one component instance
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
		
		// WHEN removing a non-existing component instance
		tested.componentInstanceRemoved(mock(ComponentInstance.class));		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceRemoved(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}	
	
	@Test
	public void testComponentInstanceRemovedExisting() {
		// GIVEN a runtime that uses a model with a component instance 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
		
		// WHEN removing an existing component instance
		tested.componentInstanceRemoved(component);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceRemoved(any(ComponentInstance.class));
	}	
	
	@Test
	public void testComponentInstanceRemovedDeletesComponentRecord() {
		// GIVEN a runtime that uses a model with a component instance 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
		assertNotNull(tested.componentRecords.get(component));		

		// WHEN removing an existing component instance
		tested.componentInstanceRemoved(component);		
		
		// THEN its record will be removed from componentRecords collection
		assertNull(tested.componentRecords.get(component));				
	}
	
	@Test
	public void testComponentInstanceRemoved0ProcessesRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		// having no processes 
		component.getComponentProcesses().clear();
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));

		// WHEN removing a component with zero processes
		tested.componentInstanceRemoved(component);
		
		// THEN the componentProcessRemoved is not called
		verify(tested, never()).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));
	}
	
	@Test
	public void testComponentInstanceRemoved2ProcessesRemoved() {
		// GIVEN a runtime that uses a model with a component instance
		// having two processes 
		ComponentProcess process2 = EcoreUtil.copy(process);
		component.getComponentProcesses().add(process2);
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
				
		// WHEN removing a component instance with two processes		
		tested.componentInstanceRemoved(component);
		
		// THEN the componentProcessRemoved is called for each of the processes
		verify(tested, times(2)).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));;
		verify(tested).componentProcessRemoved(component, process);
		verify(tested).componentProcessRemoved(component, process2);
	}
	
	@Test
	public void testComponentInstanceRemoved0ControllersRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		// having no ensemble controllers and zero processes
		component.getComponentProcesses().clear();
		component.getEnsembleControllers().clear();
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
		
		// WHEN removing a component instance with zero ensemble controllers and zero processes		
		tested.componentInstanceRemoved(component);
		
		// THEN no ensembling tasks are removed from scheduler
		verify(scheduler, never()).removeTask(any(Task.class));
	}
	
	@Test
	public void testComponentInstanceRemoved2ControllersRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		// having two ensemble controllers and zero processes
		EnsembleController econtroller2 = EcoreUtil.copy(econtroller);
		component.getComponentProcesses().clear();
		component.getEnsembleControllers().add(econtroller2);
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
	
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		
		// WHEN removing the component instance 
		tested.componentInstanceRemoved(component);
		
		// THEN the tasks of all controllers are removed from scheduler
		Task t = cir.getEnsembleTasks().get(econtroller);
		Task t2 = cir.getEnsembleTasks().get(econtroller2);
		verify(scheduler, times(2)).removeTask(any(Task.class));
		verify(scheduler).addTask(t);
		verify(scheduler).addTask(t2);
	}
	
	@Test
	public void testComponentInstanceRemovedAdapterRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, true));
		tested.componentInstanceAdded(component);
		Adapter a = tested.componentInstanceAdapters.get(component);
				
		
		// WHEN removing the component instance		
		tested.componentInstanceRemoved(component);
		
		// THEN the runtime unregisters its adapter observing changes of the instance
		assertFalse(component.eAdapters().contains(a));
		assertNull(tested.componentInstanceAdapters.get(component));
	}
	
}

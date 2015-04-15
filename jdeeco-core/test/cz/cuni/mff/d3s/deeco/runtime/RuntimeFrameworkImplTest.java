package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.StandardErrorStreamLog;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import cz.cuni.mff.d3s.deeco.executor.Executor;
import cz.cuni.mff.d3s.deeco.integrity.RatingsManager;
import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.runtimelog.RuntimeLogger;
import cz.cuni.mff.d3s.deeco.scheduler.Scheduler;
import cz.cuni.mff.d3s.deeco.task.Task;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class RuntimeFrameworkImplTest {

	
	@Rule
	public final StandardErrorStreamLog log = new StandardErrorStreamLog();

	Scheduler scheduler;
	Executor executor;
	KnowledgeManagerContainer kmContainer;
	
	RuntimeMetadata model;	
	
	ComponentInstance component;	
	ComponentProcess process;
	EnsembleController econtroller;
	
	EnsembleDefinition edefinition;
	
	KnowledgeManager km;
	KnowledgeManager kmReplacement;
	
	RuntimeFrameworkImpl spy;
	RatingsManager ratingsManager;
	
	RuntimeMetadataFactory factory = RuntimeMetadataFactoryExt.eINSTANCE;
	
	DEECoContainer deecoContainer;
	
	@Before
	public void setUp() throws Exception {
		scheduler = mock(Scheduler.class);
		executor = mock(Executor.class);
		kmContainer = mock(KnowledgeManagerContainer.class);
		ratingsManager = mock(RatingsManager.class);
		
		component = factory.createComponentInstance();		

		edefinition = factory.createEnsembleDefinition();
		edefinition.setName("dummyName");
		
		process = factory.createComponentProcess();
		econtroller = factory.createEnsembleController();
		econtroller.setEnsembleDefinition(edefinition);
		
		km = mock(KnowledgeManager.class);
		when(km.getId()).thenReturn("component");
				
		component.getComponentProcesses().add(process);
		component.getEnsembleControllers().add(econtroller);
		component.setKnowledgeManager(km);
		
		model = factory.createRuntimeMetadata();
		model.getComponentInstances().add(component);
		model.getEnsembleDefinitions().add(edefinition);
		
		kmReplacement = new BaseKnowledgeManager("component", component, null);
		when(kmContainer.createLocal(anyString(), anyObject(), any())).thenReturn(kmReplacement);		
		
		spy = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		RuntimeLogger logger = mock(RuntimeLogger.class);
		
		deecoContainer = mock(DEECoContainer.class);
		Mockito.when(deecoContainer.getRuntimeFramework()).thenAnswer(new Answer<RuntimeFramework>() {
		    @Override
		    public RuntimeFramework answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return spy;
		    }
		  });
		Mockito.when(deecoContainer.getRuntimeLogger()).thenAnswer(new Answer<RuntimeLogger>() {
		    @Override
		    public RuntimeLogger answer(InvocationOnMock invocation) throws Throwable 
		    {
		      return logger;
		    }
		  });

		spy.init(deecoContainer);
		
	}

	
	
	@Test
	public void testRuntimeFrameworkImplNotNull() {
		// GIVEN valid model, scheduler, and executor	
		// WHEN a new RuntimeFrameworkImpl is created
		RuntimeFrameworkImpl result = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager);
		// THEN the constructor finishes successfully
		assertNotNull(result);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullModel() {		
		// WHEN a new RuntimeFrameworkImpl is created with a null model
		new RuntimeFrameworkImpl(null, scheduler, executor, kmContainer, ratingsManager);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}

	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullScheduler() {
		// WHEN a new RuntimeFrameworkImpl is created with a null scheduler
		new RuntimeFrameworkImpl(model, null, executor, kmContainer, ratingsManager);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}

	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullExecutor() {
		// WHEN a new RuntimeFrameworkImpl is created with a null executor
		new RuntimeFrameworkImpl(model, scheduler, null, kmContainer, ratingsManager);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}

	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testRuntimeFrameworkImplNullKnowledgeRepositoryContainer() {
		// WHEN a new RuntimeFrameworkImpl is created with a null KM container
		new RuntimeFrameworkImpl(model, scheduler, executor, null, ratingsManager);
		// THEN the constructor throws IllegalArgumentException
		fail("Exception expected");
	}
	
	@Test
	public void testInitComponentInstancesAdapterPresent() {
		
		// GIVEN a model with no adapters  and a non-initialized runtime
		model.eAdapters().clear();
		assertEquals(0, model.eAdapters().size());			
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager);

		// WHEN init() is called on the runtime
		tested.init(deecoContainer);
		
		// THEN the runtime sets up an adapter to observe changes of the list of
		// component instances
		assertEquals(1, model.eAdapters().size());				
	}
	
	
	
	@Test
	public void testInit0ComponentInstanceAdded() {
		// GIVEN a model with no component instances and a non-initialized runtime 		
		model.getComponentInstances().clear();		
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN when init is called() on the runtime		
		tested.init(deecoContainer);
		
		// THEN the callback componentInstanceAdded is not called 
		verify(tested, never()).componentInstanceAdded(any(ComponentInstance.class));		
	}
	
	@Test
	public void testInit1ComponentInstanceAdded() {
		// GIVEN a model with one component instance and a non-initialized runtime 	
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN when init is called() on the runtime
		tested.init(deecoContainer);
		
		// THEN the component is added via the callback componentInstanceAdded 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verify(tested).componentInstanceAdded(component);
	}
	
	@Test
	public void testInit2ComponentInstanceAdded() {
		// GIVEN a model with two component instances and a non-initialized runtime 	
		ComponentInstance component2 = EcoreUtil.copy(component);
		model.getComponentInstances().add(component2);
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
	
		// WHEN when init is called() on the runtime
		tested.init(deecoContainer);		
		
		// THEN the components are all added via the callback componentInstanceAdded 
		verify(tested, times(2)).componentInstanceAdded(any(ComponentInstance.class));
		verify(tested).componentInstanceAdded(component);
		verify(tested).componentInstanceAdded(component2);
		verify(tested, times(2)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
	}
	
	/* ************************************************************************
	 * componentInstanceAdded
	 * ***********************************************************************/	
	
	@Test
	public void testComponentInstanceAddedNull() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding a null component instance
		tested.componentInstanceAdded(null);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentInstanceAddedExisting() {
		// GIVEN a runtime initialized with a component instance
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		tested.init(deecoContainer);
		reset(tested);
		
		// WHEN adding the already added component instance
		tested.componentInstanceAdded(component);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}	
	
	
	
	@Test
	public void testComponentInstanceAddedCreatesNewComponentRecord() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager);
		
		// WHEN a valid component instance is added
		tested.componentInstanceAdded(component);		
		
		// THEN a new record will be created in componentRecords collection
		assertNotNull(tested.componentRecords.get(component));		
	}
	
	@Test
	public void testComponentInstanceAdded0ProcessesAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding a component instance with zero processes
		component.getComponentProcesses().clear();
		tested.componentInstanceAdded(component);
		
		// THEN the componentProcessAdded is not called
		verify(tested, never()).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
	}
	
	@Test
	public void testComponentInstanceAdded2ProcessesAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding a component instance with three processes
		ComponentProcess process2 = EcoreUtil.copy(process);
		component.getComponentProcesses().add(process2);
		tested.componentInstanceAdded(component);
		
		// THEN the componentProcessAdded is called for each of the processes
		verify(tested, times(2)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
		verify(tested).componentProcessAdded(component, process);
		verify(tested).componentProcessAdded(component, process2);
	}
	
	@Test
	public void testComponentInstanceAdded0ControllersAdded() {
		// GIVEN a non-initialized runtime 
		Scheduler testScheduler = mock(Scheduler.class);
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, testScheduler, executor, kmContainer, ratingsManager);
		
		// WHEN adding a component instance with zero ensemble controllers and zero processes
		component.getComponentProcesses().clear();
		component.getEnsembleControllers().clear();
		tested.componentInstanceAdded(component);
		
		// THEN no ensembling tasks are created/scheduled
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		assertEquals(0, cir.getEnsembleTasks().size());		
		verify(testScheduler, never()).addTask(any(Task.class));
	}
	
	@Test
	public void testComponentInstanceAdded2ControllersAdded() {
		Scheduler testScheduler = mock(Scheduler.class);
		// GIVEN a non-initialized runtime
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, testScheduler, executor, kmContainer, ratingsManager);
		
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
		verify(testScheduler, times(2)).addTask(any(Task.class));
		verify(testScheduler).addTask(t);
		verify(testScheduler).addTask(t2);
	}
	
	@Test
	public void testComponentInstanceAddedAdapterPresent() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager);		
		
		// WHEN adding a component instance		
		tested.componentInstanceAdded(component);
		
		// THEN the runtime registers an adapter observing changes of the instance
		Adapter a = tested.componentInstanceAdapters.get(component);
		assertNotNull(a);
		assertTrue(component.eAdapters().contains(a));
	}
	
	@Test
	public void KMReplacedWhenComponentInstanceAdded() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));		
		
		// WHEN adding a component instance		
		tested.componentInstanceAdded(component);
		
		// THEN the runtime replaces the KM in the model by a new one
		verify(tested).replaceKnowledgeManager(component);
	}
	
	@Test
	public void callbackCalledWhenComponentAddedToModel() {
		// GIVEN an initialized runtime, where a dedicated spy listens to calls to componentInstanceAdded
		reset(spy);
			
		// WHEN a new component is added to the model
		ComponentInstance ci2 = EcoreUtil.copy(component);
		model.getComponentInstances().add(ci2);
		
		// THEN the componentInstanceAdded is called on the runtime
		verify(spy).componentInstanceAdded(ci2);
		// AND no errors were printed
		assertTrue(log.getLog().isEmpty());
	}
	
	/* ************************************************************************
	 * componentInstanceRemoved
	 * ***********************************************************************/	
	
	@Test
	public void testComponentInstanceRemovedNull() {
		// GIVEN a runtime that uses a model with one component instance
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing a null component instance
		tested.componentInstanceRemoved(null);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceRemoved(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentInstanceRemovedNonexisting() {
		// GIVEN a runtime that uses a model with one component instance
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing a non-existing component instance
		tested.componentInstanceRemoved(mock(ComponentInstance.class));		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceRemoved(any(ComponentInstance.class));
		verifyNoMoreInteractions(tested);
	}	
	
	@Test
	public void testComponentInstanceRemovedExisting() {
		// GIVEN a runtime that uses a model with a component instance 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing an existing component instance
		tested.componentInstanceRemoved(component);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentInstanceRemoved(any(ComponentInstance.class));
	}	
	
	@Test
	public void testComponentInstanceRemovedDeletesComponentRecord() {
		// GIVEN a runtime that uses a model with a component instance 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		tested.init(deecoContainer);
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
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));

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
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		tested.init(deecoContainer);
		reset(tested);
				
		// WHEN removing a component instance with two processes		
		tested.componentInstanceRemoved(component);
		
		// THEN the componentProcessRemoved is called for each of the processes
		verify(tested, times(2)).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));
		verify(tested).componentProcessRemoved(component, process);
		verify(tested).componentProcessRemoved(component, process2);
	}
	
	@Test
	public void testComponentInstanceRemoved0ControllersRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		// having no ensemble controllers and zero processes
		component.getComponentProcesses().clear();
		component.getEnsembleControllers().clear();
		
		Scheduler testScheduler = mock(Scheduler.class);
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, testScheduler, executor, kmContainer, ratingsManager));
		tested.init(deecoContainer);
		
		// WHEN removing a component instance with zero ensemble controllers and zero processes		
		tested.componentInstanceRemoved(component);
		
		// THEN no ensembling tasks are removed from scheduler
		verify(testScheduler, never()).removeTask(any(Task.class));
	}
	
	@Test
	public void testComponentInstanceRemoved2ControllersRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		// having two ensemble controllers and zero processes
		EnsembleController econtroller2 = EcoreUtil.copy(econtroller);
		component.getComponentProcesses().clear();
		component.getEnsembleControllers().add(econtroller2);
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		tested.init(deecoContainer);
		reset(scheduler);
	
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		
		// WHEN removing the component instance 
		tested.componentInstanceRemoved(component);
		
		// THEN the tasks of all controllers are removed from scheduler
		Task t = cir.getEnsembleTasks().get(econtroller);
		Task t2 = cir.getEnsembleTasks().get(econtroller2);
		verify(scheduler, times(2)).removeTask(any(Task.class));
		verify(scheduler).removeTask(t);
		verify(scheduler).removeTask(t2);
	}
	
	@Test
	public void testComponentInstanceRemovedAdapterRemoved() {
		// GIVEN a runtime that uses a model with a component instance 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		Adapter a = tested.componentInstanceAdapters.get(component);
				
		
		// WHEN removing the component instance		
		tested.componentInstanceRemoved(component);
		
		// THEN the runtime unregisters its adapter observing changes of the instance
		assertFalse(component.eAdapters().contains(a));
		assertNull(tested.componentInstanceAdapters.get(component));
	}
	
	@Test
	public void callbackCalledWhenComponentRemovedFromModel() {
		// GIVEN an initialized runtime, where a dedicated spy listens to calls to componentInstanceRemoved
		reset(spy);
			
		// WHEN an existing component is removed from the model		
		model.getComponentInstances().remove(component);
		
		// THEN the componentInstanceRemoved is called on the runtime
		verify(spy).componentInstanceRemoved(component);
		// AND no errors were printed
		assertTrue(log.getLog().isEmpty());
	}
	
	
	/* ************************************************************************
	 * componentProcessAdded
	 * ***********************************************************************/
	
	@Test
	public void testComponentProcessAddedNullInstance() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding a process of a null component instance
		tested.componentProcessAdded(null, process);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentProcessAddedNullProcess() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding a null process of a component instance
		tested.componentProcessAdded(component, null);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentProcessAddedNonExisting() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding a process to an unregistered component instance
		tested.componentProcessAdded(component, process);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}	
	
	@Test
	public void testComponentProcessAddedExisting() {
		// GIVEN an initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN adding an already-added process to a component instance
		tested.componentProcessAdded(component, process);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessAdded(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}	
	
	@Test
	public void testComponentProcessAddedValid() {
		// GIVEN an initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));

		tested.init(deecoContainer);
				
		// WHEN adding a process to a registered component instance
		ComponentProcess process2 = EcoreUtil.copy(process);
		int numAdapters = process2.eAdapters().size();
		tested.componentProcessAdded(component, process2);		
		
		// THEN a new ProcessTask is created and added to the instance's ComponentInstanceRecord 
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		assertNotNull(cir.getProcessTasks().get(process2));
		// AND a new ecore adapter is added to the new process
		assertEquals(numAdapters+1, process2.eAdapters().size());
		assertNotNull(tested.componentProcessAdapters.get(process2));
		assertTrue(process2.eAdapters().contains(tested.componentProcessAdapters.get(process2)));
		// AND componentProcessActiveChanged was called on the process
		verify(tested).componentProcessActiveChanged(component, process2, process.isActive());
	}	
	
	@Test
	public void testComponentProcessAddedInactive() {
		// GIVEN a non-initialized runtime			
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN the runtime is initialized with a model having a component with
		// one active and one inactive process
		ComponentProcess process2 = EcoreUtil.copy(process);
		process2.setActive(false);
		component.getComponentProcesses().add(process2);
		process.setActive(true);
		tested.init(null);		

		@SuppressWarnings("unused")
		ComponentInstanceRecord unused = tested.componentRecords.get(component);
		
		// THEN componentProcessActiveChanged is called with the right arguments
		verify(tested).componentProcessActiveChanged(component, process, true);
		verify(tested).componentProcessActiveChanged(component, process2, false);
	}
	
	@Test
	public void callbackCalledWhenProcessAddedToModel() {
		// GIVEN an initialized runtime, where a dedicated spy listens to calls to componentProcessAdded
		reset(spy);
			
		// WHEN a new process is added to the model
		ComponentProcess cp2 = EcoreUtil.copy(process);
		component.getComponentProcesses().add(cp2);
		
		// THEN the componentProcessAdded is called on the runtime
		verify(spy).componentProcessAdded(component, cp2);
		// AND no errors were printed
		assertTrue(log.getLog().isEmpty());
	}
	
	/* ************************************************************************
	 * componentProcessRemoved
	 * ***********************************************************************/
	
	@Test
	public void testComponentProcessRemovedNullInstance() {
		// GIVEN an initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing a process from a null component instance
		tested.componentProcessRemoved(null, process);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentProcessRemovedNullProcess() {
		// GIVEN an initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing a null process from a component instance
		tested.componentProcessRemoved(component, null);
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}
	
	@Test
	public void testComponentProcessRemovedNonExistingInstance() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing a process from an unregistered component instance
		tested.componentProcessRemoved(component, process);		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}	
	
	@Test
	public void testComponentProcessRemovedNonExistingProcess() {
		// GIVEN an initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		
		// WHEN removing a non-registered process from a registered component instance
		tested.componentProcessRemoved(component, mock(ComponentProcess.class));		
		
		// THEN nothing happens 
		verify(tested, times(1)).componentProcessRemoved(any(ComponentInstance.class), any(ComponentProcess.class));
		verifyNoMoreInteractions(tested);
	}	
	
	@Test
	public void testComponentProcessRemovedValid() {
		// GIVEN an initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		tested.init(deecoContainer);
		ComponentInstanceRecord cir = tested.componentRecords.get(component);
		int numAdapters = process.eAdapters().size();
		
		// WHEN removing a process from a registered component instance		
		tested.componentProcessRemoved(component, process);		
		
		// THEN the process is deactivated 
		verify(tested).componentProcessActiveChanged(component, process, false); 
		// AND the associated task is removed from the ComponentInstanceRecord
		assertFalse(cir.getProcessTasks().containsKey(process));
		// AND a the ecore adapter is removed to the process
		assertEquals(numAdapters-1, process.eAdapters().size());
		assertFalse(tested.componentProcessAdapters.containsKey(process));
	}	
	
	
	@Test
	public void callbackCalledWhenProcessRemovedFromModel() {
		// GIVEN an initialized runtime, where a dedicated spy listens to calls to componentProcessRemoved
		reset(spy);
			
		// WHEN a process is removed from the model
		component.getComponentProcesses().remove(process);
		
		// THEN the componentProcessRemoved is called on the runtime
		verify(spy).componentProcessRemoved(component, process);
		// AND no errors were printed
		assertTrue(log.getLog().isEmpty());
	}
	
	
	/* ************************************************************************
	 * componentProcessActiveChanged
	 * ***********************************************************************/
	
	@Test
	public void testComponentProcessActiveChangedNullProcess() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		reset(scheduler);
		
		// WHEN changing the activity of a null process 
		tested.componentProcessActiveChanged(component, null, false);
		
		// THEN nothing happens
		verifyZeroInteractions(scheduler);		
	}
		
	
	@Test
	public void testComponentProcessActiveChangedUnregisteredInstance() {
		// GIVEN a non-initialized runtime 
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));
		reset(scheduler);
		
		// WHEN changing the activity of a process whose instance is not registered
		tested.componentProcessActiveChanged(component, process, false);
		
		// THEN nothing happens
		verifyZeroInteractions(scheduler);		
	}
	
	@Test
	public void testComponentProcessActiveChangedUnregisteredProcess() {
		// GIVEN an initialized runtime
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));

		tested.init(deecoContainer);
		reset(scheduler);
		
		// WHEN changing the activity of an unregistered process
		tested.componentRecords.get(process.getComponentInstance()).getProcessTasks().remove(process);
		tested.componentProcessActiveChanged(component, process, false);
		
		// THEN nothing happens
		verifyZeroInteractions(scheduler);		
	}
	
	@Test
	public void testComponentProcessActiveActivate() {
		// GIVEN an initialized runtime
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));

		tested.init(deecoContainer);
		reset(scheduler);
		Task processTask = tested.componentRecords.get(component).getProcessTasks().get(process);
		
		// WHEN changing the activity of a registered process to true		
		tested.componentProcessActiveChanged(component, process, true);
		
		// THEN the task associated with process gets scheduled
		verify(scheduler).addTask(processTask);		
	}
	
	@Test
	public void testComponentProcessActiveDeactivate() {
		// GIVEN an initialized runtime
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, ratingsManager));

		tested.init(deecoContainer);
		reset(scheduler);
		
		Task processTask = tested.componentRecords.get(component).getProcessTasks().get(process);

		tested.init(deecoContainer);
		
		// WHEN changing the activity of a registered process to false		
		tested.componentProcessActiveChanged(component, process, false);
		
		// THEN the task associated with process is removed from the scheduler
		verify(scheduler).removeTask(processTask);		
	}
	
	@Test
	public void callbackCalledWhenProcessActiveChanged() {
		reset(spy);

			
		// WHEN a process is set to active
		process.setActive(true);
		
		// THEN the componentProcessActiveChanged is called on the runtime
		verify(spy).componentProcessActiveChanged(component, process, true);
		// AND no errors were printed
		assertTrue(log.getLog().isEmpty());
		
		// WHEN a process is set to inactive
		process.setActive(false);
		
		// THEN the componentProcessActiveChanged is called on the runtime
		verify(spy).componentProcessActiveChanged(component, process, false);
		// AND no errors were printed
		assertTrue(log.getLog().isEmpty());
	}
	
	
	/* ************************************************************************
	 * replaceKnowledgeManager
	 * ***********************************************************************/	
	
	@Test
	public void replaceKnowledgeManagerReplacesTheKM() {	
		// GIVEN a non-initialized runtime
		KnowledgeManagerContainer testContainer = mock(KnowledgeManagerContainer.class);
		when(testContainer.createLocal(anyString(), anyObject(), any())).thenReturn(kmReplacement);	
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, testContainer, ratingsManager);	
		
		// WHEN the replaceKnowledgeManager is called on an instance
		tested.replaceKnowledgeManager(component);
		
		// THEN the KM of the instance is replaced by a new one created by kmContainer for the instance
		verify(kmContainer).createLocal(km.getId(), component, km.getRoles());

		assertEquals(kmReplacement, component.getKnowledgeManager());		
	}
	
	@Test
	public void replaceKnowledgeManagerCopiesTheContentsOfTheKM() throws KnowledgeNotFoundException {		
		KnowledgePath kp = factory.createKnowledgePath();
		PathNodeField pn = factory.createPathNodeField();
		pn.setName("field");
		kp.getNodes().add(pn);
		Object knowledgeValue = new Object();
		ValueSet vs = new ValueSet();
		vs.setValue(kp, knowledgeValue);		
		
		when(km.get(anyCollectionOf(KnowledgePath.class))).thenReturn(vs);		
		when(km.getKnowledgeSecurityTags(anyObject())).thenReturn(new ArrayList<>());
		
		// GIVEN a non-initialized runtime
		KnowledgeManagerContainer testContainer = mock(KnowledgeManagerContainer.class);
		when(testContainer.createLocal(anyString(), anyObject(), any())).thenReturn(kmReplacement);
		
		EnsembleDefinition testEDefinition = factory.createEnsembleDefinition();
		testEDefinition.setName("dummyName");
		
		EnsembleController testEController = factory.createEnsembleController();
		testEController.setEnsembleDefinition(testEDefinition);
		

		ComponentInstance testComponent = factory.createComponentInstance();	
		
		RuntimeMetadata testModel = factory.createRuntimeMetadata();
		testModel.getComponentInstances().add(testComponent);
		testModel.getEnsembleDefinitions().add(testEDefinition);

		testComponent.getComponentProcesses().add(process);
		testComponent.getEnsembleControllers().add(testEController);
		testComponent.setKnowledgeManager(km);
		
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(testModel, scheduler, executor, testContainer, ratingsManager);	
		
		// WHEN the replaceKnowledgeManager is called on an instance
		tested.replaceKnowledgeManager(testComponent);
		
		// THEN the new KM of the instance is initialized with the values from the previous one		
		assertEquals(knowledgeValue, testComponent.getKnowledgeManager().get(Arrays.asList(kp)).getValue(kp));
	}
}

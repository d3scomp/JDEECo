package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
		// GIVEN a model with no adapters 
		assertEquals(0, model.eAdapters().size());			
		// WHEN init() is called on a properly-constructed runtime
		RuntimeFrameworkImpl tested = new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false);
		tested.init();
		// THEN the runtime sets up an adapter to observe changes of the list of
		// component instances
		assertEquals(1, model.eAdapters().size());				
	}
	
	
	
	@Test
	public void testInitComponentInstanceAddedEmpty() {
		// GIVEN a model with no component instance 
		// 
		model.getComponentInstances().clear();		
		
		// WHEN when init is called() on a properly-constructed runtime
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		tested.init();
		
		// THEN the callback componentInstanceAdded is not called 
		verify(tested, never()).componentInstanceAdded(any(ComponentInstance.class));		
	}
	
	@Test
	public void testInitComponentInstanceAddedOne() {
		// GIVEN a model with one component instance
		model.getComponentInstances().clear();
		model.getComponentInstances().add(component);
		
		// WHEN when init is called() on a properly-constructed runtime
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		tested.init();
		
		// THEN the component is added via the callback componentInstanceAdded 
		verify(tested, times(1)).componentInstanceAdded(any(ComponentInstance.class));
		verify(tested).componentInstanceAdded(component);
	}
	
	@Test
	public void testInitComponentInstanceAddedThree() {
		// GIVEN a model with three component instances
		model.getComponentInstances().clear();		
		ComponentInstance component2 = EcoreUtil.copy(component);
		ComponentInstance component3 = EcoreUtil.copy(component);
		model.getComponentInstances().add(component);
		model.getComponentInstances().add(component2);
		model.getComponentInstances().add(component3);
	
		// WHEN when init is called() on a properly-constructed runtime
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer, false));
		tested.init();		
		
		// THEN the components are all added via the callback componentInstanceAdded 
		verify(tested, times(3)).componentInstanceAdded(any(ComponentInstance.class));
		verify(tested).componentInstanceAdded(component);
		verify(tested).componentInstanceAdded(component2);
		verify(tested).componentInstanceAdded(component3);
	}
	
	
	
}

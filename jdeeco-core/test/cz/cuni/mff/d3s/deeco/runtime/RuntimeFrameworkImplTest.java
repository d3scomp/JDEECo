package cz.cuni.mff.d3s.deeco.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.emf.common.util.EList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.executor.Executor;
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
	}

	
	@Test
	public void testInitComponentInstanceAdapter() {
		// WHEN a runtime with proper configuration and model is created
		RuntimeFrameworkImpl tested = spy(new RuntimeFrameworkImpl(model, scheduler, executor, kmContainer));
		// THEN the runtime sets up an adaptor to observe changes of the list of
		// component instances
		assertEquals(1, model.eAdapters().size());		
	}

}

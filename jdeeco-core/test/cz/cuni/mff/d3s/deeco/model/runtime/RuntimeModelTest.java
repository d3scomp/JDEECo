/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.api.Component;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SchedulingSpecification;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class RuntimeModelTest {

	RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE; 
	}

	@Test
	public void testExtensions() {
		// THEN RuntimeMetadataFactory is an instance of RuntimeMetadataFactoryExt (i.e. our custom class)
		assertTrue(factory instanceof RuntimeMetadataFactoryExt);
	}
	
	public static void dummyMethodThatStandsForAProcess() {
	}

	@Test
	@Ignore
	public void testSaveAndLoad() throws Exception {
		// WHEN a RuntimeMetadata instance exists
		// AND it contains a method (within an Invocable)
		// AND it contains a a knowledge manager with some knowledge
		RuntimeMetadata model = factory.createRuntimeMetadata();
		
		ComponentInstance instA = factory.createComponentInstance();
		model.getComponentInstances().add(instA);
		instA.setId("an instance of component A");		
		instA.setKnowledgeManager(null); // TODO: add a knowledge manager with some knowledge
		
		Component compA = factory.createComponent();
		model.getComponents().add(compA);
		instA.setComponent(compA);
		compA.setName("component A");
		
		Process procA1 = factory.createProcess();
		compA.getProcesses().add(procA1);
		procA1.setName("process #1 of component A");
		procA1.setMethod(this.getClass().getMethod("dummyMethodThatStandsForAProcess"));
		
		SchedulingSpecification procA1Sched = factory.createSchedulingSpecification();
		procA1Sched.setPeriod(42);
		procA1.setSchedule(procA1Sched);
	
		// THEN the RuntimeMetadata instance can be saved
		ResourceSet resourceSet = new ResourceSetImpl();

		// This needs to be uncommented, but for that it needs a dependency on XMI
		// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		
		File testXMIFile = new File("test-temp/test.xmi");
		URI fileURI = URI.createFileURI(testXMIFile.getAbsolutePath());
		Resource resource = resourceSet.createResource(fileURI);
		resource.getContents().add(model);
		resource.save(Collections.EMPTY_MAP);
		assertTrue(testXMIFile.exists());

		// WHEN a RuntimeMetadata instance with a method is loaded
		resourceSet = new ResourceSetImpl();
		
		// This needs to be uncommented, but for that it needs a dependency on XMI
		// resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		
		RuntimeMetadata loadedModel = (RuntimeMetadata)resourceSet.getResource(fileURI, true).getContents().get(0);
		
		// THEN it has the same values
		// AND refers to the same method within the same class as before
		// AND contains the same knowledge as before
		ComponentInstance loadedInstA = loadedModel.getComponentInstances().get(0);
		Component loadedCompA = loadedModel.getComponents().get(0);
		Process loadedProcA1 = loadedCompA.getProcesses().get(0);
		SchedulingSpecification loadedProcA1Sched = loadedProcA1.getSchedule(); 
		assertEquals(instA.getId(), loadedInstA.getId());
		assertEquals(instA.getKnowledgeManager(), loadedInstA.getKnowledgeManager());
		assertEquals(loadedCompA, instA.getComponent());
		assertEquals(compA.getName(), loadedCompA.getName());
		assertEquals(procA1.getName(), loadedProcA1.getName());
		assertEquals(procA1.getMethod(), loadedProcA1.getMethod());
		assertEquals(procA1Sched.getPeriod(), loadedProcA1Sched.getPeriod());
		// TODO: Check the knowledge manager
	}
}

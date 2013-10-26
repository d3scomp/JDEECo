/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Component;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.InvocableExt;
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
		
		// WHEN an Invocable is created by the RuntimeMetadataFactory
		Invocable invocable = factory.createInvocable();
		// THEN it is an instance of InvocableExt
		assertTrue(invocable instanceof InvocableExt);
	}
	
	@Test
	@Ignore
	public void testSaveAndLoad() {
		// WHEN a RuntimeMetadata instance contains a knowledge manager (with some knowledge inside)
		// THEN the RuntimeMetadata instance can be saved
		RuntimeMetadata model = factory.createRuntimeMetadata();
		
		ComponentInstance instA = factory.createComponentInstance();
		instA.setId("an instance of component A");

		KnowledgeManager instAKM = null; // TODO: creat a new knowledge manager here
		instA.setKnowledgeManager(instAKM);
		
		Component compA = factory.createComponent();
		compA.setName("component A");
		
		Process procA1 = factory.createProcess();
//		procA1.setMethod(value)
	
		// TODO: Complete the test...

		
		// WHEN a RuntimeMetadata instance with some knowledge is saved 
		// THEN the RuntimeMetadata instance can be loaded
		// AND it will contain the same values

		// TODO: Complete the test...

		fail("Not yet implemented");
	}

}

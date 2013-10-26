/**
 * 
 */
package cz.cuni.mff.d3s.deeco.model.runtime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 *
 */
public class RuntimeModelTest {

	RuntimeMetadata model;
	RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE; 
		model = factory.createRuntimeMetadata();
//		model.
	}

	@Test
	@Ignore
	public void test() {
		// GIVEN a RuntimeMetadata instance
		// THEN a RuntimeMetadata instance can be saved

		// WHEN a RuntimeMetadata instance is saved
		// THEN a RuntimeMetadata instance with the same values can be loaded

		fail("Not yet implemented");
	}

}

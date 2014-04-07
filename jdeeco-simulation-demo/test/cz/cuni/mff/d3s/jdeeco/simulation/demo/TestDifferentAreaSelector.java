package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.DeecoProperties;


public class TestDifferentAreaSelector extends TestAreaBase {
		
	protected DifferentAreaSelector tested;

	@Before
	public void setUp() throws Exception {
		init();
		AreaNetworkRegistry.INSTANCE.initialize(areas);
		for (PositionAwareComponent c: components)
			AreaNetworkRegistry.INSTANCE.addComponent(c);	
		
		tested = new DifferentAreaSelector();
		tested.initialize(AreaNetworkRegistry.INSTANCE);		
	}	
	
	@After
	public void tearDown() {
		AreaNetworkRegistry.INSTANCE.clear();
	}	
	
	@Test
	public void testGetRecipientsForTeam(){		
		assertContainsAll(Arrays.<String>asList("M01", "M1", "M2"), tested.getRecipientsForTeam("T0"));
		assertContainsAll(Arrays.<String>asList("M01", "M1"), tested.getRecipientsForTeam("T1"));
		assertContainsAll(Arrays.<String>asList("M2"), tested.getRecipientsForTeam("T2"));
		assertContainsAll(Arrays.<String>asList("M0"), tested.getRecipientsForTeam("T3"));
	}

	@Test
	public void testWhenBoundaryDisabled(){
		System.setProperty(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS, "true");
		tested.initialize(AreaNetworkRegistry.INSTANCE);
		
		assertContainsAll(Arrays.<String>asList("M01", "M1", "M2", "M0"), tested.getRecipientsForTeam("T0"));
		assertContainsAll(Arrays.<String>asList("M01", "M1", "M2", "M0"), tested.getRecipientsForTeam("T1"));
		assertContainsAll(Arrays.<String>asList("M01", "M1", "M2", "M0"), tested.getRecipientsForTeam("T2"));
		assertContainsAll(Arrays.<String>asList("M01", "M1", "M2", "M0"), tested.getRecipientsForTeam("T3"));
	}

	
	
	
	
}

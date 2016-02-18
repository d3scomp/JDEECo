package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.DeecoProperties;


public class TestAreaBasedSelector extends TestAreaBase {
		
	protected AreaBasedSelector tested;

	@Before
	public void setUp() throws Exception {
		init();
		AreaNetworkRegistry.INSTANCE.initialize(areas);
		for (PositionAwareComponent c: components)
			AreaNetworkRegistry.INSTANCE.addComponent(c);	
		
		tested = new AreaBasedSelector();
		tested.initialize(AreaNetworkRegistry.INSTANCE);		
	}	
	
	@After
	public void tearDown() {
		AreaNetworkRegistry.INSTANCE.clear();
	}	
	
	@Test
	public void testGetRecipientsForTeam(){		
		assertReturnsExact();
	}

	void assertReturnsExact() {
		assertContainsAll(Arrays.<String>asList("M02", "M1", "M2"), tested.getRecipientsForTeam("T0"));
		assertContainsAll(Arrays.<String>asList("M02", "M1"), tested.getRecipientsForTeam("T1"));
		assertContainsAll(Arrays.<String>asList("M02", "M2"), tested.getRecipientsForTeam("T2"));
		assertContainsAll(Arrays.<String>asList("M0"), tested.getRecipientsForTeam("T3"));
	}

	@Test
	public void testWhenBoundaryDisabled(){
		System.setProperty(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS, "true");
		System.setProperty(AreaBasedSelector.IP_BOUNDARY_ALWAYS_ON, "false");
		tested.initialize(AreaNetworkRegistry.INSTANCE);
		
		assertReturnsAll();
	}
	
	@Test
	public void testWhenAlwaysOn(){
		System.setProperty(DeecoProperties.DISABLE_BOUNDARY_CONDITIONS, "true");
		System.setProperty(AreaBasedSelector.IP_BOUNDARY_ALWAYS_ON, "true");

		tested.initialize(AreaNetworkRegistry.INSTANCE);
		
		
		assertReturnsExact();
	}

	void assertReturnsAll() {
		assertContainsAll(Arrays.<String>asList("M02", "M1", "M2", "M0"), tested.getRecipientsForTeam("T0"));
		assertContainsAll(Arrays.<String>asList("M02", "M1", "M2", "M0"), tested.getRecipientsForTeam("T1"));
		assertContainsAll(Arrays.<String>asList("M02", "M1", "M2", "M0"), tested.getRecipientsForTeam("T2"));
		assertContainsAll(Arrays.<String>asList("M02", "M1", "M2", "M0"), tested.getRecipientsForTeam("T3"));
	}

	
	
	
	
}

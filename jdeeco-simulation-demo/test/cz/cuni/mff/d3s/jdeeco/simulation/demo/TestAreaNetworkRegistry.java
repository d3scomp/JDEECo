package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.jdeeco.simulation.demo.Position;


public class TestAreaNetworkRegistry extends TestAreaBase {
		
	protected AreaNetworkRegistry tested;

	@Before
	public void setUp() throws Exception {
		init();
		
		tested = AreaNetworkRegistry.INSTANCE;
		tested.initialize(areas);
		for (PositionAwareComponent c: components)
			tested.addComponent(c);	
	}	
	@After
	public void tearDown() {
		tested.clear();
	}	
	
	@Test
	public void testGetAllComponents(){		
		assertContainsAll(components, tested.getAllComponents());
	}

	@Test
	public void testGetIpComponents(){		
		assertContainsAll(Arrays.<PositionAwareComponent>asList(m02, m1, m2, m0), tested.getIpEnabledComponents());
	}

	@Test
	public void testOutside(){		
		assertContainsAll(Arrays.<PositionAwareComponent>asList(m0), tested.getComponentsOutside());
	}

	@Test
	public void testInArea(){		
		assertContainsAll(Arrays.<PositionAwareComponent>asList(m01, m02, m1), tested.getComponentsInArea(a1));
		assertContainsAll(Arrays.<PositionAwareComponent>asList(m02, m03, m2), tested.getComponentsInArea(a2));
	}
	
	@Test
	public void testTeamSites(){		
		assertContainsAll(Arrays.<Area>asList(a1, a2), tested.getTeamSites("T0"));
		assertContainsAll(Arrays.<Area>asList(a1), tested.getTeamSites("T1"));
		assertContainsAll(Arrays.<Area>asList(a2), tested.getTeamSites("T2"));
		assertContainsAll(Arrays.<Area>asList(), tested.getTeamSites("T3"));
	}
	
	@Test
	public void testIsAtTheTeamsSite(){		
		assertTrue(tested.isAtTheTeamsSite("T0", new Position(1, 2)));
		assertTrue(tested.isAtTheTeamsSite("T0", new Position(1, 60)));
		assertTrue(tested.isAtTheTeamsSite("T0", new Position(2, 130)));
		assertFalse(tested.isAtTheTeamsSite("T0", new Position(101, 151)));
		
		assertTrue(tested.isAtTheTeamsSite("T1", new Position(1, 2)));
		assertTrue(tested.isAtTheTeamsSite("T1", new Position(1, 60)));
		assertFalse(tested.isAtTheTeamsSite("T1", new Position(2, 130)));
		assertFalse(tested.isAtTheTeamsSite("T1", new Position(101, 151)));
		
		assertFalse(tested.isAtTheTeamsSite("T2", new Position(1, 2)));
		assertTrue(tested.isAtTheTeamsSite("T2", new Position(1, 60)));
		assertTrue(tested.isAtTheTeamsSite("T2", new Position(2, 130)));
		assertFalse(tested.isAtTheTeamsSite("T2", new Position(101, 151)));
		
		assertFalse(tested.isAtTheTeamsSite("T2", new Position(1, 2)));
		assertTrue(tested.isAtTheTeamsSite("T2", new Position(1, 60)));
		assertTrue(tested.isAtTheTeamsSite("T2", new Position(2, 130)));
		assertFalse(tested.isAtTheTeamsSite("T2", new Position(101, 151)));		
	}
	
	
	
}

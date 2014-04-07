package cz.cuni.mff.d3s.jdeeco.simulation.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestAreaBase {

	protected Set<Area> areas;
	protected List<PositionAwareComponent> components;
	protected Member m01;
	protected Member m02;
	protected Member m03;
	protected Member m1;
	protected Member m2;
	protected Member m0;
	protected Area a1;
	protected Area a2;

	public TestAreaBase() {
		super();
	}
	
	protected void init() {
		a1 = new RectangularArea("A1", 0, 0, 100, 100, new String[]{"T0", "T1"});
		a2 = new RectangularArea("A2", 0, 50, 100, 100, new String[]{"T0", "T2"});
		areas = new HashSet<>(Arrays.<Area>asList(a1,a2)); 
		
		m01 = new Member("M01", "T0", new Position(1, 1), true);
		m02 = new Member("M02", "T0", new Position(1, 60), false);
		m03 = new Member("M03", "T0", new Position(1, 120), false);
		m1 = new Member("M1", "T1", new Position(1, 1), true);
		m2 = new Member("M2", "T2", new Position(1, 120), true);
		m0 = new Member("M0", "T3", new Position(200, 200), true);
		
		components = Arrays.<PositionAwareComponent>asList(m01, m02, m03, m1, m2, m0);
	}
	
	public <T> void assertContainsAll(Collection<T> expected, Collection<T> actual) {
		assertEquals(expected.size(), actual.size());
		
		Collection<T> notFound = new ArrayList<>();
		for (T item: expected) {
			if (!actual.contains(item))
				notFound.add(item);
		}
		assertTrue(String.format("Items <%s> not found in <%s>.",
				Arrays.deepToString(notFound.toArray()), Arrays.deepToString(actual.toArray())), 
				notFound.isEmpty());
	}

}
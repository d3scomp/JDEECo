package cz.cuni.mff.d3s.jdeeco.position;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests position to position distance calculation
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PositionTest {
	final double PRECISION = 0.01; 
	
	@Test
	public void testEuclidDistanceSimple() {
		Position a = new Position(0, 0, 0);
		Position b = new Position(3, 4, 0);
		
		assertEquals(5, a.euclidDistanceTo(b), PRECISION);
	}
	
	@Test
	public void testEuclidDistance3d() {
		Position a = new Position(0, 0, 0);
		Position b = new Position(3, 4, 12);
		
		assertEquals(13, a.euclidDistanceTo(b), PRECISION);
	}
	
	@Test
	public void testEuclidDistanceComplex() {
		Position a = new Position(-1, 0, 4);
		Position b = new Position(2, -4, -8);
		
		assertEquals(13, a.euclidDistanceTo(b), PRECISION);
	}
}

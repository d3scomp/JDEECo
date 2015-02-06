package cz.cuni.mff.d3s.jdeeco.network;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.jdeeco.network.l2.Layer;

/**
 * Tests for layer 2 jDEECo networking
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class L2Tests {
	Layer l2Layer;
	
	/**
	 * Initializes L2 layer
	 */
	@Before
	void initializeL2() {
		l2Layer = new Layer();
	}
	
	/**
	 * Tests L2 packet marshaling availability and consistency 
	 */
	@Test
	void testL2PacketMarshalling() {
		
	}
	
}

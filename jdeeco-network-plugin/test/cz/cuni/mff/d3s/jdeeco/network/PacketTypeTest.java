package cz.cuni.mff.d3s.jdeeco.network;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests packet type behavior
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class PacketTypeTest {
	/**
	 * Tests all valid packet types
	 */
	@Test
	public void testValid() {
		for (int i = 0; i <= 0b1111; ++i) {
			PacketType type = new PacketType((byte) i);
			assertEquals(i, type.getValue());
		}
	}

	/**
	 * Tests invalid (negative) value to produce exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testInvalidNegative() {
		PacketType type = new PacketType((byte) -1);
		assertEquals(-1, type.getValue());
	}

	/**
	 * Tests invalid (too high) value to produce exception
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testInvalidHigh() {
		PacketType type = new PacketType((byte) 0b10000);
		assertEquals(0b10000, type.getValue());
	}
}

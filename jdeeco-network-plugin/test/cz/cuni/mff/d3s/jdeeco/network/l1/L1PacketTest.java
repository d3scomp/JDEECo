package cz.cuni.mff.d3s.jdeeco.network.l1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import cz.cuni.mff.d3s.jdeeco.network.exceptions.PacketTooBig;

/**
 * Tests the L1Packet methods
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class L1PacketTest {

	@Test
	public void encodeIntegerInto2BytesTest() {
		int testedValue = 10;
		byte [] expectedResult = new byte [] {10, 0};
		byte [] actualResult = L1Packet.encodeIntegerInto2Bytes(testedValue);
		assertTrue(Arrays.equals(expectedResult, actualResult));
	}
	
	@Test(expected = PacketTooBig.class)
	public void encodeIntegerInto2BytesOverflowTest() {
		int testedValue = 0xffff1;
		L1Packet.encodeIntegerInto2Bytes(testedValue);
	}
	
	@Test
	public void decodeIntegerFrom2BytesTest() {
		byte [] testedValue = new byte [] {10, 0};
		int expectedResult = 10;
		int actualResult = L1Packet.decodeIntegerFrom2Bytes(testedValue);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void encodeDecodeTest() {
		L1Packet before = new L1Packet(new byte[]{10}, (byte) 0, 12, 5, 100);
		byte [] bytes = before.getBytes();
		L1Packet after = L1Packet.fromBytes(bytes, 0);
		assertEquals(after.totalSize, before.totalSize);
		assertEquals(after.dataId, before.dataId);
		assertEquals(after.payloadSize, before.payloadSize);
		assertEquals(after.srcNode, before.srcNode);
		assertEquals(after.startPos, before.startPos);
		assertTrue(Arrays.equals(after.payload, before.payload));
	}

}

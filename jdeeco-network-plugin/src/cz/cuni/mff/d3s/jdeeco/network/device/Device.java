package cz.cuni.mff.d3s.jdeeco.network.device;

import cz.cuni.mff.d3s.jdeeco.network.address.Address;
import cz.cuni.mff.d3s.jdeeco.network.l1.Layer1;
import cz.cuni.mff.d3s.jdeeco.network.l1.ReceivedInfo;

/**
 * Represents a network interface card.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public abstract class Device {
	protected Layer1 layer1;

	/**
	 * Retrieves the ID of the device
	 * 
	 * @return id of the device
	 */
	public abstract String getId();

	/**
	 * Returns maximum transmission unit size in bytes.
	 * 
	 * @return maximum transmission unit size in bytes.
	 */
	public abstract int getMTU();

	/**
	 * Says whether the device can send to the given address.
	 * 
	 * @param address
	 *            network address
	 * @return true when the device is capable of sending data to the given address. False otherwise.
	 */
	public abstract boolean canSend(Address address);

	/**
	 * Sends data (given as an array of bytes) to the specified address.
	 * 
	 * @param data
	 *            data to be sent
	 * @param address
	 *            recipient address
	 */
	public abstract void send(byte[] data, Address address);

	/**
	 * Registers layer 1 instance for receiving data from device
	 * 
	 * @param layer
	 *            Layer to register
	 */
	public void registerL1(Layer1 layer1) {
		this.layer1 = layer1;
	}

	/**
	 * Passes data to upper layer
	 * 
	 * To be called whenever data is received from network.
	 * 
	 * @param data
	 *            L0 packet data
	 * @param srcAddress
	 *            source address
	 */
	protected void receive(byte[] data, ReceivedInfo info) {
		layer1.processL0Packet(data, this, info);
	}
}

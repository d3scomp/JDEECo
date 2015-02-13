package cz.cuni.mff.d3s.jdeeco.network;



// XXX TB: Why this is suddenly called "device"? I think we need some consistent naming:
/**
 * Interface for network features used by network device
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface NetworkToDevice {
	/**
	 * Registers network device
	 * 
	 * @param device
	 *            Network device to register
	 */
	public void registerDevice(Device device);

	/**
	 * Processes packet from network device
	 * 
	 * @param l0Packet
	 *            Packet received by device
	 * @param srcAddress
	 *            Source address
	 */
	public void processDevicePacket(byte[] l0Packet, Address srcAddress);
}

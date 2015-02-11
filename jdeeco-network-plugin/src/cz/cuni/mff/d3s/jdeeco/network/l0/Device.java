package cz.cuni.mff.d3s.jdeeco.network.l0;

import cz.cuni.mff.d3s.jdeeco.network.Address;


/**
 * Represents a network interface card.
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface Device {
	
	/**
	 * Retrieves the ID of the device
	 * 
	 * @return id of the device
	 */
	public String getId();
	
	/**
	 * Returns maximum transmission unit size in bytes.
	 * 
	 * @return maximum transmission unit size in bytes.
	 */
	public int getMTU();
	
	/**
	 * Says whether the device can send to the given address.
	 * 
	 * @param address network address
	 * @return true when the device is capable of sending data to the given address. False otherwise.
	 */
	public boolean canSend(Address address);
	
	/**
	 * Sends data (given as an array of bytes) to the specified address.
	 * 
	 * @param data data to be sent
	 * @param address recipient address
	 */
	public void send(byte [] data, Address address);
}

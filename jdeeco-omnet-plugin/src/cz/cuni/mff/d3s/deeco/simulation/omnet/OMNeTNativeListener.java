package cz.cuni.mff.d3s.deeco.simulation.omnet;

/**
 * Listener for native event from OMNeT
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public interface OMNeTNativeListener {
	/**
	 * Called when the OMNeT reaches the registered event
	 * 
	 * @param absoluteTime
	 *            Simulation time in seconds
	 */
	public void at(double absoluteTime);

	/**
	 * Called when packet is received from network
	 * 
	 * @param packet
	 *            Packet data
	 * @param rssi
	 *            Packet rssi
	 */
	public void packetReceived(byte[] packet, double rssi, int sender);
}

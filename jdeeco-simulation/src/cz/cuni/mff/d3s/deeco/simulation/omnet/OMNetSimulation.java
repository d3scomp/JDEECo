package cz.cuni.mff.d3s.deeco.simulation.omnet;

import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkInterface;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.network.PositionProvider;
import cz.cuni.mff.d3s.deeco.scheduler.CurrentTimeProvider;
import cz.cuni.mff.d3s.deeco.simulation.CallbackProvider;
import cz.cuni.mff.d3s.deeco.simulation.SimulationHost;

/**
 * Class representing the entry point for a simulation. It is responsible for
 * initialising the OMNet simulator and serves as a factory for {@link Host}
 * class instances. It is the JNI interface.
 * 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class OMNetSimulation implements CurrentTimeProvider, NetworkProvider,
		PositionProvider, CallbackProvider {

	/**
	 * Retrieves current time of the simulation.
	 * 
	 * @return current time in seconds. Do not change this method without
	 *         changing its C counterpart.
	 */
	public native double nativeGetCurrentTime();

	/**
	 * Binds the java site host with the simulation one. It registers callbacks
	 * on time and data events. Do not change this method without changing its C
	 * counterpart.
	 * 
	 * @param host
	 *            java site host
	 */
	private native void nativeRegister(Object host, String id);

	/**
	 * Sends given array of bytes to the specified recipient. Do not change this
	 * method without changing its C counterpart.
	 * 
	 * @param id
	 *            Sender id
	 * @param data
	 *            data being send
	 * @param recipient
	 *            recipient id
	 */
	private native void nativeSendPacket(String id, byte[] data,
			String recipient);

	/**
	 * Starts the simulation and blocks until its finished. Do not change this
	 * method without changing its C counterpart.
	 */
	private native void nativeRun(String environment, String configFile);
	/**
	 * Registers a callback within the simulation resulting in method "at"
	 * execution at the absoluteTime. Do not change this method without changing
	 * its C counterpart.
	 * 
	 * @param absoluteTime
	 * @param nodeId
	 */
	private native void nativeCallAt(double absoluteTime, String nodeId);
	private native boolean nativeIsPositionInfoAvailable(String nodeId);
	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	private native double nativeGetPositionX(String nodeId);
	private native double nativeGetPositionY(String nodeId);
	private native double nativeGetPositionZ(String nodeId);
	private native double nativeSetPositionX(String nodeId, double value);
	private native double nativeSetPositionY(String nodeId, double value);
	private native double nativeSetPositionZ(String nodeId, double value);

	private final Map<String, SimulationHost> networkAddressesToHosts;

	public OMNetSimulation() {
		networkAddressesToHosts = new HashMap<String, SimulationHost>();
	}

	public void initialize() {
		System.loadLibrary("libintegration");
	}

	/**
	 * Creates new instance of the {@link Host}.
	 * 
	 * @return new host instance
	 */
	public SimulationHost getHost(String logicalId, String networkId) {
		return getHost(logicalId, networkId, true, true);
	}
	
	public SimulationHost getHost(String logicalId, String networkId, boolean hasMANETNic, boolean hasEthernetNic) {
		SimulationHost host;
		if (networkAddressesToHosts.containsKey(networkId)) {
			host = networkAddressesToHosts.get(networkId);
		} else {
			host = new SimulationHost(this, this, this, logicalId, hasMANETNic, hasEthernetNic);
			networkAddressesToHosts.put(networkId, host);
			registerInNetwork(host, networkId);
		}
		return host;
	}

	public void registerInNetwork(NetworkInterface networkInterface, String networkId) {
		nativeRegister(networkInterface, networkInterface.getHostId());
	}

	public void sendPacket(String fromId, byte[] data, String recipient) {
		String sendTo = null;
		if (recipient == null || recipient.equals("")) {
			sendTo = "";
		} else {
			for (String networkId : networkAddressesToHosts.keySet()) {
				if (networkAddressesToHosts.get(networkId).getHostId().equals(recipient)) {
					sendTo = networkId;
					break;
				}
			}
			if (sendTo == null)
				sendTo = recipient;
			
		}
		nativeSendPacket(fromId, data, sendTo);
	}

	public void run(String environment, String configFile) {
		nativeRun(environment, configFile);
	}

	public void callAt(long absoluteTime, String nodeId) {
		nativeCallAt(timeLongToDouble(absoluteTime), nodeId);
	}

	public boolean isPositionSensorAvailable(Host host) {
		return nativeIsPositionInfoAvailable(host.getHostId());
	}

	public double getPositionX(Host host) {
		return nativeGetPositionX(host.getHostId());
	}

	public double getPositionY(Host host) {
		return nativeGetPositionY(host.getHostId());
	}

	public double getPositionZ(Host host) {
		return nativeGetPositionZ(host.getHostId());
	}

	public long getCurrentTime() {
		double nativeTime = nativeGetCurrentTime();
		if (nativeTime < 0)
			return 0;
		else
			return timeDoubleToLong(nativeTime);
	}

	public static double timeLongToDouble(long time) {
		return time / 1000.0;
	}

	public static long timeDoubleToLong(double time) {
		return Math.round(time * 1000);
	}

	public void finalize() {
		for (Host h : networkAddressesToHosts.values())
			h.finalize();
	}

}

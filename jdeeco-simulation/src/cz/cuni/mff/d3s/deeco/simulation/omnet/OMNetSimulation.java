package cz.cuni.mff.d3s.deeco.simulation.omnet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.network.NetworkInterface;
import cz.cuni.mff.d3s.deeco.network.NetworkProvider;
import cz.cuni.mff.d3s.deeco.simulation.Simulation;
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
public class OMNetSimulation extends Simulation implements NetworkProvider {

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

	private Map<String, SimulationHost> networkAddressesToHosts;

	public OMNetSimulation(NetworkProvider networkProvider) {
		super(networkProvider);
		if (networkProvider == null) {
			this.networkProvider = this;
		} else {
			networkAddressesToHosts = new HashMap<String, SimulationHost>();
		}
		System.loadLibrary("libintegration");
	}

	public OMNetSimulation() {
		this(null);
	}

	public SimulationHost getHost(String logicalId, String networkId,
			boolean hasMANETNic, boolean hasEthernetNic) {
		SimulationHost host = (SimulationHost) networkProvider.getNetworkInterfaceByNetworkAddress(networkId);
		if (host == null) {
			host = super.getHost(logicalId, networkId, hasMANETNic, hasEthernetNic);
			nativeRegister(host, host.getHostId());
		}
		return host;
	}

	public void registerInNetwork(NetworkInterface networkInterface,
			String networkId) {
		networkAddressesToHosts.put(networkId,
				(SimulationHost) networkInterface);
	}
	
	@Override
	public NetworkInterface getNetworkInterfaceByNetworkAddress(String address) {
		return networkAddressesToHosts.get(address);
	}

	public void sendPacket(String fromId, byte[] data, String recipient) {
		String sendTo = null;
		if (recipient == null || recipient.equals("")) {
			sendTo = "";
		} else {
			for (String networkId : networkAddressesToHosts.keySet()) {
				if (networkAddressesToHosts.get(networkId).getHostId()
						.equals(recipient)) {
					sendTo = networkId;
					break;
				}
			}
			if (sendTo == null)
				sendTo = recipient;

		}
		nativeSendPacket(fromId, data, sendTo);
	}

	public Collection<? extends NetworkInterface> getNetworkInterfaces() {
		if (networkProvider == this) {
			return networkAddressesToHosts.values();
		} else {
			return networkProvider.getNetworkInterfaces();
		}
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

	public double setPositionX(Host host, double x) {
		return nativeSetPositionX(host.getHostId(), x);
	}

	public double setPositionY(Host host, double y) {
		return nativeSetPositionY(host.getHostId(), y);
	}

	public double setPositionZ(Host host, double z) {
		return nativeSetPositionZ(host.getHostId(), z);
	}

	public long getCurrentMilliseconds() {
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
		for (NetworkInterface ni : getNetworkInterfaces())
			((Host) ni).finalize();
	}

}

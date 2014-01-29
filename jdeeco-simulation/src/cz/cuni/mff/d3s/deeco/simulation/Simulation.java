package cz.cuni.mff.d3s.deeco.simulation;

import cz.cuni.mff.d3s.deeco.publish.PacketReceiver;

/**
 * Class representing the entry point for a simulation. It is responsible for
 * initialising the OMNet simulator and serves as a factory for {@link Host}
 * class instances. It is the JNI interface.
 * 
 * 
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class Simulation {

	/**
	 * Retrieves current time of the simulation.
	 * 
	 * @return current time in seconds.
	 */
	public native double nativeGetCurrentTime();

	/**
	 * Binds the java site host with the simulation one. It registers callbacks
	 * on time and data events.
	 * 
	 * @param host
	 *            java site host
	 */
	private native void nativeRegister(Object host, String id);

	private native void nativeSendPacket(String id, byte[] data, String recipient);

	/**
	 * Starts the simulation and blocks until its finished.
	 */
	private native void nativeRun(String environment);

	private native void nativeCallAt(double absoluteTime, String nodeId);

	public void initialize() {
		System.loadLibrary("libintegration");
	}

	/**
	 * Creates new instance of the {@link Host}.
	 * 
	 * @return new host instance
	 */
	public Host getHost(String id, int packetSize) {
		return new Host(this, id, packetSize);
	}

	// Wrapper methods we may need them.

	public void register(Object host, String id) {
		nativeRegister(host, id);
	}

	public void sendPacket(String id, byte[] data, String recipient) {
		nativeSendPacket(id, data, recipient);
	}

	public void run(String environment) {
		nativeRun(environment);
	}

	public void callAt(long absoluteTime, String nodeId) {
		nativeCallAt(timeLongToDouble(absoluteTime), nodeId);
	}

	public long getSimulationTime() {
		return timeDoubleToLong(nativeGetCurrentTime());
	}
	
	public static double timeLongToDouble(long time) {
		return time / 1000.0;
	}
	
	public static long timeDoubleToLong(double time) {
		return Math.round(time * 1000);
	}

}

package cz.cuni.mff.d3s.deeco.simulation;


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
	 * @return current time in seconds. Do not change this method without changing its C counterpart.
	 */
	public native double nativeGetCurrentTime();

	/**
	 * Binds the java site host with the simulation one. It registers callbacks
	 * on time and data events. Do not change this method without changing its C counterpart.
	 * 
	 * @param host
	 *            java site host
	 */
	private native void nativeRegister(Object host, String id);

	/**
	 * Sends given array of bytes to the specified recipient.
	 * Do not change this method without changing its C counterpart.
	 * 
	 * @param id Sender id
	 * @param data data being send
	 * @param recipient recipient id
	 */
	private native void nativeSendPacket(String id, byte[] data, String recipient);

	/**
	 * Starts the simulation and blocks until its finished.
	 * Do not change this method without changing its C counterpart.
	 */
	private native void nativeRun(String environment, String configFile);

	/**
	 * Registers a callback within the simulation resulting in method "at" execution at the absoluteTime.
	 * Do not change this method without changing its C counterpart.
	 * 
	 * @param absoluteTime
	 * @param nodeId
	 */
	private native void nativeCallAt(double absoluteTime, String nodeId);
	
	private native boolean nativeIsGPSAvailable(String nodeId);
	private native double nativeGetPositionX(String nodeId);
	private native double nativeGetPositionY(String nodeId);
	private native double nativeGetPositionZ(String nodeId);

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

	public void run(String environment, String configFile) {
		nativeRun(environment, configFile);
	}

	public void callAt(long absoluteTime, String nodeId) {
		nativeCallAt(timeLongToDouble(absoluteTime), nodeId);
	}
	
	public boolean isGPSAvailable(String id) {
		return nativeIsGPSAvailable(id);
	}
	
	public double getPositionX(String id) {
		return nativeGetPositionX(id);
	}
	
	public double getPositionY(String id) {
		return nativeGetPositionY(id);
	}
	
	public double getPositionZ(String id) {
		return nativeGetPositionZ(id);
	}

	public long getSimulationTime() {
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

}

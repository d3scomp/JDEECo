package cz.cuni.mff.d3s.deeco.simulation.omnet;

public class OMNeTNative {
	static {
		System.loadLibrary("jdeeco-omnetpp");
	}

	/**
	 * Retrieves current time of the simulation.
	 * 
	 * @return current time in seconds. Do not change this method without changing its C counterpart.
	 */
	public static native double nativeGetCurrentTime();

	/**
	 * Binds the java site host with the simulation one. It registers callbacks on time and data events. Do not change
	 * this method without changing its C counterpart.
	 * 
	 * @param host
	 *            java site host
	 */
	public static native void nativeRegister(Object host, String id);

	/**
	 * Sends given array of bytes to the specified recipient. Do not change this method without changing its C
	 * counterpart.
	 * 
	 * @param id
	 *            Sender id
	 * @param data
	 *            data being send
	 * @param recipient
	 *            recipient id
	 */
	public static native void nativeSendPacket(String id, byte[] data, String recipient);

	/**
	 * Starts the simulation and blocks until its finished. Do not change this method without changing its C
	 * counterpart.
	 */
	public static native void nativeRun(String environment, String configFile);

	/**
	 * Registers a callback within the simulation resulting in method "at" execution at the absoluteTime. Do not change
	 * this method without changing its C counterpart.
	 * 
	 * @param absoluteTime
	 * @param nodeId
	 */
	public static native void nativeCallAt(double absoluteTime, String nodeId);

	public native boolean nativeIsPositionInfoAvailable(String nodeId);

	/**
	 * 
	 * @param nodeId
	 * @return
	 */
	public static native double nativeGetPositionX(String nodeId);

	public static native double nativeGetPositionY(String nodeId);

	public static native double nativeGetPositionZ(String nodeId);

	public static native void nativeSetPosition(String nodeId, double valX, double valY, double valZ);
}

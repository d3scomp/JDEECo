package cz.cuni.mff.d3s.deeco.simulation.omnet;

/**
 * Wrapper for OMNeT native functionality
 * 
 * Methods here are static as native integration is static. OMNeT cannot be used twice from one JVM.
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 *
 */
public class OMNeTNative {
	public static final String LIB_PATH = "omnet";

	static {
		// Load windows dependencies (order matters)
		if (System.getProperty("os.name").contains("Windows")) {
			System.loadLibrary("libwinpthread-1");
			System.loadLibrary("libgcc_s_seh-1");
			System.loadLibrary("libstdc++-6");
		} else {
			throw new UnsupportedOperationException("Unsupported OS " + System.getProperty("os.name"));
		}
		
		// Load shared dependencies (order matters)
		// TODO: What about release flavor?
		System.loadLibrary("liboppcommond");
		System.loadLibrary("liboppnedxmld");
		System.loadLibrary("liboppeventlogd");
		System.loadLibrary("liboppscaved");
		System.loadLibrary("libopplayoutd");
		System.loadLibrary("liboppsimd");
		System.loadLibrary("liboppenvird");
		System.loadLibrary("libopptkenvd");
		System.loadLibrary("liboppcmdenvd");
		
		// Load plug-ins (order matters)
		System.loadLibrary("libinet");
		System.loadLibrary("libmixim");

		// Load native library when the class is used for first time
		System.loadLibrary("jdeeco-omnetpp");
	}

	/**
	 * Retrieves current time of the simulation.
	 * 
	 * @return current time in seconds. Do not change this method without changing its C counterpart.
	 */
	public static native double nativeGetCurrentTime();

	/**
	 * Binds the java site host with the simulation one. It registers call-backs on time and data events. Do not change
	 * this method without changing its C counterpart.
	 * 
	 * @param host
	 *            java site host
	 */
	public static native void nativeRegister(Object host, int id);

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
	public static native void nativeSendPacket(int id, byte[] data, String recipient);

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
	public static native void nativeCallAt(double absoluteTime, int nodeId);

	/**
	 * Checks whenever the position info is available
	 * 
	 * @param nodeId
	 *            Id of the node to check
	 * @return True if available, false otherwise
	 */
	public native boolean nativeIsPositionInfoAvailable(int nodeId);

	/**
	 * Gets node position X coordinate
	 * 
	 * @param nodeId
	 *            Node id
	 * @return Position in meters
	 */
	public static native double nativeGetPositionX(int nodeId);

	/**
	 * Gets node position Y coordinate
	 * 
	 * @param nodeId
	 *            Node id
	 * @return Position in meters
	 */
	public static native double nativeGetPositionY(int nodeId);

	/**
	 * Gets node position Z coordinate
	 * 
	 * @param nodeId
	 *            Node id
	 * @return Position in meters
	 */
	public static native double nativeGetPositionZ(int nodeId);

	/**
	 * Sets native position
	 * 
	 * @param nodeId
	 *            Node id
	 * @param valX
	 *            New X position
	 * @param valY
	 *            New Y position
	 * @param valZ
	 *            New Z position
	 */
	public static native void nativeSetPosition(int nodeId, double valX, double valY, double valZ);

	/**
	 * Converts OMNeT native time to JDEECo time
	 * 
	 * @param omnetTime
	 *            OMNeT representation of time
	 * @return jDEECo time
	 */
	public static long timeFromOmnet(double omnetTime) {
		return (long) Math.round(omnetTime * 1000);
	}

	/**
	 * Converts jDEECo time to OMNet representation
	 * 
	 * @param jDEECoTime
	 *            jDEECo representation of time
	 * @return OMNeT Time
	 */
	public static double timeToOmnet(long jdeecoTime) {
		return ((double) jdeecoTime) / 1000;
	}
}

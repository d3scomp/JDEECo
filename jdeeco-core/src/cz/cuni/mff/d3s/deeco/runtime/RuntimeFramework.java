package cz.cuni.mff.d3s.deeco.runtime;

/**
 * JDEECo runtime framework interface.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public interface RuntimeFramework {

	/**
	 * Starts the execution of the runtime framework
	 */
	public abstract void start();

	/**
	 * Stops the execution of the runtime framework
	 */
	public abstract void stop();

}
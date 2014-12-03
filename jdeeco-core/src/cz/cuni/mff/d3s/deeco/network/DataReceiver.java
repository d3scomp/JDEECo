/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network;

/**
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public abstract class DataReceiver<T> {

	private final Class<T> expectedType;

	/**
	 * Each extending class needs to call this constructor with the particular
	 * class used in generic.
	 * 
	 * @param expectedType class reference of this generic
	 */
	public DataReceiver(Class<T> expectedType) {
		this.expectedType = expectedType;
	}

	/**
	 * @param data
	 * @param rssi radio signal strength indicator. -1 in case of IP
	 */
	public final void checkAndReceive(Object data, double rssi) {
		if (data == null || expectedType.isAssignableFrom(data.getClass())) {
			receive(expectedType.cast(data), rssi);
		}
	}

	protected abstract void receive(T data, double rssi);
}

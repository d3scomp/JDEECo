/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network;

/**
 * An object which is able to send general type of data to a recipient identified
 * by ${@link String} through the network or directly passing the reference.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 *
 */
public interface DataSender {
	/**
	 * 
	 * @param data Object instance to be transfered over network or directly by passing a
	 * reference.
	 * @param recipient String identification of the recipient
	 */
	public void sendData(Object data, String recipient);
	
	/**
	 * @param data
	 */
	public void broadcastData(Object data);
}

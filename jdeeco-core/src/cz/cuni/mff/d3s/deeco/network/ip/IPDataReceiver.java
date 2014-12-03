/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network.ip;

import cz.cuni.mff.d3s.deeco.network.DataReceiver;


/**
 * An object that is capable of processing {@link IPData} coming from the network.
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public abstract class IPDataReceiver extends DataReceiver<IPData> {
	public IPDataReceiver(Class<IPData> expectedType) {
		super(IPData.class);
	}
}

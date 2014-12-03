/**
 * 
 */
package cz.cuni.mff.d3s.deeco.network.ip;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cz.cuni.mff.d3s.deeco.network.DataReceiver;

/**
 * 
 * @author Ondrej Kováč <info@vajanko.me>
 */
public class IPControllerImpl extends DataReceiver<IPData> implements
		IPController {

	// key: partitionValue, value: partition IP table
	private Map<Object, IPRegister> ipTables;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.cuni.mff.d3s.deeco.network.ip.IPController#getIPTable(java.lang.String
	 * )
	 */
	@Override
	public IPRegister getRegister(Object partitionValue) {

		if (!ipTables.containsKey(partitionValue))
			ipTables.put(partitionValue, new IPRegister());

		return ipTables.get(partitionValue);
	}

	public IPControllerImpl() {
		super(IPData.class);
		this.ipTables = new HashMap<Object, IPRegister>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (Entry<Object, IPRegister> item : ipTables.entrySet()) {
			str.append(item.getKey().toString() + "->"
					+ item.getValue().toString() + "\n");
		}
		return str.toString();
	}

	@Override
	public void receive(IPData data, double rssi) {
		IPData ipdata = (IPData) data;

		IPRegister ipTable = getRegister(ipdata.getMetaData());

		ipTable.clear();
		ipTable.addAll(ipdata.getAddresses());
	}
}

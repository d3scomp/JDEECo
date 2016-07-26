package cz.cuni.mff.d3s.deeco.simulation;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.DataReceiver;

public interface DataReceiversHolder {
	
	public void addDataReceiver(AbstractHost host, DataReceiver<?> receiver);
}

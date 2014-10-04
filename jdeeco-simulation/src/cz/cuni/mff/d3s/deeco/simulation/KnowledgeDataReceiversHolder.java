package cz.cuni.mff.d3s.deeco.simulation;

import cz.cuni.mff.d3s.deeco.network.AbstractHost;
import cz.cuni.mff.d3s.deeco.network.KnowledgeDataReceiver;

public interface KnowledgeDataReceiversHolder {
	
	public void addKnowledgeDataReceiver(AbstractHost host, KnowledgeDataReceiver receiver);
}

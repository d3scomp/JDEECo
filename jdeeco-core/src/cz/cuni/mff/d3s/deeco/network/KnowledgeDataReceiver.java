package cz.cuni.mff.d3s.deeco.network;

import java.util.List;

/**
 * An object that is capable of processing {@link KnowledgeData} coming from the network.
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface KnowledgeDataReceiver {
	public void receive(List<? extends KnowledgeData> knowledgeData);
}

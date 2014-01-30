package cz.cuni.mff.d3s.deeco.knowledge;

import java.util.List;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public interface KnowledgeDataProvider {
	//TODO will change in the future
	public List<? extends KnowledgeData> getKnowledgeData();
}

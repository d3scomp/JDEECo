package cz.cuni.mff.d3s.deeco.integrity;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public interface RatingsManager {
	public ReadonlyRatingsHolder createReadonlyRatingsHolder(String targetComponentId, KnowledgePath absolutePath);
	public RatingsHolder createRatingsHolder(String askingComponentId, String targetComponentId, KnowledgePath absolutePath);
	public void createRatingsChangeSet(Map<KnowledgePath, RatingsHolder> pathRatings);
	public void update(List<RatingsChangeSet> changeSets);
	public List<RatingsChangeSet> getRatingsChangeSets();
}

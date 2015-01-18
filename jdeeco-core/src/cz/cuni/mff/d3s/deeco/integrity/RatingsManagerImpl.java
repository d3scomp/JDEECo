package cz.cuni.mff.d3s.deeco.integrity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsManagerImpl implements RatingsManager {
	
	private Map<KnowledgePath, Map<String, Map<String, PathRating>>> ratings;          // path->target component->claiming component->rating
	private List<RatingsChangeSet> pendingChanges;
	
	public RatingsManagerImpl() {
		this.ratings = new HashMap<>();		
		this.pendingChanges = new ArrayList<>();
	}
	
	protected Map<String, PathRating> getRatings(String targetComponentId, KnowledgePath absolutePath) {
		if (absolutePath == null || !KnowledgePathHelper.isAbsolutePath(absolutePath)) {
			throw new IllegalArgumentException("Knowledge path must be not null and absolute.");
		}
		
		if (!ratings.containsKey(absolutePath)) {
			ratings.put(absolutePath, new HashMap<>());
		}
		if (!ratings.get(absolutePath).containsKey(targetComponentId)) {
			ratings.get(absolutePath).put(targetComponentId, new HashMap<>());
		}
		
		return ratings.get(absolutePath).get(targetComponentId);
	}
	
	protected void setRating(String authorComponentId, String targetComponentId, KnowledgePath absolutePath, PathRating rating) {
		if (absolutePath == null || !KnowledgePathHelper.isAbsolutePath(absolutePath)) {
			throw new IllegalArgumentException("Knowledge path must be not null and absolute.");
		}
		
		Map<String, PathRating> targetComponentRatings = getRatings(targetComponentId, absolutePath);
		targetComponentRatings.put(authorComponentId, rating);
	}
	
	public ReadonlyRatingsHolder createReadonlyRatingsHolder(String targetComponentId, KnowledgePath absolutePath) {
		if (absolutePath == null || !KnowledgePathHelper.isAbsolutePath(absolutePath)) {
			throw new IllegalArgumentException("Knowledge path must be not null and absolute.");
		}
		
		Map<String, PathRating> individualRating = getRatings(targetComponentId, absolutePath);
		
		if (individualRating == null) {
			return new ReadonlyRatingsHolder(Collections.emptyMap());
		} else {
			Map<PathRating, Long> pathRating = aggregateRatings(individualRating);			
			return new ReadonlyRatingsHolder(pathRating);
		}
	}
	
	public RatingsHolder createRatingsHolder(String askingComponentId, String targetComponentId, KnowledgePath absolutePath) {		
		if (absolutePath == null || !KnowledgePathHelper.isAbsolutePath(absolutePath)) {
			throw new IllegalArgumentException("Knowledge path must be not null and absolute.");
		}
			
		Map<String, PathRating> individualRating = getRatings(targetComponentId, absolutePath);
		PathRating oldPathRating =  individualRating.get(askingComponentId);
		Map<PathRating, Long> pathRating = aggregateRatings(individualRating);	
		// TODO - write information back
		
		return new RatingsHolder(askingComponentId, targetComponentId, oldPathRating, pathRating);
	}
	
	public synchronized void createRatingsChangeSet(Map<KnowledgePath, RatingsHolder> pathRatings) {
		if (pathRatings == null) return;
		
		for (Entry<KnowledgePath, RatingsHolder> entry : pathRatings.entrySet()) {
			RatingsHolder holder = entry.getValue();
			if (holder.isDirty()) {				
				pendingChanges.add(new RatingsChangeSet(holder.getAskingComponentId(), holder.getTargetComponentId(), entry.getKey(), holder.getMyRating()));
			}
		}
	}
	
	@Override
	public synchronized List<RatingsChangeSet> getRatingsChangeSets() {
		List<RatingsChangeSet> changes = new ArrayList<>();
		changes.addAll(pendingChanges);		
		return changes;
	}
	
	public void clearRatingsChangeSets() {
		pendingChanges.clear();
	}
	
	@Override
	public synchronized void update(List<RatingsChangeSet> changeSets) {
		if (changeSets == null) return;
		
		for (RatingsChangeSet changeSet : changeSets) {			
			setRating(changeSet.getAuthorComponentId(), changeSet.getTargetComponentId(), changeSet.getKnowledgePath(), changeSet.getPathRating());
		}
	}
	
	private Map<PathRating, Long> aggregateRatings(Map<String, PathRating> individualRating) {
		return individualRating.values().stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
	}


}

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
 * The implementation of RatingsManager.
 *
 * @author Ondřej Štumpf
 */
public class RatingsManagerImpl implements RatingsManager {
	
	/** Contains all ratings. path->target component->author component->rating  */
	private Map<KnowledgePath, Map<String, Map<String, PathRating>>> ratings;  
	
	/** Changes to the ratings container that had not yet been distributed */
	private List<RatingsChangeSet> pendingChanges;
	
	/** singleton instance */
	private static RatingsManagerImpl instance;
	
	/**
	 * Instantiates a new ratings manager.
	 */
	RatingsManagerImpl() {
		this.ratings = new HashMap<>();		
		this.pendingChanges = new ArrayList<>();
	}
	
	/**
	 * Gets the unique singleton instance.
	 * @return
	 */
	public static synchronized RatingsManagerImpl getInstance() {
		if (instance == null) {
			instance = new RatingsManagerImpl();
		}
		return instance;
	}
	
	/**
	 * Drops the current singleton instance. This method should only be used in tests.
	 */
	public static void resetSingleton() {
		instance = null;
	}
	
	/**
	 * Gets the map of ratings, containing ID of the component and the rating this component assigned to the given knowledge in the given component.
	 *
	 * @param targetComponentId
	 *            the ID of the component whose knowledge is rated
	 * @param absolutePath
	 *            the absolute path to the knowledge
	 * @return the ratings map
	 */
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
	
	/**
	 * Sets the rating.
	 *
	 * @param authorComponentId
	 *            the ID of the component that issues the rating
	 * @param targetComponentId
	 *            the ID of the component whose knowledge is rated
	 * @param absolutePath
	 *            the absolute path to the knowledge
	 * @param rating
	 *            the rating
	 */
	protected void setRating(String authorComponentId, String targetComponentId, KnowledgePath absolutePath, PathRating rating) {
		if (absolutePath == null || !KnowledgePathHelper.isAbsolutePath(absolutePath)) {
			throw new IllegalArgumentException("Knowledge path must be not null and absolute.");
		}
		
		Map<String, PathRating> targetComponentRatings = getRatings(targetComponentId, absolutePath);
		targetComponentRatings.put(authorComponentId, rating);
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.integrity.RatingsManager#createReadonlyRatingsHolder(java.lang.String, cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath)
	 */
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
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.integrity.RatingsManager#createRatingsHolder(java.lang.String, java.lang.String, cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath)
	 */
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
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.integrity.RatingsManager#createRatingsChangeSet(java.util.Map)
	 */
	public synchronized List<RatingsChangeSet> createRatingsChangeSet(Map<KnowledgePath, RatingsHolder> pathRatings) {
		if (pathRatings == null) return Collections.emptyList();
		
		List<RatingsChangeSet> changes = new ArrayList<>();
		for (Entry<KnowledgePath, RatingsHolder> entry : pathRatings.entrySet()) {
			RatingsHolder holder = entry.getValue();
			if (holder.isDirty()) {				
				changes.add(new RatingsChangeSet(holder.getAskingComponentId(), holder.getTargetComponentId(), entry.getKey(), holder.getMyRating()));
			}
		}
		
		return changes;
	}

	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.integrity.RatingsManager#getPendingChangeSets()
	 */
	public List<RatingsChangeSet> getPendingChangeSets() {
		return pendingChanges;
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.integrity.RatingsManager#update(java.util.List)
	 */
	public synchronized void update(List<RatingsChangeSet> changeSets) {
		if (changeSets == null) return;
		
		for (RatingsChangeSet changeSet : changeSets) {			
			setRating(changeSet.getAuthorComponentId(), changeSet.getTargetComponentId(), changeSet.getKnowledgePath(), changeSet.getPathRating());
		}
	}
	
	/* (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.integrity.RatingsManager#addToPendingChangeSets(java.util.List)
	 */
	public void addToPendingChangeSets(List<RatingsChangeSet> changeSets) {
		pendingChanges.addAll(changeSets);		
	}

	/**
	 * Groups the individual ratings by the value and counts the number of occurences.
	 * @param individualRating
	 * @return
	 */
	private Map<PathRating, Long> aggregateRatings(Map<String, PathRating> individualRating) {
		return individualRating.values().stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
	}
}

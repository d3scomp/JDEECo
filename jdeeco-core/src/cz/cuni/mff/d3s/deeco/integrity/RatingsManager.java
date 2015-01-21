package cz.cuni.mff.d3s.deeco.integrity;

import java.util.List;
import java.util.Map;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * Interface for a container of ratings data.
 * @author Ondřej Štumpf
 *
 */
public interface RatingsManager {
	
	/**
	 * Creates the {@link ReadonlyRatingsHolder}, which enables component or ensemble to read the rating data, but not modify them.
	 *
	 * @param targetComponentId
	 *            the ID of the component whose knowledge is being rated
	 * @param absolutePath
	 *            the knowledge path that is rated
	 * @return the read-only ratings holder
	 */
	public ReadonlyRatingsHolder createReadonlyRatingsHolder(String targetComponentId, KnowledgePath absolutePath);
	
	/**
	 * Creates the {@link RatingsHolder}, which enables component rating process to read and modify rating data.
	 *
	 * @param askingComponentId
	 *            the ID of the component that asked for this holder (and later becomes author of the rating)
	 * @param targetComponentId
	 *            the ID of the component whose knowledge is being rated
	 * @param absolutePath
	 *            the knowledge path that is rated
	 * @return the ratings holder
	 */
	public RatingsHolder createRatingsHolder(String askingComponentId, String targetComponentId, KnowledgePath absolutePath);
	
	/**
	 * Creates the list of changes to the central rating data container. This list is later distributed across components.
	 *
	 * @param pathRatings
	 *            rating for each knowledge path
	 * @return the list of change sets
	 */
	public List<RatingsChangeSet> createRatingsChangeSet(Map<KnowledgePath, RatingsHolder> pathRatings);
	
	/**
	 * Updates the container with given change sets.
	 *
	 * @param changeSets
	 *            the set of changes (possibly obtained from other component)
	 */
	public void update(List<RatingsChangeSet> changeSets);
	
	/**
	 * Adds the list of change sets to the list of change sets that gets distributed.
	 *
	 * @param changeSets
	 *            the change sets
	 */
	public void addToPendingChangeSets(List<RatingsChangeSet> changeSets);
	
	/**
	 * Gets the list of change sets that has not yet been redistributed.
	 *
	 * @return the change sets
	 */
	public List<RatingsChangeSet> getPendingChangeSets();
}

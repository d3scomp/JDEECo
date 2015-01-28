package cz.cuni.mff.d3s.deeco.integrity;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Rating;
import cz.cuni.mff.d3s.deeco.annotations.RatingsProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * Contains rating information about a single {@link KnowledgePath} of a single component.
 * This class is passed as an argument to the {@link RatingsProcess} for those arguments that are decorated with {@link Rating}
 *
 * @author Ondřej Štumpf
 */
public class RatingsHolder extends ReadonlyRatingsHolder {

	private PathRating oldRating, newRating;	
	private final String askingComponentId, targetComponentId;
	
	/**
	 * Instantiates a new ratings holder.
	 *
	 * @param askingComponentId
	 *            the ID of the component that asked for this holder (and later becomes author of the rating)
	 * @param targetComponentId
	 *            the ID of the component whose knowledge is being rated
	 * @param rating
	 *            the old rating
	 * @param ratings
	 *            the aggregated ratings from other components
	 */
	protected RatingsHolder(String askingComponentId, String targetComponentId, PathRating rating, Map<PathRating, Long> ratings) {
		super(ratings);
		this.oldRating = rating;
		this.newRating = rating;	
		this.askingComponentId = askingComponentId;
		this.targetComponentId = targetComponentId;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating
	 *            the new rating
	 */
	public void setMyRating(PathRating rating) {
		this.oldRating = this.newRating;
		this.newRating = rating;
		
		if (oldRating != null) {
			Long oldValue = ratings.get(oldRating);
			long oldValueL = oldValue == null ? 0 : oldValue.longValue();
			ratings.put(oldRating, oldValueL - 1);
		}
		
		if (rating != null) {
			Long oldValue = ratings.get(rating);
			long oldValueL = oldValue == null ? 0 : oldValue.longValue();
			ratings.put(rating, oldValueL + 1);
		}		
	}
	
	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public PathRating getMyRating() {
		return newRating;
	}
	
	/**
	 * Checks if is dirty (i.e. the new rating is different from the original).
	 *
	 * @return true, if is dirty
	 */
	public boolean isDirty() {
		return oldRating != newRating;
	}
	
	/**
	 * Gets the ID of the component that asked for this holder (and later becomes author of the rating).
	 *
	 * @return the ID of the component
	 */
	protected String getAskingComponentId() {
		return askingComponentId;
	}
	
	/**
	 * Gets the ID of the component whose knowledge is being rated
	 *
	 * @return the ID of the component
	 */
	protected String getTargetComponentId() {
		return targetComponentId;
	}
}

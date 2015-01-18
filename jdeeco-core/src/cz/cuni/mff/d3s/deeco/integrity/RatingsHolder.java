package cz.cuni.mff.d3s.deeco.integrity;

import java.util.Map;



/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsHolder extends ReadonlyRatingsHolder {

	private PathRating oldRating, newRating;	
	private final String askingComponentId, targetComponentId;
	
	protected RatingsHolder(String askingComponentId, String targetComponentId, PathRating rating, Map<PathRating, Long> ratings) {
		super(ratings);
		this.oldRating = rating;
		this.newRating = rating;	
		this.askingComponentId = askingComponentId;
		this.targetComponentId = targetComponentId;
	}

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
	
	public PathRating getMyRating() {
		return newRating;
	}
	
	public boolean isDirty() {
		return oldRating != newRating;
	}
	
	protected String getAskingComponentId() {
		return askingComponentId;
	}
	
	protected String getTargetComponentId() {
		return targetComponentId;
	}
}

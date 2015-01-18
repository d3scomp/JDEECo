package cz.cuni.mff.d3s.deeco.integrity;

import java.util.Map;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class ReadonlyRatingsHolder  {
	
	protected Map<PathRating, Long> ratings;
	
	protected ReadonlyRatingsHolder(Map<PathRating, Long> ratings) {
		this.ratings = ratings;
	}
	
	public long getRatings(PathRating rating) {
		Long l = ratings.get(rating);
		return l == null ? 0l : l.longValue();		
	}	
}

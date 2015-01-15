package cz.cuni.mff.d3s.deeco.integrity;

import java.util.Map;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class ReadonlyRatingsHolder  {
	
	protected Map<PathRating, Integer> ratings;
	
	public int getOpinionsCount(PathRating rating) {
		return ratings.get(rating);		
	}	
}

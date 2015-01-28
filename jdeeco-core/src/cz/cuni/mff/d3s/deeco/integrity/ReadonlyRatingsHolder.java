package cz.cuni.mff.d3s.deeco.integrity;

import java.util.Map;

import cz.cuni.mff.d3s.deeco.annotations.Rating;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * Contains rating information about a single {@link KnowledgePath} of a single component.
 * This class is passed to methods with arguments that are decorated with {@link Rating}.
 * @author Ondřej Štumpf
 *
 */
public class ReadonlyRatingsHolder  {
	
	protected Map<PathRating, Long> ratings;
	
	/**
	 * Instantiates a new read-only ratings holder.
	 *
	 * @param ratings
	 *            the ratings from the {@link RatingsManager}
	 */
	public ReadonlyRatingsHolder(Map<PathRating, Long> ratings) {
		this.ratings = ratings;
	}
	
	/**
	 * Gets the number of components that rated the given knowledge with this rating.
	 *
	 * @param rating
	 *            the rating
	 * @return the number of components
	 */
	public long getRatings(PathRating rating) {
		Long l = ratings.get(rating);
		return l == null ? 0l : l.longValue();		
	}	
}

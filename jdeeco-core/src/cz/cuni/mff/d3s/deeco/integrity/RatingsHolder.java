package cz.cuni.mff.d3s.deeco.integrity;


/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsHolder extends ReadonlyRatingsHolder {
	
	private PathRating localComponentOpinion;
	
	public void setOpinion(PathRating rating) {
		this.localComponentOpinion = rating;
	}
}

package cz.cuni.mff.d3s.deeco.integrity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsHolderTest {

	private RatingsHolder holder;
	
	@Before
	public void setUp() {
		Map<PathRating, Long> ratings = new HashMap<>();
		ratings.put(PathRating.OK, 20l);
		ratings.put(PathRating.UNUSUAL, 25l);
		
		holder = new RatingsHolder("ASK", "TARGET", null, ratings);
	}
	
	@Test
	public void getRatingsTest1() {
		// given ratings are prepared
		
		// when getRatings() for existing argument is called
		assertEquals(20l, holder.getRatings(PathRating.OK));
	}
	
	@Test
	public void getRatingsTest2() {
		// given ratings are prepared
		
		// when getRatings() for NON existing argument is called
		assertEquals(0l, holder.getRatings(PathRating.OUT_OF_RANGE));
	}
	
	@Test
	public void setMyRatingTest1() {
		// given old rating is null
		assertNull(holder.getMyRating());
		
		// when rating is changed
		holder.setMyRating(PathRating.OUT_OF_RANGE);
		
		// then numbers change
		assertEquals(1l, holder.getRatings(PathRating.OUT_OF_RANGE));
	}
	
	@Test
	public void setMyRatingTest2() {
		// given old rating is not null		
		holder.setMyRating(PathRating.OK);
		
		// when rating is changed
		holder.setMyRating(PathRating.UNUSUAL);
		
		// then numbers change
		assertEquals(20l, holder.getRatings(PathRating.OK));
		assertEquals(26l, holder.getRatings(PathRating.UNUSUAL));
	}
}

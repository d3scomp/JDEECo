package cz.cuni.mff.d3s.deeco.integrity;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RatingsManagerImplTest {

	private RatingsManagerImpl target;
	private KnowledgePath path1, path2;
	
	@Before
	public void setUp() {
		target = new RatingsManagerImpl();		
		
		path1 = RuntimeModelHelper.createKnowledgePath("field");
		path2 = RuntimeModelHelper.createKnowledgePath("field", "X");
		
		target.setRating("author1", "target", path1, PathRating.OK);
		target.setRating("author2", "target", path1, PathRating.OK);
		target.setRating("author3", "target", path1, PathRating.OK);
		target.setRating("author4", "target", path1, PathRating.OUT_OF_RANGE);
		target.setRating("author1", "target", path2, PathRating.OK);
		target.setRating("author2", "target", path2, PathRating.UNUSUAL);
		target.setRating("author1", "target2", path2, PathRating.OUT_OF_RANGE);
		target.setRating("author2", "target2", path2, PathRating.OUT_OF_RANGE);
	}
	
	@Test
	public void setAndGetRatingTest1() {
		// given rating is set
		
		// when getRatings() is called
		Map<String, PathRating> result = target.getRatings("target", path1);
		
		// then correct result is returned
		assertEquals(4, result.size());
		assertEquals(PathRating.OK, result.get("author1"));
		assertEquals(PathRating.OK, result.get("author2"));
		assertEquals(PathRating.OK, result.get("author3"));
		assertEquals(PathRating.OUT_OF_RANGE, result.get("author4"));
	}

	@Test
	public void createReadonlyRatingsHolderTest1() {
		// given rating is set
		
		// when createReadonlyRatingsHolder() is called
		ReadonlyRatingsHolder holder = target.createReadonlyRatingsHolder("target", path1);
		
		// then correct count is present
		assertEquals(3, holder.getRatings(PathRating.OK));
		assertEquals(1, holder.getRatings(PathRating.OUT_OF_RANGE));
		assertEquals(0, holder.getRatings(PathRating.UNUSUAL));
		assertEquals(0, holder.getRatings(null));
	}
	
	@Test
	public void createReadonlyRatingsHolderTest2() {
		// given rating is set
		
		// when createReadonlyRatingsHolder() is called
		ReadonlyRatingsHolder holder = target.createReadonlyRatingsHolder("target_nonexisting", path2);
		
		// then correct count is present
		assertEquals(0, holder.getRatings(PathRating.OK));
		assertEquals(0, holder.getRatings(PathRating.OUT_OF_RANGE));
		assertEquals(0, holder.getRatings(PathRating.UNUSUAL));
		assertEquals(0, holder.getRatings(null));
	}
	
	@Test
	public void createRatingsHolderTest1() {
		// given rating is set
		
		// when createRatingsHolder() is called
		RatingsHolder holder = target.createRatingsHolder("me", "target", path1);
		
		// then correct count is present
		assertEquals(3, holder.getRatings(PathRating.OK));
		assertEquals(1, holder.getRatings(PathRating.OUT_OF_RANGE));
		assertEquals(0, holder.getRatings(PathRating.UNUSUAL));
		assertEquals(0, holder.getRatings(null));
		
		// then data are properly initialized
		assertNull(holder.getMyRating());
		assertEquals("me", holder.getAskingComponentId());
		assertEquals("target", holder.getTargetComponentId());
		assertFalse(holder.isDirty());
		
		// when setMyRating() is called
		holder.setMyRating(PathRating.OK);
		assertEquals(4, holder.getRatings(PathRating.OK));
		assertEquals(PathRating.OK, holder.getMyRating());
		assertTrue(holder.isDirty());
	}
	
	@Test
	public void createRatingsHolderTest2() {
		// given rating is set
		
		// when createRatingsHolder() is called
		RatingsHolder holder = target.createRatingsHolder("author2", "target2", path2);
		
		// then holder is properly initialized
		assertEquals(PathRating.OUT_OF_RANGE, holder.getMyRating());
		assertFalse(holder.isDirty());
	}
	
	@Test
	public void createRatingsChangeSetTest1() {
		// given rating is set and holders created		
		RatingsHolder holder1 = target.createRatingsHolder("author", "target", path1);
		RatingsHolder holder2 = target.createRatingsHolder("author5", "target", path2);
		
		// when holders are modified
		holder1.setMyRating(PathRating.UNUSUAL);
		holder2.setMyRating(PathRating.OUT_OF_RANGE);
		
		Map<KnowledgePath, RatingsHolder> holders = new HashMap<>();
		holders.put(path1, holder1);
		holders.put(path2, holder2);
		assertEquals(0, target.getRatingsChangeSets().size());
		
		// when change set is created
		target.createRatingsChangeSet(holders);
		
		// then proper change set is returned
		List<RatingsChangeSet> changeSets = target.getRatingsChangeSets();
		assertEquals(2, changeSets.size());
		
		assertEquals("author", changeSets.get(0).getAuthorComponentId());
		assertEquals("target", changeSets.get(0).getTargetComponentId());
		assertEquals(path1, changeSets.get(0).getKnowledgePath());
		assertEquals(PathRating.UNUSUAL, changeSets.get(0).getPathRating());
		
		assertEquals("author5", changeSets.get(1).getAuthorComponentId());
		assertEquals("target", changeSets.get(1).getTargetComponentId());
		assertEquals(path2, changeSets.get(1).getKnowledgePath());
		assertEquals(PathRating.OUT_OF_RANGE, changeSets.get(1).getPathRating());
	}
	
	@Test
	public void updateTest1() {
		// given rating is set and holders created		
		RatingsHolder holder1 = target.createRatingsHolder("author", "target", path1);
		RatingsHolder holder2 = target.createRatingsHolder("author", "target", path2);
		
		// when holders are modified
		holder1.setMyRating(PathRating.UNUSUAL);
		holder2.setMyRating(PathRating.OUT_OF_RANGE);
		
		Map<KnowledgePath, RatingsHolder> holders = new HashMap<>();
		holders.put(path1, holder1);
		holders.put(path2, holder2);
		assertEquals(0, target.getRatingsChangeSets().size());
		
		// when change set is created
		target.createRatingsChangeSet(holders);	
		List<RatingsChangeSet> changeSets = target.getRatingsChangeSets();
		
		// when update() is called
		target.update(changeSets);
		
		// then data are modified
		Map<String, PathRating> result1 = target.getRatings("target", path1);
		Map<String, PathRating> result2 = target.getRatings("target", path2);
		
		assertEquals(5, result1.size());
		assertEquals(PathRating.UNUSUAL, result1.get("author"));
		assertEquals(PathRating.OK, result1.get("author1"));
		assertEquals(PathRating.OK, result1.get("author2"));
		assertEquals(PathRating.OK, result1.get("author3"));
		assertEquals(PathRating.OUT_OF_RANGE, result1.get("author4"));
		
		assertEquals(3, result2.size());
		assertEquals(PathRating.OUT_OF_RANGE, result2.get("author"));
		assertEquals(PathRating.OK, result2.get("author1"));
		assertEquals(PathRating.UNUSUAL, result2.get("author2"));
	}
}

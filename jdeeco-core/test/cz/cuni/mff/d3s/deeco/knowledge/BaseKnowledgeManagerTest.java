package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeChangeTrigger;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * BaseKnowledgeManager testing.
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 * 
 */
public class BaseKnowledgeManagerTest {

	private BaseKnowledgeManager tested;

	@Mock
	private TriggerListener triggerListener;

	@Before
	public void setUp() throws Exception {
		tested = new BaseKnowledgeManager("TEST", null);
		tested.update(createKnowledge());
		initMocks(this);
	}

	public static ChangeSet createKnowledge() {
		ChangeSet result = new ChangeSet();
		result.setValue(RuntimeModelHelper.createKnowledgePath("id"), "Test");
		result.setValue(RuntimeModelHelper.createKnowledgePath("number"), 10);
		result.setValue(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), "x");
		result.setValue(RuntimeModelHelper.createKnowledgePath("date"), null);
		List<Integer> list = new LinkedList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		result.setValue(RuntimeModelHelper.createKnowledgePath("list"), list);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		result.setValue(RuntimeModelHelper.createKnowledgePath("map"), map);
		result.setValue(
				RuntimeModelHelper.createKnowledgePath("innerKnowledge"),
				new InnerKnowledge("innerA", "innerB"));
		Map<String, String> mapNested = new HashMap<>();
		mapNested.put("x", "a");
		result.setValue(RuntimeModelHelper.createKnowledgePath("mapNested"), mapNested);
		
		return result;
	}

	@Test
	public void testUpdateIntegerField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for the 'number' field is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("number");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 17);

		tested.update(toUpdate, "X");
		// THEN when accessed the number field the KnowledgeManager should
		// return updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals(17, result.getValue(kp));
		assertEquals("X", tested.getAuthor(kp));
	}

	@Test
	public void testInnerKnowledgeUpdate() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for some nested inner field is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath(
				"innerKnowledge", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, "innerAModified");

		tested.update(toUpdate, "X");
		// THEN when accessed the inner knowledge the KnowledgeManager should
		// return updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals("innerAModified", result.getValue(kp));
		assertEquals("X", tested.getAuthor(kp));
		assertEquals("TEST", tested.getAuthor(RuntimeModelHelper.createKnowledgePath("innerKnowledge")));
	}

	@Test
	public void testUpdateListField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'list' items is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("list", "2");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 4);
		tested.update(toUpdate, "X");

		// THEN when accessed the item value the KnowledgeManager should return
		// updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals(4, result.getValue(kp));
		assertEquals("X", tested.getAuthor(kp));
		assertEquals("TEST", tested.getAuthor(RuntimeModelHelper.createKnowledgePath("list")));
	}

	@Test
	public void testUpdateMapField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'map' items is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 16);
		tested.update(toUpdate, "X");

		// THEN when accessed the item value the KnowledgeManager should return
		// updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals(16, result.getValue(kp));
		assertEquals("X", tested.getAuthor(kp));
		assertEquals("TEST", tested.getAuthor(RuntimeModelHelper.createKnowledgePath("map")));
	}

	@Test
	public void testRemovalFromList() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the removal of one of the 'list' items is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("list", "2");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);
		Object nextItemValue = tested.get(knowledgePaths).getValue(kp);
		kp = RuntimeModelHelper.createKnowledgePath("list", "1");
		knowledgePaths.clear();
		knowledgePaths.add(kp);

		ChangeSet toDelete = new ChangeSet();
		toDelete.setDeleted(kp);
		tested.update(toDelete);

		// THEN when accessed the deleted item index, the KnowledgeManager
		// should return
		// the value of the next item
		assertEquals(nextItemValue, tested.get(knowledgePaths).getValue(kp));
	}

	@Test(expected = KnowledgeNotFoundException.class)
	public void testDeleteFromMap() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the removal of one of the 'map' elements is
		// passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toDelete = new ChangeSet();
		toDelete.setDeleted(kp);
		tested.update(toDelete);

		// THEN when accessed the removed element value the KnowledgeManager
		// should throw the KnowledgeNotExistentException
		tested.get(knowledgePaths);
	}

	@Test
	public void testGetIntegerField() throws Exception {
		// WHEN a field is accessed from the ReadOnlyKnowledgeManager
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("number");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = tested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(10, result.getValue(kp));
	}

	@Test
	public void testInnerKnowledgeGet() throws Exception {
		// WHEN inner knowledge is accessed from the ReadOnlyKnowledgeManager
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath(
				"innerKnowledge", "a");

		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = tested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals("innerA", result.getValue(kp));
	}

	@Test(expected = KnowledgeNotFoundException.class)
	public void testNullBaseKnowledgeAccess() throws Exception {
		tested = new BaseKnowledgeManager("TEST", null);
		// WHEN a field is accessed from the knowledge manager initialized with
		// null base knowledge
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("number");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);
		// THEN exception is thrown.
		tested.get(knowledgePaths);
	}

	@Test
	public void testGetListField() throws Exception {
		// WHEN an item of a list is accessed from the ReadOnlyKnowledgeManager
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("list", "2");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = tested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(3, result.getValue(kp));
	}

	@Test
	public void testGetMapField() throws Exception {
		// WHEN an element of a map is accessed from the
		// ReadOnlyKnowledgeManager
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = tested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(1, result.getValue(kp));
	}

	@Test(expected = KnowledgeNotFoundException.class)
	public void testNotExsistentAccess() throws Exception {
		// WHEN a not existent entry is accessed from the
		// ReadOnlyKnowledgeManager
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("dummy");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		// THEN the ReadOnlyKnowledgeManager should throw the
		// KnowledgeNotExistentException
		tested.get(knowledgePaths);
	}

	@Test
	public void testRegisterListener() throws Exception {
		// WHEN a listener is registered at the KnowledgeManager
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("number");
		KnowledgeChangeTrigger trigger = RuntimeModelHelper
				.createKnowledgeChangeTrigger();
		trigger.setKnowledgePath(kp);
		tested.register(trigger, triggerListener);
		// and WHEN listener's releavant knowledge is updated
		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 17);
		tested.update(toUpdate);
		// THEN the listener is notify once.
		verify(triggerListener).triggered(trigger);
		verifyNoMoreInteractions(triggerListener);
	}

	@Test
	public void testUnregisterListener() throws Exception {
		// WHEN a previously registered listener
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("number");
		KnowledgeChangeTrigger trigger = RuntimeModelHelper
				.createKnowledgeChangeTrigger();
		trigger.setKnowledgePath(kp);
		tested.register(trigger, triggerListener);
		// is unregistered from the KnowledgeManager
		tested.register(trigger, triggerListener);
		// THEN it is not notified about knowledge changes any more
		verifyNoMoreInteractions(triggerListener);
	}

	@Test
	public void testGetRootKnowledge() throws Exception {
		// WHEN empty knowledge path is created
		KnowledgePath emptyKP = RuntimeModelHelper.createKnowledgePath();
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(emptyKP);
		// and WHEN it is used to access knowledge manager data
		ValueSet result = tested.get(knowledgePaths);
		// THEN as a result the knowledge manager should return its root
		// knowledge
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("id");
		assertTrue(result.getKnowledgePaths().contains(kp));
		kp = RuntimeModelHelper.createKnowledgePath("date");
		assertTrue(result.getKnowledgePaths().contains(kp));
	}

	public void testForbiddenUpdate() throws Exception {
		// WHEN the knowledge manager update is required, with one list element
		// removal and number field update and incorrect inner knowledge change
		KnowledgePath numberPath = RuntimeModelHelper
				.createKnowledgePath("number");
		KnowledgePath listElementPath = RuntimeModelHelper
				.createKnowledgePath("list", "1");
		KnowledgePath innerPath = RuntimeModelHelper.createKnowledgePath(
				"innerKnowledge", "a");
		ChangeSet toChange = new ChangeSet();
		toChange.setValue(numberPath, 100);
		toChange.setValue(innerPath, 66);
		toChange.setDeleted(listElementPath);

		boolean exceptionThrown = false;

		try {
			tested.update(toChange, "update_author");
		} catch (KnowledgeUpdateException e) {
			exceptionThrown = true;
		}
		// THEN the KnowledgeUpdateException is thrown
		assertTrue(exceptionThrown);

		KnowledgePath listPath = RuntimeModelHelper.createKnowledgePath("list");
		List<KnowledgePath> listOfPaths = new LinkedList<>();
		// and THEN list remains unchanged
		listOfPaths.add(listPath);
		assertEquals(3,
				((List<?>) tested.get(listOfPaths).getValue(listPath)).size());
		listOfPaths.clear();
		// and THEN inner knowledge has its original value
		listOfPaths.add(innerPath);
		assertEquals("innerA", tested.get(listOfPaths).getValue(innerPath));
		listOfPaths.clear();
		// and THEN number field has its original value
		listOfPaths.add(numberPath);
		assertEquals(10, tested.get(listOfPaths).getValue(numberPath));
		
		// then authors of knowledge remain the same
		assertEquals("TEST", tested.getAuthor(numberPath));
		assertEquals("TEST", tested.getAuthor(listElementPath));
		assertEquals("TEST", tested.getAuthor(innerPath));
	}

	@Test
	public void securityTagsTest() {
		// given single-noded knowledge path and security tags are prepared
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("field");
		KnowledgeSecurityTag tag = RuntimeMetadataFactory.eINSTANCE.createKnowledgeSecurityTag();
		tag.setRequiredRole(RuntimeMetadataFactory.eINSTANCE.createSecurityRole());
		tag.getRequiredRole().setRoleName("role");
		Collection<SecurityTag> expectedTags = Arrays.asList(tag);
		
		// when setSecurityTags() is called
		tested.setSecurityTags(kp, expectedTags);
		
		// when security tags are then retrieved
		KnowledgePath kp_same = RuntimeModelHelper.createKnowledgePath("field");
		List<KnowledgeSecurityTag> actualTags = tested.getKnowledgeSecurityTags((PathNodeField) kp_same.getNodes().get(0));
		
		// then collections are equal
		assertEquals(expectedTags, actualTags);
	}
	
	@Test
	public void addSecurityTagsTest() {
		// given single-noded knowledge path and security tags are prepared
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("field");
		KnowledgeSecurityTag tag = RuntimeMetadataFactory.eINSTANCE.createKnowledgeSecurityTag();
		tag.setRequiredRole(RuntimeMetadataFactory.eINSTANCE.createSecurityRole());
		tag.getRequiredRole().setRoleName("role");
		Collection<SecurityTag> expectedTags = Arrays.asList(tag);
		
		// when setSecurityTags() is called
		tested.setSecurityTags(kp, expectedTags);
		tested.addSecurityTag(kp, tag);
		
		// when security tags are then retrieved
		KnowledgePath kp_same = RuntimeModelHelper.createKnowledgePath("field");
		List<KnowledgeSecurityTag> actualTags = tested.getKnowledgeSecurityTags((PathNodeField) kp_same.getNodes().get(0));
		
		// then collections are equal
		assertEquals(2, actualTags.size());
		assertEquals(tag, actualTags.get(0));
		assertEquals(tag, actualTags.get(1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void markAsSecured_MultiNodePathTest() {
		// given multi-noded knowledge path and security tags are prepared
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("field", "inner");
		Collection<SecurityTag> expectedTags = Arrays.asList();
		
		// when setSecurityTags() is called
		tested.setSecurityTags(kp, expectedTags);
		
		// then exception is thrown
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void markAsSecured_IdPathTest() {
		// given single-noded knowledge path and security tags are prepared
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath();
		kp.getNodes().add(RuntimeMetadataFactory.eINSTANCE.createPathNodeComponentId());
		Collection<SecurityTag> expectedTags = Arrays.asList();
		
		// when setSecurityTags() is called
		tested.setSecurityTags(kp, expectedTags);
		
		// then exception is thrown
	}
	
	@Test
	public void getAuthorTest1() {
		// given basic knowledge is created
		KnowledgePath nestedPath = RuntimeModelHelper.createKnowledgePath("innerKnowledge", "a");
		KnowledgePath basicPath = RuntimeModelHelper.createKnowledgePath("innerKnowledge");
		
		// when nested path is used and getAuthor() called
		String innerAuthor = tested.getAuthor(nestedPath);
		String outerAuthor = tested.getAuthor(basicPath);
		
		// then author is returned
		assertEquals("TEST", innerAuthor);
		assertEquals("TEST", outerAuthor);
		
		// then knowledge paths remain intact
		assertEquals(RuntimeModelHelper.createKnowledgePath("innerKnowledge", "a"), nestedPath);
		assertEquals(RuntimeModelHelper.createKnowledgePath("innerKnowledge"), basicPath);
	}
	
	@Test
	public void getAuthorTest2() {
		// given basic knowledge is created
		KnowledgePath nonExistentPath = RuntimeModelHelper.createKnowledgePath("non", "existent", "path");
		
		// when getAuthor() is called
		String author = tested.getAuthor(nonExistentPath);
		
		// then null is returned
		assertNull(author);
	}
	
	@Test
	public void getAuthorTest3() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'map' items is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 16);
		tested.update(toUpdate, "X");

		// WHEN the 'map' itself is then updated by a different author
		KnowledgePath kp2 = RuntimeModelHelper.createKnowledgePath("map");
		List<KnowledgePath> knowledgePaths2 = new LinkedList<>();
		knowledgePaths2.add(kp2);

		ChangeSet toUpdate2 = new ChangeSet();
		toUpdate2.setValue(kp2, new HashMap<>());
		tested.update(toUpdate2, "Y");
		
		// THEN author of the 'map' and the 'map.a' is set to Y
		assertEquals("Y", tested.getAuthor(kp2));
		assertEquals("Y", tested.getAuthor(kp));
	}
	
	@Test
	public void getAuthorTest4() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'map' items is passed
		KnowledgePath kp = RuntimeModelHelper.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 16);
		tested.update(toUpdate, "X");

		// WHEN the 'map' itself is then deleted
		KnowledgePath kp2 = RuntimeModelHelper.createKnowledgePath("map");
		List<KnowledgePath> knowledgePaths2 = new LinkedList<>();
		knowledgePaths2.add(kp2);

		ChangeSet toUpdate2 = new ChangeSet();
		toUpdate2.setDeleted(kp2);
		tested.update(toUpdate2, "Y");
		
		// THEN author of the 'map' and the 'map.a' is null
		assertNull(tested.getAuthor(kp2));
		assertNull(tested.getAuthor(kp));
	}
	
	@Test
	public void lockPathTest1() {
		// when knowledge path is locked		
		tested.lockKnowledgePath(RuntimeModelHelper.createKnowledgePath("map"));
		
		// then isLocked returns true
		assertTrue(tested.isLocked(RuntimeModelHelper.createKnowledgePath("map")));
		assertFalse(tested.isLocked(RuntimeModelHelper.createKnowledgePath("map_not_locked")));
	}
	
	@Test
	public void lockPathTest2() {
		// when parent knowledge path is locked		
		tested.lockKnowledgePath(RuntimeModelHelper.createKnowledgePath("map"));
		
		// then isLocked on nested returns true
		assertTrue(tested.isLocked(RuntimeModelHelper.createKnowledgePath("map", "a")));		
	}
	
	
	public static class InnerKnowledge {
		public String a;
		public String b;

		public InnerKnowledge(String a, String b) {
			super();
			this.a = a;
			this.b = b;
		}
	}

}

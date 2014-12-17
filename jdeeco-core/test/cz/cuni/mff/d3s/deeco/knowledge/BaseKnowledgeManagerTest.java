package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

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

		tested.update(toUpdate);
		// THEN when accessed the number field the KnowledgeManager should
		// return updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals(17, result.getValue(kp));
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

		tested.update(toUpdate);
		// THEN when accessed the inner knowledge the KnowledgeManager should
		// return updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals("innerAModified", result.getValue(kp));
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
		tested.update(toUpdate);

		// THEN when accessed the item value the KnowledgeManager should return
		// updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals(4, result.getValue(kp));
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
		tested.update(toUpdate);

		// THEN when accessed the item value the KnowledgeManager should return
		// updated value
		ValueSet result = tested.get(knowledgePaths);
		assertEquals(16, result.getValue(kp));
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
			tested.update(toChange);
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

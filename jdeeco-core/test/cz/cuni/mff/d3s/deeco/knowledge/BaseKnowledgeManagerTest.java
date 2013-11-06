package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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

	private BaseKnowledgeManager toBeTested;

	@Mock
	private TriggerListener triggerListener;

	@Before
	public void setUp() {
		toBeTested = new BaseKnowledgeManager(new Knowledge());
		initMocks(this);
	}

	@Test
	public void testUpdateIntegerField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for the 'number' field is passed
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("number");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 17);

		toBeTested.update(toUpdate);
		// THEN when accessed the number field the KnowledgeManager should
		// return updated value
		ValueSet result = toBeTested.get(knowledgePaths);
		assertEquals(17, result.getValue(kp));
	}

	@Test
	public void testInnerKnowledgeUpdate() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for some nested inner field is passed
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath(
				"innerKnowledge", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, "innerAModified");

		toBeTested.update(toUpdate);
		// THEN when accessed the inner knowledge the KnowledgeManager should
		// return updated value
		ValueSet result = toBeTested.get(knowledgePaths);
		assertEquals("innerAModified", result.getValue(kp));
	}

	@Test
	public void testUpdateListField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'list' items is passed
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("list", "2");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 4);
		toBeTested.update(toUpdate);

		// THEN when accessed the item value the KnowledgeManager should return
		// updated value
		ValueSet result = toBeTested.get(knowledgePaths);
		assertEquals(4, result.getValue(kp));
	}

	@Test
	public void testUpdateMapField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'map' items is passed
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 16);
		toBeTested.update(toUpdate);

		// THEN when accessed the item value the KnowledgeManager should return
		// updated value
		ValueSet result = toBeTested.get(knowledgePaths);
		assertEquals(16, result.getValue(kp));
	}

	@Test
	public void testRemovalFromList() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the removal of one of the 'list' items is passed
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("list", "2");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);
		Object nextItemValue = toBeTested.get(knowledgePaths).getValue(kp);
		kp = KnowledgePathUtils.createKnowledgePath("list", "1");
		knowledgePaths.clear();
		knowledgePaths.add(kp);
		
		ChangeSet toDelete = new ChangeSet();
		toDelete.setDeleted(kp);
		toBeTested.update(toDelete);

		// THEN when accessed the deleted item index, the KnowledgeManager
		// should return
		// the value of the next item
		assertEquals(nextItemValue, toBeTested.get(knowledgePaths).getValue(kp));
	}

	@Test(expected = KnowledgeNotFoundException.class)
	public void testDeleteFromMap() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the removal of one of the 'map' elements is
		// passed
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ChangeSet toDelete = new ChangeSet();
		toDelete.setDeleted(kp);
		toBeTested.update(toDelete);

		// THEN when accessed the removed element value the KnowledgeManager
		// should throw the KnowledgeNotExistentException
		toBeTested.get(knowledgePaths);
	}

	@Test
	public void testGetIntegerField() throws Exception {
		// WHEN a field is accessed from the ReadOnlyKnowledgeManager
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("number");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = toBeTested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(10, result.getValue(kp));
	}

	@Test
	public void testInnerKnowledgeGet() throws Exception {
		// WHEN inner knowledge is accessed from the ReadOnlyKnowledgeManager
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath(
				"innerKnowledge", "a");

		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = toBeTested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals("innerA", result.getValue(kp));
	}

	@Test(expected = KnowledgeNotFoundException.class)
	public void testNullBaseKnowledgeAccess() throws Exception {
		toBeTested = new BaseKnowledgeManager();
		// WHEN a field is accessed from the knowledge manager initialized with
		// null base knowledge
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("number");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);
		// THEN exception is thrown.
		toBeTested.get(knowledgePaths);
	}

	@Test
	public void testGetListField() throws Exception {
		// WHEN an item of a list is accessed from the ReadOnlyKnowledgeManager
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("list", "2");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = toBeTested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(3, result.getValue(kp));
	}

	@Test
	public void testGetMapField() throws Exception {
		// WHEN an element of a map is accessed from the
		// ReadOnlyKnowledgeManager
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("map", "a");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = toBeTested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(1, result.getValue(kp));
	}

	@Test(expected = KnowledgeNotFoundException.class)
	public void testNotExsistentAccess() throws Exception {
		// WHEN a not existent entry is accessed from the
		// ReadOnlyKnowledgeManager
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("dummy");
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		// THEN the ReadOnlyKnowledgeManager should throw the
		// KnowledgeNotExistentException
		toBeTested.get(knowledgePaths);
	}

	@Test
	public void testRegisterListener() {
		// WHEN a listener is registered at the KnowledgeManager
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("number");
		KnowledgeChangeTrigger trigger = KnowledgePathUtils
				.createKnowledgeChangeTrigger();
		trigger.setKnowledgePath(kp);
		toBeTested.register(trigger, triggerListener);
		// and WHEN listener's releavant knowledge is updated
		ChangeSet toUpdate = new ChangeSet();
		toUpdate.setValue(kp, 17);
		toBeTested.update(toUpdate);
		// THEN the listener is notify once.
		verify(triggerListener).triggered(trigger);
		verifyNoMoreInteractions(triggerListener);
	}

	@Test
	public void testUnregisterListener() {
		// WHEN a previously registered listener
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath("number");
		KnowledgeChangeTrigger trigger = KnowledgePathUtils
				.createKnowledgeChangeTrigger();
		trigger.setKnowledgePath(kp);
		toBeTested.register(trigger, triggerListener);
		// is unregistered from the KnowledgeManager
		toBeTested.register(trigger, triggerListener);
		// THEN it is not notified about knowledge changes any more
		verifyNoMoreInteractions(triggerListener);
	}

	public static class Knowledge {
		public String id;
		public Integer number;
		public List<Integer> list;
		public Date date;
		public Map<String, Integer> map;
		public InnerKnowledge innerKnowledge;

		public Knowledge() {
			this.id = "Test";
			this.number = 10;
			this.list = new LinkedList<>();
			this.list.add(1);
			this.list.add(2);
			this.list.add(3);
			this.map = new HashMap<String, Integer>();
			this.map.put("a", 1);
			this.map.put("b", 2);
			this.map.put("c", 3);
			this.innerKnowledge = new InnerKnowledge("innerA", "innerB");
		}
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

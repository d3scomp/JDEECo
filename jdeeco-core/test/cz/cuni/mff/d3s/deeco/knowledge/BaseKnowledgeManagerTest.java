package cz.cuni.mff.d3s.deeco.knowledge;

/**
 * KnowledgeManagerImpl testing. 
 * 
 * @author Rima Al Ali <alali@d3s.mff.cuni.cz>
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.exceptions.KnowledgeNotExistentException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;

public class BaseKnowledgeManagerTest {

	private BaseKnowledgeManager toBeTested;

	@Before
	public void setUp() {
		toBeTested = new BaseKnowledgeManager(new Knowledge());
	}

	@Test
	public void testUpdateIntegerField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for the 'number' field is passed
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("number");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
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
	public void testUpdateListField() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the update for one of the 'list' items is passed
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("list");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("2");
		kp.getNodes().add(pnf);
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
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("map");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("a");
		kp.getNodes().add(pnf);
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
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("list");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("2");
		kp.getNodes().add(pnf);

		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		Object nextItemValue = toBeTested.get(knowledgePaths).getValue(kp);
		pnf.setName("1");

		ChangeSet toDelete = new ChangeSet();
		toDelete.setDeleted(kp);
		toBeTested.update(toDelete);

		// THEN when accessed the deleted item index, the KnowledgeManager
		// should return
		// the value of the next item
		assertEquals(nextItemValue, toBeTested.get(knowledgePaths).getValue(kp));
	}

	@Test(expected = KnowledgeNotExistentException.class)
	public void testDeleteFromMap() throws Exception {
		// WHEN the update method is called on the KnowledgeManager
		// and as a ChangeSet, the removal of one of the 'map' elements is
		// passed
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("map");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("a");
		kp.getNodes().add(pnf);
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
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("number");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = toBeTested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(10, result.getValue(kp));
	}

	@Test
	public void testGetListField() throws Exception {
		// WHEN an item of a list is accessed from the ReadOnlyKnowledgeManager
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("list");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("2");
		kp.getNodes().add(pnf);
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
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("map");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("a");
		kp.getNodes().add(pnf);
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		ValueSet result = toBeTested.get(knowledgePaths);
		// THEN the correct value is returned
		assertEquals(1, result.getValue(kp));
	}

	@Test(expected = KnowledgeNotExistentException.class)
	public void testNotExsistentAccess() throws Exception {
		// WHEN a not existent entry is accessed from the
		// ReadOnlyKnowledgeManager
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("dummy");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);

		// THEN the ReadOnlyKnowledgeManager should throw the
		// KnowledgeNotExistentException
		toBeTested.get(knowledgePaths);
	}

	public class Knowledge {
		public String id;
		public Integer number;
		public List<Integer> list;
		public Map<String, Integer> map;

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
		}
	}

}

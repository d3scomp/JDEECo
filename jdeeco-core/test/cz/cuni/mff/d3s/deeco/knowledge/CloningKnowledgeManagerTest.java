package cz.cuni.mff.d3s.deeco.knowledge;

import static org.junit.Assert.assertFalse;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManagerTest.Knowledge;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;

/**
 * @author Michal Kit <kit@d3s.mff.cuni.cz>
 *
 */
public class CloningKnowledgeManagerTest {

	private CloningKnowledgeManager toBeTested;

	@Before
	public void setUp() {
		toBeTested = new CloningKnowledgeManager(new Knowledge());
	}

	@Test
	public void testImmutabilityOfGetAndUpdate() throws Exception {
		// WHEN the update method is called on the CloningKnowledgeManager
		// and as a ChangeSet, the update for the 'number' field is passed
		PathNodeField pnf = KnowledgePathUtils.createPathNodeField();
		pnf.setName("number");
		KnowledgePath kp = KnowledgePathUtils.createKnowledgePath();
		kp.getNodes().add(pnf);
		List<KnowledgePath> knowledgePaths = new LinkedList<>();
		knowledgePaths.add(kp);
		
		ChangeSet toUpdate = new ChangeSet();
		Integer originalSeventeen = new Integer(17);
		toUpdate.setValue(kp, originalSeventeen);

		toBeTested.update(toUpdate);
		// THEN when accessed the number field the KnowledgeManager should
		// return updated value
		ValueSet result = toBeTested.get(knowledgePaths);
		Integer retrievedSeventeen = (Integer) result.getValue(kp);
		assertFalse(originalSeventeen == retrievedSeventeen);
	}
}

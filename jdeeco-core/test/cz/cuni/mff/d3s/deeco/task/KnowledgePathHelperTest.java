package cz.cuni.mff.d3s.deeco.task;

import static cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper.createKnowledgePath;
import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getStrippedPath;
import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot.COORDINATOR;
import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot.MEMBER;
import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;


/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgePathHelperTest {

	RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() throws Exception {
		initMocks(this);

		factory = RuntimeMetadataFactory.eINSTANCE;
	}
	
	@Test
	public void testgetStrippedPath() {
		// WHEN getStrippedPath is called with absolute path starting with coordinator prefix
		// THEN it returns a path without the coordinator prefix and indicates the coordinator prefix
		assertEquals(new KnowledgePathAndRoot(createKnowledgePath("level1", "level2"), COORDINATOR), 
				getStrippedPath(createKnowledgePath("<C>", "level1", "level2")));

		// WHEN getStrippedPath is called with absolute path starting with member prefix
		// THEN it returns a path without the member prefix and indicates the member prefix
		assertEquals(new KnowledgePathAndRoot(createKnowledgePath("level1", "level2"), MEMBER), 
				getStrippedPath(createKnowledgePath("<M>", "level1", "level2")));

		// WHEN getStrippedPath is called with absolute path without a prefix starting with member prefix
		// THEN it returns null
		assertEquals(null, getStrippedPath(createKnowledgePath("level1", "level2")));

		// WHEN getStrippedPath is called with a path which is not absolute
		KnowledgePath knowledgePath = RuntimeModelHelper.createKnowledgePath("<C>", "level1");
		knowledgePath.getNodes().add(factory.createPathNodeMapKey());
		// THEN it returns null
		assertEquals(null, getStrippedPath(knowledgePath));
	}	
}

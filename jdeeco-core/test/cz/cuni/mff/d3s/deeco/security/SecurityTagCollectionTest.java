package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ReadOnlyKnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class SecurityTagCollectionTest {
		
	private KnowledgeManager localKnowledgeManager, shadowKnowledgeManager;
	private ComponentInstance localComponent, shadowComponent;
	private RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() throws KnowledgeNotFoundException, KnowledgeUpdateException {
		initMocks(this);
	
		factory = RuntimeMetadataFactory.eINSTANCE;
		
		localComponent = factory.createComponentInstance();
		shadowComponent = factory.createComponentInstance();
		
		localKnowledgeManager = new CloningKnowledgeManager("123", localComponent);						
		shadowKnowledgeManager = new CloningKnowledgeManager("124", shadowComponent);		
		
		localComponent.setKnowledgeManager(localKnowledgeManager);
		shadowComponent.setKnowledgeManager(shadowKnowledgeManager);		
	}	
	
	@Test
	public void getSecurityTags_AbsolutePathTest1() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// given absolute path coord.mapKeyInner is prepared
		KnowledgePath absolutePath = RuntimeModelHelper.createKnowledgePath("<C>", "mapKeyInner");
		
		// when the path is secured
		KnowledgeSecurityTag tag = createSecurityTag("role");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), Arrays.asList(tag));
		
		// update the knowledge so that the path can be resolved
		localKnowledgeManager.update(createKnowledge());
		
		// when getSecurityTags() is called
		Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
		SecurityTagCollection collection = SecurityTagCollection.getSecurityTags(PathRoot.COORDINATOR, absolutePath, localKnowledgeManager, null, securityTagManager);
		assertEquals(1, collection.size());
		collection.stream().forEach(l -> assertEquals(1, l.size()));

		assertArrayEquals(Arrays.asList("role").toArray(), collection.get(0).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
	}
	
	@Test
	public void getSecurityTags_NonAbsolutePathTest1() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// given the path map[mapNested[mapKeyInner]] is prepared
		// and all three levels are secured
		KnowledgePath knowledgePath = RuntimeModelHelper.createKnowledgePath("<C>", "map");
		
		PathNodeMapKey pathNode = RuntimeMetadataFactory.eINSTANCE.createPathNodeMapKey();
		KnowledgePath keyPath = RuntimeModelHelper.createKnowledgePath("<C>", "mapNested");
		pathNode.setKeyPath(keyPath);
		knowledgePath.getNodes().add(pathNode);
	
		PathNodeMapKey innerPathNode = RuntimeMetadataFactory.eINSTANCE.createPathNodeMapKey();
		KnowledgePath innerKeyPath = RuntimeModelHelper.createKnowledgePath("<C>", "mapKeyInner");
		innerPathNode.setKeyPath(innerKeyPath);
		keyPath.getNodes().add(innerPathNode);
		
		// given path, keyPath and innerKeyPath are secured
		KnowledgeSecurityTag tagMap1 = createSecurityTag("role1a");
		KnowledgeSecurityTag tagMap2 = createSecurityTag("role1b");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map"), Arrays.asList(tagMap1, tagMap2));
		
		KnowledgeSecurityTag tagInner1 = createSecurityTag("role2a");
		KnowledgeSecurityTag tagInner2 = createSecurityTag("role2b");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), Arrays.asList(tagInner1, tagInner2));
		
		KnowledgeSecurityTag tagMapNested1 = createSecurityTag("role3a");
		KnowledgeSecurityTag tagMapNested2 = createSecurityTag("role3b");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapNested"), Arrays.asList(tagMapNested1, tagMapNested2));
		
		// update the knowledge so that the path can be resolved
		localKnowledgeManager.update(createKnowledge());
		
		// when non-absolute path is passed
		Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
		SecurityTagCollection collection = SecurityTagCollection.getSecurityTags(PathRoot.COORDINATOR, knowledgePath, localKnowledgeManager, null, securityTagManager);
		assertEquals(8, collection.size());
		collection.stream().forEach(l -> assertEquals(3, l.size()));

		assertArrayEquals(Arrays.asList("role1a", "role3a", "role2a").toArray(), collection.get(0).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1a", "role3a", "role2b").toArray(), collection.get(1).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1a", "role3b", "role2a").toArray(), collection.get(2).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1a", "role3b", "role2b").toArray(), collection.get(3).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1b", "role3a", "role2a").toArray(), collection.get(4).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1b", "role3a", "role2b").toArray(), collection.get(5).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1b", "role3b", "role2a").toArray(), collection.get(6).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());
		assertArrayEquals(Arrays.asList("role1b", "role3b", "role2b").toArray(), collection.get(7).stream().map(r -> ((KnowledgeSecurityTag)r).getRequiredRole().getRoleName()).toArray());	
	}
	
	@Test
	public void getSecurityTags_NonAbsolutePathTest2() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// given the path mapNested[mapKeyInner] is prepared
		// and only key level is secured
		KnowledgePath knowledgePath = RuntimeModelHelper.createKnowledgePath("<C>", "mapNested");
		
		PathNodeMapKey innerPathNode = RuntimeMetadataFactory.eINSTANCE.createPathNodeMapKey();
		KnowledgePath innerKeyPath = RuntimeModelHelper.createKnowledgePath("<C>", "mapKeyInner");
		innerPathNode.setKeyPath(innerKeyPath);
		knowledgePath.getNodes().add(innerPathNode);
		
		// given innerKeyPath is secured
		KnowledgeSecurityTag tagInner1 = createSecurityTag("role2a");
		KnowledgeSecurityTag tagInner2 = createSecurityTag("role2b");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), Arrays.asList(tagInner1, tagInner2));
		
		// update the knowledge so that the path can be resolved
		localKnowledgeManager.update(createKnowledge());
		
		// when non-absolute path is passed
		Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
		SecurityTagCollection collection = SecurityTagCollection.getSecurityTags(PathRoot.COORDINATOR, knowledgePath, localKnowledgeManager, null, securityTagManager);
		assertEquals(2, collection.size());
		collection.stream().forEach(l -> assertEquals(1, l.size()));

		assertEquals("role2a", ((KnowledgeSecurityTag) collection.get(0).get(0)).getRequiredRole().getRoleName());
		assertEquals("role2b", ((KnowledgeSecurityTag) collection.get(1).get(0)).getRequiredRole().getRoleName());	
		
		assertSame(localKnowledgeManager, securityTagManager.get(tagInner1));
		assertSame(localKnowledgeManager, securityTagManager.get(tagInner2));
	}
	
	@Test
	public void getSecurityTags_NonAbsolutePathTest3() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// given the path mapNested[mapKeyInner] is prepared
		// and only map level is secured
		KnowledgePath knowledgePath = RuntimeModelHelper.createKnowledgePath("<C>", "mapNested");
		
		PathNodeMapKey innerPathNode = RuntimeMetadataFactory.eINSTANCE.createPathNodeMapKey();
		KnowledgePath innerKeyPath = RuntimeModelHelper.createKnowledgePath("<C>", "mapKeyInner");
		innerPathNode.setKeyPath(innerKeyPath);
		knowledgePath.getNodes().add(innerPathNode);
		
		// given innerKeyPath is secured
		KnowledgeSecurityTag tagMap1 = createSecurityTag("role1a");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapNested"), Arrays.asList(tagMap1));
		
		KnowledgeSecurityTag tagInner1 = createSecurityTag("role2a");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), Arrays.asList(tagInner1));
		
		// update the knowledge so that the path can be resolved
		localKnowledgeManager.update(createKnowledge());
		
		// when non-absolute path is passed
		Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
		SecurityTagCollection collection = SecurityTagCollection.getSecurityTags(PathRoot.COORDINATOR, knowledgePath, localKnowledgeManager, null, securityTagManager);
		assertEquals(1, collection.size());
		collection.stream().forEach(l -> assertEquals(2, l.size()));

		assertEquals("role1a", ((KnowledgeSecurityTag) collection.get(0).get(0)).getRequiredRole().getRoleName());
		assertEquals("role2a", ((KnowledgeSecurityTag) collection.get(0).get(1)).getRequiredRole().getRoleName());	
		
		assertSame(localKnowledgeManager, securityTagManager.get(tagMap1));
		assertSame(localKnowledgeManager, securityTagManager.get(tagInner1));
	}
	
	@Test
	public void getSecurityTags_NonAbsolutePathTest4() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// given the path mapNested[mapKeyInner] is prepared
		// and map and key level is secured
		KnowledgePath knowledgePath = RuntimeModelHelper.createKnowledgePath("<C>", "mapNested");
		
		PathNodeMapKey innerPathNode = RuntimeMetadataFactory.eINSTANCE.createPathNodeMapKey();
		KnowledgePath innerKeyPath = RuntimeModelHelper.createKnowledgePath("<C>", "mapKeyInner");
		innerPathNode.setKeyPath(innerKeyPath);
		knowledgePath.getNodes().add(innerPathNode);
		
		// given innerKeyPath is secured
		KnowledgeSecurityTag tagInner1 = createSecurityTag("role2a");
		KnowledgeSecurityTag tagInner2 = createSecurityTag("role2b");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapNested"), Arrays.asList(tagInner1, tagInner2));
		
		// update the knowledge so that the path can be resolved
		localKnowledgeManager.update(createKnowledge());
		
		// when non-absolute path is passed
		Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
		SecurityTagCollection collection = SecurityTagCollection.getSecurityTags(PathRoot.COORDINATOR, knowledgePath, localKnowledgeManager, null, securityTagManager);
		assertEquals(2, collection.size());
		collection.stream().forEach(l -> assertEquals(1, l.size()));

		assertEquals("role2a", ((KnowledgeSecurityTag) collection.get(0).get(0)).getRequiredRole().getRoleName());
		assertEquals("role2b", ((KnowledgeSecurityTag) collection.get(1).get(0)).getRequiredRole().getRoleName());	
		
		assertSame(localKnowledgeManager, securityTagManager.get(tagInner1));
		assertSame(localKnowledgeManager, securityTagManager.get(tagInner2));
	}

	@Test
	public void getSecurityTags_MixedKnowledgeManagersTest() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// given the path coord.mapNested[member.mapKeyInner] is prepared
		// and map and key level is secured
		KnowledgePath knowledgePath = RuntimeModelHelper.createKnowledgePath("<C>", "mapNested");
		
		PathNodeMapKey innerPathNode = RuntimeMetadataFactory.eINSTANCE.createPathNodeMapKey();
		KnowledgePath innerKeyPath = RuntimeModelHelper.createKnowledgePath("<M>", "mapKeyInner");
		innerPathNode.setKeyPath(innerKeyPath);
		knowledgePath.getNodes().add(innerPathNode);
		
		// given innerKeyPath is secured
		KnowledgeSecurityTag tagMap = createSecurityTag("role2a");
		KnowledgeSecurityTag tagKey = createSecurityTag("role2b");
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapNested"), Arrays.asList(tagMap));
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), Arrays.asList(tagKey));
		
		// update the knowledge so that the path can be resolved
		localKnowledgeManager.update(createKnowledge());
		shadowKnowledgeManager.update(createKnowledge());
		
		// when non-absolute path is passed
		Map<SecurityTag, ReadOnlyKnowledgeManager> securityTagManager = new HashMap<>();
		SecurityTagCollection collection = SecurityTagCollection.getSecurityTags(PathRoot.COORDINATOR, knowledgePath, localKnowledgeManager, shadowKnowledgeManager, securityTagManager);
		assertEquals(1, collection.size());
		collection.stream().forEach(l -> assertEquals(2, l.size()));

		assertEquals("role2a", ((KnowledgeSecurityTag) collection.get(0).get(0)).getRequiredRole().getRoleName());
		assertEquals("role2b", ((KnowledgeSecurityTag) collection.get(0).get(1)).getRequiredRole().getRoleName());	
		
		assertSame(localKnowledgeManager, securityTagManager.get(tagMap));
		assertSame(shadowKnowledgeManager, securityTagManager.get(tagKey));
	}
	
	
	private KnowledgeSecurityTag createSecurityTag(String roleName, String... args) {
		KnowledgeSecurityTag tag = factory.createKnowledgeSecurityTag();
		tag.setRequiredRole(factory.createSecurityRole());
		tag.getRequiredRole().setRoleName(roleName);
		
		for (String arg : args) {
			PathSecurityRoleArgument argument = factory.createPathSecurityRoleArgument();
			argument.setKnowledgePath(RuntimeModelHelper.createKnowledgePath(arg));
			tag.getRequiredRole().getArguments().add(argument);
		}
		
		return tag;
	}
	
	private static ChangeSet createKnowledge() {
		ChangeSet result = new ChangeSet();
		result.setValue(RuntimeModelHelper.createKnowledgePath("mapKeyInner"), "x");
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("a", 1);
		map.put("b", 2);
		map.put("c", 3);
		result.setValue(RuntimeModelHelper.createKnowledgePath("map"), map);
		Map<String, String> mapNested = new HashMap<>();
		mapNested.put("x", "a");
		result.setValue(RuntimeModelHelper.createKnowledgePath("mapNested"), mapNested);
		
		return result;
	}
}

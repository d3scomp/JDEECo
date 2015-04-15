package cz.cuni.mff.d3s.deeco.task;

import static cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper.createKnowledgePath;
import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.getStrippedPath;
import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.isAbsolutePath;
import static cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.cloneKnowledgePath;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.TokenMgrError;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessor;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.KnowledgePathAndRoot;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;


/**
 * @author Tomas Bures <bures@d3s.mff.cuni.cz>
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 * 
 */
public class KnowledgePathHelperTest {

	RuntimeMetadataFactory factory;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		initMocks(this);

		factory = RuntimeMetadataFactory.eINSTANCE;
	}
	
	
	@Test 
	public void testCreateKnowledgePath() throws ParseException, AnnotationProcessorException {
		String pathStr = "level1.level2.level3";
		KnowledgePath kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level2");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level3");
		
		pathStr = "level1.[level21.level22.level23]";
		kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assert kp.getNodes().get(1) instanceof PathNodeMapKey;
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level21");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level22");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level23");
		
		pathStr = "level1.[level21.[level221.level222].level23]";
		kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level21");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level221");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level222");
		
		pathStr = "details.[id]";
		kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"details");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertTrue(kp.getNodes().get(0) instanceof PathNodeComponentId);

		pathStr = "level1.id.level2";
		kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"id");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level2");
		
		pathStr = "[coord.names]";
		kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),1);
		assertTrue(kp.getNodes().get(0) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(0)).getKeyPath();
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"coord");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"names");
		
		pathStr = "[coord.names]";
		kp = KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.ENSEMBLE);
		assertEquals(kp.getNodes().size(),1);
		assertTrue(kp.getNodes().get(0) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(0)).getKeyPath();
		assertTrue(kp.getNodes().get(0) instanceof PathNodeCoordinator);
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"names");
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath1() throws ParseException, AnnotationProcessorException {
		String pathStr = "namesToAddresses[member.name]";
		exception.expect(ParseException.class);
		exception.expectMessage(
				"The structure 'data1[data2]' is not allowed in a path, " +
				"use the dot separator: 'data1.[data2]'");
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath2() throws ParseException, AnnotationProcessorException {
		String pathStr = "namesToAddresses.[member.name";
		exception.expect(ParseException.class);
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath3() throws ParseException, AnnotationProcessorException {
		String pathStr = "level1..level2";
		exception.expect(ParseException.class);
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath4() throws ParseException, AnnotationProcessorException {
		String pathStr = "level1.  .level2";
		exception.expect(TokenMgrError.class);
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath5() throws ParseException, AnnotationProcessorException {
		String pathStr = "";
		exception.expect(ParseException.class);
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}

	@Test 
	public void testExceptionsInCreateKnowledgePath6() throws ParseException, AnnotationProcessorException {
		String pathStr = "id.level2";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"A component identifier cannot be followed by any other fields in a path.");
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath7() throws ParseException, AnnotationProcessorException {
		String pathStr = "details.[id.x]";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"A component identifier cannot be followed by any other fields in a path.");
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath8() throws ParseException, AnnotationProcessorException {
		String pathStr = "whatever.level2";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"The path does not start with one of the 'coord' or 'member' keywords.");
		// false means that the knowledge path is found in an ensemble definition:
		KnowledgePathHelper.createKnowledgePath(pathStr, PathOrigin.ENSEMBLE);		
	}

	
	
	
	@Test
	public void testGetAbsoluteStrippedPath0() throws KnowledgeNotFoundException {
		// WHEN component's path is 'coord.positions.[member.id].x'
		KnowledgePath path = factory.createKnowledgePath();
		
		PathNodeCoordinator coord = factory.createPathNodeCoordinator();
		path.getNodes().add(coord);
		
		PathNodeField field = factory.createPathNodeField();
		field.setName("positions");
		path.getNodes().add(field);
		
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
		KnowledgePath nestedPath = factory.createKnowledgePath();
			
			PathNodeMember member = factory.createPathNodeMember();
			nestedPath.getNodes().add(member);

			PathNodeComponentId cId = factory.createPathNodeComponentId();
			nestedPath.getNodes().add(cId);
			
		mapKey.setKeyPath(nestedPath);
		path.getNodes().add(mapKey);
		
		field = factory.createPathNodeField();
		field.setName("x");
		path.getNodes().add(field);
		
		// WHEN the id of member is '30'
		KnowledgeManager memberKnowledgeManager = new CloningKnowledgeManager("30", null, null);
		KnowledgeManager coordKnowledgeManager = new CloningKnowledgeManager("1", null, null);
		// THEN absolute path is 'positions.30.x' on the COORDINATOR side
		
		String originalPath = path.toString();
		KnowledgePathAndRoot res = KnowledgePathHelper.getAbsoluteStrippedPath(path, coordKnowledgeManager, memberKnowledgeManager);
		assertEquals(res.knowledgePath.toString(),"positions.30.x");
		assertEquals(res.root,PathRoot.COORDINATOR);
		
		// then original path is not changed
		String pathAfterCall = path.toString();
		assertEquals(originalPath, pathAfterCall); 
	}
	
	@Test
	public void testGetAbsoluteStrippedPath1() throws KnowledgeNotFoundException, KnowledgeUpdateException {
		// WHEN component's path is 'member.[coord.positions.[member.id]].x'
		KnowledgePath path = factory.createKnowledgePath();

		PathNodeMember member = factory.createPathNodeMember();
		path.getNodes().add(member);

		PathNodeMapKey mapKey1 = factory.createPathNodeMapKey();
			KnowledgePath nestedPath1 = factory.createKnowledgePath();
			
			PathNodeCoordinator coord = factory.createPathNodeCoordinator();
			nestedPath1.getNodes().add(coord);

			PathNodeField field = factory.createPathNodeField();
			field.setName("positions");
			nestedPath1.getNodes().add(field);
			
			PathNodeMapKey mapKey2 = factory.createPathNodeMapKey();
				KnowledgePath nestedPath2 = factory.createKnowledgePath();
				
				PathNodeMember member2 = factory.createPathNodeMember();
				nestedPath2.getNodes().add(member2);
				
				PathNodeComponentId cId = factory.createPathNodeComponentId();
				nestedPath2.getNodes().add(cId);
				
			mapKey2.setKeyPath(nestedPath2);
			nestedPath1.getNodes().add(mapKey2);
			
		mapKey1.setKeyPath(nestedPath1);
		path.getNodes().add(mapKey1);
		
		field = factory.createPathNodeField();
		field.setName("x");
		path.getNodes().add(field);
		
		// WHEN the id of MEMBER is '4'
		KnowledgeManager memberKnowledgeManager = new CloningKnowledgeManager("4", null, null);
		KnowledgeManager coordKnowledgeManager = new CloningKnowledgeManager("1", null, null);
		
		// WHEN '[positions.4]' on the COORDINATOR knowledge is evaluated to '50'
		ChangeSet cs = new ChangeSet();
		KnowledgePath evaluatedPath = factory.createKnowledgePath();
		field = factory.createPathNodeField();
		field.setName("positions");
		evaluatedPath.getNodes().add(field);
		field = factory.createPathNodeField();
		field.setName("4");
		evaluatedPath.getNodes().add(field);
		cs.setValue(evaluatedPath, "50");
		coordKnowledgeManager.update(cs);
		
		// THEN absolute path is '50.x' on the MEMBER side
		String originalPath = path.toString();
		KnowledgePathAndRoot res = KnowledgePathHelper.getAbsoluteStrippedPath(path, coordKnowledgeManager, memberKnowledgeManager);
		assertEquals(res.knowledgePath.toString(),"50.x");
		assertEquals(res.root,PathRoot.MEMBER);
		
		// then original path is not changed
		String pathAfterCall = path.toString();
		assertEquals(originalPath, pathAfterCall);
	}

	
	@Test
	public void testGetAbsolutePath0() throws KnowledgeNotFoundException, KnowledgeUpdateException {
		// WHEN component's path is 'level1.[level21.level22]'
		KnowledgePath path = factory.createKnowledgePath();
		
		PathNodeField field = factory.createPathNodeField();
		field.setName("level1");
		path.getNodes().add(field);
		
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
			KnowledgePath nestedPath = createKnowledgePath("level12","level22");
			
		mapKey.setKeyPath(nestedPath);
		path.getNodes().add(mapKey);
		// WHEN '[level21.level22]' is evaluated to '5'
		ChangeSet cs = new ChangeSet();
		cs.setValue(nestedPath, "5");
		KnowledgeManager km = new CloningKnowledgeManager("id", null, null);
		km.update(cs);
		// THEN absolute path is 'level1.5'
		KnowledgePath res = KnowledgePathHelper.getAbsolutePath(path, km);
		assertEquals(res.toString(),"level1.5");
	}
	
	@Test
	public void testGetAbsolutePath1() throws KnowledgeNotFoundException, KnowledgeUpdateException {
		// WHEN component's path is 'level1.[level21.level22.[level31]]'
		KnowledgePath path = factory.createKnowledgePath();
		
		PathNodeField  field = factory.createPathNodeField();
		field.setName("level1");
		path.getNodes().add(field);
		
		PathNodeMapKey mapKey1 = factory.createPathNodeMapKey();
			KnowledgePath nestedPath1 = factory.createKnowledgePath();
			
			field = factory.createPathNodeField();
			field.setName("level21");
			nestedPath1.getNodes().add(field);
		
			field = factory.createPathNodeField();
			field.setName("level22");
			nestedPath1.getNodes().add(field);
		
			PathNodeMapKey mapKey2 = factory.createPathNodeMapKey();
				KnowledgePath nestedPath2 = factory.createKnowledgePath();
			
				field = factory.createPathNodeField();
				field.setName("level31");
				nestedPath2.getNodes().add(field);
				
			mapKey2.setKeyPath(nestedPath2);
			nestedPath1.getNodes().add(mapKey2);
		
		mapKey1.setKeyPath(nestedPath1);
		path.getNodes().add(mapKey1);
		// WHEN '[level31]' is evaluated to '3'
		ChangeSet cs = new ChangeSet();
		cs.setValue(nestedPath2, "3");
		KnowledgeManager km = new CloningKnowledgeManager("id", null, null);
		km.update(cs);
		// WHEN '[level21.level22.3]' is evaluated to '5'
		KnowledgePath newNestedPath1 = EcoreUtil.copy(nestedPath1);
		newNestedPath1.getNodes().remove(2);
		field = factory.createPathNodeField();
		field.setName("3");
		newNestedPath1.getNodes().add(field);
		cs = new ChangeSet();
		cs.setValue(newNestedPath1, "5");
		km.update(cs);
		
		// THEN absolute path is 'level1.5' 
		String originalPath = path.toString();
		KnowledgePath res = KnowledgePathHelper.getAbsolutePath(path, km);
		assertEquals(res.toString(),"level1.5");
		
		// then original path is not changed
		String pathAfterCall = path.toString();
		assertEquals(originalPath, pathAfterCall);
	}
	
	@Test
	public void testGetAbsolutePath2() throws KnowledgeNotFoundException {
		// WHEN component's path is 'coordinates.[id].x'
		KnowledgePath path = factory.createKnowledgePath();
		PathNodeField  field = factory.createPathNodeField();
		field.setName("coordinates");
		path.getNodes().add(field);
		
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
			KnowledgePath nestedPath = factory.createKnowledgePath();
			
			PathNodeComponentId id = factory.createPathNodeComponentId();
			nestedPath.getNodes().add(id);
		
		mapKey.setKeyPath(nestedPath);
		path.getNodes().add(mapKey);
		
		field = factory.createPathNodeField();
		field.setName("x");
		path.getNodes().add(field);
		
		// WHEN '[id]' is evaluated to '42'
		KnowledgeManager km = new CloningKnowledgeManager("42", null, null);
		// THEN absolute path is 'coordinates.42.x'
		
		String originalPath = path.toString();
		KnowledgePath res = KnowledgePathHelper.getAbsolutePath(path, km);
		assertEquals(res.toString(),"coordinates.42.x");
		
		// then original path is not changed
		String pathAfterCall = path.toString();
		assertEquals(originalPath, pathAfterCall);
	}

	@Test
	public void testExceptionInGetAbsoluteStrippedPath() throws KnowledgeNotFoundException {
		// WHEN path is 'coord.names.[member.name]'
		KnowledgePath path = factory.createKnowledgePath();
		
		PathNodeCoordinator coord = factory.createPathNodeCoordinator();
		path.getNodes().add(coord);
		
		PathNodeField  field = factory.createPathNodeField();
		field.setName("names");
		path.getNodes().add(field);
		
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
			KnowledgePath nestedPath = factory.createKnowledgePath();
			
			PathNodeMember member = factory.createPathNodeMember();
			nestedPath.getNodes().add(member);
			
			field = factory.createPathNodeField();
			field.setName("name");
			nestedPath.getNodes().add(field);
		
		mapKey.setKeyPath(nestedPath);
		path.getNodes().add(mapKey);
		
		KnowledgeManager memberKnowledgeManager = new CloningKnowledgeManager("1", null, null);
		KnowledgeManager coordKnowledgeManager = new CloningKnowledgeManager("2", null, null);
		// WHEN [name] cannot be resolved on the MEMBER's knowledge
		// THEN a KnowledgeNotFoundException is thrown
		exception.expect(KnowledgeNotFoundException.class);
		KnowledgePathHelper.getAbsoluteStrippedPath(path, coordKnowledgeManager, memberKnowledgeManager);
	}

		
	@Test
	public void testExceptionInGetAbsolutePath() throws KnowledgeNotFoundException {
		// WHEN component's path is 'details.[name]'
		KnowledgePath path = factory.createKnowledgePath();
		
		PathNodeField  field = factory.createPathNodeField();
		field.setName("details");
		path.getNodes().add(field);
		
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
			KnowledgePath nestedPath = factory.createKnowledgePath();
			
			field = factory.createPathNodeField();
			field.setName("name");
			nestedPath.getNodes().add(field);
		
		mapKey.setKeyPath(nestedPath);
		path.getNodes().add(mapKey);

		KnowledgeManager km = new CloningKnowledgeManager("4324", null, null);
		// WHEN [name] cannot be resolved ('name' is not present in km's knowledge)
		// THEN a KnowledgeNotFoundException is thrown
		exception.expect(KnowledgeNotFoundException.class);
		KnowledgePathHelper.getAbsolutePath(path, km);
	}
		
		
	@Test
	public void testGetStrippedPath() {
		// WHEN getStrippedPath is called with absolute path starting with coordinator prefix
		// THEN it returns a path without the coordinator prefix and indicates the coordinator prefix
		assertEquals(new KnowledgePathAndRoot(createKnowledgePath("level1", "level2"), PathRoot.COORDINATOR), 
				getStrippedPath(createKnowledgePath("<C>", "level1", "level2")));

		// WHEN getStrippedPath is called with absolute path starting with member prefix
		// THEN it returns a path without the member prefix and indicates the member prefix
		assertEquals(new KnowledgePathAndRoot(createKnowledgePath("level1", "level2"), PathRoot.MEMBER), 
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
	
	@Test
	public void testIsAbsolutePath1() {
		// WHEN the path is absolute
		KnowledgePath knowledgePath = createKnowledgePath("level1", "level2");
		
		// THEN isAbsolutePath returns true
		assertTrue(isAbsolutePath(knowledgePath));
	}
	
	@Test
	public void testIsAbsolutePath2() {
		// WHEN the path is not absolute
		KnowledgePath knowledgePath = createKnowledgePath("level1", "level2");
		knowledgePath.getNodes().add(factory.createPathNodeMapKey());
		
		// THEN isAbsolutePath returns false
		assertFalse(isAbsolutePath(knowledgePath));
	}
	
	@Test
	public void testcloneKnowledgePath() {
		// WHEN the path is created
		KnowledgePath knowledgePath = createKnowledgePath("<C>", "level1", "level2");
		PathNodeMapKey mapKey = factory.createPathNodeMapKey();
		mapKey.setKeyPath(createKnowledgePath("<M>", "level1", "level2"));
		knowledgePath.getNodes().add(mapKey);
		
		// THEN cloneKnowledgePath returns deep copy		
		KnowledgePath copy= cloneKnowledgePath(knowledgePath);		
		
		assertEquals(knowledgePath, copy);
		
		// modify the original to verify deep clone
		((PathNodeMapKey)knowledgePath.getNodes().get(3)).getKeyPath().getNodes().remove(1);
		assertNotEquals(knowledgePath, copy);
	}
}

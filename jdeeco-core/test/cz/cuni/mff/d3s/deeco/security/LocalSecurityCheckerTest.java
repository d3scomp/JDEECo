package cz.cuni.mff.d3s.deeco.security;

import static org.mockito.MockitoAnnotations.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerContainer;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleController;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class LocalSecurityCheckerTest {
	
	LocalSecurityChecker target;
	
	@Mock
	EnsembleController ensembleController;
	
	@Mock
	KnowledgeManagerContainer kmContainer;
	
	@Mock
	EnsembleDefinition ensembleDefinition;
	
	Exchange knowledgeExchange;
	
	KnowledgeManager localKnowledgeManager, shadowKnowledgeManager;
	Parameter param1, param2, param3, param_error, param4;
	ComponentInstance localComponent, shadowComponent;
	RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() throws KnowledgeNotFoundException, KnowledgeUpdateException {
		initMocks(this);
		
		when(kmContainer.hasReplica("remote")).thenReturn(true);
		
		factory = RuntimeMetadataFactory.eINSTANCE;
		target = new LocalSecurityChecker(ensembleController, kmContainer);
		
		localComponent = factory.createComponentInstance();
		shadowComponent = factory.createComponentInstance();
		
		localKnowledgeManager = new CloningKnowledgeManager("123", localComponent);						
		shadowKnowledgeManager = new CloningKnowledgeManager("124", shadowComponent);		
		
		localComponent.setKnowledgeManager(localKnowledgeManager);
		shadowComponent.setKnowledgeManager(shadowKnowledgeManager);
		
		knowledgeExchange = factory.createExchange();
		when(ensembleController.getComponentInstance()).thenReturn(localComponent);
		when(ensembleController.getEnsembleDefinition()).thenReturn(ensembleDefinition);
		when(ensembleDefinition.getKnowledgeExchange()).thenReturn(knowledgeExchange);
		
		param1 = factory.createParameter();
		param2 = factory.createParameter();
		param3 = factory.createParameter();
		param4 = factory.createParameter();
		param_error = factory.createParameter();
	
		param1.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("<C>", "field1_oc"));
		param1.setKind(ParameterKind.INOUT);
		param2.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("<M>", "field1_om"));
		param2.setKind(ParameterKind.INOUT);
		param3.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("<C>", "field2_oc"));
		param3.setKind(ParameterKind.INOUT);
			
		KnowledgePath mapPath = RuntimeModelHelper.createKnowledgePath("<C>", "map");
		KnowledgePath innerPath = RuntimeModelHelper.createKnowledgePath("<C>", "mapKey");
		PathNodeMapKey innerNode = factory.createPathNodeMapKey();
		innerNode.setKeyPath(innerPath);
		mapPath.getNodes().add(innerNode);
		param4.setKnowledgePath(mapPath);
		param4.setKind(ParameterKind.INOUT);
		
		param_error.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("field1"));		
		
	}
	
	public static ChangeSet createKnowledge() {
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
	
	@Test
	public void checkSecurity_replicaTest() throws TaskInvocationException {
		// given replica is created
		KnowledgeManager km = new CloningKnowledgeManager("remote", localComponent);
		
		// when access to a replica is checked
		boolean result = target.checkSecurity(PathRoot.COORDINATOR, km);
		
		// then true is returned
		assertTrue(result);
	}
	
	@Test
	public void checkSecurity_localTest1() throws TaskInvocationException, KnowledgeUpdateException {
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.getMembership().getParameters().add(param1);
		ensembleDefinition.getMembership().getParameters().add(param2);
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
		ensembleDefinition.getKnowledgeExchange().getParameters().add(param3);
		when(ensembleController.getEnsembleDefinition()).thenReturn(ensembleDefinition);
		
		KnowledgeSecurityTag roleATagLocal = createSecurityTag("roleA", "some_param");
		KnowledgeSecurityTag roleATagShadow = createSecurityTag("roleA", "some_param");
		
		KnowledgeSecurityTag roleBTagLocal = createSecurityTag("roleB");
		KnowledgeSecurityTag roleBTagShadow = createSecurityTag("roleB");
		
		KnowledgeSecurityTag roleCTagLocal = createSecurityTag("roleC");
		KnowledgeSecurityTag roleCTagShadow = createSecurityTag("roleC");
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("field1_oc") , Arrays.asList(roleATagShadow));
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("field1_oc"), Arrays.asList(roleATagLocal));
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("field1_om"), Arrays.asList(roleCTagLocal));
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("field1_om"), Arrays.asList(roleCTagShadow));
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("field2_oc"), Arrays.asList(roleBTagShadow));
		localKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("field2_oc"), Arrays.asList(roleBTagLocal));

		ChangeSet changeSet = new ChangeSet();
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("some_param"), 123);
		shadowKnowledgeManager.update(changeSet);
		localKnowledgeManager.update(changeSet);
		
		SecurityRole roleA = factory.createSecurityRole();
		roleA.setRoleName("roleA");
		PathSecurityRoleArgument arg_some_param = factory.createPathSecurityRoleArgument();
		arg_some_param.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("some_param"));
		roleA.getArguments().add(arg_some_param);
		localComponent.getRoles().add(roleA);
		
		SecurityRole roleB = factory.createSecurityRole();
		roleB.setRoleName("roleB");
		localComponent.getRoles().add(roleB);
			
		SecurityRole roleC = factory.createSecurityRole();
		roleC.setRoleName("roleC");
		shadowComponent.getRoles().add(roleC);
		
		SecurityRole roleC2 = factory.createSecurityRole();
		roleC2.setRoleName("roleC");
		localComponent.getRoles().add(roleC2);
		
		// when checkSecurity() as coordinator is called
		assertFalse(target.checkSecurity(PathRoot.COORDINATOR, shadowKnowledgeManager));
		
		// when checkSecurity() as member is called
		assertTrue(target.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager));
		
		// switch components
		when(ensembleController.getComponentInstance()).thenReturn(shadowComponent);
		
		// when checkSecurity() as coordinator is called
		assertTrue(target.checkSecurity(PathRoot.COORDINATOR, localKnowledgeManager));
		
		// when checkSecurity() as member is called
		assertFalse(target.checkSecurity(PathRoot.MEMBER, localKnowledgeManager));
				
	}

	@Test
	public void checkSecurity_localTest2() throws TaskInvocationException, KnowledgeUpdateException {
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.getMembership().getParameters().add(param4);
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
		when(ensembleController.getEnsembleDefinition()).thenReturn(ensembleDefinition);
		
		KnowledgeSecurityTag roleBTagShadow = createSecurityTag("roleB");	
		KnowledgeSecurityTag roleCTagShadow = createSecurityTag("roleC");
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map"), Arrays.asList(roleCTagShadow));
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKey"), Arrays.asList(roleBTagShadow));
		
		shadowKnowledgeManager.update(createKnowledge());
		
		SecurityRole roleB = factory.createSecurityRole();
		roleB.setRoleName("roleB");
		localComponent.getRoles().add(roleB);
		
		// when checkSecurity() as member is called
		assertFalse(target.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager));
		
		SecurityRole roleC2 = factory.createSecurityRole();
		roleC2.setRoleName("roleC");
		localComponent.getRoles().add(roleC2);
		
		// when checkSecurity() as member is called
		assertTrue(target.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager));
				
	}
	
	@Test
	public void checkSecurity_localNestedPathTest() throws TaskInvocationException, KnowledgeUpdateException {
		// create parameter that uses nested paths: map[mapKey]
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.getMembership().getParameters().add(param4);
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
		when(ensembleController.getEnsembleDefinition()).thenReturn(ensembleDefinition);
		
		KnowledgeSecurityTag roleATag = createSecurityTag("roleA");
		KnowledgeSecurityTag roleBTag = createSecurityTag("roleB");
		
		// add security tags for map and key
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map") , Arrays.asList(roleATag));
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("mapKey") , Arrays.asList(roleBTag));

		ChangeSet changeSet = new ChangeSet();
		Map<Integer, Integer> map = new HashMap<>();
		map.put(123, 456);
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("mapKey"), 123);
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("map"), map);
		
		shadowKnowledgeManager.update(changeSet);
		
		// when checkSecurity() as member is called (missing roleA and roleB)
		assertFalse(target.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager));
				
		SecurityRole roleA = factory.createSecurityRole();
		roleA.setRoleName("roleA");
		localComponent.getRoles().add(roleA);
		
		// when checkSecurity() as member is called (missing roleB)
		assertFalse(target.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager));
		
		SecurityRole roleB = factory.createSecurityRole();
		roleB.setRoleName("roleB");
		localComponent.getRoles().add(roleB);
		
		// when checkSecurity() as member is called
		assertTrue(target.checkSecurity(PathRoot.MEMBER, shadowKnowledgeManager));
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
}

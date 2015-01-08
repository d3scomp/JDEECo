package cz.cuni.mff.d3s.deeco.security;

import static org.mockito.MockitoAnnotations.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

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
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterDirection;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;
import cz.cuni.mff.d3s.deeco.task.TaskInvocationException;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class SecurityCheckerTest {
	
	SecurityChecker target;
	
	@Mock
	EnsembleController ensembleController;
	
	@Mock
	KnowledgeManagerContainer kmContainer;
	
	KnowledgeManager localKnowledgeManager, shadowKnowledgeManager;
	Parameter param1, param2, param3, param_error;
	ComponentInstance localComponent, shadowComponent;
	RuntimeMetadataFactory factory;
	
	@Before
	public void setUp() throws KnowledgeNotFoundException, KnowledgeUpdateException {
		initMocks(this);
		
		when(kmContainer.hasReplica("remote")).thenReturn(true);
		
		factory = RuntimeMetadataFactory.eINSTANCE;
		target = new SecurityChecker(ensembleController, kmContainer);
		
		localComponent = factory.createComponentInstance();
		shadowComponent = factory.createComponentInstance();
		
		localKnowledgeManager = new CloningKnowledgeManager("123", localComponent);						
		shadowKnowledgeManager = new CloningKnowledgeManager("124", shadowComponent);		
		
		localComponent.setKnowledgeManager(localKnowledgeManager);
		shadowComponent.setKnowledgeManager(shadowKnowledgeManager);
		
		when(ensembleController.getComponentInstance()).thenReturn(localComponent);
		
		param1 = factory.createParameter();
		param2 = factory.createParameter();
		param3 = factory.createParameter();
		param_error = factory.createParameter();
	
		param1.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("<C>", "field1_oc"));
		param1.setDirection(ParameterDirection.INOUT);
		param2.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("<M>", "field1_om"));
		param2.setDirection(ParameterDirection.INOUT);
		param3.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("<C>", "field2_oc"));
		param3.setDirection(ParameterDirection.INOUT);
		param_error.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("field1"));		
		
	}
	
	@Test
	public void getPathsFrom_CoordTest() throws TaskInvocationException {
		// given parameters contain paths
		Collection<KnowledgePath> localPaths = new LinkedList<>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<>();
		Collection<Parameter> formalParams = new LinkedList<>();
		
		formalParams.add(param1);
		formalParams.add(param2);
		formalParams.add(param3);
		
		// when path root is set to coordinator
		target.getPathsFrom(PathRoot.COORDINATOR, formalParams, shadowKnowledgeManager, localPaths, shadowPaths);
		
		// then knowledge paths are correctly divided
		assertEquals(2, localPaths.size());
		assertTrue(localPaths.contains(RuntimeModelHelper.createKnowledgePath("field1_oc")));
		assertTrue(localPaths.contains(RuntimeModelHelper.createKnowledgePath("field2_oc")));
		
		assertEquals(1, shadowPaths.size());
		assertTrue(shadowPaths.contains(RuntimeModelHelper.createKnowledgePath("field1_om")));
	}
	
	@Test
	public void getPathsFrom_MemberTest() throws TaskInvocationException {
		// given parameters contain paths
		Collection<KnowledgePath> localPaths = new LinkedList<>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<>();
		Collection<Parameter> formalParams = new LinkedList<>();
		
		formalParams.add(param1);
		formalParams.add(param2);
		formalParams.add(param3);
		
		// when path root is set to member
		target.getPathsFrom(PathRoot.MEMBER, formalParams, shadowKnowledgeManager, localPaths, shadowPaths);
		
		// then knowledge paths are correctly divided
		assertEquals(1, localPaths.size());
		assertTrue(localPaths.contains(RuntimeModelHelper.createKnowledgePath("field1_om")));
		
		assertEquals(2, shadowPaths.size());
		assertTrue(shadowPaths.contains(RuntimeModelHelper.createKnowledgePath("field1_oc")));
		assertTrue(shadowPaths.contains(RuntimeModelHelper.createKnowledgePath("field2_oc")));		
	}
	
	@Test(expected = TaskInvocationException.class)
	public void getPathsFrom_ExceptionTest() throws TaskInvocationException {
		// given parameters contain path without member/coord
		Collection<KnowledgePath> localPaths = new LinkedList<>();
		Collection<KnowledgePath> shadowPaths = new LinkedList<>();
		Collection<Parameter> formalParams = new LinkedList<>();
		
		formalParams.add(param_error);		
		
		// when path root is set to coordinator
		target.getPathsFrom(PathRoot.COORDINATOR, formalParams, shadowKnowledgeManager, localPaths, shadowPaths);
		
		// then exception is thrown
	}
	
	@Test
	public void checkSecurity_replicaTest() throws TaskInvocationException {
		// given replica is created
		KnowledgeManager km = new CloningKnowledgeManager("remote", null);
		
		// when access to a replica is checked
		boolean result = target.checkSecurity(PathRoot.COORDINATOR, km);
		
		// then true is returned
		assertTrue(result);
	}
	
	@Test
	public void checkSecurity_localTest() throws TaskInvocationException, KnowledgeUpdateException {
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.getMembership().getParameters().add(param1);
		ensembleDefinition.getMembership().getParameters().add(param2);
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
		ensembleDefinition.getKnowledgeExchange().getParameters().add(param3);
		when(ensembleController.getEnsembleDefinition()).thenReturn(ensembleDefinition);
		
		KnowledgeSecurityTag roleATag = factory.createKnowledgeSecurityTag();
		roleATag.setRoleName("roleA");
		roleATag.getArguments().add(RuntimeModelHelper.createKnowledgePath("some_param"));
		
		KnowledgeSecurityTag roleBTag = factory.createKnowledgeSecurityTag();
		roleBTag.setRoleName("roleB");
		
		KnowledgeSecurityTag roleCTag = factory.createKnowledgeSecurityTag();
		roleCTag.setRoleName("roleC");
		
		shadowKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("field1_oc") , Arrays.asList(roleATag));
		localKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("field1_oc"), Arrays.asList(roleATag));
		localKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("field1_om"), Arrays.asList(roleCTag));
		shadowKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("field1_om"), Arrays.asList(roleCTag));
		shadowKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("field2_oc"), Arrays.asList(roleBTag));
		localKnowledgeManager.markAsSecured(RuntimeModelHelper.createKnowledgePath("field2_oc"), Arrays.asList(roleBTag));

		ChangeSet changeSet = new ChangeSet();
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("some_param"), 123);
		shadowKnowledgeManager.update(changeSet);
		localKnowledgeManager.update(changeSet);
		
		SecurityRole roleA = factory.createSecurityRole();
		roleA.setRoleName("roleA");
		roleA.getArguments().add(RuntimeModelHelper.createKnowledgePath("some_param"));
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
}

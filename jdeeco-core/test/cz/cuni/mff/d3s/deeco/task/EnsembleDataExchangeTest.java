package cz.cuni.mff.d3s.deeco.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.knowledge.ValueSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Condition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public class EnsembleDataExchangeTest {
	
	private static class TestEnsemble {
		@SuppressWarnings("unused")
		public static boolean membership(@In("coord.x") Integer c, @In("member.x") Integer m) {
			return c >= m;
		}
		
		@SuppressWarnings("unused")
		public static void exchange(@In("coord.x") Integer c, @In("member.x") Integer m,
				@InOut("coord.s") ParamHolder<String> cs, @Out("member.s") ParamHolder<String> ms) {
			cs.value += m.toString();
			ms.value = c.toString();
		}
	}
		
	private static class TestException extends Exception {
		public TestException() {
			super();
		}

		public TestException(Throwable cause) {
			super(cause);
		}
	}
	
	private EnsembleDefinition getTestEnsemble() throws TestException {
		try {
			RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
			EnsembleDefinition ed = factory.createEnsembleDefinition();
			ed.setName("TestEnsemble");
			
			Condition c = factory.createCondition();
			c.setMethod(TestEnsemble.class.getMethod("membership", Integer.class, Integer.class));
			Parameter cp1 = factory.createParameter();
			cp1.setKind(ParameterKind.IN);
			cp1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
			cp1.setType(Integer.class);
			cp1.setGenericType(Integer.class);
			c.getParameters().add(cp1);
			
			Parameter cp2 = factory.createParameter();
			cp2.setKind(ParameterKind.IN);
			cp2.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("member.x", PathOrigin.ENSEMBLE));
			cp2.setType(Integer.class);
			cp2.setGenericType(Integer.class);
			c.getParameters().add(cp2);
			ed.setMembership(c);
			
			Exchange e = factory.createExchange();
			e.setMethod(TestEnsemble.class.getMethod("exchange", Integer.class, Integer.class, ParamHolder.class, ParamHolder.class));
			// the first two parameters of exchange are identical to the first two parameters of the condition
			Parameter ep1 = factory.createParameter();
			ep1.setKind(ParameterKind.IN);
			ep1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
			ep1.setType(Integer.class);
			ep1.setGenericType(Integer.class);
			e.getParameters().add(ep1);
			
			Parameter ep2 = factory.createParameter();
			ep2.setKind(ParameterKind.IN);
			ep2.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("member.x", PathOrigin.ENSEMBLE));
			ep2.setType(Integer.class);
			ep2.setGenericType(Integer.class);
			e.getParameters().add(ep2);
			ed.setMembership(c);
			
			Parameter ep3 = factory.createParameter();
			ep3.setKind(ParameterKind.INOUT);
			ep3.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.s", PathOrigin.ENSEMBLE));
			ep3.setType(ParamHolder.class);
			ep3.setGenericType(e.getMethod().getGenericParameterTypes()[2]);
			e.getParameters().add(ep3);
			
			Parameter ep4 = factory.createParameter();
			ep4.setKind(ParameterKind.OUT);
			ep4.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("member.s", PathOrigin.ENSEMBLE));
			ep4.setType(ParamHolder.class);
			ep4.setGenericType(e.getMethod().getGenericParameterTypes()[3]);
			e.getParameters().add(ep4);
			ed.setKnowledgeExchange(e);
			
			return ed;
			
		} catch (NoSuchMethodException | SecurityException | ParseException | AnnotationProcessorException e) {
			throw new TestException(e);
		}
	}
	
	private KnowledgeManager getTestKnowledgeManager(Integer x, String s, Class<?>... roleClasses) throws TestException {
		try {
			KnowledgeManager result = new BaseKnowledgeManager(s, null, roleClasses);
			ChangeSet changeSet = new ChangeSet();
			changeSet.setValue(KnowledgePathHelper.createKnowledgePath("x", PathOrigin.COMPONENT), x);
			changeSet.setValue(KnowledgePathHelper.createKnowledgePath("s", PathOrigin.COMPONENT), s);
			result.update(changeSet);
			return result;
			
		} catch (ParseException | AnnotationProcessorException | KnowledgeUpdateException e) {
			throw new TestException(e);
		}
	}
	
	/*
	 * Membership condition tests
	 */
	
	@Test
	public void testMembershipNoRoles() throws TestException, TaskInvocationException {
		@Role
		class RoleClass1 { }
		
		@Role
		class RoleClass2 { }
		
		EnsembleDataExchange ede = new EnsembleDataExchange(getTestEnsemble(), null);
		KnowledgeManager firstKnowledgeManager = getTestKnowledgeManager(4, "ab");
		KnowledgeManager secondKnowledgeManager = getTestKnowledgeManager(5, "bc");
		
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// if I set any role to the components, it should not matter
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass2.class);
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
	}
	
	@Test
	public void testMembershipSameRole() throws TestException, TaskInvocationException {
		@Role
		class RoleClass1 { }
		
		@Role
		class RoleClass2 { }
		
		EnsembleDataExchange ede = new EnsembleDataExchange(getTestEnsemble(), null);
		ede.ensembleDefinition.setCoordinatorRole(RoleClass1.class);
		ede.ensembleDefinition.setMemberRole(RoleClass1.class);
		
		// when components have no roles defined, no membership is possible
		KnowledgeManager firstKnowledgeManager = getTestKnowledgeManager(4, "ab");
		KnowledgeManager secondKnowledgeManager = getTestKnowledgeManager(5, "bc");
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// both components have the correct role
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass1.class);
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// second component with wrong role
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass2.class);
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// first component with no role, second with correct role
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab");
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass1.class);
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// both components with wrong roles
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass2.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass2.class);
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
	}
	
	@Test
	public void testMembershipDifferentRoles() throws TestException, TaskInvocationException {
		@Role
		class RoleClass1 { }
		
		@Role
		class RoleClass2 { }
		
		EnsembleDataExchange ede = new EnsembleDataExchange(getTestEnsemble(), null);
		ede.ensembleDefinition.setCoordinatorRole(RoleClass1.class);
		ede.ensembleDefinition.setMemberRole(RoleClass2.class);
		
		// when components have no roles defined, no membership is possible
		KnowledgeManager firstKnowledgeManager = getTestKnowledgeManager(4, "ab");
		KnowledgeManager secondKnowledgeManager = getTestKnowledgeManager(5, "bc");
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));

		// both components have the correct role
		firstKnowledgeManager = getTestKnowledgeManager(5, "ab", RoleClass1.class);
		secondKnowledgeManager = getTestKnowledgeManager(4, "bc", RoleClass2.class);
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));

		// both components have the correct role (but the membership condition prevents creating the ensemble)
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass2.class);
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// second component with wrong role
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass1.class);
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
	}
	
	@Test
	public void testMembershipMultipleRoles() throws TestException, TaskInvocationException {
		@Role
		class RoleClass1 { }
		
		@Role
		class RoleClass2 { }
		
		@Role
		class RoleClass3 { }
				
		EnsembleDataExchange ede = new EnsembleDataExchange(getTestEnsemble(), null);
		ede.ensembleDefinition.setMemberRole(RoleClass1.class);
		ede.ensembleDefinition.setCoordinatorRole(RoleClass2.class);
		
		// correct role assignment
		KnowledgeManager firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class, RoleClass2.class);
		KnowledgeManager secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass1.class, RoleClass2.class);
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
		
		// member incorrect
		firstKnowledgeManager = getTestKnowledgeManager(4, "ab", RoleClass1.class, RoleClass2.class);
		secondKnowledgeManager = getTestKnowledgeManager(5, "bc", RoleClass1.class, RoleClass3.class);
		assertTrue(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, firstKnowledgeManager));
		assertTrue(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, firstKnowledgeManager, secondKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.COORDINATOR, secondKnowledgeManager, firstKnowledgeManager));
		assertFalse(ede.checkMembership(PathRoot.MEMBER, secondKnowledgeManager, firstKnowledgeManager));
	}
	
	/*
	 * Knowledge exchange tests
	 */
	
	private void assertKnowledge(KnowledgeManager km, Integer x, String s) throws TestException {
		try {
			KnowledgePath xPath = KnowledgePathHelper.createKnowledgePath("x", PathOrigin.COMPONENT);
			KnowledgePath sPath = KnowledgePathHelper.createKnowledgePath("s", PathOrigin.COMPONENT);
			ValueSet values = km.get(Arrays.asList(xPath, sPath));
			assertEquals(x, values.getValue(xPath));
			
		} catch (KnowledgeNotFoundException | ParseException
				| AnnotationProcessorException e) {
			throw new TestException(e);
		}
	}
	
	@Test
	public void testKnowledgeExchange() throws TestException, TaskInvocationException {		
		EnsembleDataExchange ede = new EnsembleDataExchange(getTestEnsemble(), null);
		KnowledgeManager firstKnowledgeManager = getTestKnowledgeManager(4, "ab");
		KnowledgeManager secondKnowledgeManager = getTestKnowledgeManager(5, "bc");
		
		ede.performExchange(PathRoot.COORDINATOR, firstKnowledgeManager, secondKnowledgeManager);
		assertKnowledge(firstKnowledgeManager, 4, "ab5");
		assertKnowledge(secondKnowledgeManager, 5, "4");
	}
}

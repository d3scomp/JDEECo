package cz.cuni.mff.d3s.deeco.annotations.checking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.checking.AnnotationCheckerException;
import cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathCheckException;
import cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathChecker;
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterException;
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor;
import cz.cuni.mff.d3s.deeco.annotations.checking.RolesAnnotationChecker;
import cz.cuni.mff.d3s.deeco.annotations.checking.TypeComparer;
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor.KnowledgePathAndType;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.WrongCE1;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

public class RolesAnnotationCheckerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	
	/*
	 * Checks for the validateComponent method
	 */
	
	/**
	 * Checks that the checkValidateComponent method calls the checkComponentRolesImplementation method
	 */
	@Test
	public void checkValidateComponentTest() throws AnnotationCheckerException {
		@Component
		class ComponentClass { }
		
		ComponentClass component = new ComponentClass();
		RolesAnnotationChecker rolesChecker = spy(new RolesAnnotationChecker(null, null));
		Mockito.doNothing().when(rolesChecker).checkComponentRolesImplementation(any());
		rolesChecker.validateComponent(component, null);
		
		InOrder io = Mockito.inOrder(rolesChecker);
		io.verify(rolesChecker).checkComponentRolesImplementation(component);
		io.verifyNoMoreInteractions();
	}
	
	@Test
	public void checkValidateComponentNullInstanceTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance cannot be null.");
		new RolesAnnotationChecker(null, null).validateComponent(null, null);
	}
	
	
	/*
	 * Checks for the validateEnsemble method
	 */

	@Test
	public void checkValidateEnsembleTest() throws AnnotationCheckerException {
		@Role
		class RoleClass1 { }
		
		@Role
		class RoleClass2 { }
		
		@Ensemble
		@CoordinatorRole(RoleClass1.class)
		@MemberRole(RoleClass2.class) 
		class EnsembleClass { }
		
		class IsListWithRoles extends ArgumentMatcher<Class<?>[]> {
			private Class<?>[] roleList;
			
			public IsListWithRoles(Class<?>[] roles) {
				roleList = roles;
			}
			
			@Override
			public boolean matches(Object argument) {
				Class<?>[] roles = (Class<?>[]) argument;
				return Arrays.equals(roles, roleList);
			}
		}
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
		
		RolesAnnotationChecker rolesChecker = spy(new RolesAnnotationChecker(null, null));
		Mockito.doNothing().when(rolesChecker).checkEnsembleMethodRolesImplementation(any(), any(), any());
		rolesChecker.validateEnsemble(EnsembleClass.class, ensembleDefinition);

		IsListWithRoles coordinatorRolesMatcher = new IsListWithRoles(new Class<?>[] {RoleClass1.class});
		IsListWithRoles memberRolesMatcher = new IsListWithRoles(new Class<?>[] {RoleClass2.class});
		verify(rolesChecker, times(1)).validateEnsemble(EnsembleClass.class, ensembleDefinition);
		verify(rolesChecker, times(1)).checkEnsembleMethodRolesImplementation(Mockito.refEq(ensembleDefinition.getMembership().getParameters()),
				Mockito.argThat(coordinatorRolesMatcher), Mockito.argThat(memberRolesMatcher));
		verify(rolesChecker, times(1)).checkEnsembleMethodRolesImplementation(Mockito.refEq(ensembleDefinition.getKnowledgeExchange().getParameters()), 
				Mockito.argThat(coordinatorRolesMatcher), Mockito.argThat(memberRolesMatcher));
		verifyNoMoreInteractions(rolesChecker);
	}
	
	@Test
	public void checkValidateEnsembleNullClassTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input ensemble class cannot be null.");
		new RolesAnnotationChecker(null, null).validateEnsemble(null, null);
	}
	
	@Test
	public void checkValidateEnsembleNullDefinitionTest() throws AnnotationCheckerException {
		@Ensemble
		class EnsembleClass { }
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input ensemble definition cannot be null.");
		new RolesAnnotationChecker(null, null).validateEnsemble(EnsembleClass.class, null);
	}
	
	@Test
	public void checkValidateEnsembleInvalidCoordinatorRoleTest() throws AnnotationCheckerException {
		class RoleClass { }
		
		@Ensemble
		@CoordinatorRole(RoleClass.class)
		class EnsembleClass { }
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
				
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The class RoleClass is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		new RolesAnnotationChecker(null, null).validateEnsemble(EnsembleClass.class, ensembleDefinition);
	}
	
	@Test
	public void checkValidateEnsembleInvalidMemRoleTest() throws AnnotationCheckerException {
		class RoleClass { }
		
		@Ensemble
		@MemberRole(RoleClass.class)
		class EnsembleClass { }
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		EnsembleDefinition ensembleDefinition = factory.createEnsembleDefinition();
		ensembleDefinition.setMembership(factory.createCondition());
		ensembleDefinition.setKnowledgeExchange(factory.createExchange());
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The class RoleClass is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		new RolesAnnotationChecker(null, null).validateEnsemble(EnsembleClass.class, ensembleDefinition);
	}
	
	/*
	 * Checks for the checkComponentRolesImplementation(Object) method
	 */
	
	@Test
	public void checkRI1WhenNoRolesTest() throws AnnotationCheckerException {
		@Component
		class ComponentClass {
			public String id;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		// even when no roles are present, still, 'String id' is checked to be present
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}
	
	@Test
	public void checkRI1WhenNoMissingIdTest() throws AnnotationCheckerException {
		@Component
		class ComponentClass {
			public String name;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field public String id, which is mandatory in component classes, is missing.");
		// when no roles are present, no checks are performed, therefore parameters can be null
		new RolesAnnotationChecker(null, null).checkComponentRolesImplementation(component);
	}
	
	@Test
	public void checkRI1NullInputTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance cannot be null.");
		new RolesAnnotationChecker(null, null).checkComponentRolesImplementation(null);
	}
	
	@Test
	public void checkRI1NonComponentTest() throws AnnotationCheckerException {
		WrongCE1 nonComponent = new WrongCE1();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		new RolesAnnotationChecker(null, null).checkComponentRolesImplementation(nonComponent);
	}
	
	@Test
	public void checkRI1ComponentWithPlaysRoleTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		// almost correct, but the @Component annotation is missing
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		new RolesAnnotationChecker(null, null).checkComponentRolesImplementation(component);
	}
	
	@Test
	public void checkRI1CorrectComponentWithRoleTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public String id; // not mandatory
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
		
		Mockito.verify(mock, times(1)).compareTypes(Mockito.eq(int.class), Mockito.eq(int.class));
		Mockito.verify(mock, Mockito.atLeast(1)).compareTypes(Mockito.eq(String.class), Mockito.eq(String.class));
			// for the ID (can be checked twice since the ID is present also in the role class
		Mockito.verifyNoMoreInteractions(mock);
	}
	
	@Test
	public void checkRI1MissingRoleAnnotationTest() throws AnnotationCheckerException {
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
		}
				
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The class RoleClass is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		// the role is invalid, but the annotation checker could first check the presence of ID
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}

	@Test
	public void checkRI1MissingFieldTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public int y;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		// we need the mock because ID field is present and its type may be checked
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}

	@Test
	public void checkRI1MissingIdFieldTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public int x;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field public String id, which is mandatory in component classes, is missing.");
		// we need the mock because the x field is present and its type may be checked
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}
	
	@Test
	public void checkRI1BadlyTypedFieldTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public String y;
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		Mockito.when(mock.compareTypes(eq(int.class), eq(int.class))).thenReturn(false); // let's assume that int does not equal to int
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}

	@Test
	public void checkRI1CorrectComplexComponentTest() throws AnnotationCheckerException, NoSuchFieldException, SecurityException {
		@Role
		class RoleClass {
			public int x;
			public String str;
			public Map<String, Integer> map;
			public List<List<String>> list;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public List<List<String>> map;
			public String str;
			public Map<String, String> list;
			public int x;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
		
		Type mapType = RoleClass.class.getField("map").getGenericType();
		Type listType = RoleClass.class.getField("list").getGenericType();
		Type map2Type = ComponentClass.class.getField("list").getGenericType();
		Mockito.verify(mock, times(1)).compareTypes(Mockito.eq(int.class), Mockito.eq(int.class));
		Mockito.verify(mock, times(2)).compareTypes(Mockito.eq(String.class), Mockito.eq(String.class)); // for the ID and str
		Mockito.verify(mock, times(1)).compareTypes(Mockito.eq(map2Type), Mockito.eq(listType)); // let's assume that list equals to map
		Mockito.verify(mock, times(1)).compareTypes(Mockito.eq(listType), Mockito.eq(mapType)); // ... and vice versa
		Mockito.verifyNoMoreInteractions(mock);
	}

	@Test
	public void checkRI1MultipleRolesCorrectTest() throws AnnotationCheckerException {
		@Role
		class RoleClass1 {
			public int x;
			public int y;
		}
		
		@Role
		class RoleClass2 {
			public int y;
			public int z;
		}
		
		@Role
		class RoleClass3 {
			public String str;
		}
		
		@Component
		@PlaysRole(RoleClass1.class)
		@PlaysRole(RoleClass2.class)
		@PlaysRole(RoleClass3.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
			public int z;
			public String str;
			public String k;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
		
		Mockito.verify(mock, times(4)).compareTypes(Mockito.eq(int.class), Mockito.eq(int.class));
				// RoleClass1.x, RoleClass1.y, RoleClass2.y, RoleClass2.z
		Mockito.verify(mock, times(2)).compareTypes(Mockito.eq(String.class), Mockito.eq(String.class));
				// for the ID and RoleClass3.str
		Mockito.verifyNoMoreInteractions(mock);
	}	
	
	@Test
	public void checkRI1MultipleRolesMissingAnnotationTest() throws AnnotationCheckerException {
		@Role
		class RoleClass1 {
			public int x;
			public int y;
		}
		
		class RoleClass2 {
			public int y;
			public int z;
		}
		
		@Role
		class RoleClass3 {
			public String str;
		}
		
		@Component
		@PlaysRole(RoleClass1.class)
		@PlaysRole(RoleClass2.class)
		@PlaysRole(RoleClass3.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
			public int z;
			public String str;
			public String k;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The class RoleClass2 is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		// the annotation checker could first check the RoleClass1 implementation, or presence of ID,
		// so we need the mock
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}
	
	@Test
	public void checkRI1MultipleRolesImpossibleTest() throws AnnotationCheckerException {
		@Role
		class RoleClass1 {
			public int x;
			public int y;
		}
		
		@Role
		class RoleClass2 {
			public long y;
			public long z;
		}
		
		@Component
		@PlaysRole(RoleClass1.class)
		@PlaysRole(RoleClass2.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
			public long z;
		}
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		Mockito.when(mock.compareTypes(int.class, long.class)).thenReturn(false);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass2.y is not implemented (or has a different type than long).");
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}	
		
	@Test
	public void checkRI1IgnoreFieldsInRoleTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
			private long z;
			protected String s;
			public static final int i = 0;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public int x;
			public int y;
		}	
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
		Mockito.verify(mock, times(1)).compareTypes(Mockito.eq(String.class), Mockito.eq(String.class)); // for the ID
		Mockito.verify(mock, times(1)).compareTypes(Mockito.eq(int.class), Mockito.eq(int.class)); // x
		Mockito.verifyNoMoreInteractions(mock); // other fields should be ignored
	}
	
	@Test
	public void checkRI1IgnoreNonpublicFieldsInComponentsTest() throws AnnotationCheckerException {
		@Component
		class ComponentClass {
			protected String id;
			public int x;
			public int y;
		}	
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field public String id, which is mandatory in component classes, is missing.");
		// the type comparer should not be used - but if the ID was wrongly assumed to be public,
		// we want the test to fail by not throwing the expected exception - not by throwing
		// a NullPointerException
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}	
	
	@Test
	public void checkRI1IgnoreStaticFieldsInComponentsTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public static final int x = 0;
			public int y;
		}	
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}
	
	@Test
	public void checkRI1IgnoreLocalFieldsInComponentsTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			@Local
			public int x = 0;
			public int y;
		}	
		
		TypeComparer mock = Mockito.mock(TypeComparer.class);
		Mockito.when(mock.compareTypes(any(), any())).thenReturn(true);
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker(null, mock).checkComponentRolesImplementation(component);
	}
	
	
	
	/*
	 * Checks for the checkRolesImplementation(List<Parameter>, CoordinatorRole[], MemberRole[]) method
	 */

	@Test
	public void checkRI2NoRolesTest() throws AnnotationCheckerException, KnowledgePathCheckException {
		KnowledgePathChecker mock = Mockito.mock(KnowledgePathChecker.class);
		
		RolesAnnotationChecker checker = new RolesAnnotationChecker(mock, null);
		Mockito.doReturn(true).when(mock).isFieldInClass(any(), any(), any());
		
		checker.checkEnsembleMethodRolesImplementation(new ArrayList<Parameter>(), new Class<?>[0], new Class<?>[0]);
		
		verifyNoMoreInteractions(mock);
	}
	
	@Test
	public void checkRI2NullParametersTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input parameters cannot be null.");
		new RolesAnnotationChecker(null, null).checkEnsembleMethodRolesImplementation(null, new Class<?>[0], new Class<?>[0]);
	}	
	
	@Test
	public void checkRI2NullCoordinatorRolesTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The coordinatorRoles parameter cannot be null.");
		new RolesAnnotationChecker(null, null).checkEnsembleMethodRolesImplementation(new ArrayList<Parameter>(), null, new Class<?>[0]);
	}
	
	@Test
	public void checkRI2NullMemberRolesTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The memberRoles parameter cannot be null.");
		new RolesAnnotationChecker(null, null).checkEnsembleMethodRolesImplementation(new ArrayList<Parameter>(), new Class<?>[0], null);
	}

	private static Parameter[] testParameters = new Parameter[] {
		RuntimeMetadataFactory.eINSTANCE.createParameter(),
		RuntimeMetadataFactory.eINSTANCE.createParameter()
	};
	
	private static KnowledgePathAndType[] testKnowledgePaths;
	static {
		try {
			testKnowledgePaths = new KnowledgePathAndType[] {
				new KnowledgePathAndType(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE).getNodes(), Integer.class),
				new KnowledgePathAndType(KnowledgePathHelper.createKnowledgePath("coord.a.b", PathOrigin.ENSEMBLE).getNodes(), null),
				new KnowledgePathAndType(KnowledgePathHelper.createKnowledgePath("member.s", PathOrigin.ENSEMBLE).getNodes(), String.class)
			};
		} catch (ParseException | AnnotationProcessorException e) {
			testKnowledgePaths = null;
		}
	};
	
	private ParameterKnowledgePathExtractor getKnowledgeExtractorMock() throws ParameterException, ParseException, AnnotationProcessorException {
		ParameterKnowledgePathExtractor mock = Mockito.mock(ParameterKnowledgePathExtractor.class);
		Mockito.when(mock.extractAllKnowledgePaths(eq(testParameters[0]))).thenReturn(
				Arrays.asList(testKnowledgePaths[0], testKnowledgePaths[1]));
		Mockito.when(mock.extractAllKnowledgePaths(eq(testParameters[1]))).thenReturn(
				Arrays.asList(testKnowledgePaths[1], testKnowledgePaths[2]));
		return mock;
	}
	
	@Test
	public void checkRI2ParametersButNoRolesTest() throws AnnotationCheckerException, ParseException, AnnotationProcessorException, ParameterException, KnowledgePathCheckException {
		ParameterKnowledgePathExtractor extractorMock = getKnowledgeExtractorMock();
		KnowledgePathChecker checkerMock = Mockito.mock(KnowledgePathChecker.class);
		Mockito.doReturn(true).when(checkerMock).isFieldInClass(any(), any(), any());
		
		RolesAnnotationChecker checker = new RolesAnnotationChecker(checkerMock, null, extractorMock);
		checker.checkEnsembleMethodRolesImplementation(Arrays.asList(testParameters), new Class<?>[0], new Class<?>[0]);
		
		verifyNoMoreInteractions(checkerMock);
	}
	
	@Test
	public void checkRI2RolesButNoParametersTest() throws AnnotationCheckerException, ParseException, KnowledgePathCheckException {
		
		@Role
		class RoleClass1 {
			public Integer x;
			public Integer y;
		}
		
		Class<?>[] roles = new Class<?>[] {RoleClass1.class};
		
		KnowledgePathChecker checkerMock = Mockito.mock(KnowledgePathChecker.class);
		Mockito.doReturn(true).when(checkerMock).isFieldInClass(any(), any(), any());
		
		RolesAnnotationChecker checker = new RolesAnnotationChecker(checkerMock, null, null);		
		checker.checkEnsembleMethodRolesImplementation(new ArrayList<Parameter>(), roles, roles);
		
		verifyNoMoreInteractions(checkerMock);
	}
	
	@Test
	public void checkRI2ExampleTest() throws AnnotationCheckerException, ParseException, AnnotationProcessorException, KnowledgePathCheckException, ParameterException {
		
		@Role
		class RoleClass1 {
			public Integer x;
			public Integer y;
		}
		
		@Role
		class RoleClass2 {
			public Integer y;
			public HashMap<Integer, String> z;
		}
		
		@Role
		class RoleClass3 {
			public String str;
		}
		
		Class<?>[] coordinatorRoles = new Class<?>[] {RoleClass1.class, RoleClass2.class};
		Class<?>[] memberRoles = new Class<?>[] {RoleClass2.class, RoleClass3.class};
		
		ParameterKnowledgePathExtractor extractorMock = getKnowledgeExtractorMock();
		KnowledgePathChecker checkerMock = Mockito.mock(KnowledgePathChecker.class);
		Mockito.doReturn(true).when(checkerMock).isFieldInClass(any(), any(), any());
		Mockito.doReturn(false).when(checkerMock).isFieldInClass(any(), any(), Mockito.eq(RoleClass2.class));
		
		RolesAnnotationChecker checker = new RolesAnnotationChecker(checkerMock, null, extractorMock);
		
		// It is sufficient if the field is present in at least one role. When the isFieldInRole
		// method returns always true, only the first role has to be tested (remaining are optional)
		// When the isFieldRole returns false, then also the second role has to be tested (and this
		// must return true, else we would get an exception)
		checker.checkEnsembleMethodRolesImplementation(Arrays.asList(testParameters), coordinatorRoles, memberRoles);	
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[0].type, testKnowledgePaths[0].knowledgePath, RoleClass1.class);
		verify(checkerMock, atMost(1)).isFieldInClass(testKnowledgePaths[0].type, testKnowledgePaths[0].knowledgePath, RoleClass2.class);
		verify(checkerMock, times(2)).isFieldInClass(testKnowledgePaths[1].type, testKnowledgePaths[1].knowledgePath, RoleClass1.class);
		verify(checkerMock, atMost(2)).isFieldInClass(testKnowledgePaths[1].type, testKnowledgePaths[1].knowledgePath, RoleClass2.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[2].type, testKnowledgePaths[2].knowledgePath, RoleClass2.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[2].type, testKnowledgePaths[2].knowledgePath, RoleClass3.class);
		
		verifyNoMoreInteractions(checkerMock);
	}
	
	@Test
	public void checkRI2WrongKnowledgePathTest() throws AnnotationCheckerException, ParseException, AnnotationProcessorException, KnowledgePathCheckException, ParameterException {
		
		@Role
		class RoleClass1 {
			public Integer x;
			public Integer y;
		}
		
		@Role
		class RoleClass2 {
			public Integer x;
			public String z;
			
		}
		
		Class<?>[] roles = new Class<?>[] {RoleClass1.class, RoleClass2.class};
		
		ParameterKnowledgePathExtractor extractorMock = getKnowledgeExtractorMock();
		KnowledgePathChecker checkerMock = Mockito.mock(KnowledgePathChecker.class);
		Mockito.doReturn(false).when(checkerMock).isFieldInClass(any(), any(), any());
		
		RolesAnnotationChecker checker = new RolesAnnotationChecker(checkerMock, null, extractorMock);
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("Parameter 1: Knowledge path '<COORDINATOR>.x' of type " + Integer.class + ": The knowledge path is not valid for any of the roles: RoleClass1, RoleClass2. Check whether the field (or sequence of fields) exists in the role and that it has correct type(s) and is public, nonstatic and non@Local");
		
		AnnotationCheckerException ex = null;
		try {
			checker.checkEnsembleMethodRolesImplementation(Arrays.asList(testParameters), roles, roles);
		} catch (AnnotationCheckerException e) {
			ex = e;
		}
		
		// verify that both roles were tested
		checker.checkEnsembleMethodRolesImplementation(Arrays.asList(testParameters), roles, roles);	
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[0].type, testKnowledgePaths[0].knowledgePath, RoleClass1.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[0].type, testKnowledgePaths[0].knowledgePath, RoleClass2.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[1].type, testKnowledgePaths[1].knowledgePath, RoleClass1.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[1].type, testKnowledgePaths[1].knowledgePath, RoleClass2.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[2].type, testKnowledgePaths[2].knowledgePath, RoleClass1.class);
		verify(checkerMock, times(1)).isFieldInClass(testKnowledgePaths[2].type, testKnowledgePaths[2].knowledgePath, RoleClass2.class);
		
		verifyNoMoreInteractions(checkerMock);
		
		if (ex != null)
			throw ex;
	}
}

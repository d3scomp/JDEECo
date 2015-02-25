package cz.cuni.mff.d3s.deeco.annotations.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.WrongCE1;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

public class RolesAnnotationCheckerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	
	/*
	 * Checks for the validateComponent method
	 */
	
	@Test
	public void checkValidateComponentTest() throws AnnotationCheckerException {
		@Component
		class ComponentClass { }
		
		ComponentClass component = new ComponentClass();
		RolesAnnotationChecker rolesChecker = spy(new RolesAnnotationChecker());
		Mockito.doNothing().when(rolesChecker).checkRolesImplementation(any());
		rolesChecker.validateComponent(component, null);
		
		InOrder io = Mockito.inOrder(rolesChecker);
		io.verify(rolesChecker).checkRolesImplementation(component);
		io.verifyNoMoreInteractions();
	}
	
	@Test
	public void checkValidateComponentNullInstanceTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance cannot be null.");
		new RolesAnnotationChecker().validateComponent(null, null);
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
		
		RolesAnnotationChecker rolesChecker = spy(new RolesAnnotationChecker());
		Mockito.doNothing().when(rolesChecker).checkRolesImplementation(any(), any(), any());
		rolesChecker.validateEnsemble(EnsembleClass.class, ensembleDefinition);

		IsListWithRoles coordinatorRolesMatcher = new IsListWithRoles(new Class<?>[] {RoleClass1.class});
		IsListWithRoles memberRolesMatcher = new IsListWithRoles(new Class<?>[] {RoleClass2.class});
		verify(rolesChecker, times(1)).validateEnsemble(EnsembleClass.class, ensembleDefinition);
		verify(rolesChecker, times(1)).checkRolesImplementation(Mockito.refEq(ensembleDefinition.getMembership().getParameters()),
				Mockito.argThat(coordinatorRolesMatcher), Mockito.argThat(memberRolesMatcher));
		verify(rolesChecker, times(1)).checkRolesImplementation(Mockito.refEq(ensembleDefinition.getKnowledgeExchange().getParameters()), 
				Mockito.argThat(coordinatorRolesMatcher), Mockito.argThat(memberRolesMatcher));
		verifyNoMoreInteractions(rolesChecker);
	}
	
	@Test
	public void checkValidateEnsembleNullClassTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input ensemble class cannot be null.");
		new RolesAnnotationChecker().validateEnsemble(null, null);
	}
	
	@Test
	public void checkValidateEnsembleNullDefinitionTest() throws AnnotationCheckerException {
		@Ensemble
		class EnsembleClass { }
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input ensemble definition cannot be null.");
		new RolesAnnotationChecker().validateEnsemble(EnsembleClass.class, null);
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
		new RolesAnnotationChecker().validateEnsemble(EnsembleClass.class, ensembleDefinition);
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
		new RolesAnnotationChecker().validateEnsemble(EnsembleClass.class, ensembleDefinition);
	}
	
	/*
	 * Checks for the checkRolesImplementation(Object) method
	 */
	
	@Test
	public void checkRI1WhenNoRolesTest() throws AnnotationCheckerException {
		@Component
		class ComponentClass {
			public String id;
		}
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1NullInputTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance cannot be null.");
		new RolesAnnotationChecker().checkRolesImplementation(null);
	}
	
	@Test
	public void checkRI1NonComponentTest() throws AnnotationCheckerException {
		WrongCE1 nonComponent = new WrongCE1();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		new RolesAnnotationChecker().checkRolesImplementation(nonComponent);
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
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The class RoleClass is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field public String id, which is mandatory in component classes, is missing.");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1BadlyTypedFieldTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public String id;
			public String x;
			public int y;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1CorrectComplexComponentTest() throws AnnotationCheckerException {
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
			public Map<String, Integer> map;
			public String str;
			public List<List<String>> list;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1SubtypeFieldTest() throws AnnotationCheckerException {
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
			public HashMap<String, Integer> map; // this should not be allowed (TODO or should it?)
			public String str;
			public List<List<String>> list;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.map is not implemented (or has a different type than java.util.Map<java.lang.String, java.lang.Integer>).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1BadlyTypedGenericArgumentTest() throws AnnotationCheckerException {
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
			public Map<String, Long> map; // value type should be Integer
			public String str;
			public List<List<String>> list;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.map is not implemented (or has a different type than java.util.Map<java.lang.String, java.lang.Integer>).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1BadlyTypedGenericArgumentsArgumentTest() throws AnnotationCheckerException {
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
			public Map<String, Integer> map;
			public String str;
			public List<List<Integer>> list; // inner type should be String
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.list is not implemented (or has a different type than java.util.List<java.util.List<java.lang.String>>).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The class RoleClass2 is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass2.y is not implemented (or has a different type than long).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1IgnoreNonpublicFieldsInComponentsTest() throws AnnotationCheckerException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			protected String id;
			public int x;
			public int y;
		}	
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field public String id, which is mandatory in component classes, is missing.");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
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
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	
	
	/*
	 * Checks for the checkRolesImplementation(List<Parameter>, CoordinatorRole[], MemberRole[]) method
	 */
	
	@Test
	public void checkRI2NoRolesTest() throws AnnotationCheckerException {
		RolesAnnotationChecker checker = spy(new RolesAnnotationChecker());
		when(checker.isFieldInRole(any(), any(), any())).thenReturn(true);
		
		checker.checkRolesImplementation(new ArrayList<Parameter>(), new Class<?>[0], new Class<?>[0]);
		
		verify(checker, times(1)).checkRolesImplementation(any(), any(), any());
		verifyNoMoreInteractions(checker);
	}
	
	@Test
	public void checkRI2NullParametersTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input parameters cannot be null.");
		new RolesAnnotationChecker().checkRolesImplementation(null, new Class<?>[0], new Class<?>[0]);
	}	
	
	@Test
	public void checkRI2NullCoordinatorRolesTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The coordinatorRoles parameter cannot be null.");
		new RolesAnnotationChecker().checkRolesImplementation(new ArrayList<Parameter>(), null, new Class<?>[0]);
	}
	
	@Test
	public void checkRI2NullMemberRolesTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The memberRoles parameter cannot be null.");
		new RolesAnnotationChecker().checkRolesImplementation(new ArrayList<Parameter>(), new Class<?>[0], null);
	}
	
	private List<Parameter> getTestParameters() throws ParseException, AnnotationCheckerException, AnnotationProcessorException {	
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.IN);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setGenericType(Integer.class);
		Parameter param2 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param2.setKind(ParameterKind.OUT);
		param2.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.a.b", PathOrigin.ENSEMBLE));
		param2.setGenericType(Long.class);
		Parameter param3 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param3.setKind(ParameterKind.INOUT);
		param3.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.z.[member.y.[coord.id]]", PathOrigin.ENSEMBLE));
		param3.setGenericType(String.class);
		return Arrays.asList(param1, param2, param3);
	}
	
	@Test
	public void checkRI2ParametersButNoRolesTest() throws AnnotationCheckerException, ParseException, AnnotationProcessorException {
		RolesAnnotationChecker checker = spy(new RolesAnnotationChecker());
		when(checker.isFieldInRole(any(), any(), any())).thenReturn(false);
		
		checker.checkRolesImplementation(getTestParameters(), new Class<?>[0], new Class<?>[0]);
		
		verify(checker, times(1)).checkRolesImplementation(any(), any(), any());
		verifyNoMoreInteractions(checker);
	}
	
	@Test
	public void checkRI2RolesButNoParametersTest() throws AnnotationCheckerException, ParseException {
		
		@Role
		class RoleClass1 {
			public Integer x;
			public Integer y;
		}
		
		class RoleAnnotationImpl implements CoordinatorRole, MemberRole {
			@Override
			public Class<?> value() {
				return RoleClass1.class;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return RoleAnnotationImpl.class;
			}
		}
		
		Class<?>[] roles = new Class<?>[] {RoleClass1.class};
		
		RolesAnnotationChecker checker = spy(new RolesAnnotationChecker());
		when(checker.isFieldInRole(any(), any(), any())).thenReturn(false);
		
		checker.checkRolesImplementation(new ArrayList<Parameter>(), roles, roles);
		
		verify(checker, times(1)).checkRolesImplementation(any(), any(), any());
		verifyNoMoreInteractions(checker);
	}
	
	@Test
	public void checkRI2ExampleTest() throws AnnotationCheckerException, ParseException, AnnotationProcessorException {
		
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
		
		class RoleAnnotationImpl implements CoordinatorRole, MemberRole {
			private Class<?> roleClass;
			
			public RoleAnnotationImpl(Class<?> roleClass) {
				this.roleClass = roleClass;
			}
			
			@Override
			public Class<?> value() {
				return roleClass;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return RoleAnnotationImpl.class;
			}
		}
		
		Class<?>[] coordinatorRoles = new Class<?>[] {RoleClass1.class, RoleClass2.class};
		Class<?>[] memberRoles = new Class<?>[] {RoleClass2.class, RoleClass3.class};
		
		RolesAnnotationChecker checker = spy(new RolesAnnotationChecker());
		when(checker.isFieldInRole(any(), any(), any())).thenReturn(true);
		
		checker.checkRolesImplementation(getTestParameters(), coordinatorRoles, memberRoles);
		verify(checker, times(1)).isFieldInRole(Integer.class, Arrays.asList("x"), RoleClass1.class);
		verify(checker, times(1)).isFieldInRole(Integer.class, Arrays.asList("x"), RoleClass2.class);
		verify(checker, times(1)).isFieldInRole(Long.class, Arrays.asList("a", "b"), RoleClass1.class);
		verify(checker, times(1)).isFieldInRole(Long.class, Arrays.asList("a", "b"), RoleClass2.class);
		verify(checker, times(1)).isFieldInRole(null, Arrays.asList("id"), RoleClass1.class);
		verify(checker, times(1)).isFieldInRole(null, Arrays.asList("id"), RoleClass2.class); // TODO this needs subtyping approval
		verify(checker, times(1)).isFieldInRole(null, Arrays.asList("y"), RoleClass2.class);
		verify(checker, times(1)).isFieldInRole(null, Arrays.asList("y"), RoleClass3.class);
		verify(checker, times(1)).isFieldInRole(String.class, Arrays.asList("z"), RoleClass1.class);
		verify(checker, times(1)).isFieldInRole(String.class, Arrays.asList("z"), RoleClass2.class);
		
		verify(checker, times(1)).checkRolesImplementation(any(), any(), any());
		verifyNoMoreInteractions(checker);
	}
	
	@Test
	public void checkRI2WrongKnowledgePathTest() throws AnnotationCheckerException, ParseException, AnnotationProcessorException {
		
		@Role
		class RoleClass1 {
			public Integer x;
			public Integer y;
		}
		
		class RoleAnnotationImpl implements CoordinatorRole, MemberRole {
			@Override
			public Class<?> value() {
				return RoleClass1.class;
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return RoleAnnotationImpl.class;
			}
		}
		
		Class<?>[] roles = new Class<?>[] {RoleClass1.class};
		
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.IN);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setType(Integer.class);
		
		RolesAnnotationChecker checker = spy(new RolesAnnotationChecker());
		when(checker.isFieldInRole(any(), any(), any())).thenReturn(false);
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The knowledge path '<COORDINATOR>.x' is not valid for the role 'RoleClass1'.");
		
		checker.checkRolesImplementation(Arrays.asList(param1), roles, roles);
	}
	
}

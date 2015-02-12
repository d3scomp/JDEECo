package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC1;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.WrongCE1;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

public class RolesAnnotationCheckerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/*
	 * Checks for the checkRolesImplementation(Object) method
	 */
	
	@Test
	public void checkRI1WhenNoRolesTest() throws AnnotationProcessorException {
		CorrectC1 component = new CorrectC1();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1NullInputTest() throws AnnotationProcessorException {
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The input instance cannot be null.");
		new RolesAnnotationChecker().checkRolesImplementation(null);
	}
	
	@Test
	public void checkRI1NonComponentTest() throws AnnotationProcessorException {
		WrongCE1 nonComponent = new WrongCE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		new RolesAnnotationChecker().checkRolesImplementation(nonComponent);
	}
	
	@Test
	public void checkRI1ComponentWithPlaysRoleTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		// almost correct, but the @Component annotation is missing
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1CorrectComponentWithRoleTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public int x;
			public int y;
		}
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1MissingRoleAnnotationTest() throws AnnotationProcessorException {
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public int x;
			public int y;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The class RoleClass is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1MissingFieldTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public int y;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1BadlyTypedFieldTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public long x;
			public int y;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1CorrectComplexComponentTest() throws AnnotationProcessorException {
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
			public Map<String, Integer> map;
			public String str;
			public List<List<String>> list;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1SubtypeFieldTest() throws AnnotationProcessorException {
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
			public Map<String, Long> map; // this should not be allowed (TODO or should it?)
			public String str;
			public List<List<String>> list;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.map is not implemented (or has a different type than java.util.Map<java.lang.String, java.lang.Integer>).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1BadlyTypedGenericArgumentTest() throws AnnotationProcessorException {
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
			public Map<String, Long> map; // value type should be Integer
			public String str;
			public List<List<String>> list;
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.map is not implemented (or has a different type than java.util.Map<java.lang.String, java.lang.Integer>).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}

	@Test
	public void checkRI1BadlyTypedGenericArgumentsArgumentTest() throws AnnotationProcessorException {
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
			public Map<String, Integer> map;
			public String str;
			public List<List<Integer>> list; // inner type should be String
			public int x;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.list is not implemented (or has a different type than java.util.List<java.util.List<java.lang.String>>).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1MultipleRolesCorrectTest() throws AnnotationProcessorException {
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
	public void checkRI1MultipleRolesMissingAnnotationTest() throws AnnotationProcessorException {
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
			public int x;
			public int y;
			public int z;
			public String str;
			public String k;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The class RoleClass2 is used as a role class, but it is not annotated by the @" + Role.class.getSimpleName() + " annotation.");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1MultipleRolesImpossibleTest() throws AnnotationProcessorException {
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
			public int x;
			public int y;
			public long z;
		}
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass2.y is not implemented (or has a different type than long).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}	
		
	@Test
	public void checkRI1IgnoreFieldsInRoleTest() throws AnnotationProcessorException {
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
			public int x;
			public int y;
		}	
		
		ComponentClass component = new ComponentClass();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1IgnoreNonpublicFieldsInComponentsTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			protected int x;
			public int y;
		}	
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}	
	
	@Test
	public void checkRI1IgnoreStaticFieldsInComponentsTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			public static final int x = 0;
			public int y;
		}	
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkRI1IgnoreLocalFieldsInComponentsTest() throws AnnotationProcessorException {
		@Role
		class RoleClass {
			public int x;
		}
		
		@Component
		@PlaysRole(RoleClass.class)
		class ComponentClass {
			@Local
			public int x = 0;
			public int y;
		}	
		
		ComponentClass component = new ComponentClass();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The field RoleClass.x is not implemented (or has a different type than int).");
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	
	
	/*
	 * Checks for the checkRolesImplementation(List<Parameter>, CoordinatorRole[], MemberRole[]) method
	 */
		
	//@Test
	public void checkRI2NoRolesTest() throws AnnotationProcessorException, ParseException {
		
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
		
		CoordinatorRole[] coordinatorRoles = new CoordinatorRole[] {new RoleAnnotationImpl(RoleClass1.class), new RoleAnnotationImpl(RoleClass2.class)};
		MemberRole[] memberRoles = new MemberRole[] {new RoleAnnotationImpl(RoleClass2.class), new RoleAnnotationImpl(RoleClass3.class)};;
		
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.IN);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setType(Integer.class);
		Parameter param2 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param2.setKind(ParameterKind.OUT);
		param2.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.a.b", PathOrigin.ENSEMBLE));
		param2.setType(Long.class);
		Parameter param3 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param3.setKind(ParameterKind.INOUT);
		param3.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.z.[member.y.[coord.id]]", PathOrigin.ENSEMBLE));
		param3.setType(String.class);
		List<Parameter> parameters = Arrays.asList(param1, param2, param3);
		
		RolesAnnotationChecker checker = mock(RolesAnnotationChecker.class);
		when(checker.isFieldInRole(any(), any(), any())).thenReturn(true);
		
		// TODO NO ORDER
		InOrder order = inOrder(checker);
		checker.checkRolesImplementation(parameters, coordinatorRoles, memberRoles);
		
		order.verify(checker).isFieldInRole(Integer.class, Arrays.asList("x"), RoleClass1.class);
		order.verify(checker).isFieldInRole(Integer.class, Arrays.asList("x"), RoleClass2.class);
		order.verify(checker).isFieldInRole(Long.class, Arrays.asList("a", "b"), RoleClass1.class);
		order.verify(checker).isFieldInRole(Long.class, Arrays.asList("a", "b"), RoleClass2.class);
		order.verify(checker).isFieldInRole(Integer.class, Arrays.asList("id"), RoleClass1.class);
		order.verify(checker).isFieldInRole(Integer.class, Arrays.asList("id"), RoleClass2.class); // TODO this needs subtyping approval
		order.verify(checker).isFieldInRole(Integer.class, Arrays.asList("y"), RoleClass2.class);
		order.verify(checker).isFieldInRole(Integer.class, Arrays.asList("y"), RoleClass3.class);
		order.verify(checker).isFieldInRole(String.class, Arrays.asList("z"), RoleClass1.class);
		order.verify(checker).isFieldInRole(String.class, Arrays.asList("z"), RoleClass2.class);
		
	}
	
}

package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC1;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.WrongCE1;

public class RolesAnnotationCheckerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/*
	 * Checks for the checkRolesImplementation method
	 */
	
	@Test
	public void checkWhenNoRolesTest() throws AnnotationProcessorException {
		CorrectC1 component = new CorrectC1();
		new RolesAnnotationChecker().checkRolesImplementation(component);
	}
	
	@Test
	public void checkNullInputTest() throws AnnotationProcessorException {
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The input instance cannot be null.");
		new RolesAnnotationChecker().checkRolesImplementation(null);
	}
	
	@Test
	public void checkNonComponentTest() throws AnnotationProcessorException {
		WrongCE1 nonComponent = new WrongCE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The input instance is not a component (the class is not annotated by the @" + Component.class.getSimpleName() + " annotation).");
		new RolesAnnotationChecker().checkRolesImplementation(nonComponent);
	}
	
	@Test
	public void checkComponentWithPlaysRoleTest() throws AnnotationProcessorException {
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
	public void checkCorrectComponentWithRoleTest() throws AnnotationProcessorException {
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
	public void checkMissingRoleAnnotationTest() throws AnnotationProcessorException {
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
	public void checkMissingFieldTest() throws AnnotationProcessorException {
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
	public void checkBadlyTypedFieldTest() throws AnnotationProcessorException {
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
	public void checkCorrectComplexComponentTest() throws AnnotationProcessorException {
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
	public void checkSubtypeFieldTest() throws AnnotationProcessorException {
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
	public void checkBadlyTypedGenericArgumentTest() throws AnnotationProcessorException {
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
	public void checkBadlyTypedGenericArgumentsArgumentTest() throws AnnotationProcessorException {
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
	public void checkMultipleRolesCorrectTest() throws AnnotationProcessorException {
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
	public void checkMultipleRolesMissingAnnotationTest() throws AnnotationProcessorException {
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
	public void checkMultipleRolesImpossibleTest() throws AnnotationProcessorException {
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
	public void checkIgnoreFieldsInRoleTest() throws AnnotationProcessorException {
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
	public void checkIgnoreNonpublicFieldsInComponentsTest() throws AnnotationProcessorException {
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
	public void checkIgnoreStaticFieldsInComponentsTest() throws AnnotationProcessorException {
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
	public void checkIgnoreLocalFieldsInComponentsTest() throws AnnotationProcessorException {
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
}

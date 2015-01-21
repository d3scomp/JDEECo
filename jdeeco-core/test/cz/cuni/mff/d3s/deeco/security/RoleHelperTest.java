package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeNotFoundException;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class RoleHelperTest {

	@Test
	public void getTransitiveRolesTest1() {
		// when roles are flat
		List<SecurityRole> roles = Arrays.asList(createRole("role1"), createRole("role2"));
		
		// then the same list is returned
		List<SecurityRole> returnedRoles = RoleHelper.getTransitiveRoles(roles);
		
		assertEquals(roles, returnedRoles);
	}

	@Test
	public void getTransitiveRolesTest2() {
		// when roles are nested
		SecurityRole role1 = createRole("role1");
		SecurityRole role2 = createRole("role2");
		SecurityRole role1_1 = createRole("role1_1");
		SecurityRole role1_2 = createRole("role1_2");
		SecurityRole role2_1 = createRole("role2_1");
		SecurityRole role1_2_1 = createRole("role1_2_1");
		
		role1.getConsistsOf().add(role1_1);
		role1.getConsistsOf().add(role1_2);
		
		role2.getConsistsOf().add(role2_1);
		
		role1_2.getConsistsOf().add(role1_2_1);
		
		List<SecurityRole> roles = Arrays.asList(role1, role2);
		
		// then flattened list (including potential duplicates) is returned
		List<SecurityRole> returnedRoles = RoleHelper.getTransitiveRoles(roles);
		
		assertEquals(Arrays.asList(role1, role2, role1_1, role1_2, role1_2_1, role2_1), returnedRoles);
	}	
	
	@Test
	public void argumentsRoleMatchTest1() {
		// when two same maps are prepared
		Map<String, Object> roleArguments = new HashMap<>();
		roleArguments.put("x", 123);
		roleArguments.put("y", "y");
		roleArguments.put("z", Arrays.asList(1,2,3));
		
		Map<String, Object> tagArguments = new HashMap<>();
		tagArguments.put("x", 123);
		tagArguments.put("y", "y");
		tagArguments.put("z", Arrays.asList(1,2,3));
		
		// then true is returned
		assertTrue(RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
	}
	
	@Test
	public void argumentsRoleMatchTest2() {
		// when null is in place of role argument
		Map<String, Object> roleArguments = new HashMap<>();
		roleArguments.put("x", null);
		roleArguments.put("y", "y");
		roleArguments.put("z", null);
		
		Map<String, Object> tagArguments = new HashMap<>();
		tagArguments.put("x", 123);
		tagArguments.put("y", "y");
		tagArguments.put("z", Arrays.asList(1,2,3));
		
		// then true is returned
		assertTrue(RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
	}
	
	@Test
	public void argumentsRoleMatchTest3() {
		// when null is in place of tag argument
		Map<String, Object> roleArguments = new HashMap<>();
		roleArguments.put("x", null);
		roleArguments.put("y", "y");
		roleArguments.put("z", Arrays.asList(1,2,3));
		
		Map<String, Object> tagArguments = new HashMap<>();
		tagArguments.put("x", 123);
		tagArguments.put("y", null);
		tagArguments.put("z", Arrays.asList(1,2,3));
		
		// then false is returned
		assertFalse(RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
	}
	
	@Test
	public void argumentsRoleMatchTest4() {
		// when null is in place of both tag and role argument
		Map<String, Object> roleArguments = new HashMap<>();
		roleArguments.put("x", null);
		roleArguments.put("y", "y");
		roleArguments.put("z", Arrays.asList(1,2,3));
		
		Map<String, Object> tagArguments = new HashMap<>();
		tagArguments.put("x", null);
		tagArguments.put("y", "y");
		tagArguments.put("z", Arrays.asList(1,2,3));
		
		// then true is returned
		assertTrue(RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
	}
	
	@Test
	public void argumentsRoleMatchTest5() {
		// when the role contains an extra parameter
		Map<String, Object> roleArguments = new HashMap<>();
		roleArguments.put("x", null);
		roleArguments.put("y", "y");
		roleArguments.put("z", Arrays.asList(1,2,3));
		
		Map<String, Object> tagArguments = new HashMap<>();
		tagArguments.put("x", null);
		tagArguments.put("y", "y");
				
		// then true is returned
		assertTrue(RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
	}
	
	@Test
	public void argumentsRoleMatchTest6() {
		// when the tag contains an extra parameter
		Map<String, Object> roleArguments = new HashMap<>();
		roleArguments.put("x", null);
		roleArguments.put("y", "y");
				
		Map<String, Object> tagArguments = new HashMap<>();
		tagArguments.put("x", null);
		tagArguments.put("y", "y");
		tagArguments.put("z", Arrays.asList(1,2,3));
		
		// then false is returned
		assertTrue(RoleHelper.roleArgumentsMatch(roleArguments, tagArguments));
	}
	
	@Test
	public void readRoleArgumentsTest() throws KnowledgeUpdateException, KnowledgeNotFoundException {
		// when role has arguments
		SecurityRole role = createRole("testrole");
		role.getArguments().add(createBlankArgument("blank"));
		role.getArguments().add(createPathArgument("path", "field"));
		role.getArguments().add(createAbsoluteArgument("absolute", 123));
		
		// when knowledge manager is prepared
		KnowledgeManager km = new BaseKnowledgeManager("test", null);
		ChangeSet changeSet = new ChangeSet();
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("field"), "field_content");
		km.update(changeSet);
		
		// when readRoleArguments is called
		Map<String, Object> values = RoleHelper.readRoleArguments(role, km);
		
		// then correct data are returned
		assertEquals(3, values.size());
		assertEquals("field_content", values.get("path"));
		assertEquals(123, values.get("absolute"));
		assertNull(values.get("blank"));
	}
	
	
	
	private BlankSecurityRoleArgument createBlankArgument(String name) {
		BlankSecurityRoleArgument arg = RuntimeMetadataFactory.eINSTANCE.createBlankSecurityRoleArgument();		
		arg.setName(name);
		return arg;
	}
	
	private PathSecurityRoleArgument createPathArgument(String name, String path) {
		PathSecurityRoleArgument arg = RuntimeMetadataFactory.eINSTANCE.createPathSecurityRoleArgument();		
		arg.setName(name);
		arg.setKnowledgePath(RuntimeModelHelper.createKnowledgePath(path));
		return arg;
	}
	
	private AbsoluteSecurityRoleArgument createAbsoluteArgument(String name, Object value) {
		AbsoluteSecurityRoleArgument arg = RuntimeMetadataFactory.eINSTANCE.createAbsoluteSecurityRoleArgument();		
		arg.setName(name);
		arg.setValue(value);
		return arg;
	}
	
	private SecurityRole createRole(String name) {
		SecurityRole role = RuntimeMetadataFactory.eINSTANCE.createSecurityRole();		
		role.setRoleName(name);		
		return role;
	}
}

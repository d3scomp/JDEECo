package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class ModelSecurityValidatorTest {

	private KnowledgeManager knowledgeManager;
	private ComponentInstance component;
	private RuntimeMetadataFactory factory;
	private ComponentProcess process;
	private KnowledgeSecurityTag tag_with_path, tag_only_role, tag_with_blank, tag_with_absolute, inherited_tag_with_path;
	
	@Before
	public void setUp() throws KnowledgeUpdateException {
		factory = RuntimeMetadataFactory.eINSTANCE;
		
		component = factory.createComponentInstance();
		knowledgeManager = new BaseKnowledgeManager("ID1", component);
		process = factory.createComponentProcess();
		tag_with_path = factory.createKnowledgeSecurityTag();
		tag_only_role = factory.createKnowledgeSecurityTag();
		tag_with_blank = factory.createKnowledgeSecurityTag();
		tag_with_absolute = factory.createKnowledgeSecurityTag();
		inherited_tag_with_path = factory.createKnowledgeSecurityTag();
		
		// create role1
		SecurityRole role1 = factory.createSecurityRole();
		role1.setRoleName("role1");		
		
		PathSecurityRoleArgument role1PathArgument = factory.createPathSecurityRoleArgument();
		role1PathArgument.setName("role1pathArgument");
		role1PathArgument.setKnowledgePath(RuntimeModelHelper.createKnowledgePath("field"));
		role1.getArguments().add(role1PathArgument);
		
		tag_with_path.setRequiredRole(role1);
		
		// create role2
		SecurityRole role2 = factory.createSecurityRole();
		role2.setRoleName("role2");		
		tag_only_role.setRequiredRole(role2);
		
		// create role3
		SecurityRole role3 = factory.createSecurityRole();
		role3.setRoleName("role1");		
		
		BlankSecurityRoleArgument role3BlankArgument = factory.createBlankSecurityRoleArgument();
		role3BlankArgument.setName("role1pathArgument");		
		role3.getArguments().add(role3BlankArgument);
		
		tag_with_blank.setRequiredRole(role3);		
		
		// create role4
		SecurityRole role4 = factory.createSecurityRole();
		role4.setRoleName("role4");		
		
		AbsoluteSecurityRoleArgument role4AbsoluteArgument = factory.createAbsoluteSecurityRoleArgument();
		role4AbsoluteArgument.setName("role4AbsoluteArgument");		
		role4AbsoluteArgument.setValue("hello");
		role4.getArguments().add(role4AbsoluteArgument);
		
		tag_with_absolute.setRequiredRole(role4);
		
		// create descendant of role1
		SecurityRole role5 = factory.createSecurityRole();
		role5.setRoleName("role5");

		BlankSecurityRoleArgument role5BlankArgument = factory.createBlankSecurityRoleArgument();
		role5BlankArgument.setName("role1pathArgument");		
		role5.getArguments().add(role5BlankArgument);
		role5.getConsistsOf().add(role1);
		inherited_tag_with_path.setRequiredRole(role5);
		
		// connect
		component.setKnowledgeManager(knowledgeManager);	
		component.getComponentProcesses().add(process);
		process.setComponentInstance(component);
		
		knowledgeManager.update(createKnowledge());
	}
	
	private ChangeSet createKnowledge() {
		ChangeSet result = new ChangeSet();
		result.setValue(RuntimeModelHelper.createKnowledgePath("field"), 123);
		return result;
	}
	
	@Test
	public void validateProcessTest1() {
		// given security of input and output is identical
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map"), Arrays.asList(tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("key"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map"}, RuntimeModelHelper.createKnowledgePath("key"))));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest2() {
		// given output is part of input
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("key"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest3() {
		// given output is missing part of security tags
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));		
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("key"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"}, RuntimeModelHelper.createKnowledgePath("key"))));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map1"} )));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map1 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest4() {
		// given roles are different for input and output
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_only_role));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest5() {
		// given input is generalization of output
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_blank));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_path));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest6() {
		// given output is generalization of input
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_blank));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest7() {
		// given absolute arguments are used
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest8() {
		// given combination of tags is used
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute, tag_with_path));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest9() {
		// given local knowledge is present in the output
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		knowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("key")));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest10() {
		// given output has no tags
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map2.[key] is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest11() {
		// given output is mixed (contains local and non-local knowledge)
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map3"), Arrays.asList(tag_with_absolute, tag_only_role));
		knowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("map2")));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"key"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map3"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter key is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest12() {
		// given output has an extra role
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute, tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest13() {
		// given input has an extra role
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest14() {
		// given input has no tags
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute, tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest15() {
		// given inheritance is used the correct way (output argument has role inheriting the role from input argument)
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(inherited_tag_with_path));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest16() {
		// given inheritance is used the wrong way (input argument has role inheriting the role from output argument)
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(inherited_tag_with_path));
		knowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_path));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(process);
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	
	
	private Parameter createParameter(ParameterKind kind, KnowledgePath path) {
		Parameter parameter = factory.createParameter();
		parameter.setKind(kind);
		parameter.setKnowledgePath(path);
		return parameter;
	}
	
	private KnowledgePath createKnowledgePath(String[] fields, KnowledgePath... expressions) {
		KnowledgePath path = RuntimeModelHelper.createKnowledgePath(fields);
		
		for (KnowledgePath innerPath : expressions) {
			PathNodeMapKey mapKey = factory.createPathNodeMapKey();
			mapKey.setKeyPath(innerPath);
			path.getNodes().add(mapKey);
		}
		
		return path;
	}
}

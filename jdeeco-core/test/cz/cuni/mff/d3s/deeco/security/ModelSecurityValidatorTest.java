package cz.cuni.mff.d3s.deeco.security;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.rits.cloning.Cloner;

import cz.cuni.mff.d3s.deeco.knowledge.BaseKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeUpdateException;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Exchange;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Invocable;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.custom.RuntimeMetadataFactoryExt;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper.PathRoot;

/**
 * 
 * @author Ondřej Štumpf
 *
 */
public class ModelSecurityValidatorTest {

	private KnowledgeManager simpleKnowledgeManager, complexKnowledgeManager;
	private ComponentInstance simpleComponent, complexComponent;
	private RuntimeMetadataFactory factory;
	private ComponentProcess process, process1, process2, process3;
	private KnowledgeSecurityTag tag_with_path, tag_only_role, tag_with_blank, tag_with_absolute, inherited_tag;
	
	@Before
	public void setUp() throws KnowledgeUpdateException {
		factory = RuntimeMetadataFactoryExt.eINSTANCE;
		
		simpleComponent = factory.createComponentInstance();
		simpleKnowledgeManager = new BaseKnowledgeManager("ID1", simpleComponent);		
		process = factory.createComponentProcess();
		tag_with_path = factory.createKnowledgeSecurityTag();
		tag_only_role = factory.createKnowledgeSecurityTag();
		tag_with_blank = factory.createKnowledgeSecurityTag();
		tag_with_absolute = factory.createKnowledgeSecurityTag();
		inherited_tag = factory.createKnowledgeSecurityTag();
		
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
		
		// create descendant of role2
		SecurityRole role5 = factory.createSecurityRole();
		role5.setRoleName("role5");		
		role5.getConsistsOf().add(new Cloner().deepClone(role2));
		inherited_tag.setRequiredRole(role5);
		
		// create complex component to test transitivity
		complexComponent = factory.createComponentInstance();
		complexKnowledgeManager = new BaseKnowledgeManager("ID2", complexComponent);
		process1 = factory.createComponentProcess();
		process2 = factory.createComponentProcess();
		process3 = factory.createComponentProcess();
		
		process1.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "in1_1"));
		process1.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "in1_2"));
		process1.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.INOUT, "inout1_1"));
		process1.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "out1_1"));
		
		process2.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "inout1_1"));
		process2.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "out2_1"));
		process2.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "out2_2"));
		
		process3.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.INOUT, "out2_1"));
		process3.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "out1_1"));
		
		complexComponent.getComponentProcesses().add(process1);
		complexComponent.getComponentProcesses().add(process2);
		complexComponent.getComponentProcesses().add(process3);
		
		// connect
		simpleComponent.setKnowledgeManager(simpleKnowledgeManager);	
		simpleComponent.getComponentProcesses().add(process);
		process.setComponentInstance(simpleComponent);
		
		simpleKnowledgeManager.update(createKnowledge());
		
		complexComponent.setKnowledgeManager(complexKnowledgeManager);
		
		complexKnowledgeManager.update(createKnowledge());
	}
	
	private ChangeSet createKnowledge() {
		ChangeSet result = new ChangeSet();
		result.setValue(RuntimeModelHelper.createKnowledgePath("field"), 123);
		return result;
	}
	
	@Test
	public void validateProcessTest1() {
		// given security of input and output is identical
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map"), Arrays.asList(tag_with_path));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("key"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map"}, RuntimeModelHelper.createKnowledgePath("key"))));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest2() {
		// given output is part of input
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_path));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("key"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest3() {
		// given output is missing part of security tags
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));		
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("key"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"}, RuntimeModelHelper.createKnowledgePath("key"))));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map1"} )));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map1 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest4() {
		// given roles are different for input and output
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_only_role));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest5() {
		// given input is generalization of output
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_blank));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_path));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest6() {
		// given output is generalization of input
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_path));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_blank));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest7() {
		// given absolute arguments are used
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest8() {
		// given combination of tags is used
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute, tag_with_path));		
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest9() {
		// given local knowledge is present in the output
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		simpleKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("key")));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest10() {
		// given output has no tags
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"}, RuntimeModelHelper.createKnowledgePath("key"))));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map2.[key] is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest11() {
		// given output is mixed (contains local and non-local knowledge)
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map3"), Arrays.asList(tag_with_absolute, tag_only_role));
		simpleKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("map2")));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"key"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map3"})));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter key is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest12() {
		// given output has an extra role
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute, tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest13() {
		// given input has an extra role
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_with_absolute, tag_only_role));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest14() {
		// given input has no tags
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_absolute, tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest15() {
		// given inheritance is used the correct way (output argument has role inheriting the role from input argument)
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(tag_only_role));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(inherited_tag));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest16() {
		// given inheritance is used the wrong way (input argument has role inheriting the role from output argument)
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList(inherited_tag));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_only_role));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		List<String> errors = ModelSecurityValidator.validate(simpleComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter map2 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void validateProcessTest17() {
		// given data is copied into local
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map1"), Arrays.asList());
		simpleKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("map2")));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest18() {
		// given data is copied from local
		simpleKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("map1")));
		
		process.getParameters().add(createParameter(ParameterKind.INOUT, createKnowledgePath(new String[] {"map1"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest19() {
		// given data is copied from local to unsecured		
		simpleKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("map1")));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList());
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest20() {
		// given data is copied from local to unsecured		
		simpleKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("map1")));
		simpleKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("map2"), Arrays.asList(tag_with_path));
		
		process.getParameters().add(createParameter(ParameterKind.IN, createKnowledgePath(new String[] {"map1"})));
		process.getParameters().add(createParameter(ParameterKind.OUT, createKnowledgePath(new String[] {"map2"})));
		
		Set<String> errors = ModelSecurityValidator.validate(simpleComponent);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateProcessTest21() {
		// given data is transitively dependent and all conditions are met
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1_1"), Arrays.asList(tag_with_path));
		complexKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("inout1_1")));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_1"), Arrays.asList(tag_with_path));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_2"), Arrays.asList(tag_with_path));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out1_1"), Arrays.asList(tag_with_path));
		
		Set<String> errors = ModelSecurityValidator.validate(complexComponent);
		assertTrue(errors.isEmpty());		
	}
	
	@Test
	public void validateProcessTest22() {
		// given data is transitively dependent
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1_1"), Arrays.asList(tag_with_path));
		complexKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("inout1_1")));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_2"), Arrays.asList(tag_with_path));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out1_1"), Arrays.asList(tag_with_path));
		
		List<String> errors = ModelSecurityValidator.validate(complexComponent).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter out2_1 is not appropriately secured.", errors.get(0));
	}
	
	@Test
	public void getAllTransitiveInputParametersTest1() {
		Map<KnowledgePath, Invocable> result = new HashMap<>();
		List<Invocable> invocables = process1.getComponentInstance().getComponentProcesses().stream()
				.map(process -> (Invocable)process)
				.collect(Collectors.toList());
		ModelSecurityValidator.getAllTransitiveInputParameters(RuntimeModelHelper.createKnowledgePath("inout1_1"), process1, invocables, path -> path, result);
		
		assertEquals(3, result.size());
		assertEquals("inout1_1", result.keySet().toArray()[0].toString());
		assertEquals("in1_1", result.keySet().toArray()[1].toString());
		assertEquals("in1_2", result.keySet().toArray()[2].toString());
		assertEquals(process1, result.values().toArray()[0]);
		assertEquals(process1, result.values().toArray()[1]);
		assertEquals(process1, result.values().toArray()[2]);
	}
	
	@Test
	public void getAllTransitiveInputParametersTest2() {
		Map<KnowledgePath, Invocable> result = new HashMap<>();
		List<Invocable> invocables = process1.getComponentInstance().getComponentProcesses().stream()
				.map(process -> (Invocable)process)
				.collect(Collectors.toList());
		ModelSecurityValidator.getAllTransitiveInputParameters(RuntimeModelHelper.createKnowledgePath("out1_1"), process3, invocables, path -> path,result);
		
		assertEquals(4, result.size());
		assertEquals("inout1_1", result.keySet().toArray()[0].toString());
		assertEquals("in1_1", result.keySet().toArray()[1].toString());
		assertEquals("in1_2", result.keySet().toArray()[2].toString());
		assertEquals("out2_1", result.keySet().toArray()[3].toString());	
		assertEquals(process1, result.values().toArray()[0]);
		assertEquals(process1, result.values().toArray()[1]);
		assertEquals(process1, result.values().toArray()[2]);
		assertEquals(process3, result.values().toArray()[3]);
	}
	
	@Test
	public void getAllTransitiveInputParametersTest3() {
		Map<KnowledgePath, Invocable> result = new HashMap<>();
		List<Invocable> invocables = process1.getComponentInstance().getComponentProcesses().stream()
				.map(process -> (Invocable)process)
				.collect(Collectors.toList());
		
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "exchange_in"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "out2_1"));
		invocables.add(exchange);
		
		ModelSecurityValidator.getAllTransitiveInputParameters(RuntimeModelHelper.createKnowledgePath("out2_2"), process3, invocables, path -> path, result);
		
		assertEquals(5, result.size());
		assertEquals("inout1_1", result.keySet().toArray()[0].toString());
		assertEquals("in1_1", result.keySet().toArray()[1].toString());
		assertEquals("exchange_in", result.keySet().toArray()[2].toString());
		assertEquals("in1_2", result.keySet().toArray()[3].toString());
		assertEquals("out2_1", result.keySet().toArray()[4].toString());	
		assertEquals(process1, result.values().toArray()[0]);
		assertEquals(process1, result.values().toArray()[1]);
		assertEquals(exchange, result.values().toArray()[2]);
		assertEquals(process1, result.values().toArray()[3]);
		assertEquals(process3, result.values().toArray()[4]);
	}
	
	@Test
	public void validateExchangeTest1() {
		// given simple exchange without transitive dependencies is prepared and all condition are met
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "out"));
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out"), Arrays.asList(tag_with_absolute));
		
		Set<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager);
		assertTrue(errors.isEmpty());		
	}
	
	@Test
	public void validateExchangeTest2() {
		// given simple exchange without transitive dependencies is prepared
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "out"));
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		// when output is not secured
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out"), Arrays.asList());
		
		List<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager).stream().collect(Collectors.toList());
		assertEquals(1, errors.size());
		assertEquals("Parameter <COORDINATOR>.out is not appropriately secured (compromises <MEMBER>.in).", errors.get(0));
	}
	
	@Test
	public void validateExchangeTest3() {
		// given exchange with transitive dependencies is prepared
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "in1_1"));
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1_1"), Arrays.asList(tag_with_absolute));
		
		List<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager).stream().collect(Collectors.toList());
		assertEquals(8, errors.size());
		assertEquals("Parameter out1_1 is not appropriately secured (compromises in1_1).", errors.get(0));
		assertEquals("Parameter out2_2 is not appropriately secured (compromises <MEMBER>.in).", errors.get(1));
		assertEquals("Parameter out2_1 is not appropriately secured (compromises <MEMBER>.in).", errors.get(2));				
		assertEquals("Parameter inout1_1 is not appropriately secured (compromises in1_1).", errors.get(3));		
		assertEquals("Parameter out2_2 is not appropriately secured (compromises in1_1).", errors.get(4));		
		assertEquals("Parameter inout1_1 is not appropriately secured (compromises <MEMBER>.in).", errors.get(5));				
		assertEquals("Parameter out1_1 is not appropriately secured (compromises <MEMBER>.in).", errors.get(6));		
		assertEquals("Parameter out2_1 is not appropriately secured (compromises in1_1).", errors.get(7));	
	}
	
	@Test
	public void validateExchangeTest4() {
		// given exchange with transitive dependencies is prepared and conditions are met
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "in1_1"));
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("inout1_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out1_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_2"), Arrays.asList(tag_with_absolute));
		
		Set<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateExchangeTest5() {
		// given exchange with transitive dependencies is prepared and transitive condition is not met
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "in1_1"));
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("inout1_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out1_1"), Arrays.asList(tag_with_absolute));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_1"), Arrays.asList(tag_with_absolute));
		
		List<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager).stream().collect(Collectors.toList());
		assertEquals(3, errors.size());
		assertEquals("Parameter out2_2 is not appropriately secured (compromises <MEMBER>.in).", errors.get(0));
		assertEquals("Parameter out2_2 is not appropriately secured (compromises inout1_1).", errors.get(1));
		assertEquals("Parameter out2_2 is not appropriately secured (compromises in1_1).", errors.get(2));
	}
	
	@Test
	public void validateExchangeTest6() {
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in1"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in2"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "out2_1"));
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1"), Arrays.asList(tag_only_role));
		complexKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("out2_1"), RuntimeModelHelper.createKnowledgePath("out1_1")));
		
		Set<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	public void validateExchangeTest7() throws KnowledgeUpdateException {
		// given there is a dependency between knowledge exchange and component processes
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in1"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in2"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.OUT, "<C>", "out2_1"));
		
		ChangeSet changeSet = new ChangeSet();
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("in1"), 123);
		changeSet.setValue(RuntimeModelHelper.createKnowledgePath("in2"), 456);
		
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.update(changeSet);
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1"), Arrays.asList(tag_only_role));
		complexKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("out2_1"), Arrays.asList(tag_only_role));
			
		List<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager).stream().collect(Collectors.toList());
		assertEquals(2, errors.size());
		assertEquals("Parameter out1_1 is not appropriately secured (compromises out2_1).", errors.get(0));
		assertEquals("Parameter out1_1 is not appropriately secured (compromises <MEMBER>.in1).", errors.get(1));
	}
	
	@Test
	public void validateExchangeTest8() throws KnowledgeUpdateException {
		// given there is a dependency between knowledge exchange and component processes
		Exchange exchange = factory.createExchange();
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in1"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.IN, "<M>", "in2"));
		exchange.getParameters().add(RuntimeModelHelper.createParameter(ParameterKind.INOUT, "<C>", "out1"));
				
		KnowledgeManager shadowKnowledgeManager = new BaseKnowledgeManager("shadow123", complexComponent);
		
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in1"), Arrays.asList(tag_only_role));
		shadowKnowledgeManager.setSecurityTags(RuntimeModelHelper.createKnowledgePath("in2"), Arrays.asList());
		complexKnowledgeManager.markAsLocal(Arrays.asList(RuntimeModelHelper.createKnowledgePath("out1")));
			
		Set<String> errors = ModelSecurityValidator.validate(PathRoot.COORDINATOR, exchange, complexComponent, shadowKnowledgeManager);
		assertTrue(errors.isEmpty());
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

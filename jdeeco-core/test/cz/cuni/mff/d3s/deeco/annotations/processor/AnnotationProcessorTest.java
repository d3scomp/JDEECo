package cz.cuni.mff.d3s.deeco.annotations.processor;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junitx.framework.FileAssert;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Out;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.TokenMgrError;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.*;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.RuntimeModelHelper;
import cz.cuni.mff.d3s.deeco.model.runtime.api.AbsoluteSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.BlankSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.EnsembleDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgeSecurityTag;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathSecurityRoleArgument;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RatingsProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.api.SecurityRole;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class AnnotationProcessorTest {

	protected RuntimeMetadataFactory factory;
	protected File tempFile;
	protected KnowledgeManagerFactory knowledgeManagerFactory;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE;
		tempFile = Files.createTempFile(null, ".xmi").toFile();
		knowledgeManagerFactory = new CloningKnowledgeManagerFactory();	
	}

	@After
	public void tearDown() throws IOException {
		Files.deleteIfExists(tempFile.toPath());
	}
	
	/*
	 * Acceptance tests. 
	 * The produced XMI-serialized models (saved in a temporary file) 
	 * are compared to their pre-generated expected outputs.
	 * If an exception is thrown, its message is compared to the expected one.  
	 */
	
	@Test 
	public void testAllComponentAnnotations() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);
		
		CorrectC1 input = new CorrectC1();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testComponentSecurityAnnotations() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		CorrectC4 input = new CorrectC4();
		
		// when process() is called
		processor.process(input);
		
		// then annotations are parsed correctly
		assertEquals(1, model.getComponentInstances().size());
		ComponentInstance component = model.getComponentInstances().get(0);
		KnowledgeManager km = component.getKnowledgeManager();
		
		List<KnowledgeSecurityTag> nameSecurityTags = km.getKnowledgeSecurityTags(RuntimeModelHelper.createPathNodeField("name"));
		assertEquals(0, nameSecurityTags.size());
		
		List<KnowledgeSecurityTag> capacitySecurityTags = km.getKnowledgeSecurityTags(RuntimeModelHelper.createPathNodeField("capacity"));
		assertEquals(2, capacitySecurityTags.size());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role1.class.getName(), capacitySecurityTags.get(0).getRequiredRole().getRoleName());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role3.class.getName(), capacitySecurityTags.get(1).getRequiredRole().getRoleName());
		assertEquals(1, capacitySecurityTags.get(0).getRequiredRole().getArguments().size());
		assertEquals(0, capacitySecurityTags.get(1).getRequiredRole().getArguments().size());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role3.class.getName(), capacitySecurityTags.get(0).getRequiredRole().getAliasRole().getRoleName());
		assertEquals(capacitySecurityTags.get(0).getRequiredRole().getAliasRole().getArguments().get(0), capacitySecurityTags.get(0).getRequiredRole().getArguments().get(0));
		
		List<KnowledgeSecurityTag> timeSecurityTags = km.getKnowledgeSecurityTags(RuntimeModelHelper.createPathNodeField("time"));
		assertEquals(1, timeSecurityTags.size());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role2.class.getName(), timeSecurityTags.get(0).getRequiredRole().getRoleName());
		assertEquals(5, timeSecurityTags.get(0).getRequiredRole().getArguments().size());
		
		PathSecurityRoleArgument pathArgument = (PathSecurityRoleArgument)timeSecurityTags.get(0).getRequiredRole().getArguments().get(0);
		assertEquals(RuntimeModelHelper.createKnowledgePath("name"), pathArgument.getKnowledgePath());
		assertEquals("name", pathArgument.getName());
		assertEquals("time", timeSecurityTags.get(0).getRequiredRole().getArguments().get(1).getName());
		assertTrue(timeSecurityTags.get(0).getRequiredRole().getArguments().get(0) instanceof PathSecurityRoleArgument);
		assertTrue(timeSecurityTags.get(0).getRequiredRole().getArguments().get(1) instanceof BlankSecurityRoleArgument);
		assertTrue(timeSecurityTags.get(0).getRequiredRole().getArguments().get(2) instanceof AbsoluteSecurityRoleArgument);
		assertTrue(timeSecurityTags.get(0).getRequiredRole().getArguments().get(3) instanceof AbsoluteSecurityRoleArgument);
		
		assertEquals(RuntimeModelHelper.createKnowledgePath("name"), ((PathSecurityRoleArgument) timeSecurityTags.get(0).getRequiredRole().getArguments().get(0)).getKnowledgePath());
		assertEquals(123, ((AbsoluteSecurityRoleArgument) timeSecurityTags.get(0).getRequiredRole().getArguments().get(2)).getValue());
		assertEquals("some_value", ((AbsoluteSecurityRoleArgument) timeSecurityTags.get(0).getRequiredRole().getArguments().get(3)).getValue());
		
		assertEquals(2, component.getSecurityRoles().size());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role1.class.getName(), component.getSecurityRoles().get(0).getRoleName());
		assertEquals(1, component.getSecurityRoles().get(0).getArguments().size());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role2.class.getName(), component.getSecurityRoles().get(1).getRoleName());
		assertEquals(5, component.getSecurityRoles().get(1).getArguments().size());
		
		pathArgument = (PathSecurityRoleArgument)component.getSecurityRoles().get(1).getArguments().get(0);
		assertEquals(RuntimeModelHelper.createKnowledgePath("name"), pathArgument.getKnowledgePath());
		assertEquals("name", pathArgument.getName());
		assertEquals("time", component.getSecurityRoles().get(1).getArguments().get(1).getName());		
	}
		
	@Test 
	public void testCloningOfSecurityAnnotations() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		CorrectC4 input = new CorrectC4();
		
		// when process() is called
		processor.process(input);
		
		// then annotations are parsed correctly
		assertEquals(1, model.getComponentInstances().size());
		ComponentInstance component = model.getComponentInstances().get(0);
		KnowledgeManager km = component.getKnowledgeManager();
		
		List<KnowledgeSecurityTag> timeSecurityTags = km.getKnowledgeSecurityTags(RuntimeModelHelper.createPathNodeField("time"));
		SecurityRole role = timeSecurityTags.get(0).getRequiredRole();		
		AbsoluteSecurityRoleArgument argument = (AbsoluteSecurityRoleArgument) role.getArguments().stream().filter(arg -> arg.getName().equals("x_array")).findFirst().get();
		
		// get current content of the tag
		String[] oldValue = (String[]) argument.getValue();
		assertArrayEquals(new String[] {"a", "b", "c"}, oldValue);
		
		// modify the array
		cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role2.x_array[0] = "x";
		
		// verify the change didn't affect loaded parameter
		assertArrayEquals(new String[] {"a", "b", "c"}, oldValue);
		
		// restore the original value 
		cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC4.Role2.x_array[0] = "a";
	}
	
	@Test 
	public void testLockingOfSecurityAnnotations() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		CorrectC4 input = new CorrectC4();
		
		// when process() is called
		processor.process(input);
		
		// then annotations are parsed correctly
		assertEquals(1, model.getComponentInstances().size());
		ComponentInstance component = model.getComponentInstances().get(0);
		KnowledgeManager km = component.getKnowledgeManager();
		
		// verify that the knowledge path used as a security role argument is locked
		assertTrue(km.isLocked(RuntimeModelHelper.createKnowledgePath("name")));
	}
	
	@Test
	public void testNonExistingSecurityArgument() throws AnnotationProcessorException {
		// given component with role, which contains unresolvable argument is processed
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC12 input = new WrongC12();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Parameter no_such_parameter is not present in the knowledge.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test
	public void testSecurityOnNonSerializableField() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC13 input = new WrongC13();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Field nonserializable is not serializable.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test
	public void testNonExistingAliasClass() throws AnnotationProcessorException {
		// given roleAlias refers to something that is not a role definition
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC10 input = new WrongC10();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Role class must be decoreted with @RoleDefinition.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test 
	public void testComponentSecurityInheritanceAnnotations1() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		CorrectC5 input = new CorrectC5();
		
		// when process() is called
		processor.process(input);
		
		// then annotations are parsed correctly
		assertEquals(1, model.getComponentInstances().size());
		ComponentInstance component = model.getComponentInstances().get(0);
		KnowledgeManager km = component.getKnowledgeManager();
		
		List<KnowledgeSecurityTag> nameSecurityTags = km.getKnowledgeSecurityTags(RuntimeModelHelper.createPathNodeField("name"));
		SecurityRole securityRole = nameSecurityTags.get(0).getRequiredRole();
		
		assertEquals(1, nameSecurityTags.size());
		assertEquals(cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.CorrectC5.Role3.class.getName(), securityRole.getRoleName());
		assertEquals(5, securityRole.getArguments().size());
		
		assertEquals("fieldRole2", securityRole.getArguments().get(0).getName());
		assertEquals("fieldRole3", securityRole.getArguments().get(1).getName());
		assertEquals("fieldRole0", securityRole.getArguments().get(2).getName());
		assertEquals("fieldRole1", securityRole.getArguments().get(3).getName());
		assertEquals("fieldRole_date", securityRole.getArguments().get(4).getName());

		assertTrue(securityRole.getArguments().get(0) instanceof BlankSecurityRoleArgument);
		assertTrue(securityRole.getArguments().get(1) instanceof PathSecurityRoleArgument);
		assertTrue(securityRole.getArguments().get(2) instanceof AbsoluteSecurityRoleArgument);
		assertTrue(securityRole.getArguments().get(3) instanceof PathSecurityRoleArgument);
		assertTrue(securityRole.getArguments().get(4) instanceof AbsoluteSecurityRoleArgument);
		
		assertEquals(RuntimeModelHelper.createKnowledgePath("v"), ((PathSecurityRoleArgument)securityRole.getArguments().get(1)).getKnowledgePath() );
		assertEquals("value_override", ((AbsoluteSecurityRoleArgument)securityRole.getArguments().get(2)).getValue() );
		assertEquals(RuntimeModelHelper.createKnowledgePath("x"), ((PathSecurityRoleArgument)securityRole.getArguments().get(3)).getKnowledgePath() );
		assertEquals(123, ((AbsoluteSecurityRoleArgument)securityRole.getArguments().get(4)).getValue() );
	}	
	
	@Test
	public void testSecurityCompromise() throws AnnotationProcessorException {
		// given component with role, which contains unresolvable argument is processed
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC11 input = new WrongC11();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Parameter capacity is not appropriately secured.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test 
	public void testRatingAnnotations() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		CorrectC6 input = new CorrectC6();
		
		// when process() is called
		processor.process(input);
		
		// then annotations are parsed correctly
		assertEquals(1, model.getComponentInstances().size());
		ComponentInstance component = model.getComponentInstances().get(0);
		
		assertEquals(1, component.getComponentProcesses().size());
		assertNotNull(component.getRatingsProcess());
		
		ComponentProcess process = component.getComponentProcesses().get(0);
		RatingsProcess ratingsProcess = component.getRatingsProcess();
		
		assertEquals(3, process.getParameters().size());
		assertEquals(3, ratingsProcess.getParameters().size());
		
		assertEquals(ParameterKind.IN, process.getParameters().get(0).getKind());
		assertEquals(ParameterKind.RATING, process.getParameters().get(1).getKind());
		assertEquals(ParameterKind.OUT, process.getParameters().get(2).getKind());
		
		assertEquals(ParameterKind.IN, ratingsProcess.getParameters().get(0).getKind());
		assertEquals(ParameterKind.RATING, ratingsProcess.getParameters().get(1).getKind());
		assertEquals(ParameterKind.RATING, ratingsProcess.getParameters().get(2).getKind());
		
		assertTrue(process.isIgnoreKnowledgeCompromise());
	}
	
	@Test 
	public void testRatingAnnotationsEnsemble() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		CorrectE4 input = new CorrectE4();
		
		// when process() is called
		processor.process(input);
		
		// then annotations are parsed correctly
		assertEquals(1, model.getEnsembleDefinitions().size());
		EnsembleDefinition ensemble = model.getEnsembleDefinitions().get(0);
		
		assertEquals(3, ensemble.getMembership().getParameters().size());
		assertEquals(3, ensemble.getKnowledgeExchange().getParameters().size());
		
		assertEquals(ParameterKind.IN, ensemble.getMembership().getParameters().get(0).getKind());
		assertEquals(ParameterKind.RATING, ensemble.getMembership().getParameters().get(1).getKind());
		assertEquals(ParameterKind.IN, ensemble.getMembership().getParameters().get(2).getKind());
		
		assertEquals(ParameterKind.INOUT, ensemble.getKnowledgeExchange().getParameters().get(0).getKind());
		assertEquals(ParameterKind.RATING, ensemble.getKnowledgeExchange().getParameters().get(1).getKind());
		assertEquals(ParameterKind.IN, ensemble.getKnowledgeExchange().getParameters().get(2).getKind());
	}
	
	@Test 
	public void testRatingAnnotations_Error1() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC6 input = new WrongC6();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The rating process method parameter cannot contain " + Out.class.getSimpleName() + " nor " + InOut.class.getSimpleName() + " parameters.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test 
	public void testRatingAnnotations_Error2() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC7 input = new WrongC7();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The rating process method parameter must be of type " + ReadonlyRatingsHolder.class.getSimpleName() + ".");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test 
	public void testRatingAnnotations_Error3() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongE6 input = new WrongE6();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The rating process method parameter must be of type " + ReadonlyRatingsHolder.class.getSimpleName() + ".");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test 
	public void testSecurityAnnotations_Error1() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC8 input = new WrongC8();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Cannot assign the same role " + WrongC8.Role1.class.getSimpleName() + " multiple times.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test 
	public void testSecurityAnnotations_Error2() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		WrongC9 input = new WrongC9();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Local knowledge must not be secured.");
		
		// when process() is called
		processor.process(input);
	}
	
	@Test
	public void testComponentModelInheritance() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		CorrectC1 input = new ChildOfCorrectC1();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
//		saveInXMI(model, expected);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testAllEnsembleAnnotations() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		CorrectE1 input = new CorrectE1();
		processor.process(input);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testModelDirectlyFromEnsembleClassDefinition() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		processor.process(CorrectE1.class);
		CorrectE1 input = new CorrectE1();
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test
	public void testSequencialUpdateOfTheSameModel() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		Object input = new CorrectC1();
		processor.process(input);
		input = new CorrectC2();
		processor.process(input);
		input = new CorrectC3();
		processor.process(input);
		input = new CorrectE1();
		processor.process(input);
		input = new CorrectE2();
		processor.process(input);
		input = new CorrectE3();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}	
	
	@Test
	public void testBatchUpdateOfTheSameModel1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		Object input1 = new CorrectC1();
		Object input2 = new CorrectC2();
		Object input3 = new CorrectC3();
		Object input4 = new CorrectE1();
		Object input5 = new CorrectE2();
		Object input6 = new CorrectE3();
		processor.process(input1, input2, input3, input4, input5, input6);
		removeKnowledgeManagersFromComponents(model);		
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}

	@Test 
	public void testBatchUpdateOfTheSameModel2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		List<Object> inputs = new ArrayList<>();
		inputs.add(new CorrectC1());
		inputs.add(new CorrectC2());
		inputs.add(new CorrectC3());
		inputs.add(new CorrectE1());
		inputs.add(new CorrectE2());
		inputs.add(new CorrectE3());
		processor.process(inputs);
		removeKnowledgeManagersFromComponents(model);		
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
		
	@Test 
	public void testEventBasedComponents() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		// no periodic trigger, 1 knowledge change trigger:
		CorrectC2 input = new CorrectC2();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testEventBasedEnsembles() throws AnnotationProcessorException {		
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		// no periodic trigger, 2 knowledge change triggers:
		CorrectE2 input = new CorrectE2();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testParameterWithMapEntry() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		CorrectC3 input = new CorrectC3();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}

	@Test 
	public void testParameterWithNestedMapEntry() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	

		CorrectE3 input = new CorrectE3();
		processor.process(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testExceptionsNullModel() throws AnnotationProcessorException {
		AnnotationProcessor processor = new AnnotationProcessor(factory,null,knowledgeManagerFactory);	
		
		Object input = new Object();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Provided model cannot be null.");
		processor.process(input);	
	}
	
	@Test 
	public void testExceptionsNullObject() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Provide an initialized object or a non-empty list of objects.");
		processor.process(null);	
	}	
	
	@Test 
	public void testExceptionsNonInitializedComponent() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("For a component to be parsed, it has to be an INSTANCE of a class annotated with @Component.");
		processor.process(CorrectC1.class);	
	}
	
	@Test 
	public void testExceptionsInClassAnnotations1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongCE1 input = new WrongCE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Class: "+input.getClass().getCanonicalName()+"->" +
				"No @Component or @Ensemble annotation found.");
		processor.process(input);	
	}
	
	@Test 
	public void testExceptionsInClassAnnotations2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongCE2 input = new WrongCE2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Class: "+input.getClass().getCanonicalName()+"->" +
				"Both @Component or @Ensemble annotation found.");
		processor.process(input);	
	}

	@Test 
	public void testExceptionsInComponentParsing1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongC1 input = new WrongC1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Method process1 annotated as @Process should be public and static.");
		processor.process(input);
	}

	@Test 
	public void testExceptionsInComponentParsing2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongC2 input = new WrongC2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"Parameter: 2->" +
				"No kind annotation was found.");
		processor.process(input);
	}

	@Test 
	public void testExceptionsInComponentParsing3() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongC3 input = new WrongC3();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"Parameter: 3->" +
				"More than one kind annotation was found.");
		processor.process(input);
	}
	
	@Test 
	public void testExceptionsInComponentParsing4() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongC4 input = new WrongC4();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"No triggers were found.");
		processor.process(input);
	}
	
	@Test 
	public void testExceptionsInComponentParsing5() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongC5 input = new WrongC5();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"The component process cannot have zero parameters.");
		processor.process(input);
	}
	
	@Test 
	public void testExceptionsInEnsembleParsing1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongE1 input = new WrongE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"Method knowledgeExchange annotated as @KnowledgeExchange should be public and static.");
		processor.process(input);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongE2 input = new WrongE2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"No @Membership annotation was found");
		processor.process(input);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing3() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongE3 input = new WrongE3();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"More than one instance of @KnowledgeExchange annotation was found");
		processor.process(input);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing4()
			throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongE4 input = new WrongE4();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"No triggers were found.");
		processor.process(input);
	}
	
	@Test 
	public void testExceptionsInEnsembleParsing5()
			throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongE5 input = new WrongE5();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"KnowledgeExchange->" +
				"Parameter: 1->" +
				"The path does not start with one of the 'coord' or 'member' keywords.");
		processor.process(input);
	}
	
	/*
	 * Unit tests. 
	 */
	
	@Test 
	public void testCreateKnowledgePath() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "level1.level2.level3";
		KnowledgePath kp = processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level2");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level3");
		
		pathStr = "level1.[level21.level22.level23]";
		kp = processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assert kp.getNodes().get(1) instanceof PathNodeMapKey;
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level21");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level22");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level23");
		
		pathStr = "level1.[level21.[level221.level222].level23]";
		kp = processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level21");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level221");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level222");
		
		pathStr = "details.[id]";
		kp = processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"details");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertTrue(kp.getNodes().get(0) instanceof PathNodeComponentId);

		pathStr = "level1.id.level2";
		kp = processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"id");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level2");
		
		pathStr = "[coord.names]";
		kp = processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);
		assertEquals(kp.getNodes().size(),1);
		assertTrue(kp.getNodes().get(0) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(0)).getKeyPath();
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"coord");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"names");
		
		pathStr = "[coord.names]";
		kp = processor.createKnowledgePath(pathStr, PathOrigin.ENSEMBLE);
		assertEquals(kp.getNodes().size(),1);
		assertTrue(kp.getNodes().get(0) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(0)).getKeyPath();
		assertTrue(kp.getNodes().get(0) instanceof PathNodeCoordinator);
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"names");
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath1() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "namesToAddresses[member.name]";
		exception.expect(ParseException.class);
		exception.expectMessage(
				"The structure 'data1[data2]' is not allowed in a path, " +
				"use the dot separator: 'data1.[data2]'");
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath2() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "namesToAddresses.[member.name";
		exception.expect(ParseException.class);
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath3() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "level1..level2";
		exception.expect(ParseException.class);
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath4() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "level1.  .level2";
		exception.expect(TokenMgrError.class);
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath5() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "";
		exception.expect(ParseException.class);
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}

	@Test 
	public void testExceptionsInCreateKnowledgePath6() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "id.level2";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"A component identifier cannot be followed by any other fields in a path.");
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath7() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "details.[id.x]";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"A component identifier cannot be followed by any other fields in a path.");
		processor.createKnowledgePath(pathStr, PathOrigin.COMPONENT);		
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath8() throws ParseException, AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		String pathStr = "whatever.level2";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"The path does not start with one of the 'coord' or 'member' keywords.");
		// false means that the knowledge path is found in an ensemble definition:
		processor.createKnowledgePath(pathStr, PathOrigin.ENSEMBLE);		
	}

	
	@Test
	public void testProcessInitialKnowledge() throws AnnotationProcessorException{
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		Object o = new CorrectC1();
		/*  "CorrectC1" fields are:
		 * 
		 * 	public String name;
		 *	public Integer capacity;
		 *	public Date time;
		 */
		ChangeSet cs = processor.extractInitialKnowledge(o, false);
		assertEquals(cs.getUpdatedReferences().size(),3);
		assertEquals(cs.getDeletedReferences().size(),0);
		KnowledgePath kp = factory.createKnowledgePath();
		PathNodeField f = factory.createPathNodeField();
		kp.getNodes().add(f);
		f.setName("time");
		assertTrue(cs.getUpdatedReferences().contains(kp));
		f.setName("capacity");
		assertTrue(cs.getUpdatedReferences().contains(kp));
		f.setName("name");
		assertTrue(cs.getUpdatedReferences().contains(kp));
		f.setName("anotherName");
		assertFalse(cs.getUpdatedReferences().contains(kp));		
	}
	
	@Test 
	public void testSecurityOfIdField() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory);	
		
		WrongC4WithSecurity input = new WrongC4WithSecurity();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The component ID must not be secured");
		processor.process(input);	
	}
	
	/*
	 * Helpers.
	 */
	
	private void saveInXMI(RuntimeMetadata model, File file) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet
				.getResourceFactoryRegistry()
				.getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		URI fileURI = URI.createFileURI(file.getAbsolutePath());
		Resource resource = resourceSet.createResource(fileURI);
		resource.getContents().add(model);
		try {
			resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File getExpectedFile(Object o) {
		String outF = "test"
				+ File.separator
				+ this.getClass().getPackage().getName().replace('.', File.separatorChar)
				+ File.separator
				+ "output" + File.separator;
		String path = outF + o.getClass().getSimpleName() + ".xmi";
		return new File(path);
	}
	
	private void removeKnowledgeManagersFromComponents(RuntimeMetadata model) {
		for (ComponentInstance c: model.getComponentInstances()) {
			c.setKnowledgeManager(null);
			c.setShadowKnowledgeManagerRegistry(null);
		}
	}
}

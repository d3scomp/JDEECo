package cz.cuni.mff.d3s.deeco.annotations.processor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.isA;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junitx.framework.FileAssert;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InOrder;

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
import cz.cuni.mff.d3s.deeco.runtime.DuplicateEnsembleDefinitionException;

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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());
		
		CorrectC1 input = new CorrectC1();
		processor.processComponent(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testComponentSecurityAnnotations() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		CorrectC4 input = new CorrectC4();
		
		// when process() is called
		processor.processComponent(input);
		
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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		CorrectC4 input = new CorrectC4();
		
		// when process() is called
		processor.processComponent(input);
		
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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		CorrectC4 input = new CorrectC4();
		
		// when process() is called
		processor.processComponent(input);
		
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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC12 input = new WrongC12();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Parameter no_such_parameter is not present in the knowledge.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test
	public void testSecurityOnNonSerializableField() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC13 input = new WrongC13();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Field nonserializable is not serializable.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test
	public void testNonExistingAliasClass() throws AnnotationProcessorException {
		// given roleAlias refers to something that is not a role definition
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC10 input = new WrongC10();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Role class must be decoreted with @RoleDefinition.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test 
	public void testComponentSecurityInheritanceAnnotations1() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		CorrectC5 input = new CorrectC5();
		
		// when process() is called
		processor.processComponent(input);
		
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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC11 input = new WrongC11();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Parameter capacity is not appropriately secured.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test 
	public void testRatingAnnotations() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		CorrectC6 input = new CorrectC6();
		
		// when process() is called
		processor.processComponent(input);
		
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
	public void testRatingAnnotationsEnsemble() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		// when process() is called
		processor.processEnsemble(CorrectE4.class);
		
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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC6 input = new WrongC6();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The rating process method parameter cannot contain " + Out.class.getSimpleName() + " nor " + InOut.class.getSimpleName() + " parameters.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test 
	public void testRatingAnnotations_Error2() throws AnnotationProcessorException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC7 input = new WrongC7();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The rating process method parameter must be of type " + ReadonlyRatingsHolder.class.getSimpleName() + ".");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test 
	public void testRatingAnnotations_Error3() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		// given component with rating annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The rating process method parameter must be of type " + ReadonlyRatingsHolder.class.getSimpleName() + ".");
		
		// when process() is called
		processor.processEnsemble(WrongE6.class);
	}
	
	@Test 
	public void testSecurityAnnotations_Error1() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC8 input = new WrongC8();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Cannot assign the same role " + WrongC8.Role1.class.getSimpleName() + " multiple times.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test 
	public void testSecurityAnnotations_Error2() throws AnnotationProcessorException {
		// given component with security annotations is processed by the annotations processor
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		WrongC9 input = new WrongC9();
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Local knowledge must not be secured.");
		
		// when process() is called
		processor.processComponent(input);
	}
	
	@Test
	public void testComponentModelInheritance() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		CorrectC1 input = new ChildOfCorrectC1();
		processor.processComponent(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
//		saveInXMI(model, expected);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testAllEnsembleAnnotations() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		processor.processEnsemble(CorrectE1.class);
		CorrectE1 input = new CorrectE1();
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testModelDirectlyFromEnsembleClassDefinition() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		processor.processEnsemble(CorrectE1.class);
		CorrectE1 input = new CorrectE1();
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test
	public void testSequencialUpdateOfTheSameModel() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	

		Object input = new CorrectC1();
		processor.processComponent(input);
		input = new CorrectC2();
		processor.processComponent(input);
		input = new CorrectC3();
		processor.processComponent(input);
		processor.processEnsemble(CorrectE1.class);
		processor.processEnsemble(CorrectE2.class);
		processor.processEnsemble(CorrectE3.class);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}	

	@Test 
	public void testEventBasedComponents() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	

		// no periodic trigger, 1 knowledge change trigger:
		CorrectC2 input = new CorrectC2();
		processor.processComponent(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testEventBasedEnsembles() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {		
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	

		// no periodic trigger, 2 knowledge change triggers:
		processor.processEnsemble(CorrectE2.class);
		removeKnowledgeManagersFromComponents(model);
		CorrectE2 input = new CorrectE2();
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testParameterWithMapEntry() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	

		CorrectC3 input = new CorrectC3();
		processor.processComponent(input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}

	@Test 
	public void testParameterWithNestedMapEntry() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	

		processor.processEnsemble(CorrectE3.class);
		removeKnowledgeManagersFromComponents(model);
		CorrectE3 input = new CorrectE3();
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testExceptionsNullModel() throws AnnotationProcessorException {
		AnnotationProcessor processor = new AnnotationProcessor(factory,null,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		Object input = new Object();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("RuntimeMetadata model cannot be null.");
		processor.processComponent(input);	
	}
	
	@Test 
	public void testExceptionsNonInitializedComponent() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Provided component object(s) cannot be classes.");
		processor.processComponent(CorrectC1.class);	
	}
	
	@Test 
	public void testExceptionsInClassAnnotations1() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongCE1 input = new WrongCE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Class: "+input.getClass().getCanonicalName()+"->" +
				"No @Ensemble annotation found.");
		processor.processEnsemble(WrongCE1.class);	
	}
	
	@Test 
	public void testExceptionsInComponentParsing1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongC1 input = new WrongC1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Method process1 annotated as @Process should be public and static.");
		processor.processComponent(input);
	}

	@Test 
	public void testExceptionsInComponentParsing2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongC2 input = new WrongC2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"Parameter: 2->" +
				"No kind annotation was found.");
		processor.processComponent(input);
	}

	@Test 
	public void testExceptionsInComponentParsing3() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongC3 input = new WrongC3();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"Parameter: 3->" +
				"More than one kind annotation was found.");
		processor.processComponent(input);
	}
	
	@Test 
	public void testExceptionsInComponentParsing4() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongC4 input = new WrongC4();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"No triggers were found.");
		processor.processComponent(input);
	}
	
	@Test 
	public void testExceptionsInComponentParsing5() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongC5 input = new WrongC5();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"The component process cannot have zero parameters.");
		processor.processComponent(input);
	}
	
	@Test 
	public void testExceptionsInEnsembleParsing1() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongE1 input = new WrongE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"Method knowledgeExchange annotated as @KnowledgeExchange should be public and static.");
		processor.processEnsemble(WrongE1.class);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing2() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongE2 input = new WrongE2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"No @Membership annotation was found");
		processor.processEnsemble(WrongE2.class);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing3() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongE3 input = new WrongE3();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"More than one instance of @KnowledgeExchange annotation was found");
		processor.processEnsemble(WrongE3.class);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing4()
			throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongE4 input = new WrongE4();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"No triggers were found.");
		processor.processEnsemble(WrongE4.class);
	}
	
	@Test 
	public void testExceptionsInEnsembleParsing5()
			throws AnnotationProcessorException, DuplicateEnsembleDefinitionException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongE5 input = new WrongE5();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"KnowledgeExchange->" +
				"Parameter: 1->" +
				"The path does not start with one of the 'coord' or 'member' keywords.");
		processor.processEnsemble(WrongE5.class);
	}
	
	/*
	 * Unit tests. 
	 */
	
	@Test
	public void testProcessInitialKnowledge() throws AnnotationProcessorException{
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
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
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,new ArrayList<AnnotationChecker>());	
		
		WrongC4WithSecurity input = new WrongC4WithSecurity();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("The component ID must not be secured");
		processor.processComponent(input);	
	}
	
	@Test
	public void testAnnotationCheckers() throws AnnotationProcessorException, DuplicateEnsembleDefinitionException, AnnotationCheckerException {
		AnnotationChecker checker1 = mock(AnnotationChecker.class);
		AnnotationChecker checker2 = mock(AnnotationChecker.class);
		
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,
				Arrays.asList(checker1, checker2));
		
		CorrectC1 component = new CorrectC1();
		ComponentInstance componentInstance = processor.processComponent(component);
		EnsembleDefinition ensembleDefinition = processor.processEnsemble(CorrectE1.class);
		
		InOrder io = inOrder(checker1, checker2);
		io.verify(checker1).validateComponent(component, componentInstance);
		io.verify(checker2).validateComponent(component, componentInstance);
		io.verify(checker1).validateEnsemble(CorrectE1.class, ensembleDefinition);
		io.verify(checker2).validateEnsemble(CorrectE1.class, ensembleDefinition);
		io.verifyNoMoreInteractions();
	}
	
	@Test
	public void testAnnotationCheckersComponentException() throws AnnotationProcessorException, AnnotationCheckerException {
		AnnotationChecker checker1 = mock(AnnotationChecker.class);
		AnnotationChecker checker2 = mock(AnnotationChecker.class);
		
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,
				Arrays.asList(checker1, checker2));
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectCause(isA(AnnotationCheckerException.class));
		
		CorrectC1 component = new CorrectC1();
		doThrow(new AnnotationCheckerException("(inner)")).when(checker2).validateComponent(eq(component), any());
		processor.processComponent(component);
	}
	
	@Test
	public void testAnnotationCheckersEnsembleException() throws AnnotationProcessorException, AnnotationCheckerException, DuplicateEnsembleDefinitionException {
		AnnotationChecker checker1 = mock(AnnotationChecker.class);
		AnnotationChecker checker2 = mock(AnnotationChecker.class);
		
		RuntimeMetadata model = factory.createRuntimeMetadata();
		AnnotationProcessor processor = new AnnotationProcessor(factory,model,knowledgeManagerFactory,
				Arrays.asList(checker1, checker2));
		
		exception.expect(AnnotationProcessorException.class);
		exception.expectCause(isA(AnnotationCheckerException.class));
		
		doThrow(new AnnotationCheckerException("(inner)")).when(checker1).validateEnsemble(eq(CorrectE1.class), any());
		processor.processEnsemble(CorrectE1.class);
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

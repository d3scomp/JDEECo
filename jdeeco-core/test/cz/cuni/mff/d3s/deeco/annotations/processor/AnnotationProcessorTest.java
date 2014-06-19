package cz.cuni.mff.d3s.deeco.annotations.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.TokenMgrError;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.samples.*;
import cz.cuni.mff.d3s.deeco.knowledge.ChangeSet;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * @author Ilias Gerostathopoulos <iliasg@d3s.mff.cuni.cz>
 *
 */
public class AnnotationProcessorTest {

	protected RuntimeMetadataFactory factory;
	protected AnnotationProcessor processor;
	protected File tempFile;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE;
		processor = new AnnotationProcessor(factory);
		tempFile = Files.createTempFile(null, ".xmi").toFile();
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
		CorrectC1 input = new CorrectC1();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test
	public void testComponentModelInheritance() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata(); 
		CorrectC1 input = new ChildOfCorrectC1();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
//		saveInXMI(model, expected);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testAllEnsembleAnnotations() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		CorrectE1 input = new CorrectE1();
		processor.process(model,input);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testModelDirectlyFromEnsembleClassDefinition() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		processor.process(model, CorrectE1.class);
		CorrectE1 input = new CorrectE1();
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test
	public void testSequencialUpdateOfTheSameModel() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		Object input = new CorrectC1();
		processor.process(model,input);
		input = new CorrectC2();
		processor.process(model,input);
		input = new CorrectC3();
		processor.process(model,input);
		input = new CorrectE1();
		processor.process(model,input);
		input = new CorrectE2();
		processor.process(model,input);
		input = new CorrectE3();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}	
	
	@Test
	public void testBatchUpdateOfTheSameModel1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		Object input1 = new CorrectC1();
		Object input2 = new CorrectC2();
		Object input3 = new CorrectC3();
		Object input4 = new CorrectE1();
		Object input5 = new CorrectE2();
		Object input6 = new CorrectE3();
		processor.process(model,input1, input2, input3, input4, input5, input6);
		removeKnowledgeManagersFromComponents(model);		
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}

	@Test 
	public void testBatchUpdateOfTheSameModel2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		List<Object> inputs = new ArrayList<>();
		inputs.add(new CorrectC1());
		inputs.add(new CorrectC2());
		inputs.add(new CorrectC3());
		inputs.add(new CorrectE1());
		inputs.add(new CorrectE2());
		inputs.add(new CorrectE3());
		processor.process(model,inputs);
		removeKnowledgeManagersFromComponents(model);		
		File expected = getExpectedFile(new C1C2C3E1E2E3());
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
		
	@Test 
	public void testEventBasedComponents() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		// no periodic trigger, 1 knowledge change trigger:
		CorrectC2 input = new CorrectC2();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testEventBasedEnsembles() throws AnnotationProcessorException {		
		RuntimeMetadata model = factory.createRuntimeMetadata();
		// no periodic trigger, 2 knowledge change triggers:
		CorrectE2 input = new CorrectE2();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testParameterWithMapEntry() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		CorrectC3 input = new CorrectC3();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}

	@Test 
	public void testParameterWithNestedMapEntry() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		CorrectE3 input = new CorrectE3();
		processor.process(model,input);
		removeKnowledgeManagersFromComponents(model);
		File expected = getExpectedFile(input);
		saveInXMI(model, tempFile);
		FileAssert.assertEquals(expected, tempFile);
	}
	
	@Test 
	public void testExceptionsNullModel() throws AnnotationProcessorException {
		Object input = new Object();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Provided model cannot be null.");
		processor.process(null,input);	
	}
	
	@Test 
	public void testExceptionsNullObject() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("Provide an initialized object or a non-empty list of objects.");
		processor.process(model,null);	
	}	
	
	@Test 
	public void testExceptionsNonInitializedComponent() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage("For a component to be parsed, it has to be an INSTANCE of a class annotated with @Component.");
		processor.process(model,CorrectC1.class);	
	}
	
	@Test 
	public void testExceptionsInClassAnnotations1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongCE1 input = new WrongCE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Class: "+input.getClass().getCanonicalName()+"->" +
				"No @Component or @Ensemble annotation found.");
		processor.process(model,input);	
	}
	
	@Test 
	public void testExceptionsInClassAnnotations2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongCE2 input = new WrongCE2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Class: "+input.getClass().getCanonicalName()+"->" +
				"Both @Component or @Ensemble annotation found.");
		processor.process(model,input);	
	}

	@Test 
	public void testExceptionsInComponentParsing1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongC1 input = new WrongC1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Method process1 annotated as @Process should be public and static.");
		processor.process(model,input);
	}

	@Test 
	public void testExceptionsInComponentParsing2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongC2 input = new WrongC2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"Parameter: 2->" +
				"No direction annotation was found.");
		processor.process(model,input);
	}

	@Test 
	public void testExceptionsInComponentParsing3() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongC3 input = new WrongC3();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"Parameter: 3->" +
				"More than one direction annotation was found.");
		processor.process(model,input);
	}
	
	@Test 
	public void testExceptionsInComponentParsing4() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongC4 input = new WrongC4();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"No triggers were found.");
		processor.process(model,input);
	}
	
	@Test 
	public void testExceptionsInComponentParsing5() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongC5 input = new WrongC5();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Component: "+input.getClass().getCanonicalName()+"->" +
				"Process: process1->" +
				"The component process cannot have zero parameters.");
		processor.process(model,input);
	}
	
	@Test 
	public void testExceptionsInEnsembleParsing1() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongE1 input = new WrongE1();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"Method knowledgeExchange annotated as @KnowledgeExchange should be public and static.");
		processor.process(model,input);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing2() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongE2 input = new WrongE2();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"No @Membership annotation was found");
		processor.process(model,input);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing3() throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongE3 input = new WrongE3();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"More than one instance of @KnowledgeExchange annotation was found");
		processor.process(model,input);	
	}

	@Test 
	public void testExceptionsInEnsembleParsing4()
			throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongE4 input = new WrongE4();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"No triggers were found.");
		processor.process(model,input);
	}
	
	@Test 
	public void testExceptionsInEnsembleParsing5()
			throws AnnotationProcessorException {
		RuntimeMetadata model = factory.createRuntimeMetadata();
		WrongE5 input = new WrongE5();
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"Ensemble: "+input.getClass().getCanonicalName()+"->" +
				"KnowledgeExchange->" +
				"Parameter: 1->" +
				"The path does not start with one of the 'coord' or 'member' keywords.");
		processor.process(model,input);
	}
	
	/*
	 * Unit tests. 
	 */
	
	@Test 
	public void testCreateKnowledgePath() throws ParseException, AnnotationProcessorException {
		String pathStr = "level1.level2.level3";
		KnowledgePath kp = processor.createKnowledgePath(pathStr, true);
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level2");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level3");
		
		pathStr = "level1.[level21.level22.level23]";
		kp = processor.createKnowledgePath(pathStr, true);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assert kp.getNodes().get(1) instanceof PathNodeMapKey;
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level21");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"level22");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level23");
		
		pathStr = "level1.[level21.[level221.level222].level23]";
		kp = processor.createKnowledgePath(pathStr, true);
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
		kp = processor.createKnowledgePath(pathStr, true);
		assertEquals(kp.getNodes().size(),2);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"details");
		assertTrue(kp.getNodes().get(1) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(1)).getKeyPath();
		assertTrue(kp.getNodes().get(0) instanceof PathNodeComponentId);

		pathStr = "level1.id.level2";
		kp = processor.createKnowledgePath(pathStr, true);
		assertEquals(kp.getNodes().size(),3);
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"level1");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"id");
		assertEquals(((PathNodeField) kp.getNodes().get(2)).getName(),"level2");
		
		pathStr = "[coord.names]";
		kp = processor.createKnowledgePath(pathStr, true);
		assertEquals(kp.getNodes().size(),1);
		assertTrue(kp.getNodes().get(0) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(0)).getKeyPath();
		assertEquals(((PathNodeField) kp.getNodes().get(0)).getName(),"coord");
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"names");
		
		pathStr = "[coord.names]";
		kp = processor.createKnowledgePath(pathStr, false);
		assertEquals(kp.getNodes().size(),1);
		assertTrue(kp.getNodes().get(0) instanceof PathNodeMapKey);
		kp = ((PathNodeMapKey) kp.getNodes().get(0)).getKeyPath();
		assertTrue(kp.getNodes().get(0) instanceof PathNodeCoordinator);
		assertEquals(((PathNodeField) kp.getNodes().get(1)).getName(),"names");
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath1() throws ParseException, AnnotationProcessorException {
		String pathStr = "namesToAddresses[member.name]";
		exception.expect(ParseException.class);
		exception.expectMessage(
				"The structure 'data1[data2]' is not allowed in a path, " +
				"use the dot separator: 'data1.[data2]'");
		processor.createKnowledgePath(pathStr, true);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath2() throws ParseException, AnnotationProcessorException {
		String pathStr = "namesToAddresses.[member.name";
		exception.expect(ParseException.class);
		processor.createKnowledgePath(pathStr, true);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath3() throws ParseException, AnnotationProcessorException {
		String pathStr = "level1..level2";
		exception.expect(ParseException.class);
		processor.createKnowledgePath(pathStr, true);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath4() throws ParseException, AnnotationProcessorException {
		String pathStr = "level1.  .level2";
		exception.expect(TokenMgrError.class);
		processor.createKnowledgePath(pathStr, true);		
	}
	
	@Test
	public void testExceptionsInCreateKnowledgePath5() throws ParseException, AnnotationProcessorException {
		String pathStr = "";
		exception.expect(ParseException.class);
		processor.createKnowledgePath(pathStr, true);		
	}

	@Test 
	public void testExceptionsInCreateKnowledgePath6() throws ParseException, AnnotationProcessorException {
		String pathStr = "id.level2";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"A component identifier cannot be followed by any other fields in a path.");
		processor.createKnowledgePath(pathStr, true);		
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath7() throws ParseException, AnnotationProcessorException {
		String pathStr = "details.[id.x]";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"A component identifier cannot be followed by any other fields in a path.");
		processor.createKnowledgePath(pathStr, true);		
	}
	
	@Test 
	public void testExceptionsInCreateKnowledgePath8() throws ParseException, AnnotationProcessorException {
		String pathStr = "whatever.level2";
		exception.expect(AnnotationProcessorException.class);
		exception.expectMessage(
				"The path does not start with one of the 'coord' or 'member' keywords.");
		// false means that the knowledge path is found in an ensemble definition:
		processor.createKnowledgePath(pathStr, false);		
	}

	
	@Test
	public void testProcessInitialKnowledge(){
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

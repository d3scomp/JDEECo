package cz.cuni.mff.d3s.deeco.annotations.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.eclipse.emf.ecore.EObject;
import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.ModelValidationError.Severity;
import cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent;
import cz.cuni.mff.d3s.deeco.knowledge.CloningKnowledgeManagerFactory;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.RuntimeMetadata;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

/**
 * 
 * @author Jaroslav Keznikl <keznikl@d3s.mff.cuni.cz>
 *
 */
public class ModelValidatorTest {

	protected RuntimeMetadataFactory factory;
	protected AnnotationProcessor processor;
	protected RuntimeMetadata model; 
	protected ComponentInstance component;
	protected ComponentProcess process;
	
	
	ModelValidator tested;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setUp() throws Exception {
		factory = RuntimeMetadataFactory.eINSTANCE;
		model = factory.createRuntimeMetadata(); 
		processor = new AnnotationProcessor(factory, model, new CloningKnowledgeManagerFactory());
		
		processor.process(new ExampleComponent());
		component = model.getComponentInstances().get(0);
		process = component.getComponentProcesses().get(0);
		
		
		tested = new ModelValidator();
	}

	@After
	public void tearDown() throws IOException {
	}
	
	@Test 
	public void validationOfEmptyModelDoesNothing() throws AnnotationProcessorException {
		// WHEN validating an empty model
		// THEN validation ends properly
		assertTrue(tested.validate(factory.createRuntimeMetadata()));
	}
	
	
	@Test 
	public void validationOfNullModelDoesNotFail() throws AnnotationProcessorException {
		// WHEN validating a null model
		// THEN validation ends properly
		assertTrue(tested.validate(null));
	}
	
	@Test 
	public void validationOfValidModelSucceeds() throws AnnotationProcessorException {
		// WHEN validating a valid model
		// THEN validation ends properly
		assertTrue(tested.validate(model));
	}
	
	@Test 
	public void nonexistingProcessInputParametersAreIdentified() throws AnnotationProcessorException, ParseException {
		// WHEN validating a model where a process has a non-existing knowledge
		// field as its INOUT parameter		
		KnowledgePath nonexistent = processor.createKnowledgePath("non.existent", PathOrigin.COMPONENT);
		process.getParameters().get(1).setKnowledgePath(nonexistent);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		// AND reports it correctly
		assertValidationError(process, Severity.ERROR, 
				"INOUT parameter \"non.existent\" (2) of process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 refers to non-existent knowledge field.");
	}
	
	@Test 
	public void processInParametersThatAreNonPublicFieldsAreReported() throws AnnotationProcessorException, ParseException {
		// WHEN validating a model where a process has an IN parameter that
		// is a non-public class field
		KnowledgePath privateField = processor.createKnowledgePath("inoutInvalid", PathOrigin.COMPONENT);
		process.getParameters().get(0).setKnowledgePath(privateField);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		// AND reports it correctly
		assertValidationError(process, Severity.ERROR, 
				"IN parameter \"inoutInvalid\" (1) of process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 refers to non-existent knowledge field.");
	}
	
	@Test 
	public void processInOutParametersThatAreNonPublicFieldsAreReported() throws AnnotationProcessorException, ParseException {
		// WHEN validating a model where a process has an INOUT parameter that
		// is a non-public class field
		KnowledgePath privateField = processor.createKnowledgePath("inoutInvalid", PathOrigin.COMPONENT);
		process.getParameters().get(1).setKnowledgePath(privateField);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		// AND reports it correctly
		assertValidationError(process, Severity.ERROR, 
				"INOUT parameter \"inoutInvalid\" (2) of process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 refers to non-existent knowledge field.");
	}

	
	@Test 
	public void processInParametersTypeMismatchesAreReported() throws AnnotationProcessorException, ParseException {
		// WHEN validating a model where a process has an IN parameter that has
		// different type that the corresponding knowledge field
		KnowledgePath typeMismatch = processor.createKnowledgePath("typeMismatch", PathOrigin.COMPONENT);
		process.getParameters().get(0).setKnowledgePath(typeMismatch);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		assertValidationError(process, Severity.ERROR, 
				"IN parameter \"typeMismatch\" (1) of process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 does not match the type of the corresponding knowledge field.");
	}
	
	@Test 
	public void processInOutParametersTypeMismatchesAreReported() throws AnnotationProcessorException, ParseException {
		// WHEN validating a model where a process has an INOUT parameter that has
		// different type that the corresponding knowledge field
		KnowledgePath typeMismatch = processor.createKnowledgePath("typeMismatch", PathOrigin.COMPONENT);
		process.getParameters().get(1).setKnowledgePath(typeMismatch);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		assertValidationError(process, Severity.ERROR, 
				"INOUT parameter \"typeMismatch\" (2) of process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 does not match the type of the corresponding knowledge field.");
	}
	
	@Test 
	public void ProcessWithoutParametersIsIdentified() throws AnnotationProcessorException {
		// WHEN validating a model where a process has no parameters
		process.getParameters().clear();
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		// AND reports it correctly
		assertValidationError(process, Severity.ERROR,				
				"Process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 has no parameters.");
	}
	
	@Test 
	public void ProcessWithoutInputParametersIsIdentified() throws AnnotationProcessorException {
		// WHEN validating a model where a process has no input parameters
		process.getParameters().remove(0);
		process.getParameters().remove(0);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		// AND reports it correctly
		assertValidationError(process, Severity.WARNING,				
				"Process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 has no input parameters.");
	}
	
	@Test 
	public void ProcessWithoutOutputParametersIsIdentified() throws AnnotationProcessorException {
		// WHEN validating a model where a process has no output parameters
		process.getParameters().remove(1);
		process.getParameters().remove(1);
		
		// THEN validation finds this problem
		assertFalse(tested.validate(model));
		
		// AND reports it correctly
		assertValidationError(process, Severity.WARNING,
				"Process cz.cuni.mff.d3s.deeco.annotations.processor.input.validation.ExampleComponent.process1 has no output parameters.");
	}
	
	

	private void assertValidationError(EObject where, Severity severity, String substr) {
		assertEquals(1, tested.getErrors().size());
		ModelValidationError e = tested.getErrors().get(0);
		assertSame(where, e.getWhere());
		assertEquals(severity, e.getSeverity());
		assertThat(e.getMsg(), StringContains.containsString(substr));
	}
}

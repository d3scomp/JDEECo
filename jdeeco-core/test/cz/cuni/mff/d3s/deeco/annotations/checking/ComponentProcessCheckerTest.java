package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;

public class ComponentProcessCheckerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/*
	 * Tests for the validateComponent method
	 * TODO rewrite
	 */
/*		
	@Test
	public void validateComponentTest() throws AnnotationCheckerException {		
		@Component
		class TestComponent { };
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		ComponentInstance component = factory.createComponentInstance();
		KnowledgeManager knowledgeManager = Mockito.mock(KnowledgeManager.class);
		component.setKnowledgeManager(knowledgeManager);
		
		ComponentProcess process1 = factory.createComponentProcess();
		Parameter param11 = factory.createParameter();
		param11.setGenericType(String.class);
		process1.getParameters().add(param11);
		Parameter param12 = factory.createParameter();
		param12.setGenericType(Integer.class);
		process1.getParameters().add(param12);
		component.getComponentProcesses().add(process1);
		
		ComponentProcess process2 = factory.createComponentProcess();
		Parameter param21 = factory.createParameter();
		param21.setGenericType(Object.class);
		process2.getParameters().add(param21);
		component.getComponentProcesses().add(process2);
		Parameter param22 = factory.createParameter();
		param22.setGenericType(String.class);
		process2.getParameters().add(param22);
		
		ComponentProcessChecker checker = Mockito.spy(new ComponentProcessChecker());
		Mockito.doNothing().when(checker).checkKnowledgePath(Mockito.any(), Mockito.any(), Mockito.any());
		
		checker.validateComponent(null, component);
		
		Mockito.verify(checker, Mockito.times(1)).checkKnowledgePath(process1.getParameters(), knowledgeManager);
		Mockito.verify(checker, Mockito.times(1)).validateProcess(process2.getParameters(), knowledgeManager);
		Mockito.verify(checker, Mockito.times(1)).validateProcess(process3.getParameters(), knowledgeManager);
		Mockito.verifyNoMoreInteractions(checker);
	}
	
	@Test
	public void validateComponentFailureTest() throws AnnotationCheckerException {
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		ComponentInstance component = factory.createComponentInstance();
		KnowledgeManager knowledgeManager = Mockito.mock(KnowledgeManager.class);
		component.setKnowledgeManager(knowledgeManager);
		ComponentProcess process1 = factory.createComponentProcess();
		Parameter param1 = factory.createParameter();
		process1.getParameters().add(param1);
		component.getComponentProcesses().add(process1);
		ComponentProcess process1 = factory.createComponentProcess();
		Parameter param1 = factory.createParameter();
		process1.getParameters().add(param1);
		component.getComponentProcesses().add(process1);
		
		ComponentProcess process1 = factory.createComponentProcess();
		Parameter param1 = factory.createParameter();
		process1.getParameters().add(param1);
		component.getComponentProcesses().add(process1);
		ComponentProcess process2 = process1;
		ComponentProcessChecker checker = Mockito.spy(new ComponentProcessChecker());
		Mockito.doNothing().when(checker).validateProcess(Mockito.any(), Mockito.any());
		Mockito.doThrow(new AnnotationCheckerException("inner message")).when(checker)
				.validateProcess(Mockito.eq(process2.getParameters()), Mockito.any());
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("inner message");
		
		checker.validateComponent(null, component);
	}
	
	@Test
	public void validateComponentNullInstanceTest() throws AnnotationCheckerException {
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input component instance cannot be null.");
		new ComponentProcessChecker().validateComponent(null, null);
	}
*/	
	/*
	 * Tests for the validateProcess method
	 */
/*	
	@Test
	public void validateProcessNoParamsTest() throws AnnotationCheckerException {
		new ComponentProcessChecker().validateProcess(new ArrayList<Parameter>(), null);
	}
	
	// TODO some meaningful tests
*/
}

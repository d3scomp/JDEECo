package cz.cuni.mff.d3s.deeco.annotations.checking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.refEq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor.KnowledgePathAndType;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentProcess;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;

public class ComponentProcessCheckerTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	/*
	 * Tests for the validateComponent method
	 */
		
	@Test
	public void validateComponentTest() throws AnnotationCheckerException, ParameterException {
		//the validateComponent method should call checkParameter for each parameter for each process
		@Component
		class TestComponent { };
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		ComponentInstance componentInstance = factory.createComponentInstance();
		
		ComponentProcess process1 = factory.createComponentProcess();
		Parameter param11 = factory.createParameter();
		param11.setGenericType(String.class);
		process1.getParameters().add(param11);
		Parameter param12 = factory.createParameter();
		param12.setGenericType(Integer.class);
		process1.getParameters().add(param12);
		componentInstance.getComponentProcesses().add(process1);
		
		ComponentProcess process2 = factory.createComponentProcess();
		Parameter param21 = factory.createParameter();
		param21.setGenericType(Object.class);
		process2.getParameters().add(param21);
		componentInstance.getComponentProcesses().add(process2);
		Parameter param22 = factory.createParameter();
		param22.setGenericType(String.class);
		process2.getParameters().add(param22);
		
		ComponentProcessChecker checker = Mockito.spy(new ComponentProcessChecker(null));
		Mockito.doNothing().when(checker).checkParameter(Mockito.any(), Mockito.any());
		
		TestComponent testComponent = new TestComponent();
		checker.validateComponent(testComponent, componentInstance);
		
		Mockito.verify(checker, Mockito.times(1)).checkParameter(refEq(param11), eq(TestComponent.class));
		Mockito.verify(checker, Mockito.times(1)).checkParameter(refEq(param12), eq(TestComponent.class));
		Mockito.verify(checker, Mockito.times(1)).checkParameter(refEq(param21), eq(TestComponent.class));
		Mockito.verify(checker, Mockito.times(1)).checkParameter(refEq(param22), eq(TestComponent.class));
		Mockito.verify(checker, Mockito.times(1)).validateComponent(any(), any());
		Mockito.verifyNoMoreInteractions(checker);
	}
	
	@Test
	public void validateComponentFailureTest() throws AnnotationCheckerException, ParameterException {
		// validateComponent should throw and exception if any of the checkParameter calls fail
		@Component
		class TestComponent { };
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		ComponentInstance componentInstance = factory.createComponentInstance();
		
		ComponentProcess process1 = factory.createComponentProcess();
		Parameter param11 = factory.createParameter();
		param11.setGenericType(String.class);
		process1.getParameters().add(param11);
		Parameter param12 = factory.createParameter();
		param12.setGenericType(Integer.class);
		process1.getParameters().add(param12);
		componentInstance.getComponentProcesses().add(process1);
		
		ComponentProcess process2 = factory.createComponentProcess();
		process2.setName("process2");
		Parameter param21 = factory.createParameter();
		param21.setGenericType(Object.class);
		process2.getParameters().add(param21);
		componentInstance.getComponentProcesses().add(process2);
		Parameter param22 = factory.createParameter();
		param22.setGenericType(String.class);
		process2.getParameters().add(param22);
		
		ComponentProcessChecker checker = Mockito.spy(new ComponentProcessChecker(null));
		Mockito.doNothing().when(checker).checkParameter(Mockito.any(), Mockito.any());
		Mockito.doThrow(new ParameterException("inner message")).when(checker)
				.checkParameter(refEq(param22), Mockito.any());
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("Process process2: Parameter 2: inner message");
		
		checker.validateComponent(new TestComponent(), componentInstance);
	}
	
	@Test
	public void validateComponentNullComponentTest() throws AnnotationCheckerException {
		// validateComponent should throw an exception if null instance is provided
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input component cannot be null.");
		
		RuntimeMetadataFactory factory = RuntimeMetadataFactory.eINSTANCE;
		ComponentInstance componentInstance = factory.createComponentInstance();
		new ComponentProcessChecker(null).validateComponent(null, componentInstance);
	}
	
	@Test
	public void validateComponentNullInstanceTest() throws AnnotationCheckerException {
		// validateComponent should throw an exception if null instance is provided
		@Component
		class TestComponent { };
		
		exception.expect(AnnotationCheckerException.class);
		exception.expectMessage("The input component instance cannot be null.");
		new ComponentProcessChecker(null).validateComponent(new TestComponent(), null);
	}
	

	/*
	 * Tests for the checkParameter method
	 */

	private static Parameter testParameter = RuntimeMetadataFactory.eINSTANCE.createParameter(); 
			// x.[a.b] (see getKnowledgeExtractorMock)
	
	private static KnowledgePathAndType[] testKnowledgePaths;
	static {
		try {
			testKnowledgePaths = new KnowledgePathAndType[] {
				new KnowledgePathAndType(KnowledgePathHelper.createKnowledgePath("x", PathOrigin.COMPONENT).getNodes(), Integer.class),
				new KnowledgePathAndType(KnowledgePathHelper.createKnowledgePath("a.b", PathOrigin.COMPONENT).getNodes(), Long.class),
				new KnowledgePathAndType(KnowledgePathHelper.createKnowledgePath("s", PathOrigin.COMPONENT).getNodes(), null)
			};
		} catch (ParseException | AnnotationProcessorException e) {
			testKnowledgePaths = null;
		}
	};
	
	private ParameterKnowledgePathExtractor getKnowledgeExtractorMock() throws ParameterException {
		ParameterKnowledgePathExtractor mock = Mockito.mock(ParameterKnowledgePathExtractor.class);
		Mockito.when(mock.extractAllKnowledgePaths(eq(testParameter))).thenReturn(
				Arrays.asList(testKnowledgePaths[0], testKnowledgePaths[1], testKnowledgePaths[2]));
		return mock;
	}
	
	@Test
	public void checkParameterTest() throws ParameterException, KnowledgePathCheckException {
		// checkParameter should call KnowledgePathChecker.isFieldInClass for each field sequence in the parameter knowledge path
		ParameterKnowledgePathExtractor extractorMock = getKnowledgeExtractorMock();
		KnowledgePathChecker kpCheckerMock = Mockito.mock(KnowledgePathChecker.class);
		Mockito.doReturn(true).when(kpCheckerMock).isFieldInClass(any(), any(), any());			
		
		ComponentProcessChecker checker = new ComponentProcessChecker(kpCheckerMock, extractorMock);
		checker.checkParameter(testParameter, String.class);
		
		Mockito.verify(kpCheckerMock, times(1)).isFieldInClass(testKnowledgePaths[0].type, testKnowledgePaths[0].knowledgePath, String.class);
		Mockito.verify(kpCheckerMock, times(1)).isFieldInClass(testKnowledgePaths[1].type, testKnowledgePaths[1].knowledgePath, String.class);
		Mockito.verify(kpCheckerMock, times(1)).isFieldInClass(testKnowledgePaths[2].type, testKnowledgePaths[2].knowledgePath, String.class);
		Mockito.verifyNoMoreInteractions(kpCheckerMock);
	}
	
	@Test
	public void checkParameterErrorTest() throws ParameterException, KnowledgePathCheckException {
		// checkParameter should throw an exception if a check of any of the field seqences fail
		ParameterKnowledgePathExtractor extractorMock = getKnowledgeExtractorMock();
		KnowledgePathChecker kpCheckerMock = Mockito.mock(KnowledgePathChecker.class);
		Mockito.doReturn(true).when(kpCheckerMock).isFieldInClass(any(), any(), any());
		Mockito.doReturn(false).when(kpCheckerMock).isFieldInClass(eq(testKnowledgePaths[1].type), eq(testKnowledgePaths[1].knowledgePath), eq(String.class));
		
		exception.expect(ParameterException.class);
		exception.expectMessage("Knowledge path 'a.b' of type " + testKnowledgePaths[1].type 
				+ ": The knowledge path is not valid for the component: " + String.class.getSimpleName() + ". "
				+ "Check whether the field (or sequence of fields) exists in the component and that it has correct type(s) and is public, nonstatic and non@Local");
		
		ComponentProcessChecker checker = new ComponentProcessChecker(kpCheckerMock, extractorMock);
		checker.checkParameter(testParameter, String.class);
	}
}

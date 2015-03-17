package cz.cuni.mff.d3s.deeco.annotations.checking;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterException;
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor;
import cz.cuni.mff.d3s.deeco.annotations.checking.ParameterKnowledgePathExtractor.KnowledgePathAndType;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.ParseException;
import cz.cuni.mff.d3s.deeco.annotations.pathparser.PathOrigin;
import cz.cuni.mff.d3s.deeco.annotations.processor.AnnotationProcessorException;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.model.runtime.meta.RuntimeMetadataFactory;
import cz.cuni.mff.d3s.deeco.task.KnowledgePathHelper;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

public class ParameterKnowledgePathExtractorTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private void assertKnowledgePathAndType(Type expectedType, List<String> expectedPathNodes, KnowledgePathAndType actual) {
		assertEquals(expectedType, actual.type);
		assertEquals(expectedPathNodes.size(), actual.knowledgePath.size());
		for (int i = 0; i < expectedPathNodes.size(); i++) {
			String expectedNodeText = expectedPathNodes.get(i); 
			PathNode actualNode = actual.knowledgePath.get(i);
			if (expectedNodeText.equalsIgnoreCase("id")) assertTrue(actualNode instanceof PathNodeComponentId);
			else if (expectedNodeText.equalsIgnoreCase("coord")) assertTrue(actualNode instanceof PathNodeCoordinator);
			else if (expectedNodeText.equalsIgnoreCase("member")) assertTrue(actualNode instanceof PathNodeMember);
			else {
				assertTrue(actualNode instanceof PathNodeField);
				assertEquals(expectedNodeText, ((PathNodeField)actualNode).getName());
			}
		}
	}

	@Test
	public void simpleParameterTest() throws ParameterException, ParseException, AnnotationProcessorException {
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.IN);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setGenericType(Integer.class);
		
		List<KnowledgePathAndType> result = new ParameterKnowledgePathExtractor().extractAllKnowledgePaths(param1);
		assertEquals(1, result.size());
		assertKnowledgePathAndType(Integer.class, Arrays.asList("coord", "x"), result.get(0));
	}
	
	@Test
	public void multilevelParameterTest() throws ParameterException, ParseException, AnnotationProcessorException {
		Parameter param2 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param2.setKind(ParameterKind.IN);
		param2.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.a.b", PathOrigin.ENSEMBLE));
		param2.setGenericType(Long.class);
		
		List<KnowledgePathAndType> result = new ParameterKnowledgePathExtractor().extractAllKnowledgePaths(param2);
		assertEquals(1, result.size());
		assertKnowledgePathAndType(Long.class, Arrays.asList("coord", "a", "b"), result.get(0));
	}

	@Test
	public void outParameterTest() throws ParameterException, ParseException, AnnotationProcessorException {
		class _ParamHolder_Long extends ParamHolder<Long> { };
		
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.OUT);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setGenericType(_ParamHolder_Long.class.getGenericSuperclass());
		
		List<KnowledgePathAndType> result = new ParameterKnowledgePathExtractor().extractAllKnowledgePaths(param1);
		assertEquals(1, result.size());
		assertKnowledgePathAndType(Long.class, Arrays.asList("coord", "x"), result.get(0));
	}

	@Test
	public void inoutParameterTest() throws ParameterException, ParseException, AnnotationProcessorException {
		class _ParamHolder_Long extends ParamHolder<Long> { };
		
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.INOUT);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setGenericType(_ParamHolder_Long.class.getGenericSuperclass());
		
		List<KnowledgePathAndType> result = new ParameterKnowledgePathExtractor().extractAllKnowledgePaths(param1);
		assertEquals(1, result.size());
		assertKnowledgePathAndType(Long.class, Arrays.asList("coord", "x"), result.get(0));
	}

	@Test
	public void nestedKnowledgePathParameterTest() throws ParameterException, ParseException, AnnotationProcessorException {
		Parameter param3 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param3.setKind(ParameterKind.IN);
		param3.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.z.[member.y.[coord.id]].o", PathOrigin.ENSEMBLE));
		param3.setGenericType(String.class);
		
		List<KnowledgePathAndType> result = new ParameterKnowledgePathExtractor().extractAllKnowledgePaths(param3);
		// TODO use something like Collection Assert - so that we don't assume the particular order
		assertEquals(3, result.size());
		assertKnowledgePathAndType(String.class, Arrays.asList("coord", "z"), result.get(0));
		assertKnowledgePathAndType(null, Arrays.asList("member", "y"), result.get(1));
		assertKnowledgePathAndType(null, Arrays.asList("coord", "id"), result.get(2));
	}
	
	@Test
	public void genericParameterTest() throws ParameterException, ParseException, AnnotationProcessorException {
		class Struct<T> {
			public T x;
		}
		
		class _Struct_String extends Struct<String> { };
		class _ParamHolder_Struct_String extends ParamHolder<Struct<String>> { };
		
		Parameter param1 = RuntimeMetadataFactory.eINSTANCE.createParameter();
		param1.setKind(ParameterKind.OUT);
		param1.setKnowledgePath(KnowledgePathHelper.createKnowledgePath("coord.x", PathOrigin.ENSEMBLE));
		param1.setGenericType(_ParamHolder_Struct_String.class.getGenericSuperclass());
		
		List<KnowledgePathAndType> result = new ParameterKnowledgePathExtractor().extractAllKnowledgePaths(param1);
		assertEquals(1, result.size());
		assertKnowledgePathAndType(_Struct_String.class.getGenericSuperclass(), Arrays.asList("coord", "x"), result.get(0));
	}
}

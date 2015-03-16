package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeCoordinator;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMember;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

public class ParameterKnowledgePathExtractor {

	public static class KnowledgePathAndType {
		public List<PathNode> knowledgePath;
		public Type type;
		
		public KnowledgePathAndType(List<PathNode> knowledgePath, Type type) {
			super();
			this.knowledgePath = knowledgePath;
			this.type = type;
		}		
	}
	
	public List<KnowledgePathAndType> extractAllKnowledgePaths(Parameter parameter) throws ParameterException {
		Type parameterType;
		if (parameter.getKind() == ParameterKind.IN) {
			parameterType = parameter.getGenericType();
		} else {
			if (!(parameter.getGenericType() instanceof ParameterizedType)) {
				throw new ParameterException("A parameter with different kind than IN must be wrapped in a generic type (ie. "
						+ ParamHolder.class.getSimpleName() + ")");
			}
			
			ParameterizedType parameterHolderType = (ParameterizedType) parameter.getGenericType();
			parameterType = parameterHolderType.getActualTypeArguments()[0];
		}
		
		return extractKnowledgePaths(parameterType, parameter.getKnowledgePath());
	}
	
	private List<KnowledgePathAndType> extractKnowledgePaths(Type type, KnowledgePath knowledgePath) {
		List<KnowledgePathAndType> result = new ArrayList<>();
		List<PathNode> fieldSequence = new ArrayList<>();
		result.add(new KnowledgePathAndType(fieldSequence, type));
		
		// go through the path, evaluate inner knowledge paths of PathNodeMapKey-s
		for (int i = 0; i < knowledgePath.getNodes().size(); i++) {
			PathNode pn = knowledgePath.getNodes().get(i);
			if (pn instanceof PathNodeMapKey) {
				List<KnowledgePathAndType> subPaths = extractKnowledgePaths(null, ((PathNodeMapKey)pn).getKeyPath());
				result.addAll(subPaths);
				break; // we don't check after [], but we actually could
			} else {
				fieldSequence.add(pn);
			}
		}

		return result;
	}

}

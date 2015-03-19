package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.KnowledgePath;
import cz.cuni.mff.d3s.deeco.model.runtime.api.Parameter;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ParameterKind;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeMapKey;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;

/**
 * A helper class used to extract information about parameters of a component process, ensemble membership function,
 * or ensemble knowledge exchange function. The class is designed to extract all knowledge paths from a
 * {@link Parameter}.
 * 
 * @author Zbyněk Jiráček
 *
 * @see ParameterKnowledgePathExtractor#extractAllKnowledgePaths(Parameter)
 */
public class ParameterKnowledgePathExtractor {

	/**
	 * A pair [type, field sequence] constructed from a parameter declaration.
	 * 
	 * The {@link this#knowledgePath} describes the field sequence (ie. "a.b", or "id"). The field
	 * sequence must consist only of field names (no {@link PathNodeMapKey}s).
	 * 
	 * The {@link this#type} describes the type of the expression. The type is typically obtained from
	 * the type of the parameter. For nested knowledge paths used in map keys, the type may not be known.
	 * In this case the type can be null.
	 * 
	 * @author Zbyněk Jiráček
	 *
	 */
	public static class KnowledgePathAndType {
		public List<PathNode> knowledgePath;
		public Type type;
		
		public KnowledgePathAndType(List<PathNode> knowledgePath, Type type) {
			super();
			this.knowledgePath = knowledgePath;
			this.type = type;
		}		
	}
	
	/**
	 * Parses a parameter and returns a list of field sequences with types. One parameter typically contain
	 * one field sequence (the whole knowledge path), but when the {@link PathNodeMapKey}s are used (ie. in
	 * "coord.a.[member.id]"), one parameter in fact references multiple knowledge paths (here "coord.a",
	 * and "member.id"). These extracted knowledge paths no longer contain map keys. The type of the first
	 * knowledge path ("coord.a") is equal to the type of the parameter (or to the generic argument in
	 * case of In or InOut parameters. The type of the inner path "member.id" is equal to the key 
	 * type of the HashMap "coord.a", however due to java limitations this is hard to
	 * be acquired, so the type of inner paths is often null.
	 * 
	 * This function parses a parameter knowledge path and returns an instance of {@link KnowledgePathAndType}
	 * for each field sequence.
	 * 
	 * @param parameter A processed parameter of a component process/membership/knowledge exchange function.
	 * @return List of field sequences with their types.
	 * @throws ParameterException
	 */
	public List<KnowledgePathAndType> extractAllKnowledgePaths(Parameter parameter) throws ParameterException {
		Type parameterType;
		if (parameter.getKind() == ParameterKind.IN) {
			parameterType = parameter.getGenericType();
		} else if (parameter.getKind() == ParameterKind.RATING) {
			// TODO what?
			return new ArrayList<KnowledgePathAndType>();
		} else {
			if (!(parameter.getGenericType() instanceof ParameterizedType)) {
				throw new ParameterException("A parameter with kind than IN or RATING must be wrapped in a generic type (ie. "
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

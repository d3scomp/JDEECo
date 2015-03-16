package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;

/**
 * 
 * @author Zbyněk Jiráček
 *
 */
public class KnowledgePathCheckerImpl implements KnowledgePathChecker {

	class PathNodeCheckingException extends Exception {
		private static final long serialVersionUID = 6724099053420733270L;

		public PathNodeCheckingException(String message) {
			super(message);
			// TODO Auto-generated constructor stub
		}
	}
	
	private TypeComparer typeComparer;
	
	public KnowledgePathCheckerImpl(TypeComparer typeComparer) {
		this.typeComparer = typeComparer;
	}
	
	public static String pathNodeSequenceToString(Type type, List<PathNode> pathNodes) {
		String result = "'" + String.join(".", 
				pathNodes.stream().map(p -> p.toString()).collect(Collectors.toList())) + "'";
		
		if (type != null) {
			result += " of type " + type;
		}
		
		return result;
	}

	@Override
	public boolean isFieldInClass(Type type, List<PathNode> fieldSequence, Class<?> clazz) throws KnowledgePathCheckException {
		
		if (fieldSequence == null || fieldSequence.size() < 1) {
			throw new KnowledgePathCheckException("The field sequence cannot be null or empty.");
		}
		if (clazz == null) {
			throw new KnowledgePathCheckException("The role class cannot be null.");
		}
		
		try {
			Type fieldType = getTypeInClass(fieldSequence, clazz);
			if (fieldType == null) {
				return false; // field not present in the role class
			}
			
			return type == null || typeComparer.compareTypes(type, fieldType);
		} catch (PathNodeCheckingException e) {
			throw new KnowledgePathCheckException("Knowledge path " + pathNodeSequenceToString(type, fieldSequence)
					+ ": " + e.getMessage(), e);
		}
	}
	
	/**
	 * Returns the type of a field sequence from a given role class. If the given field sequence
	 * has only one element "id", String.class is returned (implicit field present in all roles).
	 * When the field sequence consist of multiple elements, the type of the nested field is returned
	 * (ie. if the given class contains a field x of type C, class C contains a field y and the 
	 * input field name sequence is ("x", "y"), then the result is the type of C.y).
	 * 
	 * If the given field has generic type, {@link ParameterizedType} is returned, containing
	 * the type arguments.
	 * 
	 * Sometimes generic types cannot be inferred. In that case it is possible, that an unknown
	 * type is returned. In this case the result is an instance of {@link TypeVariable}.
	 * 
	 * @param fieldNameSequence Sequence of fields (knowledge path split by dots)
	 * @param roleClass The role class
	 * @return Type of the expression.
	 */
	Type getTypeInClass(List<PathNode> fieldSequence, Class<?> roleClass) throws PathNodeCheckingException {
		if (fieldSequence.size() == 1 && fieldSequence.get(0) instanceof PathNodeComponentId) {
			// id is always present and always of type String
			return String.class;
		}
		
		return getTypeInClassNoImplicitId(fieldSequence, roleClass);
	}
	
	/**
	 * Returns the type of a field sequence from a given class. When the field sequence consists
	 * of multiple elements, the type of the nested field is returned (ie. if the given class
	 * contains a field x of type C, class C contains a field y and the input field name
	 * sequence is ("x", "y"), then the result is the type of C.y).
	 * 
	 * If the given field has generic type, {@link ParameterizedType} is returned, containing
	 * the type arguments.
	 * 
	 * Sometimes generic types cannot be inferred. In that case it is possible, that an unknown
	 * type is returned. In this case the result is an instance of {@link TypeVariable}.
	 * 
	 * @param fieldNameSequence Sequence of fields (knowledge path split by dots)
	 * @param clazz The class
	 * @return Type of the expression.
	 */
	private Type getTypeInClassNoImplicitId(List<PathNode> fieldNameSequence, Class<?> clazz) throws PathNodeCheckingException {
		
		String firstField = checkAndReadPathNodeField(fieldNameSequence.get(0));
		
		Field field;
		try {
			field = clazz.getField(firstField);
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
		
		if (!RoleAnnotationsHelper.isPublicAndNonstatic(field)) {
			return null;
		}
		
		if (fieldNameSequence.size() == 1) {
			return field.getGenericType();
		} else {
			return getTypeInClassNoImplicitId(fieldNameSequence.subList(1, fieldNameSequence.size()), field.getType());
		}
	}

	private String checkAndReadPathNodeField(PathNode pathNode) throws PathNodeCheckingException {
		if (!(pathNode instanceof PathNodeField)) {
			throw new PathNodeCheckingException("Invalid path node " + pathNode.toString() + ". Only "
					+ PathNodeField.class.getSimpleName() + " instances are expected.");
		}
		
		return ((PathNodeField)pathNode).getName();
	}

}

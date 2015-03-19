package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.stream.Collectors;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;

/**
 * Implementation of {@link KnowledgePathChecker} that uses Java class introspection in order to search
 * for the given field sequence.
 * 
 * The class uses a {@link TypeComparer} when comparing the desired type to the type of the respective field.
 * 
 * Note: instead of introspection, KnowledgeManager could be used to find the field. But, knowledge managers
 * do not store types.
 * 
 * @author Zbyněk Jiráček
 *
 * @see KnowledgePathChecker
 */
public class KnowledgePathCheckerImpl implements KnowledgePathChecker {

	class PathNodeCheckingException extends Exception {
		private static final long serialVersionUID = 6724099053420733270L;

		public PathNodeCheckingException(String message) {
			super(message);
		}
	}
	
	private TypeComparer typeComparer;

	public KnowledgePathCheckerImpl(TypeComparer typeComparer) {
		this.typeComparer = typeComparer;
	}
	
	/**
	 * Converts the given field sequence into a string representation. The resulting representation consists
	 * of the field names joined by dot(s), closed within '' quotation marks. If a type is given, the result
	 * also contains the text "of type <type-string>" at the end, where <type-string> is acquired by calling
	 * toString() on the given Type.
	 * 
	 * @param type The type of the expression (can be null).
	 * @param pathNodes List of PathNodes representing the field sequence.
	 * @return String representation of a field sequence.
	 */
	public static String pathNodeSequenceToString(Type type, List<PathNode> pathNodes) {
		String result = "'" + String.join(".", 
				pathNodes.stream().map(p -> p.toString()).collect(Collectors.toList())) + "'";
		
		if (type != null) {
			result += " of type " + type;
		}
		
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.checking.KnowledgePathChecker#isFieldInClass(java.lang.reflect.Type, java.util.List, java.lang.Class)
	 */
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
	 * Returns the type of a field sequence in a given class. If the given field sequence
	 * has only one element "id", String.class is returned (implicit field present in all roles/components).
	 * When the field sequence consists of multiple elements, the type of the nested field is returned
	 * (ie. if the given class contains a field x of type C, class C contains a field y and the 
	 * input field name sequence is ("x.y"), then the result is the type of C.y).
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
	 * sequence is ("x.y"), then the result is the type of C.y).
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

	/**
	 * Check that a given {@link PathNode} is a {@link PathNodeField} and returns the field name.
	 * @param pathNode A field name wrapped in a {@link PathNode}.
	 * @return The field name.
	 * @throws PathNodeCheckingException
	 */
	private String checkAndReadPathNodeField(PathNode pathNode) throws PathNodeCheckingException {
		if (!(pathNode instanceof PathNodeField)) {
			throw new PathNodeCheckingException("Invalid path node " + pathNode.toString() + ". Only "
					+ PathNodeField.class.getSimpleName() + " instances are expected.");
		}
		
		return ((PathNodeField)pathNode).getName();
	}

}

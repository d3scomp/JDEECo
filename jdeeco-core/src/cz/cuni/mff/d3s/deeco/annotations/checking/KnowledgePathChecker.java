package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Type;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeComponentId;
import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNodeField;

/**
 * Definition of an interface for checking knowledge paths used in parameters of ensemble membership/knowledge exchange,
 * as well as parameters of component processes. Used by the {@link RolesAnnotationChecker} 
 * (ensemble membership/knowledge exchange validation) and by the {@link ComponentProcessChecker} (component processes).
 * 
 * @author Zbyněk Jiráček
 *
 * @see RolesAnnotationChecker
 * @see ComponentProcessChecker
 */
public interface KnowledgePathChecker {

	/**
	 * Checks whether a field sequence is present in a given class. Also checks the type
	 * of the field, if wanted. The given path can contain only field names (instances of {@link PathNodeField},
	 * or {@link PathNodeComponentId}).
	 * 
	 * If the given field sequence contains only one element, then
	 * the given component/role class is checked, whether it has this field and that the field's type
	 * is equal to the given type.
	 * 
	 * If the given field sequence contains more than one field, than it is tested by introspection, that the
	 * field's type is recursively tested (by introspection), that it has a field listed as second in the given
	 * field sequence. This recursively continues to the last field in the given sequence. The given type
	 * is always matched to the type of the last field in the sequence.
	 * 
	 * @param type The desired type (or null if the type should not be checked).
	 * @param fieldNameSequence The knowledge path separated to individual path nodes (representing field names).
	 * @param clazz The class that should contain the sequence of the given type.
	 * @return True if the field sequence (of given type) was found, false otherwise.
	 */	
	boolean isFieldInClass(Type type, List<PathNode> fieldSequence, Class<?> clazz) throws KnowledgePathCheckException;	
}

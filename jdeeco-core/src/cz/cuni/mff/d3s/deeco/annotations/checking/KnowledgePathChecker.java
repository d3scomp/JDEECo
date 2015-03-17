package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Type;
import java.util.List;

import cz.cuni.mff.d3s.deeco.model.runtime.api.PathNode;

/**
 * 
 * The class is used for checking of ensemble membership and knowledge exchange functions,
 * as well as for checking of component processes.
 * 
 * @author Zbyněk Jiráček
 *
 */
public interface KnowledgePathChecker {

	/**
	 * Checks whether a field sequence is present in a given class. Also checks the type
	 * of the field, if wanted. The given path can contain only field names (not [..]). It not
	 * only checks whether the given role class contains the first-level field, it also checks
	 * whether the rest of the path is valid.
	 * @param type The desired type (or null if the type should not be checked)
	 * @param fieldNameSequence The knowledge path separated to individual path nodes
	 * @param clazz The class that should contain the path
	 * @return True if the field (of given type) is present in the role, false otherwise.
	 */	
	boolean isFieldInClass(Type type, List<PathNode> fieldSequence, Class<?> clazz) throws KnowledgePathCheckException;	
}

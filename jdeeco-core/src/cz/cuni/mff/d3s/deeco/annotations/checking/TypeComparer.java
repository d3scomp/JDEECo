package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Type;

/**
 * General interface for comparing types, when searching for role field implementations,
 * or when matching parameters of component/ensemble processes to fields
 * of the component class. 
 * 
 * @author Zbyněk Jiráček
 *
 * @see RolesAnnotationChecker
 * @see KnowledgePathChecker
 */
public interface TypeComparer {
	
	/**
	 * Checks whether a given types are (or can be) equal. The method returns true,
	 * if the types are equal, or in all cases when the types cannot
	 * be proven to be non-equal (this involves mostly unresolved generic arguments).
	 * 
	 * Detailed behavior depends on the particular implementation
	 * 
	 * @param implementationType The type of the field in the class/method, or of the process parameter.
	 * @param roleType The type of the field as declared in the role/component class that defines it.
	 * @return True if types are equal/may be equal, false otherwise
	 */
	boolean compareTypes(Type implementationType, Type roleType);
	
}

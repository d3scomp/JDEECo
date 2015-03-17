package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class GenericTypeComparer implements TypeComparer {

	/**
	 * Checks whether a given types are (or can be) equal. This method is used to check whether
	 * a field of a given type can implement (or reference) a field of a given type from a role
	 * class. The method returns true, if the types are equal, or in all cases when the types
	 * cannot be proven to be different (this involves mostly unresolved generic arguments).
	 * Note that due to technical limitations, type hierarchy is not taken into account
	 * (ie. for classes A extends B, A and B are not considered equal in any case).
	 * @param implementationType The type of the field in the class/method
	 * @param roleType The type of the field as declared in the role
	 * @return True if types are equal/may be equal, false otherwise
	 */
	@Override
	public boolean compareTypes(Type implementationType, Type roleType) {
		if (implementationType.equals(roleType)) {
			return true;
		}
		
		// nonequal types can be equal, if one of them is a generic type (or generically parameterized)
		if (implementationType instanceof GenericArrayType && roleType instanceof GenericArrayType) {
			GenericArrayType gType1 = (GenericArrayType) implementationType;
			GenericArrayType gType2 = (GenericArrayType) roleType;
			return compareTypes(gType1.getGenericComponentType(), gType2.getGenericComponentType());
		} else if (implementationType instanceof GenericArrayType && roleType instanceof Class) {
			return ((Class<?>) roleType).isArray();
		} else if (implementationType instanceof Class && roleType instanceof GenericArrayType) {
			return ((Class<?>) implementationType).isArray();
			
		} else if (implementationType instanceof ParameterizedType && roleType instanceof ParameterizedType) {
			ParameterizedType pType1 = (ParameterizedType) implementationType;
			ParameterizedType pType2 = (ParameterizedType) roleType;
			if (!compareTypes(pType1.getRawType(), pType2.getRawType())) {
				return false;
			}
			
			assert pType1.getActualTypeArguments().length == pType2.getActualTypeArguments().length;
			for (int i = 0; i < pType1.getActualTypeArguments().length; i++) {
				if (!compareTypes(pType1.getActualTypeArguments()[i], pType2.getActualTypeArguments()[i])) {
					return false;
				}
			}
			
			return true;
		
		} else if (implementationType instanceof TypeVariable || roleType instanceof TypeVariable) {
			return true;
		}
		
		return false;
	}

}

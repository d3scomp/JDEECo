package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * An implementation of the {@link TypeComparer} interface that can work with generic types 
 * (eg. List<<String>> does not equal to List<<Integer>>).
 * 
 * Due to technical limitations, type hierarchy is not taken into account
 * (ie. for classes A extends B, A and B are not considered equal in any case).
 * 
 * When working with generics, it is possible to provide types that are incomplete (containing
 * or directly being an unresolved generic argument type). In the case such type is found,
 * it is considered to be potentially equal to any type. This means, for instance, that
 * T[] equals both to int[] and SomeClass[], even to U[], and so on.
 * 
 * @author Zbyněk Jiráček
 *
 * @see TypeComparer
 */
public class GenericTypeComparer implements TypeComparer {

	/*
	 * (non-Javadoc)
	 * @see cz.cuni.mff.d3s.deeco.annotations.checking.TypeComparer#compareTypes(java.lang.reflect.Type, java.lang.reflect.Type)
	 */
	@Override
	public boolean compareTypes(Type implementationType, Type roleType) {
		if (implementationType == null || roleType == null) {
			throw new IllegalArgumentException("The both types supplied to the GenericTypeComparer.compareTypes method must not be null.");
		}
		
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

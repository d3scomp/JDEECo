package cz.cuni.mff.d3s.deeco.annotations.checking;

import java.lang.reflect.Type;

public interface TypeComparer {

	boolean compareTypes(Type implementationType, Type roleType);
	
}

package cz.cuni.mff.d3s.deeco.annotations.processor;

import java.lang.reflect.Type;

public interface TypeComparer {

	boolean compareTypes(Type implementationType, Type roleType);
	
}

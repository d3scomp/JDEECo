package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.Model;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Fun {
	int[] returnedIndex();
//	String[] params() default {};
	Class<?> referenceModel() default Model.class;
}


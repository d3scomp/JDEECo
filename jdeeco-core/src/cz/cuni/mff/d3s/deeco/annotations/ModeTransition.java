package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.MetadataType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModeTransition {
	MetadataType meta() default MetadataType.EMPTY;
	String fromMode();
	String toMode();
	String transitionCondition() default ""; // could be Condition instead of String
}


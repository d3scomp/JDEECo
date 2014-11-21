package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.model.runtime.api.ExecutionType;

/**
 * 
 * @author Rima Al Ali
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Set {
	String name();
	String init() default "";
	String guards() default "";
	String[] children() default {};
	boolean history() default false;
	ExecutionType decomposition() default ExecutionType.EXECLUSIVE;
}

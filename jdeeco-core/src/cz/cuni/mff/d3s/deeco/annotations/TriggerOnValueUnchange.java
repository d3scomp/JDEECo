/**
 * 
 */
package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.model.runtime.api.MetadataType;

/**
 * 
 * @author Rima Al Ali
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TriggerOnValueUnchange {
	String equalStr() default "";
	String notEqualStr() default "";
	long equal() default -1;
	long notEqual() default -1;
	long lessThan() default -1;
	long moreThan() default -1;
	long equalLessThan() default -1;
	long equalMoreThan() default -1;
	MetadataType meta() default MetadataType.EMPTY;
}

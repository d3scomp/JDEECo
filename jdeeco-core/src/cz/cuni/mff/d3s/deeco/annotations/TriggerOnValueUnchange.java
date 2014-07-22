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
 * Used to mark an ensemble / component process to be executed when the object the parameter's path points to changes.  
 * <p>
 * Attached to a method parameter that is annotated also with @{@link In} / @{@link InOut}. 
 * </p>
 * 
 * @author Michal Kit
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TriggerOnValueUnchange {
	long equal() default 0;
	long lessThan() default 0;
	long moreThan() default 0;
	long equalLessThan() default 0;
	long equalMoreThan() default 0;
	MetadataType meta() default MetadataType.EMPTY;
}

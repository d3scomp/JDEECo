/**
 * 
 */
package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



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
@Target(ElementType.METHOD)
public @interface PeriodicSchedulingOnActivateMode {
	int value() default 0;
	Code[] entry() default {};
	Code[] exit() default {};
}

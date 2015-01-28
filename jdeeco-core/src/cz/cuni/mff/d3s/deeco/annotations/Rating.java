package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cz.cuni.mff.d3s.deeco.integrity.RatingsHolder;
import cz.cuni.mff.d3s.deeco.integrity.ReadonlyRatingsHolder;

/**
 * Used to decorate a rating process argument as a source of rating information.
 * The only allowed types for such parameter are {@link RatingsHolder} and {@link ReadonlyRatingsHolder}.
 * 
 * @author Ondřej Štumpf
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Rating {
	String value();
}

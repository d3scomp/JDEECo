package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods (process or knowledge exchange) marked with this annotation are ignored during knowledge compromise checks.
 * 
 * @author Ondřej Štumpf
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IgnoreKnowledgeCompromise {
	
}

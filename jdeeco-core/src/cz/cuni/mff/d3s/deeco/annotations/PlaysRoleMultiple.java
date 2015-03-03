package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container of {@link PlaysRole} annotations, required by Java to enable repeatable annotations.
 * 
 * @author Zbyněk Jiráček
 * 
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlaysRoleMultiple {
	PlaysRole[] value();
}

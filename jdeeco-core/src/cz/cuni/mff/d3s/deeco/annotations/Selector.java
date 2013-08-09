package cz.cuni.mff.d3s.deeco.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a membership method parameter to be a processed selector parameter, which is used when
 * invoking an ensemble process. Such parameter is a list of booleans whom
 * the user can only change the value and not affect its size. 
 * 
 * In the list of booleans, 
 * <ul>
 * <li>an item set to false means that the node is not part of the formed ensemble.</li>
 * <li>an item set to true (by default) means selecting a node as a member of the formed ensemble.</li>
 * </ul>
 * 
 * The attribute <code>value</code> is an unique selector identifier which 
 * identifies a group of method parameters. 
 * This group describes a set of input nodes to be then selected by the user from the Selector parameter.
 * This latter will be implicitly declared in the knowledge exchange.
 * 
 * The user can then reuse the same identifier in the knowledge paths of the knowledge exchange method parameters
 * as it was done in the membership, but without the Selector parameter.
 * 
 * e.g. among the membership method parameters : 
 *  \@Selector("value") List\<Boolean\> valueSelectors;
 * membership method parameter as part of the identified set of nodes :
 *  \@In("members.value.relativeKnowledgePath") ... parameter; 
 *  
 * @author Julien Malvot
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Selector {
	String value() default "";	
}

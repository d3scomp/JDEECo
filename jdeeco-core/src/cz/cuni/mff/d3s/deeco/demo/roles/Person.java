package cz.cuni.mff.d3s.deeco.demo.roles;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.annotations.Role;

/**
 * A role class for the Person component. In the future, this could be simplified: since this role has
 * only one implementor, the Person component class could serve as the role class and the component
 * class simultaneously.
 * 
 * @author Zbyněk Jiráček
 *
 */
@Role
class PersonRole {
	public String firstName;
	public String lastName;
	public Integer requiredCapacity;
}

/**
 * See {@link BoardingEnsemble} for more details.
 * 
 * @author Zbyněk Jiráček
 *
 */
@Component
@PlaysRole(PersonRole.class)
public class Person {
	
	public String id;
	public String firstName;
	public String lastName;
	public Integer requiredCapacity;
	
	public Person(String firstName, String lastName, int requiredCapacity) {
		this.id = firstName + lastName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.requiredCapacity = requiredCapacity;
	}

}

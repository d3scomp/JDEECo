package cz.cuni.mff.d3s.deeco.demo.roles;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;

/**
 * One implementation of the vehicle role.
 * 
 * See {@link BoardingEnsemble} for more details.
 * 
 * @author Zbyněk Jiráček
 *
 */

@Component
@PlaysRole(VehicleRole.class)
public class Car {

	public String id;
	
	public Integer capacity;
	
	public String type = "Car";
	
	public Car(String id, int capacity) {
		this.id = id;
		this.capacity = capacity;
	}
	
	@Override
	public String toString() {
		return String.format("%s car (capacity: %d)", id, capacity);
	}

}

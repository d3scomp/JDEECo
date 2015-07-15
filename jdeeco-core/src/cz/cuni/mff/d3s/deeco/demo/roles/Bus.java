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
public class Bus {

	public String id;
	
	public Integer capacity;
	
	public String type = "Bus";
	
	public Bus(String id, int capacity) {
		this.id = id;
		this.capacity = capacity;
	}

}

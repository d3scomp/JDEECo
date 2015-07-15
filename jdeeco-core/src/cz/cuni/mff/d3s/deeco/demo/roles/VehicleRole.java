package cz.cuni.mff.d3s.deeco.demo.roles;

import cz.cuni.mff.d3s.deeco.annotations.Role;


/**
 * The role definition for vehicle components. {@link Car} and {@link Bus} components implement this role.
 * On the other hand, the {@link Building} does not.
 * 
 * See {@link BoardingEnsemble} for more details.
 * 
 * @author Zbyněk Jiráček
 *
 */

@Role
public class VehicleRole {

	public Integer capacity;
	
	public String type;

}

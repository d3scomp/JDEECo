package cz.cuni.mff.d3s.deeco.demo.roles;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.CoordinatorRole;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange;
import cz.cuni.mff.d3s.deeco.annotations.MemberRole;
import cz.cuni.mff.d3s.deeco.annotations.Membership;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;

/**
 * This ensemble connects persons ({@link Person} component) with vehicles ({@link Car}, {@link Bus} components).
 * 
 * Using the role annotations we allow only Person-Vehicle ensembles. Vehicle-Vehicle ensembles 
 * as well as Person-Building ensembles are not allowed, even though Building has the required knowledge 
 * and the membership condition itself would be true.
 * 
 * @author Zbyněk Jiráček
 *
 */

@Ensemble
@PeriodicScheduling(period = 1000)
@CoordinatorRole(PersonRole.class) // coordinator in this ensemble must be a component that implements PersonRole
@MemberRole(VehicleRole.class) // member in this ensemble must implement VehicleRole
public class BoardingEnsemble {

	public static PrintStream outputStream = System.out;
	
	@Membership
	public static boolean membership(
			// Thanks to the roles we can check the knowledge paths when parsing the ensemble.
			// If you try to change the knowledge path or the parameter type, you should get an exception
			@In("coord.requiredCapacity") Integer requiredCapacity,
			@In("member.capacity") Integer offeredCapacity
			) {
		
		// every person is in ensemble with all vehicles that offer sufficient capacity
		return offeredCapacity >= requiredCapacity;
	}
	
	@KnowledgeExchange
	public static void knowledgeExchange(
			// Similarly as in the membership function, the knowledge paths are verified when parsing the ensemble.
			@In("coord.firstName") String firstName,
			@In("coord.lastName") String lastName,
			@In("member.id") String vehicleId,
			@In("member.type") String vehicleType
			) {
		
		outputStream.printf("%s %s can go by %s %s\n", firstName, lastName, vehicleType, vehicleId);
	}

}

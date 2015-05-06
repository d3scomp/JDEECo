package cz.cuni.mff.d3s.deeco.demo.roles;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.PlaysRole;
import cz.cuni.mff.d3s.deeco.demo.convoy.ConvoyEnsemble;

/**
 * The building class also has capacity and type, therefore it could be a vehicle if we were using duck typing.
 * It could also implement VehicleRole, but it does not, since it is semantically incorrect. Therefore, there
 * will be no Person-Building ensembles (see {@link ConvoyEnsemble}). However, if you try to add the {@link PlaysRole}
 * annotation to the Building class, even Buildings will serve as vehicles and the ensembles with Person could be created.
 * 
 * See {@link BoardingEnsemble} for more details.
 * 
 * @author Zbyněk Jiráček
 *
 */

@Component
public class Building {

	public String id;
	
	public Integer capacity;
	
	public String type = "Building";
	
	public Building(String id, int capacity) {
		this.id = id;
		this.capacity = capacity;
	}

}

package demo_annotation.Modes;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValue;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.Model;



public class VehicleModel implements Model {
	
	public InaccurateValue getModelBoundaries(InaccurateValue[] x){
		InaccurateValue val = new InaccurateValue();
		val.minBoundary = Database.getAcceleration(x[0].minBoundary.doubleValue(), 
				x[1].minBoundary.doubleValue(), Database.lTorques, 0.0, 1.0, Database.lMass);
		val.maxBoundary = Database.getAcceleration(x[0].maxBoundary.doubleValue(),
				x[1].maxBoundary.doubleValue(), Database.lTorques, 1.0, 0.0, Database.lMass);
		return val;
	}

}

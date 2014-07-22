package demo_annotation.Modes;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccurateValueDefinition;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface;



public class VehicleModel implements ModelInterface {

	@Override
	public InaccurateValueDefinition getModelBoundaries(Collection<InaccurateValueDefinition> inaccValues) {
		InaccurateValueDefinition val = new InaccurateValueDefinition();
		ArrayList<InaccurateValueDefinition> arr = new ArrayList<InaccurateValueDefinition>();
		for (InaccurateValueDefinition inaccValue : inaccValues) {
			arr.add(inaccValue);
		}
		val.minBoundary = Database.getAcceleration(arr.get(0).minBoundary.doubleValue(), 
				arr.get(1).minBoundary.doubleValue(), Database.lTorques, 0.0, 1.0, Database.lMass);
		val.maxBoundary = Database.getAcceleration(arr.get(0).maxBoundary.doubleValue(),
				arr.get(1).maxBoundary.doubleValue(), Database.lTorques, 1.0, 0.0, Database.lMass);
		
		System.out.println("min = "+val.minBoundary);
		return val;
	}
}

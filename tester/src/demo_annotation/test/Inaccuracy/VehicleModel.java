package demo_annotation.test.Inaccuracy;



import java.util.ArrayList;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface;



public class VehicleModel implements ModelInterface {

	@Override
	public InaccuracyParamHolder getModelBoundaries(Collection eList) {
		InaccuracyParamHolder val = new InaccuracyParamHolder<>();
		ArrayList<InaccuracyParamHolder> ranges = new ArrayList<InaccuracyParamHolder>();
		ranges.addAll(eList);
		val.minBoundary = Database.getAcceleration((Double)ranges.get(0).minBoundary, 
				(Double)ranges.get(1).minBoundary, Database.lTorques, 0.0, 1.0, Database.lMass);
		val.maxBoundary = Database.getAcceleration((Double)ranges.get(0).maxBoundary,
				(Double)ranges.get(1).maxBoundary, Database.lTorques, 1.0, 0.0, Database.lMass);
		return val;
	}
}

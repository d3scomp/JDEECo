package demo_annotation.test.Modes.complete;



import java.util.ArrayList;
import java.util.Collection;

import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.InaccuracyParamHolder;
import cz.cuni.mff.d3s.deeco.model.runtime.stateflow.ModelInterface;



public class VehicleModel implements ModelInterface {

	@Override
	public InaccuracyParamHolder getModelBoundaries(Collection eList) {
		InaccuracyParamHolder val = new InaccuracyParamHolder<>();
		ArrayList<InaccuracyParamHolder<Double>> ranges = new ArrayList<InaccuracyParamHolder<Double>>();
		ranges.addAll(eList);
//		System.out.println("inside 0 min = "+ranges.get(0).minBoundary.doubleValue()+"   max = "+ranges.get(0).maxBoundary.doubleValue());
//		System.out.println("inside 1 min = "+ranges.get(1).minBoundary.doubleValue()+"   max = "+ranges.get(1).maxBoundary.doubleValue());
		val.minBoundary = Database.getAcceleration(ranges.get(0).minBoundary.doubleValue(), 
				ranges.get(1).minBoundary.doubleValue(), Database.lTorques, 0.0, 1.0, Database.lMass);
		val.maxBoundary = Database.getAcceleration(ranges.get(0).maxBoundary.doubleValue(),
				ranges.get(1).maxBoundary.doubleValue(), Database.lTorques, 1.0, 0.0, Database.lMass);
		
//		System.out.println("inside : min = "+val.minBoundary+"   max = "+val.maxBoundary);
		return val;
	}
}

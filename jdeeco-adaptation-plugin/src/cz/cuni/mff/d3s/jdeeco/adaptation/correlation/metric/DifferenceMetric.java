package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metric;


/**
 * Computes modulus of the difference of the two given values.
 * The given values has to be Numbers.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class DifferenceMetric implements Metric {

	/**
	 * Computes the distance between the given values as a simple difference.
	 * @param value1 The value to measure the distance from.
	 * @param value2 The value to measure the distance to.
	 * @return the distance between the given values measured by a simple difference.
	 */
	@Override
	public double distance(Object value1, Object value2){
		if(!(value1 instanceof Number) || !(value2 instanceof Number))
			throw new IllegalArgumentException("Can't compute a distance of anything else than Numbers.");
		
		double d1 = ((Number)value1).doubleValue();
		double d2 = ((Number)value2).doubleValue();
		
		return Math.abs(d1 - d2);
	}
}

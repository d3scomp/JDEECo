package cz.cuni.mff.d3s.jdeeco.adaptation.correlation.metric;


/**
 * The interface for matrics that computes the distance between the two given values.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public interface Metric {

	/**
	 * Computes the distance between the given values.
	 * @param value1 The value to measure the distance from.
	 * @param value2 The value to measure the distance to.
	 * @return the distance between the given values.
	 */
	double distance(Object value1, Object value2);

}

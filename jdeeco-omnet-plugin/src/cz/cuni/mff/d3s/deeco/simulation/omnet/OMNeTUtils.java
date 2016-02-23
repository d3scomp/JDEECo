package cz.cuni.mff.d3s.deeco.simulation.omnet;

public class OMNeTUtils {
	public static double RangeToPower_802_15_4(double range_m) {
		return 0.00000104504 * Math.pow(range_m, 2.49598);
	}
}

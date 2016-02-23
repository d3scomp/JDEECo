package cz.cuni.mff.d3s.deeco.simulation.omnet;

public class OMNeTUtils {
	public static double RangeToPower_802_15_4(double range_m) {
		// "measured" data from OMNeT++ simulation
		// {{range, power}}
		// {{1, 0.000001}, {2, 0.00001}, {6, 0.0001}, {15, 0.001}, {39, 0.01}, {99, 0.1}, {249, 1.0}}
		
		// Formula fitted by wolfram alpha power fit
		return 0.00000104504 * Math.pow(range_m, 2.49598);
	}
}

package cz.cuni.mff.d3s.jdeeco.network.l2.rebroadcast.bounded;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;

/**
 * Component that holds data value provided by ensemble
 */
@Component
public class DataSink {
	public String id;
	public Integer outValue;

	// Output for writing results
	@Local
	private static PrintStream out;

	public DataSink(String id, PrintStream out) {
		this.id = id;
		this.outValue = 0;
		DataSink.out = out;
	}

	/**
	 * Prints current value
	 */
	@Process
	@PeriodicScheduling(period = 1000)
	public static void dataReport(@In("id") String id, @In("outValue") Integer value) {
		out.println(id + ": " + value);
	}
}

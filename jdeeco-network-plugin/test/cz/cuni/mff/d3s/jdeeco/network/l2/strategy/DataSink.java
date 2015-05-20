package cz.cuni.mff.d3s.jdeeco.network.l2.strategy;

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
	public PrintStream out;

	public DataSink(String id, PrintStream out) {
		this.id = id;
		this.outValue = 0;
		this.out = out;
	}

	/**
	 * Prints current value
	 */
	@Process
	@PeriodicScheduling(period = 1000)
	public static void dataReport(
			@In("id") String id,
			@In("outValue") Integer value,
			@In("out") PrintStream out) {
		out.println(id + ": " + value);
	}
}

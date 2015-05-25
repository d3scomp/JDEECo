package demo.broadcast;


import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

@Component
public class Grather {
	public String id = "Follower";
	public String name;
	public Integer value;
	
	@Local
	public PrintStream out;
	
	@Local
	public CurrentTimeProvider clock;
	
	public Grather(String name, PrintStream output, CurrentTimeProvider clock) {
		this.name = name;
		out = output;
		this.clock = clock;
	}
			
	@Process
	@PeriodicScheduling(period=2500)
	public static void followProcess(
		@In("name") String name, 
		@In("value") Integer value,
		@In("out") PrintStream out,
		@In("clock") CurrentTimeProvider clock) {
			out.format("%06d: Grather %s: %d%n", clock.getCurrentMilliseconds(), name, value);
	}
}

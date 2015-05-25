package demo.broadcast;



import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;

@Component
public class Seeder {
	public String id;
	public String name;
	public Integer source;
	
	@Local
	public PrintStream out;
	
	@Local
	public CurrentTimeProvider clock;
	
	public Seeder(String name, PrintStream output, CurrentTimeProvider clock) {
		this.clock = clock;
		
		source = 0;
				
		this.name = name;
		id = "Leader1";
		out = output;
	}
	
	@Process
	@PeriodicScheduling(period=2500)
	public static void moveProcess(
			@InOut("source") ParamHolder<Integer> source,
			@In("name") String name,
			@In("out") PrintStream out,
			@In("clock") CurrentTimeProvider clock) {
		out.format("%06d: Seeder %s: %d%n", clock.getCurrentMilliseconds(), name, source.value);
		source.value++;
	}
}

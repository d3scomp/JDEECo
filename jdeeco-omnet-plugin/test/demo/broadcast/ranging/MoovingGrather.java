package demo.broadcast.ranging;

import java.io.PrintStream;

import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.Local;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.timer.CurrentTimeProvider;
import cz.cuni.mff.d3s.jdeeco.position.Position;

@Component
public class MoovingGrather {
	public String id = "Follower";
	public String name;
	public Integer value;

	@Local
	public PrintStream out;

	@Local
	public CurrentTimeProvider clock;

	@Local
	public CustomPosition position;

	public MoovingGrather(String name, PrintStream output, CurrentTimeProvider clock, CustomPosition position) {
		this.name = name;
		out = output;
		this.clock = clock;
		this.position = position;
	}

	@Process
	@PeriodicScheduling(period = 2500)
	public static void followProcess(@In("name") String name, @In("value") Integer value, @In("out") PrintStream out,
			@In("clock") CurrentTimeProvider clock, @InOut("position") ParamHolder<CustomPosition> position) {
		// Print value
		out.format("%06d: Grather %s: %d, at %s%n", clock.getCurrentMilliseconds(), name, value,
				position.value.getPosition().toString());

		// Move
		position.value.setPosition(new Position(position.value.getPosition().x + 1, position.value.getPosition().y));
	}
}

package cz.cuni.mff.d3s.jdeeco.simulation.demo;


import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.network.Host;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

public class PositionAwareComponent {
	public String id;
	public Position position;
	public boolean hasIP;
	
	public static String HOST_REFERENCE = "simulation.host";

	public PositionAwareComponent(String id, Position position, boolean hasIP) {
		this.id = id;
		this.position = position;
		this.hasIP = hasIP;
	}
	

	@Process
	@PeriodicScheduling(500)
	public static void measurePosition(
			@InOut("position") ParamHolder<Position> position) {
		Host host = (Host) ProcessContext.getCurrentProcess().getComponentInstance().getInternalData().get(HOST_REFERENCE);
		if (host != null) {
			position.value = new Position(host.getPositionX(), host.getPositionY());
		}
	}
}

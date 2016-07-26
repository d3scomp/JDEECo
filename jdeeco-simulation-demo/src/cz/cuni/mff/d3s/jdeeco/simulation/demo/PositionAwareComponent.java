package cz.cuni.mff.d3s.jdeeco.simulation.demo;


import cz.cuni.mff.d3s.deeco.annotations.InOut;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.deeco.model.runtime.api.ComponentInstance;
import cz.cuni.mff.d3s.deeco.task.ParamHolder;
import cz.cuni.mff.d3s.deeco.task.ProcessContext;

public class PositionAwareComponent {
	public String id;
	public Position position;
	public boolean hasIP;
	
	public static String POSITION_SENSOR = "gps";

	public PositionAwareComponent(String id, Position position, boolean hasIP) {
		this.id = id;
		this.position = position;
		this.hasIP = hasIP;
	}
	
	public static void initialize(ComponentInstance componentInstance, PositionSensor positionSensor) {
		componentInstance.getInternalData().put(POSITION_SENSOR, positionSensor);
	}

	@Process
	@PeriodicScheduling(period=500)
	public static void measurePosition(
			@InOut("position") ParamHolder<Position> position) {
//		FIXME
//		PositionSensor sensor = (PositionSensor) ProcessContext.getInternalData(POSITION_SENSOR);
		PositionSensor sensor = new PositionSensor(null, null);
				
		if (sensor != null) {
			position.value = new Position(sensor.getX(), sensor.getY());
		}
	}
}

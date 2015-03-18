package cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains;

import java.util.Map;


public interface ActuatorProvider {
	public <T> Actuator<T> createActuator(ActuatorType type);
	public Map<ActuatorType, Actuator<?>> getActuators();
}

package cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains;

public interface Actuator<T> {
	public void set(T value);
	public ActuatorType getActuatorType();
}

package cz.cuni.mff.d3s.jdeeco.matsim.dataaccess;

public interface Actuator<T> {
	public void set(T value);
	public ActuatorType getActuatorType();
}

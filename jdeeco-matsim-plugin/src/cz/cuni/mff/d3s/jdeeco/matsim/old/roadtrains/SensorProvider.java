package cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains;

import java.util.Map;

public interface SensorProvider {
	public <T> Sensor<T> createSensor(SensorType type);
	public Map<SensorType, Sensor<?>> getSensors();
}

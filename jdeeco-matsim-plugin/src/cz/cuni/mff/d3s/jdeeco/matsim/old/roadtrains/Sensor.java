package cz.cuni.mff.d3s.jdeeco.matsim.old.roadtrains;

public interface Sensor<T> {
	
	SensorType getSensorType();
	T read();
}

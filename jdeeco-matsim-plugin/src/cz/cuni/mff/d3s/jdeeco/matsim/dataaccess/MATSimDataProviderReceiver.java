package cz.cuni.mff.d3s.jdeeco.matsim.dataaccess;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.matsim.api.core.v01.Id;
import org.matsim.core.basic.v01.IdImpl;

import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataProvider;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimDataReceiver;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimInput;
import cz.cuni.mff.d3s.jdeeco.matsim.simulation.MATSimOutput;

public class MATSimDataProviderReceiver implements MATSimDataReceiver, MATSimDataProvider {
	// This is coming from MATSim (i.e. its output) = sensors
	protected final Map<Id, MATSimOutput> outputs;

	// This is going to MATSim (i.e. its input) = actuators
	protected final Map<Id, MATSimInput> inputs;

	protected final Map<Id, List<Id>> parked;

	public MATSimDataProviderReceiver(List<String> linksToDisable) {
		this.outputs = new HashMap<Id, MATSimOutput>();
		this.inputs = new HashMap<Id, MATSimInput>();
		this.parked = new HashMap<Id, List<Id>>();

		List<Id> parkedVehicles;
		Id parkedVehicle = new IdImpl(-1);
		for (String linkToDisable : linksToDisable) {
			parkedVehicles = new LinkedList<Id>();
			parkedVehicles.add(parkedVehicle);
			parked.put(new IdImpl(linkToDisable), parkedVehicles);
		}
	}

	// Here we return next links ids for MATSim agents
	@Override
	public Map<Id, MATSimInput> getMATSimData() {
		Map<Id, MATSimInput> result = new HashMap<Id, MATSimInput>();
		for (Map.Entry<Id, MATSimInput> entry : inputs.entrySet()) {
			result.put(entry.getKey(), entry.getValue().clone());
		}
		return result;
	}

	// Here we update sensors
	@Override
	public void setMATSimData(Map<Id, MATSimOutput> map) {
		outputs.clear();
		outputs.putAll(map);
	}

	public ActuatorProvider getActuatorProvider(final Id ownerId) {
		return new ActuatorProvider() {

			private final Map<ActuatorType, Actuator<?>> actuators = new HashMap<ActuatorType, Actuator<?>>();

			public <T> Actuator<T> createActuator(ActuatorType type) {
				@SuppressWarnings("unchecked")
				Actuator<T> actuator = (Actuator<T>) getActuatorInternal(ownerId, type, this);
				actuators.put(type, actuator);
				return actuator;
			}

			public Map<ActuatorType, Actuator<?>> getActuators() {
				return actuators;
			}
		};
	}

	public SensorProvider getSensorProvider(final Id ownerId) {
		return new SensorProvider() {

			private final Map<SensorType, Sensor<?>> sensors = new HashMap<SensorType, Sensor<?>>();

			public <T> Sensor<T> createSensor(SensorType type) {
				@SuppressWarnings("unchecked")
				Sensor<T> sensor = (Sensor<T>) getSensorInternal(ownerId, type);
				sensors.put(type, sensor);
				return sensor;
			}

			public Map<SensorType, Sensor<?>> getSensors() {
				return sensors;
			}
		};
	}

	private Sensor<?> getSensorInternal(final Id requesterId, SensorType sensorType) {
		if (sensorType == SensorType.CURRENT_LINK) {
			return new Sensor<Id>() {

				public SensorType getSensorType() {
					return SensorType.CURRENT_LINK;
				}

				public Id read() {
					MATSimOutput out = outputs.get(requesterId);
					return out != null ? out.currentLinkId : null;
				}
			};
		} else if (sensorType == SensorType.IS_PARKED) {
			return new Sensor<Boolean>() {

				public SensorType getSensorType() {
					return SensorType.IS_PARKED;
				}

				public Boolean read() {
					MATSimOutput mo = outputs.get(requesterId);
					List<Id> parkedVehicles = parked.get(mo.currentLinkId);
					if (parkedVehicles == null) {
						return false;
					} else {
						return parked.get(mo.currentLinkId).contains(requesterId);
					}
				}
			};
		} else if (sensorType == SensorType.CURRENT_LINK_FREE_PLACES) {
			return new Sensor<Integer>() {

				public SensorType getSensorType() {
					return SensorType.CURRENT_LINK_FREE_PLACES;
				}

				public Integer read() {
					MATSimOutput mo = outputs.get(requesterId);
					List<Id> parkedVehicles = parked.get(mo.currentLinkId);
					if (parkedVehicles == null || parkedVehicles.size() == 0) {
						return 1;
					} else {
						return 0;
					}
				}
			};
		}
		return null;
	}

	private Actuator<?> getActuatorInternal(final Id requesterId, ActuatorType actuatorType,
			final ActuatorProvider provider) {
		final MATSimInput mData;
		if (inputs.containsKey(requesterId)) {
			mData = inputs.get(requesterId);
		} else {
			mData = new MATSimInput();
			inputs.put(requesterId, mData);
		}
		if (actuatorType == ActuatorType.ROUTE) {
			return new Actuator<List<Id>>() {
				public void set(List<Id> value) {
					mData.route = value;
					if (value == null || value.isEmpty()) {
						MATSimOutput mo = outputs.get(requesterId);
						List<Id> parkedVehicles;
						if (parked.containsKey(mo.currentLinkId)) {
							parkedVehicles = parked.get(mo.currentLinkId);
						} else {
							parkedVehicles = new LinkedList<Id>();
							parked.put(mo.currentLinkId, parkedVehicles);
						}
						if (!parkedVehicles.contains(requesterId) && parkedVehicles.size() == 0) {
							parkedVehicles.add(requesterId);
						}
					}
				}

				public ActuatorType getActuatorType() {
					return ActuatorType.ROUTE;
				}
			};
		}
		if (actuatorType == ActuatorType.SPEED) {
			return new Actuator<Double>() {
				@Override
				public void set(Double meterPerSecond) {
					mData.speed = meterPerSecond;
				}

				@Override
				public ActuatorType getActuatorType() {
					return ActuatorType.SPEED;
				}
			};
		}
		return null;
	}
}

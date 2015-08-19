package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;
import java.util.Map;

import cz.cuni.mff.d3s.jdeeco.ros.Bumper;
import cz.cuni.mff.d3s.jdeeco.ros.Buttons;
import cz.cuni.mff.d3s.jdeeco.ros.DockIR;
import cz.cuni.mff.d3s.jdeeco.ros.FloorDistance;
import cz.cuni.mff.d3s.jdeeco.ros.Info;
import cz.cuni.mff.d3s.jdeeco.ros.Positioning;
import cz.cuni.mff.d3s.jdeeco.ros.RosServices;
import cz.cuni.mff.d3s.jdeeco.ros.SHT1x;
import cz.cuni.mff.d3s.jdeeco.ros.Wheels;

/**
 * The {@link RobotState} instance contains data from all the turtlebot's
 * sensors and represents the robot's state. The state from a certain sensor is
 * present only if the appropriate ROS service that provides the readings is
 * registered, otherwise the state value is set to null.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class RobotState implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 1097866211072734023L;

	/**
	 * The state of the bumper.
	 */
	public final BumperValue bumper;

	/**
	 * States of the buttons.
	 */
	public final Map<ButtonID, ButtonState> buttons;

	/**
	 * States of the docking IR sensors.
	 */
	public final Map<DockingIRDiod, DockingIRSignal> dockIR;

	/**
	 * States of the floor sensors.
	 */
	public final Map<FloorSensorID, FloorSensorState> floorStates;

	/**
	 * Distances measured by the floor sensors.
	 */
	public final Map<FloorSensorID, Short> floorDistances;

	/**
	 * Version info of the robot.
	 */
	public final InfoData versionInfo;

	/**
	 * Position computed by the odometry.
	 */
	public final Position odometry;

	/**
	 * Data measured by the GPS module.
	 */
	public final GpsData gpsData;

	/**
	 * Position computed by the
	 * <a href= "http://wiki.ros.org/gmapping"> gmapping algorithm</a>.
	 */
	public final PoseWithCovariance position;

	/**
	 * Temperature and humidity data.
	 */
	public final Weather weather;

	/**
	 * The state of motors power.
	 */
	public final MotorPower motorPower;

	/**
	 * The state of the wheels.
	 */
	public final Map<WheelID, WheelState> wheelStates;

	/**
	 * Create a new instance of {@link RobotState}. During the creation all the
	 * data are freshly read from the given {@link RosServices}. If any of the
	 * required service is not registered a null value is assigned to the
	 * corresponding state field.
	 * 
	 * @param services The {@link RosServices} to reed the state from.
	 * 
	 * @throws IllegalArgumentException if the services argument is null.
	 */
	public RobotState(RosServices services) {
		if (services == null) {
			throw new IllegalArgumentException(String.format("The \"%s\" argument cannot be null.", "services"));
		}

		Bumper bumperService = services.getService(Bumper.class);
		bumper = bumperService != null ? bumperService.getBumper() : null;

		Buttons buttonService = services.getService(Buttons.class);
		buttons = buttonService != null ? buttonService.getAllButtons() : null;

		DockIR dockService = services.getService(DockIR.class);
		dockIR = dockService != null ? dockService.getAllDockIR() : null;

		FloorDistance floorService = services.getService(FloorDistance.class);
		if (floorService != null) {
			floorStates = floorService.getAllFloorStates();
			floorDistances = floorService.getAllFloorDistances();
		} else {
			floorStates = null;
			floorDistances = null;
		}

		Info infoService = services.getService(Info.class);
		versionInfo = infoService != null ? infoService.getInfo() : null;

		Positioning positioningService = services.getService(Positioning.class);
		if (positioningService != null) {
			odometry = positioningService.getOdometry();
			gpsData = positioningService.getGpsData();
			position = positioningService.getPosition();
		} else {
			odometry = null;
			gpsData = null;
			position = null;
		}

		SHT1x weatherService = services.getService(SHT1x.class);
		weather = weatherService != null ? weatherService.getWeather() : null;

		Wheels wheelService = services.getService(Wheels.class);
		if (wheelService != null) {
			motorPower = wheelService.getMotorPower();
			wheelStates = wheelService.getAllWheels();
		} else {
			motorPower = null;
			wheelStates = null;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bumper: ").append(bumper).append("\n");
		for(ButtonID buttonId : buttons.keySet()){
			builder.append("Button ").append(buttonId).append(": ");
			builder.append(buttons.get(buttonId)).append("\n");
		}
		for(DockingIRDiod diodId : dockIR.keySet()){
			builder.append("Dock IR ").append(diodId).append(": ");
			builder.append(dockIR.get(diodId)).append("\n");
		}
		for(FloorSensorID floorId : floorStates.keySet()){
			builder.append("Floor sensor ").append(floorId).append(": ");
			builder.append(floorStates.get(floorId)).append(" (");
			if(floorDistances.containsKey(floorId)){
				builder.append(floorDistances.get(floorId));
			} else {
				builder.append("null");
			}
			builder.append(")\n");
		}
		builder.append("Version: ").append(versionInfo).append("\n");
		builder.append("Odometry: ").append(odometry).append("\n");
		builder.append("GPS: ").append(gpsData).append("\n");
		builder.append("Position: ").append(position).append("\n");
		builder.append(weather).append("\n");
		builder.append("Motor power: ").append(motorPower).append("\n");
		for(WheelID wheelId : wheelStates.keySet()){
			builder.append(wheelId).append("Wheel ").append(": ");
			builder.append(wheelStates.get(wheelId)).append("\n");
		}
		
		return builder.toString();
	}
}

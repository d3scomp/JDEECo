package cz.cuni.mff.d3s.jdeeco.turtlebot.simpleexchange;

import geometry_msgs.Point;
import geometry_msgs.PoseWithCovariance;
import sensor_msgs.NavSatFix;
import cz.cuni.mff.d3s.deeco.annotations.Component;
import cz.cuni.mff.d3s.deeco.annotations.In;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;
import cz.cuni.mff.d3s.deeco.annotations.Process;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.BumperValue;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.ButtonState;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.DockingIRSignal;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.FloorSensorState;
import cz.cuni.mff.d3s.jdeeco.ros.datatypes.WheelState;

@Component
public class ReceivingComponent {
	public String id;
	public String receiverId;

	public BumperValue bumperValue;
	public ButtonState button0;
	public ButtonState button1;
	public ButtonState button2;
	public DockingIRSignal dockLeft;
	public DockingIRSignal dockCenter;
	public DockingIRSignal dockRight;
	public FloorSensorState floorLeft;
	public FloorSensorState floorCenter;
	public FloorSensorState floorRight;
	public String fwInfo;
	public String swInfo;
	public String hwInfo;
	public NavSatFix gps;
	public Long gpsTime;
	public Point odometry;
	public PoseWithCovariance pose;
	public Double temperature;
	public Double humidity;
	public WheelState wheelLeft;
	public WheelState wheelRight;

	public ReceivingComponent(final String id, final String receiverId) {
		this.id = id;
		this.receiverId = id;
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 0)
	public static void printBumper(@In("bumperValue") BumperValue bumperValue) {
		System.out.println(String.format("\nBumper: %s", bumperValue));
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 5)
	public static void printButtons(@In("button0") ButtonState button0,
			@In("button1") ButtonState button1,
			@In("button2") ButtonState button2) {
		System.out.println(String.format("Button 0: %s", button0));
		System.out.println(String.format("Button 1: %s", button1));
		System.out.println(String.format("Button 2: %s", button2));
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 10)
	public static void printDock(@In("dockLeft") DockingIRSignal dockLeft,
			@In("dockCenter") DockingIRSignal dockCenter,
			@In("dockRight") DockingIRSignal dockRight) {
		System.out.println(String.format("Dock left: %s", dockLeft));
		System.out.println(String.format("Dock center: %s", dockCenter));
		System.out.println(String.format("Dock right: %s", dockRight));
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 15)
	public static void printFloor(

	@In("floorLeft") FloorSensorState floorLeft,
			@In("floorCenter") FloorSensorState floorCenter,
			@In("floorRight") FloorSensorState floorRight) {
		System.out.println(String.format("Floor left: %s", floorLeft));
		System.out.println(String.format("Floor center: %s", floorCenter));
		System.out.println(String.format("Floor right: %s", floorRight));
	}

	@Process
	@PeriodicScheduling(period = 10000, offset = 20)
	public static void printInfo(@In("fwInfo") String fwInfo,
			@In("swInfo") String swInfo, @In("hwInfo") String hwInfo) {
		System.out.println(String.format("FW: %s", fwInfo));
		System.out.println(String.format("SW: %s", swInfo));
		System.out.println(String.format("HW: %s", hwInfo));
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 25)
	public static void printPosition(@In("gps") NavSatFix gps,
			@In("gpsTime") Long gpsTime, @In("odometry") Point odometry,
			@In("pose") PoseWithCovariance pose) {
		System.out.println(String.format("GPS Lat: %f Long: %f Alt: %f",
				gps.getLatitude(), gps.getLongitude(), gps.getAltitude()));
		System.out.println(String.format("GPS Time: %d", gpsTime));
		System.out.println(String.format("Odometry: [%f, %f, %f]",
				odometry.getX(), odometry.getY(), odometry.getZ()));
		System.out.println(String.format("Position: [%f, %f, %f]", pose
				.getPose().getPosition().getX(), pose.getPose().getPosition()
				.getY(), pose.getPose().getPosition().getZ()));
		System.out.println(String.format("Orientation: [%f, %f, %f, %f]", pose
				.getPose().getOrientation().getX(), pose.getPose()
				.getOrientation().getY(), pose.getPose().getOrientation()
				.getZ(), pose.getPose().getOrientation().getW()));
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 30)
	public static void printSHT1x(@In("temperature") Double temperature,
			@In("humidity") Double humidity) {
		System.out.println(String.format("Temperature: %f", temperature));
		System.out.println(String.format("Humidity: %f", humidity));
	}

	@Process
	@PeriodicScheduling(period = 1000, offset = 35)
	public static void printWheels(@In("wheelLeft") WheelState wheelLeft,
			@In("wheelRight") WheelState wheelRight) {
		System.out.println(String.format("Wheel left: %s", wheelLeft));
		System.out.println(String.format("Wheel right: %s", wheelRight));
	}
}

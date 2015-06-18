package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import kobuki_msgs.DockInfraRed;

/**
 * Enumeration of signal values provided by turtlebot's infra-red diods.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum DockingIRSignal {
	/**
	 * The diod receives a signal from a near source placed left from the diod.
	 */
	NEAR_LEFT,
	/**
	 * The diod receives a signal from a near source placed in center of the
	 * diod.
	 */
	NEAR_CENTER,
	/**
	 * The diod receives a signal from a near source placed right from the diod.
	 */
	NEAR_RIGHT,
	/**
	 * The diod receives a signal from a far source placed left from the diod.
	 */
	FAR_LEFT,
	/**
	 * The diod receives a signal from a far source placed in the center of the
	 * diod.
	 */
	FAR_CENTER,
	/**
	 * The diod receives a signal from a far source placed right from the diod.
	 */
	FAR_RIGHT,
	/**
	 * The diod receives no signal.
	 */
	INFINITY;

	/**
	 * Convert the byte representation of the signal used in ROS messages into
	 * the {@link DockingIRSignal} instance.
	 * 
	 * @param signal the byte representation of the signal.
	 * @return The {@link DockingIRSignal} instance of the received signal.
	 */
	public static DockingIRSignal fromByte(byte signal) {
		switch (signal) {
		case DockInfraRed.NEAR_LEFT:
			return DockingIRSignal.NEAR_LEFT;
		case DockInfraRed.NEAR_CENTER:
			return DockingIRSignal.NEAR_CENTER;
		case DockInfraRed.NEAR_RIGHT:
			return DockingIRSignal.NEAR_RIGHT;
		case DockInfraRed.FAR_LEFT:
			return DockingIRSignal.FAR_LEFT;
		case DockInfraRed.FAR_CENTER:
			return DockingIRSignal.FAR_CENTER;
		case DockInfraRed.FAR_RIGHT:
			return DockingIRSignal.FAR_RIGHT;
		default:
			return DockingIRSignal.INFINITY;
		}
	}
}

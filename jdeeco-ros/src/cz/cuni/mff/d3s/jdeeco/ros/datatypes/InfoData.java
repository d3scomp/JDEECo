package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import java.io.Serializable;

/**
 * An instance of {@link InfoData} holds the version information about the
 * robot.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 *
 */
public class InfoData implements Serializable {

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 6464293319673184335L;

	/**
	 * The firmware information.
	 */
	public final String firmwareInfo;

	/**
	 * The software information.
	 */
	public final String softwareInfo;

	/**
	 * The hardware information.
	 */
	public final String hardwareInfo;

	/**
	 * Create a new instance of robot's version information.
	 * 
	 * @param firmware
	 *            The firmware information.
	 * @param software
	 *            The software information.
	 * @param hardware
	 *            The hardware information.
	 */
	public InfoData(String firmware, String software, String hardware) {
		firmwareInfo = firmware;
		softwareInfo = software;
		hardwareInfo = hardware;
	}
	
	@Override
	public String toString() {
		return String.format("Firmware: %s Software: %s Hardware: %s",
				firmwareInfo, softwareInfo, hardwareInfo);
	}
}

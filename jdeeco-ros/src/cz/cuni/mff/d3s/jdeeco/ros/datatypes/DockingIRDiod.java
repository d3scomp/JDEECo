package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

/**
 * The enumeration of turtlebot's docking infra-red diods.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public enum DockingIRDiod {
	/**
	 * The infra-red diod on the left side.
	 */
	LEFT,
	/**
	 * The infra-red diod in the center.
	 */
	CENTRAL,
	/**
	 * The infra-red diod on the right side.
	 */
	RIGHT;

	/**
	 * Convert the given index into the {@link DockingIRDiod} instance. If the
	 * index is out of range null is returned. The mapping of indices is the
	 * following:
	 * <ul>
	 * <li>0 -> {@link #RIGHT}</li>
	 * <li>1 -> {@link #CENTRAL}</li>
	 * <li>2 -> {@link #LEFT}</li>
	 * </ul>
	 * 
	 * @param index
	 *            the index to be converted to a {@link DockingIRDiod} instance.
	 * @return the {@link DockingIRDiod} representation of the diod defined by
	 *         the given index.
	 */
	public static DockingIRDiod fromIndex(int index) {
		switch (index) {
		case 0:
			return DockingIRDiod.RIGHT;
		case 1:
			return DockingIRDiod.CENTRAL;
		case 2:
			return DockingIRDiod.LEFT;
		default:
			return null;
		}
	}
}

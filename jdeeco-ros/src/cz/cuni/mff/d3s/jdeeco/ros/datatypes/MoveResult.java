package cz.cuni.mff.d3s.jdeeco.ros.datatypes;

import move_base_msgs.MoveBaseActionResult;

/**
 * Wraps information about MoveBase goal result
 * 
 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
 */
public class MoveResult {
	/**
	 * Holds goals status code
	 * 
	 * @author Vladimir Matena <matena@d3s.mff.cuni.cz>
	 */
	public static enum Status {
		Unknown0, // 0
		Unknown1, // 1
		Canceled, // 2
		Succeeded, // 3
		Rejected, // 4
		Unknown5, // 5
		Unknown6, // 6
		Unknown7, // 7
		Unknown8; // 8

		public static Status fromByte(byte status) {
			return values()[status];
		}
	}

	/**
	 * Goal identification
	 */
	public final String goalId;
	/**
	 * Goal status code
	 */
	public final Status status;
	/**
	 * Goal status text
	 */
	public final String text;

	/**
	 * Constructs MoveResult
	 * 
	 * @param goalId
	 *            Goal identification
	 * @param status
	 *            Goal status code
	 * @param text
	 *            Goal Status text
	 */
	public MoveResult(String goalId, Byte status, String text) {
		this.goalId = goalId;
		this.status = Status.fromByte(status);
		this.text = text;
	}

	/**
	 * Constructs MoveResult from ROS message
	 * 
	 * @param result
	 *            Source MoveBaseActionMessage
	 * @return MoveResult instance holding information from message
	 */
	public static MoveResult fromMoveBaseActionResult(MoveBaseActionResult result) {
		return new MoveResult(result.getStatus().getGoalId().getId(), result.getStatus().getStatus(),
				result.getStatus().getText());
	}

	/**
	 * Formats string describing instance content
	 */
	@Override
	public String toString() {
		return String.format("Goal: %s status: %s - %s", goalId, status.name(), text);
	}
}

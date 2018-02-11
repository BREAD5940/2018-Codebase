package frc.team5940.codebase2018.robot.autonomous;

import frc.team5940.codebase2018.robot.autonomous.AutoPathSelect.RobotLocation;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;

/**
 * Creates a new AutoPath that the robot will follow through during autonomous.
 * 
 * @author Michael Bentley
 *
 */
public class AutoPath {

	/**
	 * The different AutoActions in this AutoPath.
	 */
	AutoAction[] actions;

	/**
	 * The name of this path.
	 */
	String key;

	/**
	 * The corresponding {@link RobotLocation}.
	 */
	RobotLocation roboLoc;

	/**
	 * The key to the corresponding scale locations.
	 */
	String fieldRex;

	/**
	 * Creates a new {@link AutoPath}.
	 * 
	 * @param key
	 *            The name of this path to display in SmartDashboard.
	 * @param fieldRex
	 *            The corresponding switch and scale locations that work with this
	 *            location.
	 * @param roboLoc
	 *            The corresponding robot starting position with this action.
	 * @param actions
	 *            The list of AutoAction that this AutoPath should execute in order.
	 */
	public AutoPath(String key, String fieldRex, RobotLocation roboLoc, AutoAction... actions) {
		this.actions = actions;
		this.key = key;
		this.roboLoc = roboLoc;
		this.fieldRex = fieldRex;
	}

	/**
	 * Gets the corresponding scale and switch locations.
	 * 
	 * @return The corresponding scale and switch locations.
	 */
	public String getFieldRex() {
		return fieldRex;
	}

	/**
	 * The name of this path to be displayed on the SmartDashboard.
	 * 
	 * @return The name of this path.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * The required robot location for this action.
	 * 
	 * @return The robot location that corresponds with this action.
	 */
	public RobotLocation getRequiredRobotLocation() {
		return roboLoc;
	}

	/**
	 * The array of {@link AutoAction} that will be executed in order to complete
	 * this {@link AutoPath}.
	 * 
	 * @return The {@link AutoAction} in this {@link AutoPath}.
	 */
	public AutoAction[] getActions() {
		return actions;
	}

}

package frc.team5940.codebase2018.robot.autonomous.actions;

import frc.team5940.codebase2018.robot.RobotConfig;
import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower;

/**
 * An interface to represent an auto action that requires the elevator to be in
 * a particular position before running. The {@link AutoPlanFollower} will set
 * the elevator to the requested height and then wait for it to reach that
 * position before accomplishing an action that implements this. This interface
 * also includes the enum used to specify what elevator height is desired.
 */
public class ElevatorAutoAction implements AutoAction {

	/**
	 * The requested height of the elevator.
	 */
	ElevatorHeight setElevatorHeight;

	/**
	 * If the robot should wait before executing the next AutoAction.
	 */
	boolean wait;

	/**
	 * The available heights that the elevator can be set to.
	 */
	public enum ElevatorHeight {
		DOWN(0), SWITCH(2), SCALE(RobotConfig.MAX_ELEVATOR_HEIGHT);

		double height;

		ElevatorHeight(double height) {
			this.height = height;
		}

		public double getHeight() {
			return height;
		}
	}

	/**
	 * Creates a new {@link ElevatorAutoAction}.
	 * 
	 * @param setElevatorHeight
	 *            The requested elevator height.
	 * @param wait
	 *            If the robot should wait before executing the next action.
	 */
	public ElevatorAutoAction(ElevatorHeight setElevatorHeight, boolean wait) {
		if (setElevatorHeight == null) {
			throw new IllegalArgumentException("height is null");
		}
		this.setElevatorHeight = setElevatorHeight;
		this.wait = wait;
	}

	/**
	 * Returns the height that the elevator must be set to and have reached before
	 * this action is executed.
	 * 
	 * @return A value from {@link ElevatorHeight}.
	 */
	public ElevatorHeight getSetElevatorHeight() {
		return this.setElevatorHeight;
	}

	/**
	 * Returns if the AutoController should wait until the elevator is at the right
	 * height before executing the next action.
	 * 
	 * @return If this action should be completed before moving onto the next one.
	 */
	public boolean getWait() {
		return this.wait;
	}
}

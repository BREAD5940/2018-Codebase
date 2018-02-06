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
public abstract class ElevatorAction implements AutoAction {

	ElevatorHeight setElevatorHeight;

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

	public ElevatorAction(ElevatorHeight setElevatorHeight) {
		if (setElevatorHeight == null) {
			throw new IllegalArgumentException("height is null");
		}
		this.setElevatorHeight = setElevatorHeight;
	}

	/**
	 * Returns the height that the elevator must be set to and have reached before
	 * this action is executed.
	 * 
	 * @return A value from {@link ElevatorHeight}.
	 */
	public ElevatorHeight getSetElevatorHeight() {
		return setElevatorHeight;
	}
}

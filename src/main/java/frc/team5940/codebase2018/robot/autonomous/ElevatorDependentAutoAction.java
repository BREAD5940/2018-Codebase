package frc.team5940.codebase2018.robot.autonomous;

import frc.team5940.codebase2018.robot.RobotConfig;

/**
 * An interface to represent an auto action that requires the elevator to be in
 * a particular position before running. The {@link AutoPlanFollower} will set
 * the elevator to the requested height and then wait for it to reach that
 * position before accomplishing an action that implements this. This interface
 * also includes the enum used to specify what elevator height is desired.
 */
public interface ElevatorDependentAutoAction extends AutoAction {

	/**
	 * The available heights that the elevator can be set to.
	 */
	enum ElevatorHeight {
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
	 * Returns the height that the elevator must be set to and have reached before
	 * this action is executed.
	 * 
	 * @return A value from {@link ElevatorHeight}.
	 */
	public ElevatorHeight getRequiredElevatorHeight();
}

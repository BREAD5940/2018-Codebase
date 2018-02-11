package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.LoggingUtils;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import frc.team5940.codebase2018.robot.RobotConfig;
import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower;

/**
 * An {@link AutoAction} that specifies what height the elevator should be set
 * to. You can decide if you should wait before reaching the next height before
 * executing the next AutoAction.
 * 
 * @author Michael Bentley
 */
public class ElevatorAutoAction extends AutoAction {

	/**
	 * The requested height of the elevator.
	 */
	ElevatorHeight setElevatorHeight;

	/**
	 * If the robot should wait before executing the next AutoAction.
	 */
	boolean wait;

	/**
	 * The current height of the elevator.
	 */
	ValueNode<? extends Number> elevatorHeightValueNode;

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
	public ElevatorAutoAction(Network network, Logger logger, String label,
			ValueNode<? extends Number> elevatorHeightValueNode, ElevatorHeight setElevatorHeight, boolean wait)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
		LoggingUtils.checkArgument(setElevatorHeight);
		this.setElevatorHeight = setElevatorHeight;
		this.wait = wait;
		this.elevatorHeightValueNode = elevatorHeightValueNode;
	}

	@Override
	protected void setup() {
	}

	@Override
	protected Boolean checkCompletion() {
		if (this.wait) {
			return withinMargin(elevatorHeightValueNode.getValue().doubleValue(), this.setElevatorHeight.getHeight(),
					0.1);
		}
		return true;
	}

	/**
	 * Returns the height that the elevator should be set to
	 * 
	 * @return A value from {@link ElevatorHeight}.
	 */
	public ElevatorHeight getSetElevatorHeight() {
		return this.setElevatorHeight;
	}
}

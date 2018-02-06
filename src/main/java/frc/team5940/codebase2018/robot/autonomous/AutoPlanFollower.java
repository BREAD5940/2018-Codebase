package frc.team5940.codebase2018.robot.autonomous;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import frc.team5940.codebase2018.robot.autonomous.actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.DriveAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.EmptyAutonomousAction;
import frc.team5940.codebase2018.robot.autonomous.actions.OuttakeCubeAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.SetInstantElevatorAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.SetWaitElevatorHeightAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.TurnAutoAction;

/**
 * This progresses through an array of AutoActions. This will reset its progress
 * if the array changes. This also does not handle ElevatorDependentActions.
 * Should use {@link ElevatorDependentActionStatusCheckerValueNode} to handle
 * those.
 * 
 * If there is no action left in the AutoPlan than this will return an
 * {@link EmptyAutonomousAction}.
 * 
 * @author Michael Bentley
 *
 */
public class AutoPlanFollower extends ValueNode<AutoAction> {

	/**
	 * The ValueNode that returns the current auto plan.
	 */
	ValueNode<? extends AutoAction[]> autoPlanValueNode;

	/**
	 * The array of the current actions for the autoPlan.
	 */
	AutoAction[] currentActionArray;

	/**
	 * The index of the current action.
	 */
	int currentActionIndex = 0;

	/**
	 * The current distance the robot has moved.
	 */
	ValueNode<? extends Double> robotDistanceValueNode;

	/**
	 * The current angle of the robot.
	 */
	ValueNode<? extends Double> robotAngleValueNode;

	/**
	 * The target distance for the robot to move.
	 */
	double targetDistance;

	/**
	 * The target angle for the robot to turn to.
	 */
	double targetAngle;

	/**
	 * The current time of the timer for the OuttakeCubeAction.
	 */
	double timerTime = 0;

	/**
	 * The previous time this has been updated. This is only updated while the timer
	 * is active for the OuttakeCubeAction.
	 */
	long previousTime;

	/**
	 * The current height of the elevator.
	 */
	ValueNode<? extends Double> elevatorHeightValueNode;

	/**
	 * Creates an AutoPlanFollower.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param autoPlanValueNode
	 *            The ValueNode that returns the current auto plan.
	 * @param robotDistanceValueNode
	 *            The ValueNode that returns the distance the robot has traveled.
	 * @param robotAngleValueNode
	 *            The angle the robot is currently facing.
	 * @param elevatorHeightValueNode
	 *            The current height of the elevator.
	 */
	public AutoPlanFollower(Network network, Logger logger, String label,
			ValueNode<? extends AutoAction[]> autoPlanValueNode, ValueNode<? extends Double> robotDistanceValueNode,
			ValueNode<? extends Double> robotAngleValueNode, ValueNode<? extends Double> elevatorHeightValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoPlanValueNode, robotDistanceValueNode, robotAngleValueNode,
				elevatorHeightValueNode);

		this.robotAngleValueNode = robotAngleValueNode;
		this.robotDistanceValueNode = robotDistanceValueNode;
		this.autoPlanValueNode = autoPlanValueNode;
		this.elevatorHeightValueNode = elevatorHeightValueNode;
	}

	@Override
	protected AutoAction updateValue() {
		// Verifies that the AutoPlan has not changed. This is important because before
		// the match starts the AutoPlanValueNode will not return the proper value.
		if (!autoPlanValueNode.getValue().equals(this.currentActionArray)) {
			// Updates the actionArray, resets the current index, and then sets up the first
			// action.
			this.currentActionArray = autoPlanValueNode.getValue();
			this.currentActionIndex = 0;
			setupAction(currentActionIndex);
		}

		AutoAction currentAction = getCurrentAction();

		// Checks if the current action is complete. If it is then this returns the next
		// action.

		if (currentAction instanceof OuttakeCubeAutoAction) {
			// Updates the current time for the built in timer.
			this.timerTime = this.timerTime - (System.currentTimeMillis() - this.previousTime);
			this.previousTime = System.currentTimeMillis();
			if (this.timerTime < 0) {
				currentAction = nextAction();
			}
		} else if (currentAction instanceof DriveAutoAction) {
			if (withinMargin(robotDistanceValueNode.getValue(), targetDistance, 0.05)) {
				currentAction = nextAction();
			}
		} else if (currentAction instanceof TurnAutoAction) {
			if (withinMargin(robotAngleValueNode.getValue(), targetAngle, 2)) {
				currentAction = nextAction();
			}
		} else if (currentAction instanceof SetInstantElevatorAutoAction) {
			// Immediately jumps to the next action so the robot can do other things while
			// adjusting the height.
			currentAction = nextAction();
		} else if (currentAction instanceof SetWaitElevatorHeightAutoAction) {
			if (withinMargin(this.elevatorHeightValueNode.getValue(),
					((SetWaitElevatorHeightAutoAction) currentAction).getSetElevatorHeight().getHeight(), 0.05)) {
				currentAction = nextAction();
			}
		}
		return currentAction;
	}

	/**
	 * Updates the index of the current action and prepares for the next action.
	 * 
	 * @return The next action.
	 */
	private AutoAction nextAction() {
		this.currentActionIndex++;
		return setupAction(this.currentActionIndex);
	}

	/**
	 * Sets up the initial values of the next action.
	 * 
	 * @param actionIndex
	 *            The current action to setup.
	 * @return The current action.
	 */
	private AutoAction setupAction(int actionIndex) {
		AutoAction currentAction = getCurrentAction();
		if (currentAction instanceof OuttakeCubeAutoAction) {
			this.timerTime = 3;
			this.previousTime = System.currentTimeMillis();
		} else if (currentAction instanceof DriveAutoAction) {
			this.targetDistance = this.robotDistanceValueNode.getValue()
					+ ((DriveAutoAction) currentAction).getDriveFeet();
		} else if (currentAction instanceof TurnAutoAction) {
			this.targetAngle = this.robotAngleValueNode.getValue() + ((TurnAutoAction) currentAction).getTurnDegrees();
		}
		return currentAction;
	}

	/**
	 * Returns the current action for the AutoPlan. If the currentActionIndex is too
	 * great than this will just return an {@link EmptyAutonomousAction}.
	 * 
	 * @return The current action. If no action is available then it returns an
	 *         {@link EmptyAutonomousAction}.
	 */
	private AutoAction getCurrentAction() {
		if (this.currentActionIndex >= this.currentActionArray.length) {
			return new EmptyAutonomousAction();
		}
		return this.currentActionArray[this.currentActionIndex];
	}

	/**
	 * Verifies that a number is within a margin of a target number.
	 * 
	 * @param number
	 *            The number to check the value of.
	 * @param target
	 *            The target number for the number.
	 * @param margin
	 *            The acceptable margin of error.
	 * @return If the number is within the margin of error of the target.
	 */
	public static boolean withinMargin(double number, double target, double margin) {
		return number < target + margin && number > target - margin;
	}
}

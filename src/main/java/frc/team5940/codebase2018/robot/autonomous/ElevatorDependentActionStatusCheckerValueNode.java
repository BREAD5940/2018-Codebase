package frc.team5940.codebase2018.robot.autonomous;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.functional.ChangeDetectorValueNode;

import frc.team5940.codebase2018.robot.autonomous.actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.ElevatorDependentAutoAction;

/**
 * This determines if the Elevator is at the required state for the current
 * autonomous action. Autonomous actions can have required elevator heights
 * through extending ElevatorDependentAutoAction. This will return false until
 * the elevator reaches the required height. If the elevator leaves the required
 * while in the middle of an action this will continue to return true.
 * 
 * @author Michael Bentley
 *
 */
public class ElevatorDependentActionStatusCheckerValueNode extends ChangeDetectorValueNode<AutoAction, Boolean> {

	/**
	 * The current autonomous action for the robot.
	 */
	ValueNode<? extends AutoAction> autoActionValueNode;

	/**
	 * Whether the elevator has reached the target height.
	 */
	boolean atTargetHeight = true;

	/**
	 * The target height of the elevator.
	 */
	double targetHeight = 0;

	/**
	 * The height of the elevator in feet.
	 */
	ValueNode<? extends Number> elevatorHeightValueNode;

	/**
	 * This checks whether the AutoAction is elevator dependent and returns false
	 * until the elevator reaches the necessary state.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param autoActionValueNode
	 *            The current AutoAction for the robot.
	 * @param elevatorHeightValueNode
	 *            The height of the elevator in feet.
	 */
	public ElevatorDependentActionStatusCheckerValueNode(Network network, Logger logger, String label,
			ValueNode<? extends AutoAction> autoActionValueNode, ValueNode<? extends Number> elevatorHeightValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, false, autoActionValueNode, elevatorHeightValueNode);
		this.autoActionValueNode = autoActionValueNode;
		this.elevatorHeightValueNode = elevatorHeightValueNode;
	}

	@Override
	protected void valueChanged(AutoAction newValue) {
		if (newValue instanceof ElevatorDependentAutoAction) {
			this.atTargetHeight = false;
			this.targetHeight = ((ElevatorDependentAutoAction) newValue).getRequiredElevatorHeight().getHeight();
		}
	}

	@Override
	protected Boolean updateValue() {
		if (!this.atTargetHeight) {
			this.atTargetHeight = AutoPlanFollower.withinMargin(elevatorHeightValueNode.getValue().doubleValue(),
					this.targetHeight, 0.05);
		}
		return this.atTargetHeight;
	}
}

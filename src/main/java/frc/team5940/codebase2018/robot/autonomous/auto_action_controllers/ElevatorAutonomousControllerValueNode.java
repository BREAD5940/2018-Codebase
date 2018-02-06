package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import frc.team5940.codebase2018.robot.autonomous.actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.ElevatorAction;

/**
 * This will return the set height of the elevator during autonomous. This will
 * be the last set height of an auto action and default at 0.
 * 
 * @author Michael Bentley
 *
 */
public class ElevatorAutonomousControllerValueNode extends ValueNode<Double> {

	/**
	 * The current auto action.
	 */
	ValueNode<? extends AutoAction> currentActionValueNode;

	/**
	 * The set height of the elevator.
	 */
	double setElevatorHeight = 0;

	/**
	 * This returns the set height of the elevator based on the current AutoAction.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param currentActionValueNode
	 *            The current AutoAction.
	 */
	public ElevatorAutonomousControllerValueNode(Network network, Logger logger, String label,
			ValueNode<? extends AutoAction> currentActionValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, currentActionValueNode);
		this.currentActionValueNode = currentActionValueNode;
	}

	@Override
	protected Double updateValue() {
		AutoAction currentAction = this.currentActionValueNode.getValue();
		if (currentAction instanceof ElevatorAction) {
			this.setElevatorHeight = ((ElevatorAction) currentAction).getSetElevatorHeight().getHeight();
		}
		return this.setElevatorHeight;
	}

}

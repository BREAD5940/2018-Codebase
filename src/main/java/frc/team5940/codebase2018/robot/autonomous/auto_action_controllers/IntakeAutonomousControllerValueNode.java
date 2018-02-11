package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.OuttakeCubeAutoAction;

/**
 * Controls the intake of the robot during Autonomous. Will outtake while a
 * {@link OuttakeCubeAutoAction} is active. Otherwise will do nothing.
 * 
 * @author Michael Bentley
 *
 */
public class IntakeAutonomousControllerValueNode extends ValueNode<Integer> {

	/**
	 * The current AutoAction in the auto plan.
	 */
	ValueNode<? extends AutoAction> autoActionValueNode;

	/**
	 * Creates a new {@link IntakeAutonomousControllerValueNode}
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label
	 * @param autoActionValueNode
	 *            The current AutoAction of this robot's auto plan.
	 */
	public IntakeAutonomousControllerValueNode(Network network, Logger logger, String label,
			ValueNode<? extends AutoAction> autoActionValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoActionValueNode);
		this.autoActionValueNode = autoActionValueNode;
	}

	@Override
	protected Integer updateValue() {
		if (this.autoActionValueNode.getValue() instanceof OuttakeCubeAutoAction) {
			return 1;
		}
		return 0;
	}

}

package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.LoggingUtils;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.NodeGroup;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.functional.AdditionValueNode;
import org.team5940.pantry.processing_network.functional.DivisionValueNode;
import org.team5940.pantry.processing_network.functional.MultiplicationValueNode;

/**
 * This {@link NodeGroup} converts the value of a joystick axis or any ValueNode
 * with values from [-1, 1] to the set height of the elevator in feet.
 * 
 * @author Michael Bentley
 *
 */
public class ElevatorNodeGroup extends NodeGroup {

	/**
	 * The ValueNode that determines the set height of the elevator.
	 */
	MultiplicationValueNode setHeightValueNode;

	/**
	 * Creates a new {@link ElevatorNodeGroup}
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label
	 * @param elevatorHeightControl
	 *            This ValueNode that determines the height of the elevator. Should
	 *            be on a scale from [-1, 1].
	 * @param maxElevatorHeight
	 *            The max height of the elevator.
	 */
	public ElevatorNodeGroup(Network network, Logger logger, String label,
			ValueNode<? extends Double> elevatorHeightControl, double maxElevatorHeight) {
		super(network, logger, label);

		LoggingUtils.checkArgument(elevatorHeightControl);

		AdditionValueNode additionValueNode = new AdditionValueNode(network, logger, label + ": Addition",
				elevatorHeightControl, 1);
		DivisionValueNode divisionValueNode = new DivisionValueNode(network, logger, label + ": Division",
				additionValueNode, 4);
		this.setHeightValueNode = new MultiplicationValueNode(network, logger, label + ": Multiplication",
				divisionValueNode, maxElevatorHeight);
	}

	/**
	 * Gets the ValueNode that determines the height of the elevator.
	 * 
	 * @return The ValueNode that determines the height of the elevator.
	 */
	public MultiplicationValueNode getSetHeightValueNode() {
		return setHeightValueNode;
	}
}

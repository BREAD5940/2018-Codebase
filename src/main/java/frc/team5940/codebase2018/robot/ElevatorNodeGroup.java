package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.LoggingUtils;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.NodeGroup;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.functional.AdditionValueNode;
import org.team5940.pantry.processing_network.functional.DivisionValueNode;
import org.team5940.pantry.processing_network.functional.MultiplicationValueNode;

public class ElevatorNodeGroup extends NodeGroup {

	MultiplicationValueNode maxHeightValueNode;

	public ElevatorNodeGroup(Network network, Logger logger, String label,
			ValueNode<? extends Double> elevatorHeightControl, double maxElevatorHeight) {
		super(network, logger, label);

		LoggingUtils.checkArgument(elevatorHeightControl);

		AdditionValueNode additionValueNode = new AdditionValueNode(network, logger, label + ": Addition",
				elevatorHeightControl, 1);
		DivisionValueNode divisionValueNode = new DivisionValueNode(network, logger, label + ": Division",
				additionValueNode, 4);
		this.maxHeightValueNode = new MultiplicationValueNode(network, logger, label + ": Multiplication",
				divisionValueNode, maxElevatorHeight);
	}

	public MultiplicationValueNode getMaxHeightValueNode() {
		return maxHeightValueNode;
	}
}

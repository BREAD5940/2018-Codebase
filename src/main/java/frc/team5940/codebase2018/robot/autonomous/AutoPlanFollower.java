package frc.team5940.codebase2018.robot.autonomous;


import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

public class AutoPlanFollower extends ValueNode<AutonomousAction> {

	ValueNode<? extends AutonomousAction[]> autoPlanValueNode;

	AutonomousAction[] currentArray;

	int currentActionIndex = 0;

	public AutoPlanFollower(Network network, Logger logger, String label,
			ValueNode<? extends AutonomousAction[]> autoPlanValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoPlanValueNode);

		this.autoPlanValueNode = autoPlanValueNode;
	}

	@Override
	protected AutonomousAction updateValue() {
		if (!autoPlanValueNode.getValue().equals(this.currentArray)) {
			this.currentArray = autoPlanValueNode.getValue();
			this.currentActionIndex = 0;
		}
		AutonomousAction currentAction = this.currentArray[currentActionIndex];

		// TODO
		if (currentAction instanceof DeliverCubeAction) {

		} else if (currentAction instanceof MoveForwardAction) {

		} else if (currentAction instanceof TurnAction) {

		}

		return currentAction;
	}

}

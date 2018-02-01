package frc.team5940.codebase2018.robot.autonomous;


import java.util.List;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

public class AutoPlanFollower extends ValueNode<AutonomousAction> {

	ValueNode<? extends List<AutonomousAction>> autoPlanValueNode;

	List<AutonomousAction> autonomousPath;

	int currentActionIndex = 0;

	MoveForwardAction driveForward = new MoveForwardAction(11); 
	public AutoPlanFollower(Network network, Logger logger, String label,
			ValueNode<? extends List<AutonomousAction>> autoPlanValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoPlanValueNode);

		this.autoPlanValueNode = autoPlanValueNode;
	}

	@Override
	protected AutonomousAction updateValue() {
		if (!autoPlanValueNode.getValue().equals(this.autonomousPath)) {
			this.autonomousPath = autoPlanValueNode.getValue();
			this.currentActionIndex = 0;
		}
		AutonomousAction currentAction = this.autonomousPath.get(currentActionIndex);

		// TODO
		if (currentAction instanceof DeliverCubeAction) {

		} else if (currentAction instanceof MoveForwardAction) {

		} else if (currentAction instanceof TurnAction) {

		}

		return currentAction;
	}
	
	public void autoLinePath1(){
		autonomousPath.add(driveForward); 
	}

}

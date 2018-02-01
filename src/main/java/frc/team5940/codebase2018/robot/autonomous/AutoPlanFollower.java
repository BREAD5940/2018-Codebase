package frc.team5940.codebase2018.robot.autonomous;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.functional.TimerValueNode;

public class AutoPlanFollower extends ValueNode<AutonomousAction> {

	ValueNode<? extends AutonomousAction[]> autoPlanValueNode;

	AutonomousAction[] currentArray;

	int currentActionIndex = 0;

	ValueNode<? extends Double> robotDistanceValueNode;

	ValueNode<? extends Double> robotAngleValueNode;

	double targetDistance;

	double targetAngle;

	TimerValueNode timerValueNode;

	public AutoPlanFollower(Network network, Logger logger, String label,
			ValueNode<? extends AutonomousAction[]> autoPlanValueNode, TimerValueNode timer,
			ValueNode<? extends Double> robotDistanceValueNode, ValueNode<? extends Double> robotAngleValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoPlanValueNode, timer, robotDistanceValueNode, robotAngleValueNode);

		this.robotAngleValueNode = robotAngleValueNode;
		this.robotDistanceValueNode = robotDistanceValueNode;
		this.timerValueNode = timer;
		this.autoPlanValueNode = autoPlanValueNode;
	}

	// This is done poorly. Should fix.
	@Override
	protected AutonomousAction updateValue() {
		if (!autoPlanValueNode.getValue().equals(this.currentArray)) {
			this.currentArray = autoPlanValueNode.getValue();
			this.currentActionIndex = -1;
			nextAction();
		}
		AutonomousAction currentAction = this.currentArray[currentActionIndex];

		if (currentAction instanceof DeliverCubeAction) {
			if (timerValueNode.getValue()) {
				currentAction = nextAction();
			}
		} else if (currentAction instanceof MoveForwardAction) {
			if (robotDistanceValueNode.getValue() < targetDistance + 0.05
					&& robotDistanceValueNode.getValue() > targetDistance - 0.05) {
				currentAction = nextAction();
			}
		} else if (currentAction instanceof TurnAction) {
			if (robotAngleValueNode.getValue() < targetAngle + 2 && robotAngleValueNode.getValue() > targetAngle - 2) {
				currentAction = nextAction();
			}
		}

		return currentAction;
	}

	private AutonomousAction nextAction() {
		this.currentActionIndex++;
		AutonomousAction nextAction = this.currentArray[currentActionIndex];
		if (nextAction instanceof DeliverCubeAction) {
			this.timerValueNode.setTimer(nextAction.getValue());
		} else if (nextAction instanceof MoveForwardAction) {
			this.targetDistance = this.robotDistanceValueNode.getValue() + nextAction.getValue();
		} else if (nextAction instanceof TurnAction) {
			this.targetAngle = this.robotAngleValueNode.getValue() + nextAction.getValue();
		}
		return nextAction;
	}
}

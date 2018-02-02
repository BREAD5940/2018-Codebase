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
	
	public enum AutoPathsLeft{
		AUTO_LINE(new MoveForwardAction(11)), 
		PLACE_SWITCH(new MoveForwardAction(14), new TurnAction(90), new MoveForwardAction(1), new DeliverCubeAction(1)), 
		PLACE_SCALE(new MoveForwardAction(27), new TurnAction(90), new MoveForwardAction(1), new DeliverCubeAction(7));

		AutoPathsLeft(AutonomousAction... actions) {
			
		}  
	}
	//TODO differentiate between 90 l and r 
	public enum AutoPathsCenter{
		AUTO_LINE(new MoveForwardAction(11)), 
		PLACE_SWITCH_LEFT(new MoveForwardAction(4),/*left*/ new TurnAction(90), new MoveForwardAction(4.5),/*right*/ new TurnAction(6), new DeliverCubeAction(1)), 
		PLACE_SWITCH_RIGHT(new MoveForwardAction(4),/*right*/ new TurnAction(90), new MoveForwardAction(4.5),/*left*/ new TurnAction(6), new DeliverCubeAction(1));  
		;

		AutoPathsCenter(AutonomousAction... actions) {
			
		}  
	//TODO MAKE AUTOPATHSRIGHT MY DUDE	
	
	}
			
}

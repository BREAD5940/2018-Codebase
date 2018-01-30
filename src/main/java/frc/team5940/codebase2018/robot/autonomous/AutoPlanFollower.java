package frc.team5940.codebase2018.robot.autonomous;

import java.util.List;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower.AutoAction;

public class AutoPlanFollower extends ValueNode<Enum<? extends AutoAction>> {
	
	ValueNode<List<AutoAction>> autoPlanValueNode;
	
	public AutoPlanFollower(Network network, Logger logger, String label, ValueNode<List<AutoAction>> autoPlanValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoPlanValueNode);
		
		this.autoPlanValueNode = autoPlanValueNode;
	}

	public enum AutoAction {
		DELIVER_CUBE, MOVE_10_FEET, TURN_LEFT_90_DEGREES, TURN_RIGHT_90_DEGREES, MOVE_3_FEET
	}

	@Override
	protected AutoAction updateValue() {
		// TODO Auto-generated method stub
		return null;
	}

}

package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;

public class ClampAutonomousControllerValueNode extends ValueNode<DoubleSolenoid.Value>{

	public ClampAutonomousControllerValueNode(Network network, Logger logger, String label, ValueNode<? extends AutoAction> autoActionValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoActionValueNode);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Value updateValue() {
		// TODO Auto-generated method stub
		return null;
	}

}

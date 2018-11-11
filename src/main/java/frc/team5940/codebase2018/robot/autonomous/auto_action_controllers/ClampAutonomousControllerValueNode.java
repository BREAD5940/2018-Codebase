package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import frc.team5940.codebase2018.robot.autonomous.auto_actions.ClampAutoAction;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;

public class ClampAutonomousControllerValueNode extends ValueNode<DoubleSolenoid.Value> {

	ValueNode<? extends AutoAction> autoActionValueNode;
	DoubleSolenoid.Value solenoidValue = Value.kReverse;

	public ClampAutonomousControllerValueNode(Network network, Logger logger, String label, ValueNode<?
					extends AutoAction> autoActionValueNode)
					throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoActionValueNode);

		this.autoActionValueNode = autoActionValueNode;
	}

	@Override
	protected Value updateValue() {
		if (this.autoActionValueNode.getValue() instanceof ClampAutoAction) {
			this.solenoidValue = ((ClampAutoAction) this.autoActionValueNode.getValue()).getSolenoidValue();
		}
		return this.solenoidValue;
	}

}

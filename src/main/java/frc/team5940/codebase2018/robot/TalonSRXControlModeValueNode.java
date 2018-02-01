package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode.RobotState;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.team5940.codebase2018.robot.autonomous.AutonomousAction;
import frc.team5940.codebase2018.robot.autonomous.MoveForwardAction;
import frc.team5940.codebase2018.robot.autonomous.TurnAction;

/**
 * This ValueNode will return the current ControlMode of the drivetrain talons
 * based on the current robot state and what the current autonomous action is.
 * The value will return Velocity if it is not during autonomous. If it is
 * autonomous and the robot is to move forward then this returns positional
 * control if turning the ControlMode is PercentOutput. If neither this returns
 * Velocity.
 * 
 * @author Michael Bentley
 *
 */
public class TalonSRXControlModeValueNode extends ValueNode<ControlMode> {

	/**
	 * The current state of the robot.
	 */
	RobotStateValueNode robotState;

	/**
	 * The current action of the robot for autonomous.
	 */
	ValueNode<? extends AutonomousAction> currentActionValueNode;

	/**
	 * Creates a new TalonSRXModeValueNode.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' label.
	 * @param robotState
	 *            The current state of the robot.
	 * @param currentActionValueNode
	 *            The current autonomous action.
	 */
	public TalonSRXControlModeValueNode(Network network, Logger logger, String label, RobotStateValueNode robotState,
			ValueNode<? extends AutonomousAction> currentActionValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, robotState, currentActionValueNode);
		this.robotState = robotState;
		this.currentActionValueNode = currentActionValueNode;
	}

	@Override
	protected ControlMode updateValue() {
		if (robotState.getValue() == RobotState.AUTONOMOUS) {
			if (currentActionValueNode.getValue() instanceof MoveForwardAction) {
				return ControlMode.Position;
			} else if (currentActionValueNode.getValue() instanceof TurnAction) {
				return ControlMode.PercentOutput;
			}
		}
		return ControlMode.Velocity;
	}

}

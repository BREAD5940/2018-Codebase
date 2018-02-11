package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * This determines the current profile slot of the talons based on what gear the
 * robot is in and what the current {@link ControlMode} is.
 * 
 * @author Michael Bentley
 *
 */
public class DrivetrainTalonSRXParameterSlotValueNode extends ValueNode<Integer> {

	/**
	 * The current ControlMode of the drivetrain talons.
	 */
	ValueNode<? extends ControlMode> controlModeValueNode;

	/**
	 * The current shifting state of the talons.
	 */
	ValueNode<? extends DoubleSolenoid.Value> solenoidStateValueNode;

	/**
	 * Creates a new {@link DrivetrainTalonSRXParameterSlotValueNode}.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param controlModeValueNode
	 *            The current {@link ControlMode} of the talons.
	 * @param solenoidStateValueNode
	 *            The current state of the shifting solenoids.
	 */
	public DrivetrainTalonSRXParameterSlotValueNode(Network network, Logger logger, String label,
			ValueNode<? extends ControlMode> controlModeValueNode,
			ValueNode<? extends DoubleSolenoid.Value> solenoidStateValueNode) {
		super(network, logger, label, solenoidStateValueNode, controlModeValueNode);

		this.controlModeValueNode = controlModeValueNode;
		this.solenoidStateValueNode = solenoidStateValueNode;
	}

	@Override
	protected Integer updateValue() {
		if (controlModeValueNode.getValue() == ControlMode.Position) {
			return RobotConfig.LOW_GEAR_POSITION_SLOT_ID;
		} else if (solenoidStateValueNode.getValue() == Value.kForward) {
			return RobotConfig.HIGH_GEAR_VELOCITY_SLOT_ID;
		}
		return RobotConfig.LOW_GEAR_VELOCITY_SLOT_ID;
	}
}

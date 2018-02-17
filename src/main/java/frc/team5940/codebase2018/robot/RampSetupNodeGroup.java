package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.LoggingUtils;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.NodeGroup;
import org.team5940.pantry.processing_network.functional.AndValueNode;
import org.team5940.pantry.processing_network.functional.LatchingBooleanValueNode;
import org.team5940.pantry.processing_network.wpilib.input.HIDButtonValueNode;
import org.team5940.pantry.processing_network.wpilib.systems.BooleanToDoubleSolenoidValueNode;
import edu.wpi.first.wpilibj.Joystick;

/**
 * 00 Node group for creating and using the ramps on the robot
 * 
 * @author Paul Dowd
 *
 */

public class RampSetupNodeGroup extends NodeGroup {
	/**
	 * The button for extending the wheel on the ramp
	 */
	HIDButtonValueNode wheelButtonValueNode;
	/**
	 * The button for lowering the ramp
	 */
	HIDButtonValueNode rampButtonValueNode;
	/**
	 * The valueNode for making the ramp button a constant so that whether it has
	 * been pressed can be remembered
	 */
	LatchingBooleanValueNode rampButtonConstantValueNode;
	/**
	 * The node for checking whether the ramp is down, using
	 * rampButtonConstantValueNode, to make sure it's safe to extend the wheel
	 */
	AndValueNode wheelAndRampValueNode;
	/**
	 * The node that converts the ramp boolean to a double solenoid value so it can
	 * be read by the piston
	 */
	BooleanToDoubleSolenoidValueNode rampButtonConstantValueNodeToSolenoid;
	/**
	 * The node that converts the wheel boolean to a double solenoid value so it can
	 * be read by the piston
	 */
	BooleanToDoubleSolenoidValueNode wheelButtonValueNodeToSolenoid;

	/**
	 * The constructor that gives all the variables values
	 * 
	 * @param network:
	 *            the node network
	 * @param logger:
	 *            the logger
	 * @param label:
	 *            a label
	 * @param wheelJoystick:
	 *            a joystick for the wheel
	 * @param rampJoystick:
	 *            a joystick for the ramp
	 * @param rampButton:
	 *            a button for lowering the ramp, can be changed
	 * @param wheelButton:
	 *            a button for activating the wheel, can be changed
	 */

	public RampSetupNodeGroup(Network network, Logger logger, String label, Joystick wheelJoystick,
			Joystick rampJoystick, int rampButton, int wheelButton) {
		super(network, logger, label);

		LoggingUtils.checkArgument(rampJoystick);
		LoggingUtils.checkArgument(wheelJoystick);

		this.wheelButtonValueNode = new HIDButtonValueNode(network, logger, "WHEEL BUTTON", wheelJoystick, wheelButton);
		this.rampButtonValueNode = new HIDButtonValueNode(network, logger, "RAMP BUTTON", rampJoystick, rampButton);

		this.rampButtonConstantValueNode = new LatchingBooleanValueNode(network, logger, "lEFT Button Stat",
				rampButtonValueNode);

		this.wheelAndRampValueNode = new AndValueNode(network, logger, label, rampButtonConstantValueNode,
				wheelButtonValueNode);

		this.rampButtonConstantValueNodeToSolenoid = new BooleanToDoubleSolenoidValueNode(network, logger, label,
				rampButtonConstantValueNode);
		this.wheelButtonValueNodeToSolenoid = new BooleanToDoubleSolenoidValueNode(network, logger, label,
				wheelAndRampValueNode);

	}

	/**
	 * returns the value of the wheel button double solenoid
	 */

	public BooleanToDoubleSolenoidValueNode getWheelPistonValue() {
		return this.wheelButtonValueNodeToSolenoid;
	}

	/**
	 * returns the value of the ramp button double solenoid
	 */

	public BooleanToDoubleSolenoidValueNode getRampPistonValue() {
		return this.rampButtonConstantValueNodeToSolenoid;
	}

}
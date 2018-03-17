package frc.team5940.codebase2018.robot.autonomous;

import java.util.HashMap;
import java.util.Map;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.NodeGroup;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.functional.MultiplexerValueNode;
import org.team5940.pantry.processing_network.wpilib.output.NumberSmartDashboardNode;
import org.team5940.pantry.processing_network.wpilib.systems.encoder_conversion.MeasurementToEncoderNodeGroup;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5940.codebase2018.robot.RobotConfig;
import frc.team5940.codebase2018.robot.autonomous.auto_action_controllers.DrivetrainAutonomousControllerValueNode;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;

/**
 * Creates a new AutoDrivetrainController to allow control of the robot
 * depending on the current {@link AutoAction}. This is because the
 * {@link ControlMode} of the robot is different while the robot is turning from
 * while it is moving forward so only one of them has to be converted to the
 * corresponding encoder value. One of them is controlled with PID and the other
 * is PercentOutput so only one action should be converted.
 * 
 * @author Michael Bentley
 *
 */
public class AutoDrivetrainControllerNodeGroup extends NodeGroup {

	/**
	 * The current value the motors should be set to for auto.
	 */
	MultiplexerValueNode<Double, ControlMode> autoController;

	/**
	 * Creates a new {@link AutoDrivetrainControllerNodeGroup}
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param isLeftTalons
	 *            if this controls the left talons of the robot.
	 * @param planFollower
	 *            The ValueNode that returns the current AutoAction in the plan.
	 * @param controlMode
	 *            The ValueNode that determines the ControlMode of the drivetrain.
	 * @param talonPositionValueNode
	 *            The position of the corresponding talons.
	 * @param gyroAngleValueNode
	 *            The angle in degrees this robot is facing.
	 */
	public AutoDrivetrainControllerNodeGroup(Network network, Logger logger, String label, boolean isLeftTalons,
			ValueNode<? extends AutoAction> planFollower, ValueNode<ControlMode> controlMode,
			ValueNode<? extends Double> talonPositionValueNode, ValueNode<? extends Double> gyroAngleValueNode) {
		super(network, logger, label);

		DrivetrainAutonomousControllerValueNode driveAutoController = new DrivetrainAutonomousControllerValueNode(
				network, logger, "Left Drive Auto Controller", isLeftTalons, planFollower, talonPositionValueNode,
				gyroAngleValueNode);

		MeasurementToEncoderNodeGroup autoMeasurementToEncoderNodeGroup = new MeasurementToEncoderNodeGroup(network,
				logger, "Left Auto Measurement To Encoder", driveAutoController, RobotConfig.WHEEL_DIAMETER,
				RobotConfig.POSITION_PULSES_PER_ROTATION);

		Map<ControlMode, ValueNode<? extends Double>> drivetrainAutoMap = new HashMap<>();
		drivetrainAutoMap.put(ControlMode.Position, autoMeasurementToEncoderNodeGroup.getEncoderPulsesValueNode());
		drivetrainAutoMap.put(ControlMode.PercentOutput, driveAutoController);
		

		this.autoController = new MultiplexerValueNode<Double, ControlMode>(network, logger, "Auto Drive Speed",
				controlMode, drivetrainAutoMap, 0d);
		
		// TODO
		new NumberSmartDashboardNode(network, logger, "Label", true, "Set Talon Position/Speed", this.autoController);
	}

	/**
	 * Gets the ValueNode that determines the auto speed of the robot.
	 * 
	 * @return The ValueNode that determines the auto speed of the robot.
	 */
	public MultiplexerValueNode<Double, ControlMode> getAutoController() {
		return autoController;
	}
}

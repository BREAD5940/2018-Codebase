package frc.team5940.codebase2018.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.loggers.PrintStreamLogger;
import org.team5940.pantry.logging.messages.Message;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.ctre.input.TalonSRXEncoderPositionValueNode;
import org.team5940.pantry.processing_network.ctre.output.TalonSRXNode;
import org.team5940.pantry.processing_network.functional.AndValueNode;
import org.team5940.pantry.processing_network.functional.ConstantValueNode;
import org.team5940.pantry.processing_network.functional.MultiplexerValueNode;
import org.team5940.pantry.processing_network.functional.basic_arithmetic.MultiplicationValueNode;
import org.team5940.pantry.processing_network.functional.numeric_adjustment.BoundingValueNode;
import org.team5940.pantry.processing_network.wpilib.input.*;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode.RobotState;
import org.team5940.pantry.processing_network.wpilib.output.BooleanSmartDashboardNode;
import org.team5940.pantry.processing_network.wpilib.output.DoubleSolenoidNode;
import org.team5940.pantry.processing_network.wpilib.output.ObjectSmartDashboardNode;
import org.team5940.pantry.processing_network.wpilib.output.NumberSmartDashboardNode;
import org.team5940.pantry.processing_network.wpilib.systems.ArcadeDriveNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.ShiftingNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.encoder_conversion.EncoderToMeasurementNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.encoder_conversion.MeasurementToEncoderNodeGroup;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.autonomous.AutoDrivetrainControllerNodeGroup;
import frc.team5940.codebase2018.robot.autonomous.AutoPathSelect;
import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower;
import frc.team5940.codebase2018.robot.autonomous.auto_action_controllers.ElevatorAutonomousControllerValueNode;
import frc.team5940.codebase2018.robot.autonomous.auto_action_controllers.IntakeAutonomousControllerValueNode;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;

public class Robot extends IterativeRobot {

	@Override
	public void disabledInit() {
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void robotInit() {

		CameraServer.getInstance().startAutomaticCapture();

		// LOGGER SETUP
		Logger logger;
		// Logger logger = new Logger() {
		// @Override
		// public void log(Message message) {
		//
		// }
		// };
		try {
			logger = new PrintStreamLogger(new PrintStream(new File("/home/lvuser/log.json")));
		} catch (FileNotFoundException e) {
			logger = new Logger() {
				@Override
				public void log(Message message) {

				}
			};
			e.printStackTrace();
		}

		// GENERAL SETUP
		Network network = new Network(50000, logger);

		Joystick primaryJoystick = new Joystick(0);
		Joystick secondaryJoystick = new Joystick(1);

		RobotStateValueNode robotStateValueNode = new RobotStateValueNode(network, logger, "Robot State", this);

		new ObjectSmartDashboardNode(network, logger, "State", true, "Robot State", robotStateValueNode);

		// DRIVETRAIN TALON SETUP
		TalonSRX slaveLeft = new TalonSRX(RobotConfig.SLAVE_LEFT_TALON_PORT);
		TalonSRX masterLeft = new TalonSRX(RobotConfig.MASTER_LEFT_TALON_PORT);
		TalonSRX slaveRight = new TalonSRX(RobotConfig.SLAVE_RIGHT_TALON_PORT);
		TalonSRX masterRight = new TalonSRX(RobotConfig.MASTER_RIGHT_TALON_PORT);

		masterRight.setSelectedSensorPosition(0, 0, 0);
		masterLeft.setSelectedSensorPosition(0, 0, 0);

		slaveRight.set(ControlMode.Follower, masterRight.getDeviceID());
		slaveLeft.set(ControlMode.Follower, masterLeft.getDeviceID());

		masterLeft.setInverted(true);
		slaveLeft.setInverted(true);

		masterRight.config_kP(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_P, 0);
		masterRight.config_kI(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_I, 0);
		masterRight.config_kD(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_D, 0);
		masterRight.config_kF(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_F, 0);

		masterLeft.config_kP(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_P, 0);
		masterLeft.config_kI(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_I, 0);
		masterLeft.config_kD(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_D, 0);
		masterLeft.config_kF(0, RobotConfig.NEW_ROBOT_LOW_GEAR_POSITION_F, 0);

		// ELEVATOR TALON SETUP
		TalonSRX elevatorTalon = new TalonSRX(RobotConfig.MASTER_ELEVATOR_TALON_PORT);

		elevatorTalon.config_kP(0, RobotConfig.ELEVATOR_TALON_P, 0);
		elevatorTalon.config_kI(0, RobotConfig.RAISE_ELEVATOR_TALON_I, 0);
		elevatorTalon.config_kD(0, RobotConfig.RAISE_ELEVATOR_TALON_D, 0);
		elevatorTalon.config_kF(0, RobotConfig.RAISE_ELEVATOR_TALON_F, 0);

		elevatorTalon.setSensorPhase(true);
		elevatorTalon.configVoltageCompSaturation(11, 0);
		elevatorTalon.enableVoltageCompensation(true);
		elevatorTalon.setSelectedSensorPosition(0, 0, 0);

		// DRIVETRAIN MEASUREMENT NODE SETUP
		TalonSRXEncoderPositionValueNode rTalonPosition = new TalonSRXEncoderPositionValueNode(network, logger,
				"Right Talon Encoder Position", masterRight);

		EncoderToMeasurementNodeGroup rTalonEncoderMeasurementNodeGroup = new EncoderToMeasurementNodeGroup(network,
				logger, "Encoder to Measurement Node Group", rTalonPosition, RobotConfig.POSITION_PULSES_PER_ROTATION,
				RobotConfig.WHEEL_DIAMETER);

		TalonSRXEncoderPositionValueNode lTalonPosition = new TalonSRXEncoderPositionValueNode(network, logger,
				"Right Talon Encoder Position", masterLeft);

		EncoderToMeasurementNodeGroup lTalonEncoderMeasurementNodeGroup = new EncoderToMeasurementNodeGroup(network,
				logger, "Encoder to Measurement Node Group", lTalonPosition, RobotConfig.POSITION_PULSES_PER_ROTATION,
				RobotConfig.WHEEL_DIAMETER);

		new NumberSmartDashboardNode(network, logger, "Right Encoder Position Dashboard",
				RobotConfig.RIGHT_ENCODER_POSITION_SMARTDASHBOARD_REQUIRE_UPDATE, "Right Encoder Position",
				rTalonEncoderMeasurementNodeGroup.getMeasurementValueNode());

		new NumberSmartDashboardNode(network, logger, "Left Encoder Position Dashboard",
				RobotConfig.RIGHT_ENCODER_POSITION_SMARTDASHBOARD_REQUIRE_UPDATE, "Left Encoder Position",
				lTalonEncoderMeasurementNodeGroup.getMeasurementValueNode());

		// ROBOT GYRO NODE SETUP
		ADXRS450_Gyro gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);
		gyro.reset();
		GyroAngleValueNode gyroAngleValueNode = new GyroAngleValueNode(network, logger, "Gyro Angle", gyro);

		new NumberSmartDashboardNode(network, logger, "Gyro Angle Dashboard",
				RobotConfig.ROBOT_ANGLE_SMARTDASHBOARD_REQUIRE_UPDATE, "Angle", gyroAngleValueNode);

		// ELEVATOR NODE MEASUREMENT SETUP
		TalonSRXEncoderPositionValueNode elevatorPosition = new TalonSRXEncoderPositionValueNode(network, logger,
				"Elevator Encoder Position", elevatorTalon);

		EncoderToMeasurementNodeGroup elevatorMeasurementNodeGroup = new EncoderToMeasurementNodeGroup(network, logger,
				"Elevator Measurement Node Group", elevatorPosition, RobotConfig.POSITION_PULSES_PER_ROTATION,
				RobotConfig.ELEVATOR_SPROCKET_DIAMETER);

		MultiplicationValueNode elevatorHeightValueNode = new MultiplicationValueNode(network, logger,
				"Elevator Actual Height", elevatorMeasurementNodeGroup.getMeasurementValueNode(), 2);

		new NumberSmartDashboardNode(network, logger, "Elevator Height Dashboard",
				RobotConfig.ELEVATOR_CURRENT_HEIGHT_SMARTDASHBOARD_REQUIRE_UPDATE, "Elevator Height", elevatorPosition);

		// AUTO PATH SELECT NODE SETUP
		FMSGameMessageValueNode gameMessage = new FMSGameMessageValueNode(network, logger, "Game Data");
		AutoPathSelect autoPathSelectValueNode = new AutoPathSelect(network, logger, "Auto Path Selector", gameMessage,
				rTalonEncoderMeasurementNodeGroup.getMeasurementValueNode(), gyroAngleValueNode,
				elevatorHeightValueNode);

		new ObjectSmartDashboardNode(network, logger, "Auto Path Dashboard",
				RobotConfig.AUTO_PATH_SMARTDASHBOARD_REQUIRE_UPDATE, "Auto Path", autoPathSelectValueNode);

		// BASE AUTO SETUP
		AutoPlanFollower planFollower = new AutoPlanFollower(network, logger, "Auto Plan Follower",
				autoPathSelectValueNode, robotStateValueNode);

		new ObjectSmartDashboardNode(network, logger, "Current Action Dashboard",
				RobotConfig.CURRENT_ACTION_SMARTDASHBOARD_REQUIRE_UPDATE, "Auto Action", planFollower);

		// SHIFTING SETUP
		DoubleSolenoid drivetrainShiftingSolenoid = new DoubleSolenoid(RobotConfig.SOLENOID_MODULE_NUMBER,
				RobotConfig.DRIVETRAIN_SOLENOID_SHIFTING_FORWARD_CHANNEL,
				RobotConfig.DRIVETRAIN_SOLENOID_SHIFTING_REVERSE_CHANNEL);

		ShiftingNodeGroup drivetrainShiftingNodeGroup = new ShiftingNodeGroup(network, logger, "Shifting Node Group",
				primaryJoystick, RobotConfig.SHIFT_UP_BUTTON, RobotConfig.SHIFT_DOWN_BUTTON, Value.kForward);

		new ObjectSmartDashboardNode(network, logger, "Piston State Smart Dashboard",
				RobotConfig.DRIVETRAIN_SHIFTING_SMARTDASHBOARD_REQUIRE_UPDATE, "Piston State",
				drivetrainShiftingNodeGroup.getSolenoidController());

		new DoubleSolenoidNode(network, logger, "Drivetrain Shifting", RobotConfig.DRIVETRAIN_SHIFTING_REQUIRE_UPDATE,
				drivetrainShiftingNodeGroup.getSolenoidController(), drivetrainShiftingSolenoid);

		// DRIVETRAIN NODE SETUP
		DrivetrainTalonSRXControlModeValueNode controlMode = new DrivetrainTalonSRXControlModeValueNode(network, logger,
				"Drivetrain Control Mode", robotStateValueNode, planFollower);

		new ObjectSmartDashboardNode(network, logger, "Drivetrain Control Mode",
				RobotConfig.DRIVETRAIN_CONTROLMODE_SMARTDASHBOARD_REQUIRE_UPDATE, "Drivetrain ControlMode",
				controlMode);

		HIDAxisValueNode forwardAxisValueNode = new HIDAxisValueNode(network, logger, "Forward Drivetrain Axis",
				primaryJoystick, RobotConfig.DRIVETRAIN_FORWARD_AXIS, RobotConfig.DRIVETRAIN_FORWARD_AXIS_INVERTED);
		HIDAxisValueNode yawAxisValueNode = new HIDAxisValueNode(network, logger, "Yaw Drivetrain Axis",
				primaryJoystick, RobotConfig.DRIVETRAIN_YAW_AXIS, RobotConfig.DRIVETRAIN_YAW_AXIS_INVERTED);

		ArcadeDriveNodeGroup arcadeDriveNodeGroup = new ArcadeDriveNodeGroup(network, logger, "Drive Train Arcade",
				forwardAxisValueNode, yawAxisValueNode);

		RobotSpeedElevatorHeightScalingValueNode percentSpeed = new RobotSpeedElevatorHeightScalingValueNode(network,
				logger, "Robot Percent", RobotConfig.INITIAL_SPEED_SCALING_PERCENT_HEIGHT,
				RobotConfig.MAX_ELEVATOR_HEIGHT_MAX_SPEED, elevatorHeightValueNode);

		new NumberSmartDashboardNode(network, logger, "Percent Speed", true, "Percent Speed", percentSpeed);

		MultiplicationValueNode leftSpeed = new MultiplicationValueNode(network, logger, "Left Output Speed",
				percentSpeed, arcadeDriveNodeGroup.getLeftMotorValueNode());

		MultiplicationValueNode rightSpeed = new MultiplicationValueNode(network, logger, "Right Output Speed",
				percentSpeed, arcadeDriveNodeGroup.getRightMotorValueNode());

		AutoDrivetrainControllerNodeGroup lAutoDrivetrainNodeGroup = new AutoDrivetrainControllerNodeGroup(network,
				logger, "Left Auto Drivetrain", true, planFollower, controlMode,
				lTalonEncoderMeasurementNodeGroup.getMeasurementValueNode(), gyroAngleValueNode);
		AutoDrivetrainControllerNodeGroup rAutoDrivetrainNodeGroup = new AutoDrivetrainControllerNodeGroup(network,
				logger, "Right Auto Drivetrain", false, planFollower, controlMode,
				rTalonEncoderMeasurementNodeGroup.getMeasurementValueNode(), gyroAngleValueNode);

		MultiplexerValueNode<Double, RobotState> leftDriveSpeed = generateAutonMultiplexerValueNode(network, logger,
				"Left Drivetrain Multiplexer", robotStateValueNode, lAutoDrivetrainNodeGroup.getAutoController(),
				leftSpeed);
		MultiplexerValueNode<Double, RobotState> rightDriveSpeed = generateAutonMultiplexerValueNode(network, logger,
				"Right Drivetrain Multiplexer", robotStateValueNode, rAutoDrivetrainNodeGroup.getAutoController(),
				rightSpeed);

		new ObjectSmartDashboardNode(network, logger, "Drivetrain Control Mode Smartdashboard",
				RobotConfig.DRIVETRAIN_CONTROLMODE_SMARTDASHBOARD_REQUIRE_UPDATE, "Drivetrain Control Mode",
				controlMode);

		new NumberSmartDashboardNode(network, logger, "Left Drivetrain Talon Smartdashboard",
				RobotConfig.LEFT_DRIVETRAIN_TALONS_SMARTDASHBOARD_REQUIRE_UPDATE, "Left Drivetrain Talons",
				leftDriveSpeed);

		new NumberSmartDashboardNode(network, logger, "Right Drivetrain Talon Smartdashboard",
				RobotConfig.RIGHT_DRIVETRAIN_TALONS_SMARTDASHBOARD_REQUIRE_UPDATE, "Right Drivetrain Talons",
				rightDriveSpeed);

		new TalonSRXNode(network, logger, "Right Talon", RobotConfig.DRIVETRAIN_TALONS_REQUIRE_UPDATE, controlMode,
				rightDriveSpeed, masterRight);
		new TalonSRXNode(network, logger, "Left Talon", RobotConfig.DRIVETRAIN_TALONS_REQUIRE_UPDATE, controlMode,
				leftDriveSpeed, masterLeft);

		// ELEVATOR NODE SETUP
		HIDAxisValueNode elevatorAxis = new HIDAxisValueNode(network, logger, "Elvator Axis", secondaryJoystick,
				RobotConfig.ELEVATOR_CONTROL_AXIS, RobotConfig.ELEVATOR_AXIS_INVERTED);

		ElevatorNodeGroup elevatorNodeGroup = new ElevatorNodeGroup(network, logger, "Elevator Node Group",
				elevatorAxis, RobotConfig.MAX_ELEVATOR_HEIGHT);

		ElevatorAutonomousControllerValueNode elevatorAutoControllerValueNode = new ElevatorAutonomousControllerValueNode(
				network, logger, "Elevator Auto Controller", planFollower);

		MultiplexerValueNode<Double, RobotState> elevatorValueNode = generateAutonMultiplexerValueNode(network, logger,
				"Elevator Multiplexer", robotStateValueNode, elevatorAutoControllerValueNode,
				elevatorNodeGroup.getSetHeightValueNode());

		BoundingValueNode boundElevatorValueNode = new BoundingValueNode(network, logger, "Bounded Elevator Height",
				elevatorValueNode, 0, RobotConfig.MAX_ELEVATOR_HEIGHT);

		MeasurementToEncoderNodeGroup encoderElevatorNodeGroup = new MeasurementToEncoderNodeGroup(network, logger,
				"Elevator Measurement Node Group", boundElevatorValueNode, RobotConfig.ELEVATOR_SPROCKET_DIAMETER,
				RobotConfig.POSITION_PULSES_PER_ROTATION);

		ConstantValueNode<ControlMode> elevatorControlMode = new ConstantValueNode<ControlMode>(network, logger,
				"Elevator Control Mode", ControlMode.Position);

		new NumberSmartDashboardNode(network, logger, "Elevator Talon Smartdashboard",
				RobotConfig.ELEVATOR_TALON_SMARTDASHBOARD_REQUIRE_UPDATE, "Elevator Talon",
				encoderElevatorNodeGroup.getEncoderPulsesValueNode());

		new TalonSRXNode(network, logger, "Elevator Talon", RobotConfig.ELEVATOR_TALON_REQUIRE_UPDATE,
				elevatorControlMode, encoderElevatorNodeGroup.getEncoderPulsesValueNode(), elevatorTalon);

		// INTAKE CLAMP SETUP
		DoubleSolenoid intakeClampShiftingSolenoid = new DoubleSolenoid(RobotConfig.SOLENOID_MODULE_NUMBER,
				RobotConfig.INTAKE_SOLENOID_FORWARD_CHANNEL, RobotConfig.INTAKE_SOLENOID_REVERSE_CHANNEL);

		ShiftingNodeGroup intakeClampNodeGroup = new ShiftingNodeGroup(network, logger, "Intake Clamp Node Group",
				secondaryJoystick, RobotConfig.INTAKE_CLAMP_BUTTON, RobotConfig.INTAKE_UNCLAMP_BUTTON, Value.kReverse);

		new ObjectSmartDashboardNode(network, logger, "Intake Clamp State Smartdashboard",
				RobotConfig.INTAKE_PNEUMATICS_SMARTDASHBOARD_REQUIRE_UPDATE, "Intake Clamp Piston State",
				intakeClampNodeGroup.getSolenoidController());

		new DoubleSolenoidNode(network, logger, "Intake Clamp Solenoid", RobotConfig.INTAKE_PNEUMATICS_REQUIRE_UPDATE,
				intakeClampNodeGroup.getSolenoidController(), intakeClampShiftingSolenoid);

		// INTAKE TALON SETUP
		TalonSRX rightIntakeTalon = new TalonSRX(RobotConfig.RIGHT_INTAKE_TALON_PORT);
		TalonSRX leftIntakeTalon = new TalonSRX(RobotConfig.LEFT_INTAKE_TALON_PORT);

		leftIntakeTalon.setInverted(true);
		rightIntakeTalon.setInverted(true);

		// CUBE INTAKE SENSOR
		DigitalInputValueNode leftDigitalInputValueNode = new DigitalInputValueNode(network, logger, "Digital Input",
				new DigitalInput(RobotConfig.RIGHT_INTAKE_SENSOR_DIGITAL_INPUT_PORT),
				RobotConfig.INTAKE_SENSORS_INVERTED);
		DigitalInputValueNode rightDigitalInputValueNode = new DigitalInputValueNode(network, logger, "Digital Input",
				new DigitalInput(RobotConfig.LEFT_INTAKE_SENSOR_DIGITAL_INPUT_PORT),
				RobotConfig.INTAKE_SENSORS_INVERTED);

		AndValueNode cubeIntakedValueNode = new AndValueNode(network, logger, "Cube Intook Value Node",
				leftDigitalInputValueNode, rightDigitalInputValueNode);

		new BooleanSmartDashboardNode(network, logger, "Cube Sensed Smartdahsboard",
				RobotConfig.CUBE_INTAKED_SMARTDASHBOARD_REQUIRE_UPDATE, "Cube Intaked", cubeIntakedValueNode);

		// INTAKE MOTOR NODE SETUP
		HIDAxisValueNode cubeIntakeForwardAxis = new HIDAxisValueNode(network, logger, "Cube Intake Axis",
				secondaryJoystick, RobotConfig.INTAKE_CONTROL_FORWARD_AXIS,
				RobotConfig.CUBE_INTAKE_FORWARD_AXIS_INVERTED);

		HIDAxisValueNode cubeIntakeYawAxis = new HIDAxisValueNode(network, logger, "Cube Intake Axis",
				secondaryJoystick, RobotConfig.INTAKE_CONTROL_YAW_AXIS, RobotConfig.CUBE_INTAKE_YAW_AXIS_INVERTED);

		ArcadeDriveNodeGroup intakeArcadeDrive = new ArcadeDriveNodeGroup(network, logger, "Node Group",
				cubeIntakeForwardAxis, cubeIntakeYawAxis);

		HIDButtonValueNode slowOuttakeButtonValueNode = new HIDButtonValueNode(network, logger, "Slow Outtake", secondaryJoystick, RobotConfig.SLOW_OUTTAKE_BUTTON);

		IntakeValueNode leftIntakeOperatorSpeed = new IntakeValueNode(network, logger, "Left Operator Intake",
				intakeArcadeDrive.getLeftMotorValueNode(), slowOuttakeButtonValueNode);
		IntakeValueNode rightIntakeOperatorSpeed = new IntakeValueNode(network, logger, "Right Operator Intake",
				intakeArcadeDrive.getRightMotorValueNode(), slowOuttakeButtonValueNode);

		IntakeAutonomousControllerValueNode autoIntakeController = new IntakeAutonomousControllerValueNode(network,
				logger, "Intake Auto Controller", planFollower);

		MultiplexerValueNode<? extends Number, RobotState> leftIntakeValueNode = generateAutonMultiplexerValueNode(
				network, logger, "Left Intake Auto Multiplexer", robotStateValueNode, autoIntakeController,
				leftIntakeOperatorSpeed);

		MultiplexerValueNode<? extends Number, RobotState> rightIntakeValueNode = generateAutonMultiplexerValueNode(
				network, logger, "Right Intake Auto Multiplexer", robotStateValueNode, autoIntakeController,
				rightIntakeOperatorSpeed);

		ConstantValueNode<ControlMode> intakeControlMode = new ConstantValueNode<ControlMode>(network, logger,
				"Intake Control Mode", ControlMode.PercentOutput);

		new NumberSmartDashboardNode(network, logger, "Left Intake Talon Smartdashboard",
				RobotConfig.LEFT_INTAKE_TALON_SMARTDASHBOARD_REQUIRE_UPDATE, "Left Intake Talon", leftIntakeValueNode);

		new NumberSmartDashboardNode(network, logger, "Right Intake Talon Smartdashboard",
				RobotConfig.RIGHT_INTAKE_TALON_SMARTDASHBOARD_REQUIRE_UPDATE, "Right Intake Talon",
				rightIntakeValueNode);

		new TalonSRXNode(network, logger, "Right Intake Talon", RobotConfig.INTAKE_TALONS_REQUIRE_UPDATE,
				intakeControlMode, rightIntakeValueNode, rightIntakeTalon);

		new TalonSRXNode(network, logger, "Left Intake Talon", RobotConfig.INTAKE_TALONS_REQUIRE_UPDATE,
				intakeControlMode, leftIntakeValueNode, leftIntakeTalon);

		network.start();
	}

	public static <T> MultiplexerValueNode<T, RobotState> generateAutonMultiplexerValueNode(Network network,
			Logger logger, String label, RobotStateValueNode robotStateValueNode, ValueNode<? extends T> autonValueNode,
			ValueNode<? extends T> teleopValueNode) {
		Map<RobotState, ValueNode<? extends T>> map = new HashMap<>();
		map.put(RobotState.AUTONOMOUS, autonValueNode);
		map.put(RobotState.OPERATOR_CONTROL, teleopValueNode);
		MultiplexerValueNode<T, RobotState> multiplexer = new MultiplexerValueNode<T, RobotState>(network, logger,
				label, robotStateValueNode, map, teleopValueNode);
		return multiplexer;
	}

	@Override
	public void teleopInit() {

	}

	@Override
	public void testInit() {
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void testPeriodic() {
	}
}
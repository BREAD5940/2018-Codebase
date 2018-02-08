package frc.team5940.codebase2018.robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.loggers.PrintStreamLogger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.ctre.output.TalonSRXNode;
import org.team5940.pantry.processing_network.functional.ConstantValueNode;
import org.team5940.pantry.processing_network.functional.MultiplexerValueNode;
import org.team5940.pantry.processing_network.wpilib.input.HIDAxisValueNode;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode.RobotState;
import org.team5940.pantry.processing_network.wpilib.output.DoubleSolenoidNode;
import org.team5940.pantry.processing_network.wpilib.systems.MaxSpeedValueNode;
import org.team5940.pantry.processing_network.wpilib.systems.ShiftingNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.VelocityControlNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.controller_layouts.XBoxControllerLayout;
import org.team5940.pantry.processing_network.wpilib.systems.encoder_conversion.MeasurementToEncoderValueNode;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {

	TalonSRX slaveLeft = new TalonSRX(1);
	TalonSRX masterLeft = new TalonSRX(2);
	TalonSRX slaveRight = new TalonSRX(3);
	TalonSRX masterRight = new TalonSRX(4);

	Joystick primaryJoystick = new Joystick(0);
	Joystick secondaryJoystick = new Joystick(1);

	public static final boolean ROBOT_WORKS = false; 
	Network network;

	// Logger logger = new PrintStreamLogger(System.out);

	// private float lP = 0.5f;
	// private float lI = 0.5f;
	// private float lD = 0.5f;

	final float ciabattaHighGearMaxVelocity = 9.1f;
	final float ciabattaLowGearMaxVelocity = 6.7f;

	float maxVelocity = ciabattaHighGearMaxVelocity;

	private final float wheelDiameter = 3.5f / 12;

	@Override
	public void disabledInit() {
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void robotInit() {
		Logger logger = null;
		try {
			logger = new PrintStreamLogger(new PrintStream(new File("/home/lvuser/log.json")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		masterRight.setSelectedSensorPosition(0, 0, 0);
		masterLeft.setSelectedSensorPosition(0, 0, 0);
		network = new Network(10000, logger);

		// SHIFTING SETUP
		DoubleSolenoid drivetrainShiftingSolenoid = new DoubleSolenoid(RobotConfig.SOLENOID_MODULE_NUMBER,
				RobotConfig.DRIVETRAIN_SOLENOID_SHIFTING_FORWARD_CHANNEL,
				RobotConfig.DRIVETRAIN_SOLENOID_SHIFTING_REVERSE_CHANNEL);

		ShiftingNodeGroup drivetrainShiftingNodeGroup = new ShiftingNodeGroup(network, logger, "Shifting Node Group",
				primaryJoystick, XBoxControllerLayout.Buttons.RB_BUTTON, XBoxControllerLayout.Buttons.LB_BUTTON,
				drivetrainShiftingSolenoid, Value.kReverse);

		new DoubleSolenoidNode(network, logger, "Drivetrain Shifting", true,
				drivetrainShiftingNodeGroup.getSolenoidController(), drivetrainShiftingSolenoid);

		// DRIVETRAIN TALON SETUP
		slaveRight.set(ControlMode.Follower, masterRight.getDeviceID());
		slaveLeft.set(ControlMode.Follower, masterLeft.getDeviceID());

		masterLeft.setInverted(true);
		slaveLeft.setInverted(true);

		masterRight.config_kP(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_P, 0);
		masterRight.config_kI(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_I, 0);
		masterRight.config_kD(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_D, 0);
		masterRight.config_kF(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_F, 0);

		masterLeft.config_kP(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_P, 0);
		masterLeft.config_kI(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_I, 0);
		masterLeft.config_kD(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_D, 0);
		masterLeft.config_kF(0, RobotConfig.CIABATTA_LOW_GEAR_VELOCITY_F, 0);

		// DRIVETRAIN NODE SETUP
		MaxSpeedValueNode maxSpeed = new MaxSpeedValueNode(network, logger,
				drivetrainShiftingNodeGroup.getSolenoidController(), Value.kForward, ciabattaLowGearMaxVelocity,
				ciabattaHighGearMaxVelocity);

		VelocityControlNodeGroup encoderSpeed = new VelocityControlNodeGroup(network, logger, "Drivetrain",
				primaryJoystick, XBoxControllerLayout.Axis.LEFT_JOYSTICK_Y, XBoxControllerLayout.Axis.RIGHT_JOYSTICK_X,
				0.01, 0.01, maxSpeed, wheelDiameter, RobotConfig.VELOCITY_PULSES_PER_ROTATION);

		ConstantValueNode<ControlMode> controlMode = new ConstantValueNode<ControlMode>(network, logger,
				"Drivetrain Control Mode", ControlMode.Velocity);

		new TalonSRXNode(network, logger, "Right Talon", true, controlMode, encoderSpeed.getRightEncoderValue(),
				masterRight);
		new TalonSRXNode(network, logger, "Left Talon", true, controlMode, encoderSpeed.getLeftEncoderValue(),
				masterLeft);

		// ELEVATOR TALON SETUP
		TalonSRX masterElevatorTalon = new TalonSRX(RobotConfig.MASTER_ELEVATOR_TALON_PORT);
		TalonSRX slaveElevatorTalon = new TalonSRX(RobotConfig.SLAVE_ELEVATOR_TALON_PORT);

		slaveElevatorTalon.set(ControlMode.Follower, RobotConfig.MASTER_ELEVATOR_TALON_PORT);

		masterElevatorTalon.config_kP(0, RobotConfig.ELEVATOR_TALON_P, 0);
		masterElevatorTalon.config_kI(0, RobotConfig.ELEVATOR_TALON_I, 0);
		masterElevatorTalon.config_kD(0, RobotConfig.ELEVATOR_TALON_D, 0);
		masterElevatorTalon.config_kF(0, RobotConfig.ELEVATOR_TALON_F, 0);

		// ELEVATOR NODE SETUP
		HIDAxisValueNode elevatorAxis = new HIDAxisValueNode(network, logger, "Elvator Axis", secondaryJoystick,
				XBoxControllerLayout.Axis.LEFT_JOYSTICK_Y);

		ElevatorNodeGroup elevatorNodeGroup = new ElevatorNodeGroup(network, logger, "Elevator Node Group",
				elevatorAxis, RobotConfig.MAX_ELEVATOR_HEIGHT);

		MeasurementToEncoderValueNode encoderElevatorNodeGroup = new MeasurementToEncoderValueNode(network, logger,
				"Elevator Measurement Node Group", elevatorNodeGroup.getMaxHeightValueNode(),
				RobotConfig.ELEVATOR_CONTROL_SHAFT_DIAMETER, RobotConfig.POSITION_PULSES_PER_ROTATION);

		ConstantValueNode<ControlMode> elevatorControlMode = new ConstantValueNode<ControlMode>(network, logger,
				"Elevator Control Mode", ControlMode.Position);
		// TODO Bounds need to be put on the inputs to the motor node to that it doesn't
		// try to go past endstops...
		new TalonSRXNode(network, logger, "Elevator Talon", true, elevatorControlMode,
				encoderElevatorNodeGroup.getEncoderPulsesValueNode(), masterElevatorTalon);

		// INTAKE CLAMP SETUP
		DoubleSolenoid intakeClampShiftingSolenoid = new DoubleSolenoid(RobotConfig.SOLENOID_MODULE_NUMBER,
				RobotConfig.INTAKE_SOLENOID_FORWARD_CHANNEL, RobotConfig.INTAKE_SOLENOID_REVERSE_CHANNEL);

		ShiftingNodeGroup intakeClampNodeGroup = new ShiftingNodeGroup(network, logger, "Intake Clamp Node Group",
				secondaryJoystick, XBoxControllerLayout.Buttons.RB_BUTTON, XBoxControllerLayout.Buttons.LB_BUTTON,
				intakeClampShiftingSolenoid, Value.kReverse);

		new DoubleSolenoidNode(network, logger, "Intake Clamp Solenoid", true,
				intakeClampNodeGroup.getSolenoidController(), intakeClampShiftingSolenoid);

		// INTAKE TALON SETUP
		TalonSRX intakeMasterTalon = new TalonSRX(RobotConfig.RIGHT_INTAKE_TALON_DEVICE_NUMBER);
		TalonSRX intakeSlaveTalon = new TalonSRX(RobotConfig.LEFT_INTAKE_TALON_DEVICE_NUMBER);

		intakeSlaveTalon.setInverted(true);

		intakeSlaveTalon.set(ControlMode.Follower, RobotConfig.RIGHT_INTAKE_TALON_DEVICE_NUMBER);

		// INTAKE MOTOR NODE
		HIDAxisValueNode cubeIntakeAxis = new HIDAxisValueNode(network, logger, "Cube Intake Axis", secondaryJoystick,
				XBoxControllerLayout.Axis.RIGHT_JOYSTICK_Y);

		ConstantValueNode<ControlMode> intakeControlMode = new ConstantValueNode<ControlMode>(network, logger,
				"Intake Control Mode", ControlMode.PercentOutput);

		new TalonSRXNode(network, logger, "Intake Talon", true, intakeControlMode, cubeIntakeAxis, intakeMasterTalon);
		network.start();

		// IN PROGRESS AUTO CODE
		// TODO Add auto code.
		// RobotStateValueNode robotState = new RobotStateValueNode(network, logger,
		// "Robot State", this);
		// ValueNode<Double> autonLeftDrivetrainValueNode = null;
		// ValueNode<Double> autonRightDrivetrainValueNode = null;
		//
		// ValueNode<Double> defaultRobotSpeed = new ConstantValueNode<Double>(network,
		// logger, "Default Robot Speed", 0d);
		//
		// MultiplexerValueNode<Double, RobotState> leftRobotSpeed =
		// Robot.<Double>generateAutonMultiplexerValueNode(
		// network, logger, "Left Drivetrain Multiplexer", robotState,
		// autonLeftDrivetrainValueNode,
		// encoderSpeed.getLeftEncoderValue(), defaultRobotSpeed);
		//
		// MultiplexerValueNode<Double, RobotState> rightRobotSpeed =
		// Robot.<Double>generateAutonMultiplexerValueNode(
		// network, logger, "Right Drivetrain Multiplexer", robotState,
		// autonRightDrivetrainValueNode,
		// encoderSpeed.getRightEncoderValue(), defaultRobotSpeed);
		//
		// AutonomousAction[] autoPlanActions = { new MoveForwardAction(14, 3), new
		// DeliverCubeAction(3) };
		//
		// ConstantValueNode<AutonomousAction[]> autoPlan = new
		// ConstantValueNode<AutonomousAction[]>(network, logger,
		// "Auto Plan", autoPlanActions);
		//
		// TalonSRXEncoderPositionValueNode rightEncoderPosition = new
		// TalonSRXEncoderPositionValueNode(network, logger,
		// "Right Talon Position", masterRight);
		// TalonSRXEncoderPositionValueNode leftEncoderPosition = new
		// TalonSRXEncoderPositionValueNode(network, logger,
		// "Left Talon Position", masterLeft);
		//
		// EncoderToMeasurementNodeGroup rightEncoderMeasurementNodeGroup = new
		// EncoderToMeasurementNodeGroup(network,
		// logger, "Right Encoder Measurement", rightEncoderPosition,
		// distancePulsesPerRotation, wheelDiameter);
		// EncoderToMeasurementNodeGroup leftEncoderMeasurementNodeGroup = new
		// EncoderToMeasurementNodeGroup(network,
		// logger, "Left Encoder Measurement", leftEncoderPosition,
		// distancePulsesPerRotation, wheelDiameter);
		//
		// AutoPlanFollower currentActionNode = new AutoPlanFollower(network, logger,
		// "Auto Plan Follower", autoPlan, new TimerValueNode(network, logger, "Auto
		// Plan Timer"), );
		//
		// TalonSRXControlModeValueNode controlMode = new
		// TalonSRXControlModeValueNode(network, logger,
		// "Drive Train Control Mode", robotState, currentActionNode);
	}

	public static <T> MultiplexerValueNode<T, RobotState> generateAutonMultiplexerValueNode(Network network,
			Logger logger, String label, RobotStateValueNode robotStateValueNode, ValueNode<T> autonValueNode,
			ValueNode<T> teleopValueNode, ValueNode<? extends T> defaultValueNode) {
		Map<Enum<? extends RobotState>, ValueNode<? extends T>> map = new HashMap<>();
		map.put(RobotState.AUTONOMOUS, autonValueNode);
		map.put(RobotState.OPERATOR_CONTROL, teleopValueNode);
		MultiplexerValueNode<T, RobotState> multiplexer = new MultiplexerValueNode<T, RobotState>(network, logger,
				label, robotStateValueNode, map, defaultValueNode);
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
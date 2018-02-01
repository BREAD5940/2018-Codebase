package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.loggers.PrintStreamLogger;
import org.team5940.pantry.logging.messages.Message;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.ctre.input.TalonSRXEncoderPositionValueNode;
import org.team5940.pantry.processing_network.ctre.input.TalonSRXEncoderVelocityValueNode;
import org.team5940.pantry.processing_network.ctre.output.TalonSRXNode;
import org.team5940.pantry.processing_network.data_flow.MultiplexerValueNodeTest.TestEnum;
import org.team5940.pantry.processing_network.functional.ChangeDetectorNode;
import org.team5940.pantry.processing_network.functional.ConstantValueNode;
import org.team5940.pantry.processing_network.functional.DeadzoneValueNode;
import org.team5940.pantry.processing_network.functional.MaxValueValueNode;
import org.team5940.pantry.processing_network.functional.MultiplexerValueNode;
import org.team5940.pantry.processing_network.wpilib.input.HIDAxisValueNode;
import org.team5940.pantry.processing_network.wpilib.input.HIDButtonValueNode;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode;
import org.team5940.pantry.processing_network.wpilib.input.RobotStateValueNode.RobotState;
import org.team5940.pantry.processing_network.wpilib.output.DoubleSolenoidNode;
import org.team5940.pantry.processing_network.wpilib.output.SmartDashboardNode;
import org.team5940.pantry.processing_network.wpilib.systems.ArcadeDriveNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.MaxSpeedValueNode;
import org.team5940.pantry.processing_network.wpilib.systems.ShiftingNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.SolenoidTwoBooleanControllerValueNode;
import org.team5940.pantry.processing_network.wpilib.systems.VelocityControlNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.controller_layouts.XBoxControllerLayout;
import org.team5940.pantry.processing_network.wpilib.systems.encoder_conversion.EncoderToMeasurementNodeGroup;
import org.team5940.pantry.processing_network.wpilib.systems.encoder_conversion.MeasurementToEncoderValueNode;
import org.team5940.pantry.processing_network.functional.MultiplicationValueNode;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower;
import frc.team5940.codebase2018.robot.autonomous.AutonomousAction;
import frc.team5940.codebase2018.robot.autonomous.DeliverCubeAction;
import frc.team5940.codebase2018.robot.autonomous.MoveForwardAction;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Robot extends IterativeRobot {

	TalonSRX slaveLeft = new TalonSRX(1);
	TalonSRX masterLeft = new TalonSRX(2);
	TalonSRX slaveRight = new TalonSRX(3);
	TalonSRX masterRight = new TalonSRX(4);

	DoubleSolenoid shiftingSolenoid;

	Joystick joystick = new Joystick(0);
	Network network;

	// Logger logger = new PrintStreamLogger(System.out);

	private float ciabattaLowGearPositionP = 0.5f;
	private float ciabattaLowGearPositionI = 0f;
	private float ciabattaLowGearPositionD = 20f;
	private float ciabattaLowGearPositionF = 0f;

	private float ciabattaLowGearVelocityP = 0.9f;
	private float ciabattaLowGearVelocityI = 0.0025f;
	private float ciabattaLowGearVelocityD = 40f;
	private float ciabattaLowGearVelocityF = 0.15f;

	// private float lP = 0.5f;
	// private float lI = 0.5f;
	// private float lD = 0.5f;

	final float ciabattaHighGearMaxVelocity = 9.1f;
	final float ciabattaLowGearMaxVelocity = 6.7f;

	float maxVelocity = ciabattaHighGearMaxVelocity;

	private final float wheelDiameter = 3.5f / 12;

	private final float velocityPulsesPerRotation = 409.6f;
	private final float distancePulsesPerRotation = 4096f;

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

		this.shiftingSolenoid = new DoubleSolenoid(9, 6, 7);

		masterRight.setSelectedSensorPosition(0, 0, 0);
		network = new Network(10000, logger);

		// SHIFTING
		ShiftingNodeGroup shifting = new ShiftingNodeGroup(network, logger, "Shifting Node Group", joystick,
				XBoxControllerLayout.Buttons.RB_BUTTON, XBoxControllerLayout.Buttons.LB_BUTTON, shiftingSolenoid,
				Value.kReverse);

		MaxSpeedValueNode maxSpeed = new MaxSpeedValueNode(network, logger, shifting.getSolenoidController(),
				Value.kForward, ciabattaLowGearMaxVelocity, ciabattaHighGearMaxVelocity);

		// TALON SETUP
		slaveRight.set(ControlMode.Follower, masterRight.getDeviceID());
		slaveLeft.set(ControlMode.Follower, masterLeft.getDeviceID());

		masterLeft.setInverted(true);
		slaveLeft.setInverted(true);

		masterRight.config_kP(0, ciabattaLowGearVelocityP, 0);
		masterRight.config_kI(0, ciabattaLowGearVelocityI, 0);
		masterRight.config_kD(0, ciabattaLowGearVelocityD, 0);
		masterRight.config_kF(0, ciabattaLowGearVelocityF, 0);

		masterLeft.config_kP(0, ciabattaLowGearVelocityP, 0);
		masterLeft.config_kI(0, ciabattaLowGearVelocityI, 0);
		masterLeft.config_kD(0, ciabattaLowGearVelocityD, 0);
		masterLeft.config_kF(0, ciabattaLowGearVelocityF, 0);

		// DRIVETRAIN
		VelocityControlNodeGroup encoderSpeed = new VelocityControlNodeGroup(network, logger, "Drivetrain", joystick,
				XBoxControllerLayout.Axis.LEFT_JOYSTICK_Y, XBoxControllerLayout.Axis.RIGHT_JOYSTICK_X, 0.01, 0.01,
				maxSpeed, wheelDiameter, velocityPulsesPerRotation);

		RobotStateValueNode robotState = new RobotStateValueNode(network, logger, "Robot State", this);

		// TODO Placeholder. 
		ValueNode<Double> autonLeftDrivetrainValueNode = null;
		ValueNode<Double> autonRightDrivetrainValueNode = null;

		ValueNode<Double> defaultRobotSpeed = new ConstantValueNode<Double>(network, logger, "Default Robot Speed", 0d);

		MultiplexerValueNode<Double, RobotState> leftRobotSpeed = Robot.<Double>generateAutonMultiplexerValueNode(
				network, logger, "Left Drivetrain Multiplexer", robotState, autonLeftDrivetrainValueNode,
				encoderSpeed.getLeftEncoderValue(), defaultRobotSpeed);

		MultiplexerValueNode<Double, RobotState> rightRobotSpeed = Robot.<Double>generateAutonMultiplexerValueNode(
				network, logger, "Right Drivetrain Multiplexer", robotState, autonRightDrivetrainValueNode,
				encoderSpeed.getRightEncoderValue(), defaultRobotSpeed);
		
		// TODO Placeholder. 
		AutonomousAction[] autoPlanActions = {new MoveForwardAction(14, 3), new DeliverCubeAction(3)};
		
		ConstantValueNode<AutonomousAction[]> autoPlan = new ConstantValueNode<AutonomousAction[]>(network, logger, "Auto Plan", autoPlanActions);
		
		AutoPlanFollower currentActionNode = new AutoPlanFollower(network, logger, "Auto Plan Follower", autoPlan);
		
		TalonSRXControlModeValueNode controlMode = new TalonSRXControlModeValueNode(network, logger, "Drive Train Control Mode", robotState, currentActionNode);
		
		new TalonSRXNode(network, logger, "Right Talon", true, controlMode, rightRobotSpeed, masterRight);
		new TalonSRXNode(network, logger, "Left Talon", true, controlMode, leftRobotSpeed, masterLeft);

		network.start();
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
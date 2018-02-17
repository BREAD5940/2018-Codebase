package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.loggers.PrintStreamLogger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.wpilib.output.ObjectSmartDashboardNode;
import org.team5940.pantry.processing_network.wpilib.systems.controller_layouts.XBoxControllerLayout;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {
	@Override
	public void robotInit() {
		Logger logger = new PrintStreamLogger(System.out);
		Network network = new Network(20000, logger);
		Joystick joystick = new Joystick(0);
		RampSetupNodeGroup rampNodeGroup = new RampSetupNodeGroup(network, logger, "Ramp", joystick, joystick,
				XBoxControllerLayout.Buttons.LB_BUTTON, XBoxControllerLayout.Buttons.RB_BUTTON);

		new ObjectSmartDashboardNode(network, logger, "Ramp Piston State", true, "Ramp Piston State",
				rampNodeGroup.getRampPistonValue());

		new ObjectSmartDashboardNode(network, logger, "Wheel Piston State", true, "Wheel Piston State",
				rampNodeGroup.getWheelPistonValue());

		network.start();
	}

	@Override
	public void disabledInit() {
	}

	@Override
	public void autonomousInit() {
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
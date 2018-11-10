package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An {@link AutoAction} for turning some number of pre-defined degrees.
 * 
 * @author Michael Bentley
 */
public class TurnAutoAction extends AutoAction {

	/**
	 * The degrees this action should turn the robot.
	 */
	double degrees;

	/**
	 * The target angle this robot should turn to.
	 */
	double targetAngle;

	/**
	 * The current angle of the robot in degrees.
	 */
	ValueNode<? extends Number> robotAngle;

	/**
	 * Creates a new {@link TurnAutoAction} which turns the robot a set number of
	 * degrees.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param degrees
	 *            The degrees this robot should turn.
	 * @param robotAngle
	 *            The ValueNode that returns the current angle of the robot in
	 *            degrees.
	 */
	public TurnAutoAction(Network network, Logger logger, String label, double degrees,
			ValueNode<? extends Number> robotAngle) throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, robotAngle);
		this.robotAngle = robotAngle;
		this.degrees = degrees;
	}

	@Override
	protected void setup() {
		this.targetAngle = this.robotAngle.getValue().doubleValue() + degrees;
		SmartDashboard.putNumber("Turn Auto Action Target: ", this.targetAngle);
		SmartDashboard.putNumber("Setup", System.currentTimeMillis());
	}

	@Override
	protected Boolean checkCompletion() {
		return withinMargin(this.robotAngle.getValue().doubleValue(), this.targetAngle, 3);//TODO set this back to 3
	}

	/**
	 * Gets the angle the robot should turn to in degrees
	 * 
	 * @return The angle the robot should turn to in degrees.
	 */
	public double getAngle() {
		return this.degrees;
	}
}

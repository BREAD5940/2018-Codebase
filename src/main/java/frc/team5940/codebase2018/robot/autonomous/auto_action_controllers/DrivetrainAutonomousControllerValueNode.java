package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.functional.ChangeDetectorValueNode;
import org.team5940.pantry.processing_network.wpilib.output.NumberSmartDashboardNode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.DriveAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.TurnAutoAction;

/**
 * Controls the Drivetrain of the robot during autonomous. Controls
 * {@link DriveAutoAction} and {@link TurnAutoAction}. This works with only one
 * side of the drivetrain so you need a new controller for each drivetrain side.
 * 
 * @author Michael Bentley
 *
 */
public class DrivetrainAutonomousControllerValueNode extends ChangeDetectorValueNode<AutoAction, Double> {

	/**
	 * The current action of the robot.
	 */
	ValueNode<? extends AutoAction> currentActionValueNode;

	/**
	 * The angle the robot is facing.
	 */
	ValueNode<? extends Number> gyroAngleValueNode;

	/**
	 * The feet the drivetrain has moved.
	 */
	ValueNode<? extends Number> drivetrainFeetValueNode;

	/**
	 * The target distance of the robot.
	 */
	double targetDistance = 0;

	/**
	 * The target angle the robot is trying to turn to.
	 */
	double targetAngle = 0;

	/**
	 * If the robot is driving or turning right now.
	 */
	boolean isDriving = false;

	/**
	 * If this controls the left or right talons on the drivetrain.
	 */
	boolean isLeftTalons;

	/**
	 * The P value for turning with the drivetrain.
	 */
	private final double p = 0.2;

	/**
	 * Creates a new {@link DrivetrainAutonomousControllerValueNode}.
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label
	 * @param isLeftTalons
	 *            If this is controlling the left talons of the drivetrain.
	 * @param currentActionValueNode
	 *            The current action in the autoplan.
	 * @param drivetrainFeetValueNode
	 *            The distance the corresponding side of the drivetrain has moved.
	 * @param gyroAngleValueNode
	 *            The current angle the robot is facing.
	 */
	public DrivetrainAutonomousControllerValueNode(Network network, Logger logger, String label, boolean isLeftTalons,
			ValueNode<? extends AutoAction> currentActionValueNode, ValueNode<? extends Number> drivetrainFeetValueNode,
			ValueNode<? extends Number> gyroAngleValueNode) throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, false, currentActionValueNode, gyroAngleValueNode, drivetrainFeetValueNode);

		this.currentActionValueNode = currentActionValueNode;
		this.isLeftTalons = isLeftTalons;
		this.gyroAngleValueNode = gyroAngleValueNode;
		this.drivetrainFeetValueNode = drivetrainFeetValueNode;
	}

	@Override
	protected void valueChanged(AutoAction newValue) {
		if (newValue instanceof DriveAutoAction) {
			this.targetDistance = this.drivetrainFeetValueNode.getValue().doubleValue()
					+ ((DriveAutoAction) newValue).getFeet();
			this.isDriving = true;
		} else if (newValue instanceof TurnAutoAction) {
			this.targetAngle = ((TurnAutoAction) newValue).getAngle() + this.gyroAngleValueNode.getValue().doubleValue();
			this.isDriving = false;
		}
	}

	@Override
	protected Double updateValue() {
		if (this.isDriving) {
			return this.targetDistance;
		} else {
			double offset = (targetAngle - this.gyroAngleValueNode.getValue().doubleValue());
			double speed;
			if (this.isLeftTalons) {
				speed = offset * this.p;
			} else {
				speed = -offset * this.p;
			}
			if (speed < -1 || speed > 1) {
				speed = speed / Math.abs(speed);
			}
			return speed;
		}
	}
}

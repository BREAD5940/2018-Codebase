package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

/**
 * An {@link AutoAction} for driving some number of pre-defined feet forward or
 * backward.
 * 
 * @author Michael Bentley
 */
public class DriveAutoAction extends AutoAction {

	/**
	 * The distance this should move.
	 */
	double feet;

	/**
	 * The target distance the talon should move.
	 */
	double targetDistance;

	/**
	 * The distance the talon has moved.
	 */
	ValueNode<? extends Number> distanceMoved;

	/**
	 * Creates a new {@link DriveAutoAction} which moves the robot a set distance.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label
	 * @param feet
	 *            The distance in feet this robot should move.
	 * @param distanceMoved
	 *            The ValueNode that returns the distance this Node has moved.
	 */
	public DriveAutoAction(Network network, Logger logger, String label, double feet,
			ValueNode<? extends Number> distanceMoved) {
		super(network, logger, label, distanceMoved);
		this.feet = feet;
		this.distanceMoved = distanceMoved;
	}

	@Override
	protected void setup() {
		this.targetDistance = this.distanceMoved.getValue().doubleValue() + feet;
	}

	@Override
	protected Boolean checkCompletion() {
		return withinMargin(this.distanceMoved.getValue().doubleValue(), this.targetDistance, 0.1);
	}

	/**
	 * Gets the distance this Action should move.
	 * 
	 * @return This distance in feet the Action says the robot should move.
	 */
	public double getFeet() {
		return this.feet;
	}
}

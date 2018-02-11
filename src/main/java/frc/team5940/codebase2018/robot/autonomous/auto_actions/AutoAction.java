package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.LoggingUtils;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import com.google.gson.JsonArray;

/**
 * This is an action that returns a Boolean that tells when the action is
 * complete. This Node should not be updated until the Action has started.
 * 
 * @author Michael Bentley
 *
 */
public abstract class AutoAction extends ValueNode<Boolean> {

	/**
	 * If this is the first time this action has been run.
	 */
	boolean isFirstRun = true;

	/**
	 * Creates a new {@link AutoAction}
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label.
	 * @param sourcesArray
	 *            The ValueNode this AutoAction relies on.
	 */
	public AutoAction(Network network, Logger logger, String label, ValueNode<?>... sourcesArray)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, LoggingUtils.chainPut(new JsonArray(), label), sourcesArray);
	}

	@Override
	protected Boolean updateValue() {
		if (isFirstRun) {
			this.setup();
			this.isFirstRun = false;
			return false;
		}
		return checkCompletion();
	}

	/**
	 * Sets up the different variables for when this action starts.
	 */
	protected abstract void setup();

	/**
	 * Returns if the AutoAction has been completed yet.
	 * 
	 * @return If this Action has been completed.
	 */
	protected abstract Boolean checkCompletion();

	/**
	 * Verifies that a number is within a margin of a target number.
	 * 
	 * @param number
	 *            The number to check the value of.
	 * @param target
	 *            The target number for the number.
	 * @param margin
	 *            The acceptable margin of error.
	 * @return If the number is within the margin of error of the target.
	 */
	public static boolean withinMargin(double number, double target, double margin) {
		return number < target + margin && number > target - margin;
	}
}

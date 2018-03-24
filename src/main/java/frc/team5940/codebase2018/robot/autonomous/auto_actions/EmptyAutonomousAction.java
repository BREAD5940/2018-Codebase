package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;

import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower;

/**
 * This action never completes and is used to signify the end of a set of
 * actions. You should not add this to an AutoPlan. This will automatically be
 * appended to the end of an AutoPlan so the {@link AutoPlanFollower} will not
 * crash after the final action is completed. No actions are done while this
 * action is running.
 * 
 * @author Michael Bentley
 *
 */
public class EmptyAutonomousAction extends AutoAction {

	/**
	 * Creates a new {@link EmptyAutonomousAction}.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 */
	public EmptyAutonomousAction(Network network, Logger logger, String label)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
	}

	@Override
	protected void setup() {
	}

	@Override
	protected Boolean checkCompletion() {
		return false;
	}

}

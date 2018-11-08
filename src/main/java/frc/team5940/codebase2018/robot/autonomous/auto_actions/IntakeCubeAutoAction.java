package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;

/**
 * This action causes the robot to Intake the cube. 
 * As of now it intakes for three seconds just like the outtake.
 * 
 * @author Julia Reid
 *
 */
public class IntakeCubeAutoAction extends AutoAction{
	/**
	 * The time left to intake the cube.
	 */
	double timeLeft;

	/**
	 * The previous time this Node was updated.
	 */
	long targetTime;

	/**
	 * Creates a new {@link IntakeCubeAutoAction} which completes after three
	 * seconds.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label
	 */
	public IntakeCubeAutoAction(Network network, Logger logger, String label)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
	}

	@Override
	protected void setup() {
		this.targetTime = System.currentTimeMillis() + 1000;
	}

	@Override
	protected Boolean checkCompletion() {
		return System.currentTimeMillis() > this.targetTime;
	}
}

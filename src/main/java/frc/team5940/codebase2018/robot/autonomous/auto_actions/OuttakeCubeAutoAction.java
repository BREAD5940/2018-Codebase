package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;

/**
 * This action causes the robot to outtake the cube. Currentley outtakes the
 * cube for three seconds but this will be updated.
 * 
 * @author Michael Bentley
 *
 */
public class OuttakeCubeAutoAction extends AutoAction {

	/**
	 * The time left to outtake the cube.
	 */
	double timeLeft;

	/**
	 * The previous time this Node was updated.
	 */
	long targetTime;

	/**
	 * Creates a new {@link OuttakeCubeAutoAction} which completes after three
	 * seconds.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label
	 */
	public OuttakeCubeAutoAction(Network network, Logger logger, String label)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
	}

	@Override
	protected void setup() {
		this.targetTime = System.currentTimeMillis() + 3000;
	}

	@Override
	protected Boolean checkCompletion() {
		return System.currentTimeMillis() > this.targetTime;
	}

}

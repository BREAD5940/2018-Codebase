package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class OpenClampAutoAction extends AutoAction {

	long targetTime = 100; 
	/**
	 * Creates a new {@link OpenClampAutoAction} which opens clamp.
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
	public OpenClampAutoAction(Network network, Logger logger, String label)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
		
		
	}

	@Override
	protected void setup() {
	
		//TODO check if this whol thing even works because I feel like it doesn't
		targetTime += System.currentTimeMillis();
		
	}

	@Override
	protected Boolean checkCompletion() {

		return System.currentTimeMillis() > this.targetTime;
	}


}

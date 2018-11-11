package frc.team5940.codebase2018.robot.autonomous.auto_actions;
 import edu.wpi.first.wpilibj.DoubleSolenoid;
import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
 /**
 * Created by Michael Bentley on 11/10/2018
 */
public class ClampAutoAction extends AutoAction {
 	DoubleSolenoid.Value solenoidValue;
	private boolean firstRun = true;
 	/**
	 * Creates a new {@link AutoAction}
	 *
	 * @param network       This' Network
	 * @param logger        This' Logger
	 * @param label         This' Label.
	 * @param solenoidValue The value to set the double solenoid to when this action is run.
	 */
	public ClampAutoAction(Network network, Logger logger, String label, DoubleSolenoid.Value solenoidValue) throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
		this.solenoidValue = solenoidValue;
	}
	
 	public DoubleSolenoid.Value getSolenoidValue() {
		return solenoidValue;
	}
 	
 	@Override
	protected void setup() {
		this.firstRun = true;
	}
 	@Override
	protected Boolean checkCompletion() {
		if (firstRun) {
			firstRun = false;
			return false;
		}
		return true;
	}
}
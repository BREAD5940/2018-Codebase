package frc.team5940.codebase2018.robot.autonomous.auto_actions;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.wpilib.output.DoubleSolenoidNode;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.RobotConfig;



public class ClampAutoAction extends AutoAction {
	
	
	int solenoidChannelOpen = 0;
	int solenoidChannelClosed = 6;
	private boolean clampState;
	
	
	public ClampAutoAction(Network network, Logger logger, String label, boolean clampState)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
		
		this.clampState = clampState;
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void setup() {
		if (this.clampState) {
			new DoubleSolenoidNode(network, logger, "Drivetrain Shifting", false, solenoidChannelOpen, solenoidChannelOpen);
		}
		if (this.clampState = false)  {
			new DoubleSolenoidNode(network, logger, "Drivetrain Shifting", false, 0, 0);
		}
			

	}

	@Override
	protected Boolean checkCompletion() {
		// TODO Auto-generated method stub
		return null;
	}

}

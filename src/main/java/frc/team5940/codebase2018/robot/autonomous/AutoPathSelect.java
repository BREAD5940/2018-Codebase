package frc.team5940.codebase2018.robot.autonomous;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.team5940.codebase2018.robot.autonomous.AutoPathSelect.AutoPaths;

public class AutoPathSelect extends ValueNode<Enum<? extends AutoPaths>> {

	public AutoPathSelect(Network network, Logger logger, ValueNode<Enum<? extends AutoPaths>>[] sourcesArray, String fmsReturn)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, sourcesArray);
		
		SendableChooser<RobotLocation> robotLoc = new SendableChooser<RobotLocation>(); 
		robotLoc.addDefault("Center", RobotLocation.CENTER);
		robotLoc.addObject("Left", RobotLocation.LEFT);
		robotLoc.addObject("Right", RobotLocation.RIGHT);
		// TODO Auto-generated constructor stub
	}

	public enum RobotLocation{
		LEFT, CENTER, RIGHT; 
	}
	
	public enum AutoPaths {
		AUTO_LINE(new MoveForwardAction(11)), 
		LEFT_PLACE_SWITCH(new MoveForwardAction(14), new TurnAction(90), new MoveForwardAction(1), new DeliverCubeAction(1)), 
		LEFT_PLACE_SCALE(new MoveForwardAction(27), new TurnAction(90), new MoveForwardAction(1), new DeliverCubeAction(7)),
		CENTER_PLACE_SWITCH_LEFT(new MoveForwardAction(4),/*left*/ new TurnAction(90), new MoveForwardAction(4.5),/*right*/ new TurnAction(6), new DeliverCubeAction(1)), 
		CENTER_PLACE_SWITCH_RIGHT(new MoveForwardAction(4),/*right*/ new TurnAction(90), new MoveForwardAction(4.5),/*left*/ new TurnAction(6), new DeliverCubeAction(1));

		AutoPaths(AutonomousAction... actions) {
			
		}	
		//TODO make the right side auto paths
	}
	
	

	@Override
	protected Enum<? extends AutoPaths> updateValue() {
		// TODO Auto-generated method stub
		return null;
	}
}

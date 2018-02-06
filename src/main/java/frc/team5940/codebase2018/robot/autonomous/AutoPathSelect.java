package frc.team5940.codebase2018.robot.autonomous;

import java.util.ArrayList;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.messages.events.WarningEventMessage;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5940.codebase2018.robot.autonomous.AutoPathSelect.AutoPath;
import frc.team5940.codebase2018.robot.autonomous.actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.DriveAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.ElevatorDependentAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.OuttakeCubeAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.TurnAutoAction;

public class AutoPathSelect extends ValueNode<Enum<? extends AutoPath>> {

	SendableChooser<RobotLocation> robotLoc;
	RobotLocation prevRobotLoc = null;
	
	public AutoPathSelect(Network network, Logger logger, ValueNode<Enum<? extends AutoPath>>[] sourcesArray, String fmsReturn)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, sourcesArray);
		
		robotLoc = new SendableChooser<RobotLocation>(); 
		robotLoc.addDefault("Center", RobotLocation.CENTER);
		robotLoc.addObject("Left", RobotLocation.LEFT);
		robotLoc.addObject("Right", RobotLocation.RIGHT);
		SmartDashboard.putData("Robot Location Data", robotLoc);
		
		// TODO Auto-generated constructor stub
	}

	public enum RobotLocation{
		LEFT, CENTER, RIGHT; 
	}
	
	public enum AutoPath {
		AUTO_LINE("Left AutoLine", RobotLocation.LEFT, new DriveAutoAction(11)), 
		LEFT_PLACE_SWITCH("Place Switch From Left", RobotLocation.LEFT,new DriveAutoAction(14), new TurnAutoAction(90), new DriveAutoAction(1), new OuttakeCubeAutoAction(ElevatorDependentAutoAction.ElevatorHeight.SWITCH)), 
		LEFT_PLACE_SCALE("Place Scale From Left",RobotLocation.LEFT, new DriveAutoAction(27), new TurnAutoAction(90), new DriveAutoAction(1), new OuttakeCubeAutoAction(ElevatorDependentAutoAction.ElevatorHeight.SCALE)),
		CENTER_PLACE_SWITCH_LEFT("Place Left Switch From Center", RobotLocation.CENTER, new DriveAutoAction(4),/*left*/ new TurnAutoAction(90), new DriveAutoAction(4.5),/*right*/ new TurnAutoAction(6), new OuttakeCubeAutoAction(ElevatorDependentAutoAction.ElevatorHeight.SWITCH)), 
		CENTER_PLACE_SWITCH_RIGHT("Place Right Switch From Center", RobotLocation.CENTER, new DriveAutoAction(4),/*right*/ new TurnAutoAction(90), new DriveAutoAction(4.5),/*left*/ new TurnAutoAction(6), new OuttakeCubeAutoAction(ElevatorDependentAutoAction.ElevatorHeight.SWITCH));
		
		AutoAction[] actions;
		String key; 
		RobotLocation roboLoc; 
		AutoPath( String key, RobotLocation roboLoc, AutoAction... actions) {
			this.actions = actions; 
			this.key = key; 
			this.roboLoc = roboLoc; 
			
		}	
		
		public String getKey() {
			return key;
		
		}
		
		public RobotLocation getRequiredRobotLocation() {
			return roboLoc;
		}
		public AutoAction[] getActions() {
			return actions;
		}
		//TODO make the right side auto paths
	}
	
	@Override
	public boolean requiresUpdate() {
		// TODO Auto-generated method stub
		return true;
		//TODO use michaels thing once he commits to githubsbssbsbusb
		
	}

	@Override
	protected Enum<? extends AutoPath> updateValue() {
		
		if(prevRobotLoc != this.robotLoc.getSelected()) {
			for(AutoPath path : AutoPath.values()) {
				if(path.getRequiredRobotLocation() == this.robotLoc.getSelected()) {
					SmartDashboard.putString(path.getKey(), "0"); 
				}else {
					SmartDashboard.putString(path.getKey(), "Disabled"); 
				}
			}
		}
		
		ArrayList<AutoPath> autoPaths = new ArrayList<AutoPath>(); 
		
		for(AutoPath path : AutoPath.values()) {
			try {
				if(path.getRequiredRobotLocation() == this.robotLoc.getSelected() && Integer.parseInt(SmartDashboard.getString(path.getKey(), "0")) != 0) {
					autoPaths.add(path); 
				}
			}catch (NumberFormatException e) {
				this.logger.log(new WarningEventMessage(this, e));
			}
		}

		
		if(robotLoc.getSelected() == RobotLocation.CENTER) {
			
		}else if(robotLoc.getSelected() == RobotLocation.LEFT) {
			
		}else if(robotLoc.getSelected() == RobotLocation.RIGHT) {
			
		}

		prevRobotLoc = this.robotLoc.getSelected();
		// TODO Auto-generated method stub
		return null;
	}
}

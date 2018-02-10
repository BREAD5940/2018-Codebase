package frc.team5940.codebase2018.robot.autonomous;

import java.util.ArrayList;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.messages.events.WarningEventMessage;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.wpilib.input.FMSGameMessageValueNode;

import com.google.gson.JsonArray;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5940.codebase2018.robot.autonomous.AutoPathSelect.AutoPath;
import frc.team5940.codebase2018.robot.autonomous.actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.DriveAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.OuttakeCubeAutoAction;
import frc.team5940.codebase2018.robot.autonomous.actions.TurnAutoAction;

public class AutoPathSelect extends ValueNode<Enum<? extends AutoPath>> {

	SendableChooser<RobotLocation> robotLoc;
	RobotLocation prevRobotLoc = null;
	ArrayList<AutoPath> autoPaths = new ArrayList<AutoPath>(); 
	public static final boolean ROBOT_AUTONOMOUS_WORKS = false;  
	FMSGameMessageValueNode fmsReturn;
	
	public AutoPathSelect(Network network, Logger logger, JsonArray label, FMSGameMessageValueNode fmsReturn )
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label);
		
		robotLoc = new SendableChooser<RobotLocation>(); 
		robotLoc.addDefault("Center", RobotLocation.CENTER);
		robotLoc.addObject("Left", RobotLocation.LEFT);
		robotLoc.addObject("Right", RobotLocation.RIGHT);
		SmartDashboard.putData("Robot Location Data", robotLoc);
		this.fmsReturn = fmsReturn; 
		// TODO Auto-generated constructor stub
	}

	
	public enum RobotLocation{
		LEFT, CENTER, RIGHT; 
	}
	
	public enum AutoPath {

		AUTO_LINE("Left AutoLine", "XXX", RobotLocation.LEFT, new DriveAutoAction(11)), 
		LEFT_PLACE_SWITCH("Place Switch From Left","LXX", RobotLocation.LEFT,new DriveAutoAction(14), new TurnAutoAction(90), new DriveAutoAction(1), new OuttakeCubeAutoAction()), 
		LEFT_PLACE_SCALE("Place Scale From Left","XLX", RobotLocation.LEFT, new DriveAutoAction(27), new TurnAutoAction(90), new DriveAutoAction(1), new OuttakeCubeAutoAction()),
		RIGHT_PLACE_SWITCH("Place Switch From Right","RXX", RobotLocation.RIGHT,new DriveAutoAction(14), new TurnAutoAction(-90), new DriveAutoAction(1), new OuttakeCubeAutoAction()), 
		RIGHT_PLACE_SCALE("Place Scale From Right","XLX",RobotLocation.RIGHT, new DriveAutoAction(27), new TurnAutoAction(-90), new DriveAutoAction(1), new OuttakeCubeAutoAction()),
		CENTER_PLACE_SWITCH_LEFT("Place Left Switch From Center","LXX", RobotLocation.CENTER, new DriveAutoAction(4),/*left*/ new TurnAutoAction(90), new DriveAutoAction(4.5),/*right*/ new TurnAutoAction(6), new OuttakeCubeAutoAction()), 
		CENTER_PLACE_SWITCH_RIGHT("Place Right Switch From Center","RXX", RobotLocation.CENTER, new DriveAutoAction(4),/*right*/ new TurnAutoAction(90), new DriveAutoAction(4.5),/*left*/ new TurnAutoAction(6), new OuttakeCubeAutoAction());
		//TODO: Fix elevator Args
		
		

		AutoAction[] actions;
		String key; 
		RobotLocation roboLoc; 
		String fieldRex;
		AutoPath( String key, String fieldRex, RobotLocation roboLoc, AutoAction... actions) {
			this.actions = actions; 
			this.key = key; 
			this.roboLoc = roboLoc; 
			this.fieldRex = fieldRex; 
		}	
		
		public String getFieldRex() {
			return fieldRex;
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
	
	public FMSGameMessageValueNode getFmsReturn() {
		return fmsReturn;
	}
	
	public boolean pathWorks(String fms, String path) {
		for(int i  = 0; i < fms.length(); i++) {
			if(fms.charAt(i) != path.charAt(i) && path.charAt(i) != 'X'){
				return false; 
			}
		}
		return true; 
	}
	
	
	public AutoPath[] prioritySort(ArrayList<AutoPath> paths) {
		AutoPath[] auto = new AutoPath[paths.size()]; 
		int largestPriorIndex = 0; 
		
		for(int i = 0; i < auto.length; i++) {
			
			for(int j = 0; j < paths.size(); j++) {
				
				if(Integer.parseInt(SmartDashboard.getString(paths.get(j).getKey(), "0")) > Integer.parseInt(SmartDashboard.getString(paths.get(largestPriorIndex).getKey(), "0")) ) {
					
					largestPriorIndex = j; 
				}
				
				
			}
			auto[i] = paths.get(largestPriorIndex); 
			paths.remove(largestPriorIndex); 
			largestPriorIndex = 0; 
		}
		return auto; 
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
		
		
		
		for(AutoPath path : AutoPath.values()) {
			try {
				if(path.getRequiredRobotLocation() == this.robotLoc.getSelected() && Integer.parseInt(SmartDashboard.getString(path.getKey(), "0")) != 0) {
					autoPaths.add(path); 
				}
			}catch (NumberFormatException e) {
				this.logger.log(new WarningEventMessage(this, e));
			}
		}

		AutoPath[] sortedAutoPaths = this.prioritySort(autoPaths);
		for(AutoPath path: sortedAutoPaths) {
			if(this.pathWorks(this.getFmsReturn().getValue().toString(), path.getFieldRex())) {
				return path; 
			}
		}
		prevRobotLoc = this.robotLoc.getSelected();
		return null;


		
		// TODO Auto-generated method stub
		
	}
}

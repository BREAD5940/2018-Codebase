package frc.team5940.codebase2018.robot.autonomous;

import java.util.ArrayList;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.logging.messages.events.WarningEventMessage;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import org.team5940.pantry.processing_network.wpilib.input.FMSGameMessageValueNode;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.DriveAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.ElevatorAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.ElevatorAutoAction.ElevatorHeight;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.IntakeCubeAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.OpenClampAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.OuttakeCubeAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.TurnAutoAction;

/**
 * This compares the priority of each autopath, the required FMS value, and the
 * FMS value in order to return the autopath that the robot will end up doing.
 * If no paths work, the robot will simply not move. Priority of each path can
 * be set by the user. The path with the highest priority number will attempt to
 * run first (i.e. path w/ priority of 100 runs before path w/ priority of 10)
 * Autopath will always work unless the user leaves all priority at 0, then no
 * paths will run.
 * 
 * 
 * 
 * @author Julia Reid
 */
public class AutoPathSelect extends ValueNode<AutoPath> {

	SendableChooser<RobotLocation> robotLoc;
	RobotLocation prevRobotLoc = null;
	ArrayList<AutoPath> autoPaths = new ArrayList<>();

	final ArrayList<AutoPath> totalPossiblePaths = new ArrayList<>();

	final AutoPath emptyAction;

	public static final boolean ROBOT_AUTONOMOUS_WORKS = false;
	FMSGameMessageValueNode fmsReturn;
	Preferences prefs = Preferences.getInstance();

	/**
	 * This constructor initializes class variables
	 * 
	 * @param network
	 *            the network that the node belongs to
	 * @param logger
	 *            the logger for the node
	 * @param label
	 *            the label for the node
	 * @param fmsReturn
	 *            the value that the fms returns
	 * @param distanceMovedValueNode
	 *            the distance that the robot has moved
	 * @param robotAngleValueNode
	 *            the angle of the robot
	 * @param elevatorHeightValueNode
	 *            the height of the elevator
	 */
	public AutoPathSelect(Network network, Logger logger, String label, FMSGameMessageValueNode fmsReturn,
			ValueNode<? extends Number> distanceMovedValueNode, ValueNode<? extends Number> robotAngleValueNode,
			ValueNode<? extends Number> elevatorHeightValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, true, fmsReturn, distanceMovedValueNode, robotAngleValueNode,
				elevatorHeightValueNode);

		robotLoc = new SendableChooser<RobotLocation>();
		robotLoc.addObject("Far Left", RobotLocation.FAR_LEFT);
		robotLoc.addObject("Left", RobotLocation.LEFT);
		robotLoc.addDefault("Center", RobotLocation.CENTER);
		robotLoc.addObject("Right", RobotLocation.RIGHT);
		robotLoc.addObject("Far Right", RobotLocation.FAR_RIGHT);
		SmartDashboard.putData("Robot Location Data", robotLoc);
		this.fmsReturn = fmsReturn;

		// AUTO LINE
		totalPossiblePaths.add(new AutoPath("Auto Line Far Left", "XXX", RobotLocation.FAR_LEFT,
				new DriveAutoAction(network, logger, "Drive Auto", 10, distanceMovedValueNode)));

		totalPossiblePaths.add(new AutoPath("Turn", "XXX", RobotLocation.FAR_LEFT,
				new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode)));


		totalPossiblePaths.add(new AutoPath("Auto Line Far Right", "XXX", RobotLocation.FAR_RIGHT,
				new DriveAutoAction(network, logger, "Drive Auto", 10, distanceMovedValueNode)));

		totalPossiblePaths.add(new AutoPath("Auto Line Center", "XXX", RobotLocation.CENTER,
				new DriveAutoAction(network, logger, "Drive Auto", 10, distanceMovedValueNode)));

		totalPossiblePaths.add(new AutoPath("Auto Line Left", "XXX", RobotLocation.LEFT,
				new DriveAutoAction(network, logger, "Drive Auto", 10, distanceMovedValueNode)));

		totalPossiblePaths.add(new AutoPath("Auto Line Right", "XXX", RobotLocation.RIGHT,
				new DriveAutoAction(network, logger, "Drive Auto", 10, distanceMovedValueNode)));

		// LEFT SWITCH FAR_LEFT START
		totalPossiblePaths.add(new AutoPath("Place Switch From Left", "LXX", RobotLocation.FAR_LEFT,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", 14 - 1.2, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", (30.25 / 12) - 0.5, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		// LEFT SCALE FAR_LEFT START
		//TODO test this because it should work 
		totalPossiblePaths.add(new AutoPath("Place Scale From Left", "XLX", RobotLocation.FAR_LEFT,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH, false),
				new DriveAutoAction(network, logger, "Drive Auto", 27 - 1.2, distanceMovedValueNode),
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SCALE, false),
				new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", (17 / 12)-0.33, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		// RIGHT SWITCH FAR_RIGHT START DONE
		totalPossiblePaths.add(new AutoPath("Place Switch From Right", "RXX", RobotLocation.FAR_RIGHT,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH, false),
				new DriveAutoAction(network, logger, "Drive Auto", 14 - 1.2, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", -90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", (30.25 / 12) - 0.5, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		// RIGHT SCALE FAR_RIGHT START DONE
		totalPossiblePaths.add(new AutoPath("Place Scale From Right", "XRX", RobotLocation.FAR_RIGHT,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH, false),
				new DriveAutoAction(network, logger, "Drive Auto", 27 - 1.2, distanceMovedValueNode),
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SCALE, false),
				new TurnAutoAction(network, logger, "Turn Auto", -90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", (17 / 12) - 0.33, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		// LEFT SWITCH CENTER START
		totalPossiblePaths.add(new AutoPath("Place Left Switch From Center", "LXX", RobotLocation.CENTER,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH, false),
				new DriveAutoAction(network, logger, "Drive Auto", 40d / 12, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", -90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 4.5 + (4.25 / 12), distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", 95, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", (104 / 12) - (38d / 12), distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));
		
		
		// LEFT SWITCH 2 CUBE CENTER START 
//		totalPossiblePaths.add(new AutoPath("Place Left Switch From Center", "LXX", RobotLocation.CENTER,
//				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH, false),
//				new DriveAutoAction(network, logger, "Drive Auto", 40d / 12, distanceMovedValueNode),
//				new TurnAutoAction(network, logger, "Turn Auto", -90, robotAngleValueNode),
//				new DriveAutoAction(network, logger, "Drive Auto", 4.5 + (4.25 / 12), distanceMovedValueNode),
//				new TurnAutoAction(network, logger, "Turn Auto", 95, robotAngleValueNode),
//				new DriveAutoAction(network, logger, "Drive Auto", (104 / 12) - (38d / 12), distanceMovedValueNode),
//				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
//				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		

		// RIGHT SWITCH CENTER START
		totalPossiblePaths.add(new AutoPath("Place Right Switch From Center", "RXX", RobotLocation.CENTER,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", 40d / 12, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 4.5 - (4.25 / 12), distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", -95, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", (104 / 12) - (38d / 12), distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		// RIGHT SWITCH RIGHT START DONE
		totalPossiblePaths.add(new AutoPath("Direct Right Switch", "RXX", RobotLocation.RIGHT,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", ((140 - 37.5) / 12) - 0.28, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		// LEFT SWITCH LEFT START DONE
		totalPossiblePaths.add(new AutoPath("Direct Left Switch", "LXX", RobotLocation.LEFT,
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", ((140 - 37.5) / 12) - 0.28, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode)));

		//TWO CUBE AUTO RIGHT BECAUSE LMAO 
		//This should be working now --Matthew Morley
		totalPossiblePaths.add(new AutoPath("Two Cube Switch Center Right", "RXX", RobotLocation.CENTER,
				
				new DriveAutoAction(network, logger, "Drive Auto", 1, distanceMovedValueNode),//drive forward to not hit the wall
				new TurnAutoAction(network, logger, "Turn 30", 12, robotAngleValueNode),  //turn to face switch
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", 7.8, distanceMovedValueNode),//drive forward to switch
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),//outtake
				
				//pyramid code
				
				//new DriveAutoAction(network, logger, "Drive Auto", -0.5 , distanceMovedValueNode),//move back to get clear of switch
				new TurnAutoAction(network, logger, "Turn 13", 11, robotAngleValueNode),//turn back to pyramid, used to be 38 until line 203 was commented out 
				new DriveAutoAction(network, logger, "Drive Auto", -6 , distanceMovedValueNode),//move back to pyramid
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.DOWN,//move elevator down
						false),
				new TurnAutoAction(network, logger, "Turn -43", -40, robotAngleValueNode),//straighten out
				
				//Intake cube
				
				new DriveAutoAction(network, logger, "Drive Auto", 1.2, distanceMovedValueNode),//drive forward
				new IntakeCubeAutoAction(network, logger, "Intake", 2000),//intake cube
				new DriveAutoAction(network, logger, "Drive Auto", -1.5, distanceMovedValueNode),//drive back


				//return to switch
				
				new TurnAutoAction(network, logger, "Turn 43", 25, robotAngleValueNode),//turn to face switch
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", 5.5, distanceMovedValueNode),//drive to switch
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000)));//outtake
		


		//TWO CUBE AUTO LEFT BECAUSE  Y E S
		//TODO verify distances, angles --Matthew Morley
		totalPossiblePaths.add(new AutoPath("Two Cube Switch Center LEFT", "LXX", RobotLocation.CENTER,

        			//drive forward 1 to clear wall
				new DriveAutoAction(network, logger, "drive", 1, distanceMovedValueNode),
				//turn -32 deg to face switch
				new TurnAutoAction(network, logger, "turn", -31, robotAngleValueNode),
				//elevator up
				new ElevatorAutoAction(network, logger, "elevator", elevatorHeightValueNode, ElevatorHeight.SWITCH, false), 
				//go forward 9.4 ft to reach switch
				new DriveAutoAction(network, logger, "drive", 8.4, distanceMovedValueNode),
				//Face switch
				new TurnAutoAction(network, logger, "turn", 15, robotAngleValueNode),
				//outtake cube
				new OuttakeCubeAutoAction(network, logger, "Outtake", 500),
				//turn -16 deg to back up to pyramid
				//new TurnAutoAction(network, logger, "turn", , robotAngleValueNode),
				//elevator down, straaighten out
				new TurnAutoAction(network, logger, "turn", 1-5, robotAngleValueNode),
				new ElevatorAutoAction(network, logger, "elevator", elevatorHeightValueNode, ElevatorHeight.DOWN, false),
				//back up 6.2 ft to pyramid
				new DriveAutoAction(network, logger, "drive", -6.55, distanceMovedValueNode),
				//turn 48 deg to straight
				new ElevatorAutoAction(network, logger, "elevator", elevatorHeightValueNode, ElevatorHeight.DOWN, false),
				new TurnAutoAction(network, logger, "turn", 27, robotAngleValueNode),
				//go forward 0.9 ft to grab cube
				new DriveAutoAction(network, logger, "drive", 2, distanceMovedValueNode),
				//intake cube
				new IntakeCubeAutoAction(network, logger, "Intake", 400),
				//backup 0.9 ft to clear pyramid
				new DriveAutoAction(network, logger, "drive", -2, distanceMovedValueNode),
				//turn -49 deg to face switch
				new TurnAutoAction(network, logger, "turn", -27, robotAngleValueNode),
				//elevator up
				new ElevatorAutoAction(network, logger, "elevator", elevatorHeightValueNode, ElevatorHeight.SWITCH, false),
				//go forward 6 ft to reach switch
				new DriveAutoAction(network, logger, "drive", 6.5, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "turn", 15, robotAngleValueNode),
				//outtake cube
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000),
				//back up 1 ft to clear switch
				new DriveAutoAction(network, logger, "drive", -1, distanceMovedValueNode),
				//elevator down
				new ElevatorAutoAction(network, logger, "elevator", elevatorHeightValueNode, ElevatorHeight.DOWN, false)));

		totalPossiblePaths.add(new AutoPath("One Cube Switch Center Right FAST PATH", "RXX", RobotLocation.CENTER,
				
				new DriveAutoAction(network, logger, "Drive Auto", 1, distanceMovedValueNode),//drive forward to not hit the wall
				new TurnAutoAction(network, logger, "Turn 30", 12, robotAngleValueNode),  //turn to face switch
				new ElevatorAutoAction(network, logger, "Elevator Auto", elevatorHeightValueNode, ElevatorHeight.SWITCH,
						false),
				new DriveAutoAction(network, logger, "Drive Auto", 7.8, distanceMovedValueNode),//drive forward to switch
				new OuttakeCubeAutoAction(network, logger, "Outtake", 1000)));//outtake			

		totalPossiblePaths.add(new AutoPath("One Cube Switch Center Left FAST PATH", "LXX", RobotLocation.CENTER,
		new DriveAutoAction(network, logger, "drive", 1, distanceMovedValueNode),
		//turn -32 deg to face switch
		new TurnAutoAction(network, logger, "turn", -31, robotAngleValueNode),
		//elevator up
		new ElevatorAutoAction(network, logger, "elevator", elevatorHeightValueNode, ElevatorHeight.SWITCH, false), 
		//go forward 9.4 ft to reach switch
		new DriveAutoAction(network, logger, "drive", 8.4, distanceMovedValueNode),
		//Face switch
		new TurnAutoAction(network, logger, "turn", 15, robotAngleValueNode),
		//outtake cube
		new OuttakeCubeAutoAction(network, logger, "Outtake", 1000)));

		//TEST ACTION 
		this.totalPossiblePaths.add(new AutoPath("Turn 90 to 45 test", "XXX", RobotLocation.CENTER,
				new TurnAutoAction(network, logger, "Turn 90", 90, robotAngleValueNode),
				new TurnAutoAction(network, logger, "Turn back 45", -45, robotAngleValueNode)));
				
		this.totalPossiblePaths.add(new AutoPath("Move backwards", "XXX", RobotLocation.CENTER,
				new TurnAutoAction(network, logger, "Move back 2 ft", -2, distanceMovedValueNode)));		
		
		this.totalPossiblePaths.add(new AutoPath("Test Clamp", "XXX", RobotLocation.CENTER,
				new OpenClampAutoAction(network, logger, "open clamp" )));

				
		// EMPTY ACTION
		this.emptyAction = new AutoPath("Do Nothing", "XXX", RobotLocation.FAR_LEFT);
		
		
	}

	public enum RobotLocation {
		FAR_LEFT, LEFT, CENTER, RIGHT, FAR_RIGHT;
	}

	/**
	 * returns the FMS signal
	 * 
	 * @return String fmsReturn
	 */
	public FMSGameMessageValueNode getFmsReturn() {
		return fmsReturn;
	}

	/**
	 * returns a boolean that states whether or not the specified autopath is valid
	 * based off of the FMS signal
	 * 
	 * @param fms
	 *            the string that the fms signal returns
	 * @param path
	 *            the string that defines valid fms values for the specified path
	 * @return boolean stating if the path works
	 */
	public boolean pathWorks(String fms, String path) {
		for (int i = 0; i < fms.length(); i++) {
			if (fms.charAt(i) != path.charAt(i) && path.charAt(i) != 'X') {
				return false;
			}
		}
		return true;
	}

	/**
	 * standard insertion sort algorithim dont even talk to me about efficiency this
	 * array is so small the difference is negligable
	 * 
	 * @param paths
	 *            an unsorted arraylist of the available auto paths
	 * @return auto an array of AutoPaths that derives its value from the ArrayList
	 *         paths sorted based on priority
	 */
	public AutoPath[] prioritySort(ArrayList<AutoPath> paths) {
		AutoPath[] auto = new AutoPath[paths.size()];
		int largestPriorIndex = 0;

		for (int i = 0; i < auto.length; i++) {

			for (int j = 0; j < paths.size(); j++) {

				if (Integer.parseInt(prefs.getString(paths.get(j).getKey(), "0")) > Integer
						.parseInt(prefs.getString(paths.get(largestPriorIndex).getKey(), "0"))) {

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
		return true;
	}

	/**
	 * This method returns the autopath that is to be used in autonomous based off
	 * of FMS value and Priority assigned by driver
	 */
	@Override
	protected AutoPath updateValue() {

		if (prevRobotLoc != this.robotLoc.getSelected()) {
			for (AutoPath path : this.totalPossiblePaths) {
				if (path.getRequiredRobotLocation() == this.robotLoc.getSelected()) {
					prefs.putString(path.getKey(), "0");
				} else {
					prefs.putString(path.getKey(), "Disabled");
				}
			}
		}

		for (AutoPath path : this.totalPossiblePaths) {
			try {
				if (path.getRequiredRobotLocation() == this.robotLoc.getSelected()
						&& Integer.parseInt(prefs.getString(path.getKey(), "0")) != 0) {
					autoPaths.add(path);
				}
			} catch (NumberFormatException e) {
				this.logger.log(new WarningEventMessage(this, e));
			}
		}

		AutoPath[] sortedAutoPaths = this.prioritySort(autoPaths);
		for (AutoPath path : sortedAutoPaths) {
			if (this.pathWorks(this.getFmsReturn().getValue().toString(), path.getFieldRex())) {
				return path;
			}
		}
		prevRobotLoc = this.robotLoc.getSelected();
		return this.emptyAction;

	}
}

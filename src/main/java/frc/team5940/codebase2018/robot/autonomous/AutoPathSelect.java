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
import frc.team5940.codebase2018.robot.autonomous.auto_actions.OuttakeCubeAutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.TurnAutoAction;

public class AutoPathSelect extends ValueNode<AutoPath> {

	SendableChooser<RobotLocation> robotLoc;
	RobotLocation prevRobotLoc = null;
	ArrayList<AutoPath> autoPaths = new ArrayList<>();

	final ArrayList<AutoPath> totalPossiblePaths = new ArrayList<>();

	final AutoPath emptyAction;

	public static final boolean ROBOT_AUTONOMOUS_WORKS = false;
	FMSGameMessageValueNode fmsReturn;
	Preferences prefs = Preferences.getInstance();

	public AutoPathSelect(Network network, Logger logger, String label, FMSGameMessageValueNode fmsReturn,
			ValueNode<? extends Number> distanceMovedValueNode, ValueNode<? extends Number> robotAngleValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, true, fmsReturn, distanceMovedValueNode, robotAngleValueNode);

		robotLoc = new SendableChooser<RobotLocation>();
		robotLoc.addDefault("Center", RobotLocation.CENTER);
		robotLoc.addObject("Left", RobotLocation.LEFT);
		robotLoc.addObject("Right", RobotLocation.RIGHT);
		SmartDashboard.putData("Robot Location Data", robotLoc);
		this.fmsReturn = fmsReturn;

		// AUTO LINE
		totalPossiblePaths.add(new AutoPath("Auto Line", "XXX", RobotLocation.LEFT,
				new DriveAutoAction(network, logger, "Drive Auto", 11, distanceMovedValueNode)));

		// LEFT SWITCH LEFT START
		totalPossiblePaths.add(new AutoPath("Place Switch From Left", "LXX", RobotLocation.LEFT,
				new DriveAutoAction(network, logger, "Drive Auto", 14, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 1, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake")));

		// LEFT SCALE LEFT START
		totalPossiblePaths.add(new AutoPath("Place Scale From Left", "XLX", RobotLocation.LEFT,
				new DriveAutoAction(network, logger, "Drive Auto", 27, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 1, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake")));

		// RIGHT SWITCH RIGHT START
		totalPossiblePaths.add(new AutoPath("Place Switch From Right", "RXX", RobotLocation.RIGHT,
				new DriveAutoAction(network, logger, "Drive Auto", 14, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", -90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 1, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake")));

		// RIGHT SCALE RIGHT START
		totalPossiblePaths.add(new AutoPath("Place Scale From Right", "XLX", RobotLocation.RIGHT,
				new DriveAutoAction(network, logger, "Drive Auto", 27, distanceMovedValueNode),
				new TurnAutoAction(network, logger, "Turn Auto", -90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 1, distanceMovedValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake")));

		// LEFT SWITCH CENTER START
		totalPossiblePaths.add(new AutoPath("Place Left Switch From Center", "LXX", RobotLocation.CENTER,
				new DriveAutoAction(network, logger, "Drive Auto", 4, distanceMovedValueNode),
				/* left */ new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 4.5, distanceMovedValueNode),
				/* right */ new TurnAutoAction(network, logger, "Turn Auto", 6, robotAngleValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake")));

		// RIGHT SWITCH CENTER START
		totalPossiblePaths.add(new AutoPath("Place Right Switch From Center", "RXX", RobotLocation.CENTER,
				new DriveAutoAction(network, logger, "Drive Auto", 4, distanceMovedValueNode),
				/* right */ new TurnAutoAction(network, logger, "Turn Auto", 90, robotAngleValueNode),
				new DriveAutoAction(network, logger, "Drive Auto", 4.5, distanceMovedValueNode),
				/* left */ new TurnAutoAction(network, logger, "Turn Auto", 6, robotAngleValueNode),
				new OuttakeCubeAutoAction(network, logger, "Outtake")));

		// EMPTY ACTION
		this.emptyAction = new AutoPath("Do Nothing", "XXX", RobotLocation.LEFT);
	}

	public enum RobotLocation {
		LEFT, CENTER, RIGHT;
	}
	/**
	 * returns the FMS signal 
	 * @return String fmsReturn
	 * */
	public FMSGameMessageValueNode getFmsReturn() {
		return fmsReturn;
	}
	/**
	 * returns a boolean that states whether or not the specified autopath is valid based off of the FMS signal
	 * @param String fms the string that the fms signal returns
	 * @param String path the string that defines valid fms values for the specified path
	 * @return boolean stating if the path works 
	 * */
	public boolean pathWorks(String fms, String path) {
		for (int i = 0; i < fms.length(); i++) {
			if (fms.charAt(i) != path.charAt(i) && path.charAt(i) != 'X') {
				return false;
			}
		}
		return true;
	}
	/**
	 * standard insertion sort algorithim
	 * dont even talk to me about efficiency this array is so small the difference is negligable 
	 * @param ArrayList<AutoPath> paths an unsorted arraylist of the available auto paths 
	 * @return AutoPath[] an array of AutoPaths that derives its value from the ArrayList paths sorted based on priority
	 * */
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

	/**
	 * This method returns the autopath that is to be used in autonomous based off of FMS value and Priority assigned by driver
	 * */
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

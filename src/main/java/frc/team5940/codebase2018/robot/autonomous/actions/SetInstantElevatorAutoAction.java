package frc.team5940.codebase2018.robot.autonomous.actions;

import frc.team5940.codebase2018.robot.autonomous.AutoPlanFollower;

/**
 * An {@link AutoAction} to set the height of the elevator. The
 * {@link AutoPlanFollower} will move on to the next action before the elevator
 * has moved to it's newly set position. This is useful to start the elevator
 * moving before you actually need it to be in a particular position, saving
 * time.
 */
public class SetInstantElevatorAutoAction extends ElevatorAction {

	public SetInstantElevatorAutoAction(ElevatorHeight setElevatorHeight) {
		super(setElevatorHeight);
	}

}

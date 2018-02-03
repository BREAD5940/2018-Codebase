package frc.team5940.codebase2018.robot.autonomous.actions;

/**
 * An {@link AutoAction} for driving some number of pre-defined feet forward or backward.
 */
public class DriveAutoAction implements AutoAction {

    double feet;

    /**
     * Initializes this auto action with a number of feet to drive.
     * @param feet The number of feet to drive, positive is forward.
     */
	public DriveAutoAction(double feet) {
        this.feet = feet;
	}

    /**
     * The number of feet to drive to execute this action.
     * @return The number of feet to drive, positive is forward.
     */
	public double getDriveFeet() {
	    return this.feet;
    }

}

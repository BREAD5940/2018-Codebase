package frc.team5940.codebase2018.robot.autonomous;

/**
 * An {@link AutoAction} for turning some number of pre-defined degrees.
 */
public class TurnAutoAction implements AutoAction {

    double degrees;

    /**
     * Initializes this auto action with a number of degrees to rotate.
     * @param degrees The number of degrees to rotate, positive is clockwise/ right.
     */
	public TurnAutoAction(double degrees) {
        this.degrees = degrees;
    }

    /**
     * The number of degrees to rotate to execute this turn.
     * @return The number of degrees to rotate, positive is clockwise/ right.
     */
    public double getTurnDegrees() {
	    return this.degrees;
    }


}

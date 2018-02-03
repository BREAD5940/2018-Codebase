package frc.team5940.codebase2018.robot.autonomous;

/**
 * An {@link AutoAction} to set the height of the elevator. Note that this DOES NOT implement {@link ElevatorDependentAutoAction} itself, the {@link AutoPlanFollower} moves on to the next action before the elevator has moved to it's newly set position. This is useful to start the elevator moving before you actually need it to be in a particular position, saving time.
 */
public class SetElevatorAutoAction {

    ElevatorDependentAutoAction.ElevatorHeights height;

    /**
     * Initializes this action.
     * @param height The height to set the elevator to.
     * @throws IllegalArgumentException height is null.
     */
    public SetElevatorAutoAction(ElevatorDependentAutoAction.ElevatorHeights height) throws IllegalArgumentException {
        if(height == null) {
            throw new IllegalArgumentException("height is null");
        }
        this.height = height;
    }

    /**
     * Returns the height the the elevator should be set to.
     * @return Some value from ElevatorDependentAutoAction's ElevatorHeights enum.
     */
    public ElevatorDependentAutoAction.ElevatorHeights getElevatorSetHeight() {
        return this.height;
    }
}

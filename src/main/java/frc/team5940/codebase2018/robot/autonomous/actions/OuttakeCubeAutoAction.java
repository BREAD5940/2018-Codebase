package frc.team5940.codebase2018.robot.autonomous.actions;

/**
 * This action causes the robot to outtake the cube, after the elevator reaches
 * the specified height.
 * 
 * @author Michael Bentley
 *
 */
public class OuttakeCubeAutoAction implements ElevatorDependentAutoAction, AutoAction {

	/**
	 * The required height of the elevator.
	 */
	ElevatorHeight requiredHeight;

	/**
	 * This action causes the robot to outtake the cube, after the elevator reaches
	 * the set height.
	 * 
	 * @param requiredHeight
	 *            The required elevator height before outtaking the cube.
	 */
	public OuttakeCubeAutoAction(ElevatorHeight requiredHeight) {
		this.requiredHeight = requiredHeight;
	}

	@Override
	public ElevatorHeight getRequiredElevatorHeight() {
		return this.requiredHeight;
	}

}

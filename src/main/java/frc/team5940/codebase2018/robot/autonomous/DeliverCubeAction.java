package frc.team5940.codebase2018.robot.autonomous;

public class DeliverCubeAction implements AutonomousAction {
	
	double elevatorHeight;
	
	public DeliverCubeAction(double elevatorHeight) {
		this.elevatorHeight = elevatorHeight;
	}

	@Override
	public double getValue() {
		return 2;
	}

	@Override
	public double getElevatorHeight() {
		return this.elevatorHeight;
	}

}

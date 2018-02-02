package frc.team5940.codebase2018.robot.autonomous;

public class TurnAction implements AutonomousAction {

	int angle;
	double elevatorHeight;

	public TurnAction(int angle, double elevatorHeight) {
		this.angle = angle;
		this.elevatorHeight = elevatorHeight;
	}

	public TurnAction(int angle) {
		this.angle = angle;
	}
	@Override
	public double getValue() {
		return this.angle;
	}

	public double getElevatorHeight() {
		return this.elevatorHeight;
	}
}

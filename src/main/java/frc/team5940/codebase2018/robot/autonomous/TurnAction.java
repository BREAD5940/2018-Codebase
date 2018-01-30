package frc.team5940.codebase2018.robot.autonomous;

public class TurnAction implements AutonomousAction {

	int angle;

	public TurnAction(int angle) {
		this.angle = angle;
	}

	@Override
	public double getValue() {
		return this.angle;
	}
}

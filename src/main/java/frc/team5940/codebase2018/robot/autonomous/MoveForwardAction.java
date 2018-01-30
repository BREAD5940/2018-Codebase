package frc.team5940.codebase2018.robot.autonomous;

public class MoveForwardAction implements AutonomousAction {
	
	double distance;
	
	public MoveForwardAction(double distance) {
		this.distance = distance;
	}

	@Override
	public double getValue() {
		return distance;
	}

}

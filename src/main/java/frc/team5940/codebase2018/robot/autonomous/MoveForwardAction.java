package frc.team5940.codebase2018.robot.autonomous;

public class MoveForwardAction implements AutonomousAction {
	
	double distanceMove; 
	double distance;
	double elevatorHeight;
	
	public MoveForwardAction(double distance, double elevatorHeight) {
		this.distance = distance;
		this.elevatorHeight = elevatorHeight;
	}
	
	public MoveForwardAction(double distanceToMove) {
		distanceMove = distanceToMove; 
	}
	
	public void move(double distanceToMove) {
		distanceMove = distanceToMove; 
	}
	@Override
	public double getValue() {
		return this.distance;
	}

	@Override
	public double getElevatorHeight() {
		return this.elevatorHeight;
	}

}

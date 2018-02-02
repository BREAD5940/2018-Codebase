package frc.team5940.codebase2018.robot.autonomous;

public class MoveForwardAction implements AutonomousAction {
	
	double distance;
	double elevatorHeight;
	
	public MoveForwardAction(double distance, double elevatorHeight) {
		this.distance = distance;
		this.elevatorHeight = elevatorHeight;
	}
<<<<<<< HEAD
	
	public MoveForwardAction(double distance) {
		this.distance = distance; 
	}
	
	
=======

>>>>>>> 257abb5794c34cd532d72896c45d9eb465b74de6
	@Override
	public double getValue() {
		return this.distance;
	}

	@Override
	public double getElevatorHeight() {
		return this.elevatorHeight;
	}

}

package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.wpilib.input.HIDButtonValueNode;
import org.team5940.pantry.processing_network.wpilib.systems.Piston;
import org.team5940.pantry.processing_network.wpilib.systems.controller_layouts.XBoxControllerLayout;

import edu.wpi.first.wpilibj.Joystick;

public class RampSetup {

	public static void setupRampNodes(Network network, Logger logger, String label) {
		
		Joystick joystick = new Joystick(0);
		HIDButtonValueNode rampDeploymentButton = new HIDButtonValueNode(network, logger, label, joystick, XBoxControllerLayout.Buttons.LB_BUTTON);
		
		HIDButtonValueNode rampRaiserButton = new HIDButtonValueNode(network, logger, label, joystick, XBoxControllerLayout.Axis.LEFT_TRIGGER);
		
		Piston piston = new Piston(network, logger, pistonDoubleSolenoid);
		Piston piston2 = new Piston(network, logger, pistonDoubleSolenoid);
		
		Boolean rampDeployed = false;
		
		if(rampDeploymentButton.getValue() == true) {
			rampDeployed = true;
			piston = true;
		} else if(rampRaiserButton == true && rampDeployed == true) {
			piston2 = true;
		}
	}
	
	
}

package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.OpenClampAutoAction;


/**
 * This will return if the soleniod for the clamp should be 
 * open or closed. 
 * 
 * @author Julia Reid 
 *
 */
public class ClampAutonomousControllerValueNode extends ValueNode<DoubleSolenoid.Value>{

	/**
	 * The current AutoAction in the auto plan.
	 */
	ValueNode<? extends AutoAction> autoActionValueNode;

	/**
	 * Creates a new {@link ClampAutonomousControllerValueNode}
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param currentActionValueNode
	 *            The current AutoActio of robots plan 
	 */
	public ClampAutonomousControllerValueNode(Network network, Logger logger, String label, 
			ValueNode<? extends AutoAction> autoActionValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoActionValueNode);
		this.autoActionValueNode = autoActionValueNode; 
		
	}

	@Override
	protected Value updateValue() {
		
		if(autoActionValueNode.getValue() instanceof OpenClampAutoAction) {
			return DoubleSolenoid.Value.kForward; 
		}else if(autoActionValueNode.getValue() instanceof OpenClampAutoAction) {
			return Value.kReverse; 
		} else {
			return DoubleSolenoid.Value.kForward;
		}
	}

}

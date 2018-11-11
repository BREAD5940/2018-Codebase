package frc.team5940.codebase2018.robot.autonomous.auto_action_controllers;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.ClampAutoAction;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;



/**
 * This will return if the soleniod for the clamp should be 
 * open or closed. 
 * 
 * @author Julia Reid 
 *
 */
public class ClampAutonomousControllerValueNode extends ValueNode<DoubleSolenoid.Value> {

	/**
	 * The current AutoAction in the auto plan.
	 */
	ValueNode<? extends AutoAction> autoActionValueNode;
	DoubleSolenoid.Value solenoidValue = Value.kReverse;

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
	public ClampAutonomousControllerValueNode(Network network, Logger logger, String label, ValueNode<?extends AutoAction> autoActionValueNode)
			throws IllegalArgumentException, IllegalStateException {
			super(network, logger, label, autoActionValueNode);
			
				this.autoActionValueNode = autoActionValueNode;

		}

	@Override
	protected Value updateValue() {
		
		if (this.autoActionValueNode.getValue() instanceof ClampAutoAction) {
			this.solenoidValue = ((ClampAutoAction) this.autoActionValueNode.getValue()).getSolenoidValue();
		}
		return this.solenoidValue;
	}

}

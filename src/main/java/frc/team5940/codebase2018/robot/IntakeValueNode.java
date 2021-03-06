package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

/**
 * This ValueNode stores the speed of the intake based on if a cube is already
 * intaken. This adjusts the set speed so if it is negative (intaking) and a
 * cube is already at the back of the intake then it will return 0 instead of
 * the set speed. Otherwise it will return the set speed.
 * 
 * @author Michael Bentley
 *
 */
public class IntakeValueNode extends ValueNode<Double> {

	/**
	 * The set speed of the intake.
	 */
	ValueNode<? extends Number> setSpeed;

	/**
	 * The set speed of the intake.
	 */
	ValueNode<? extends Boolean> slowOuttakeButton;

	/**
	 * Creates a new {@link IntakeValueNode}
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param setSpeedValueNode
	 *            The set speed of the intake.
	 */
	public IntakeValueNode(Network network, Logger logger, String label, ValueNode<? extends Number> setSpeedValueNode, ValueNode<? extends Boolean> slowOuttakeButton)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, setSpeedValueNode);
		this.setSpeed = setSpeedValueNode;
		this.slowOuttakeButton = slowOuttakeButton;
	}

	@Override
	protected Double updateValue() {
		if (slowOuttakeButton.getValue()) {
			return 0.3;
		}
		if (this.setSpeed.getValue().doubleValue() < 0) {
			return this.setSpeed.getValue().doubleValue() * 0.5;
		}
		return this.setSpeed.getValue().doubleValue();
	}

}

package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

/**
 * This determines the current parameter slot of the talon on the elevator base
 * on if the elevator is moving up or down .
 * 
 * @author Michael Bentley
 *
 */
public class ElevatorTalonSRXParameterSlotValueNode extends ValueNode<Integer> {

	/**
	 * The current position of the elevator.
	 */
	ValueNode<? extends Number> currentElevatorPosition;

	/**
	 * The set position of the elevator.
	 */
	ValueNode<? extends Number> setElevatorPosition;

	/**
	 * Creates a new {@link ElevatorTalonSRXParameterSlotValueNode} which bases it
	 * slot on the set elevator height and current elevator height. Both of those
	 * should be in the same units.
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label
	 * @param currentElevatorPosition
	 *            This current position of the elevator.
	 * @param setElevatorPosition
	 *            The set position of the elevator. Should be in the same unit as
	 *            the current position.
	 */
	public ElevatorTalonSRXParameterSlotValueNode(Network network, Logger logger, String label,
			ValueNode<? extends Number> currentElevatorPosition, ValueNode<? extends Number> setElevatorPosition)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, currentElevatorPosition, setElevatorPosition);

		this.currentElevatorPosition = currentElevatorPosition;
		this.setElevatorPosition = setElevatorPosition;
	}

	@Override
	protected Integer updateValue() {
		if (this.setElevatorPosition.getValue().doubleValue() < this.currentElevatorPosition.getValue().doubleValue()) {
			return RobotConfig.LOWER_ELEVATOR_SLOT_ID;
		} else {
			return RobotConfig.RAISE_ELEVATOR_SLOT_ID;
		}
	}

}

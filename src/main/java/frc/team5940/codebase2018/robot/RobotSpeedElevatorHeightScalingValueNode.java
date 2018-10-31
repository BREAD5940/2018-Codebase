package frc.team5940.codebase2018.robot;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This ValueNode scales down a percent based on the ...
 * @author mbent
 *
 */
public class RobotSpeedElevatorHeightScalingValueNode extends ValueNode<Double> {

	/**
	 * The current height of the elevator. 
	 */
	ValueNode<? extends Double> currentElevatorHeight;

	/**
	 * The slope of the linear equation that determines the speed so m in the y = mx
	 * + b format.
	 */
	double scalingRate;

	/**
	 * This is the height where the speed should start to be scaled down.
	 */
	double initialScalingPercentHeight;

	/**
	 * This Node creates a linear equation and this value be b in the y = mx + b
	 * format. This increases the y-value to ensure that the value this node returns
	 * is positive and at minimum is the minPercent that is passed into the
	 * constructor.
	 */
	double percentAdjustment;

	/**
	 * This returns a linearly scaled value based on how far off the elevator is
	 * from its initialScalingPercentHeight.
	 * 
	 * @param network
	 *            This' Network
	 * @param logger
	 *            This' Logger
	 * @param label
	 *            This' Label
	 * @param initialScalingPercentHeight
	 *            The height to start scaling the value this node returns down.
	 * @param minPercent
	 *            The minimum percent that this node should return when the elevator
	 *            is at max height.
	 * @param currentElevatorHeight
	 *            The current height of the elevator.
	 */
	public RobotSpeedElevatorHeightScalingValueNode(Network network, Logger logger, String label,
			double initialScalingPercentHeight, double minPercent, ValueNode<? extends Double> currentElevatorHeight)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, currentElevatorHeight);

		this.currentElevatorHeight = currentElevatorHeight;

		this.initialScalingPercentHeight = initialScalingPercentHeight;

		double rise = 1 - minPercent;
		double run = initialScalingPercentHeight - 1;
		this.scalingRate = rise / run;

		this.percentAdjustment = minPercent - this.scalingRate;
	}

	@Override
	protected Double updateValue() {
		double elevatorHeight = this.currentElevatorHeight.getValue().doubleValue();
		double percentHeight = elevatorHeight / RobotConfig.MAX_ELEVATOR_HEIGHT;
		if (percentHeight < this.initialScalingPercentHeight) {
			SmartDashboard.putNumber("Percent", 1);
			return 1d;
		}
		double out = percentHeight * scalingRate + this.percentAdjustment;
		SmartDashboard.putNumber("Percent", out);
		return out;
	}

}

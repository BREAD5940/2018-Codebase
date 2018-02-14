package frc.team5940.codebase2018.robot.autonomous;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import frc.team5940.codebase2018.robot.autonomous.auto_actions.AutoAction;
import frc.team5940.codebase2018.robot.autonomous.auto_actions.EmptyAutonomousAction;

/**
 * This progresses through an array of AutoActions. This will reset its progress
 * if the array changes. This also does not handle ElevatorDependentActions.
 * Should use {@link ElevatorDependentActionStatusCheckerValueNode} to handle
 * those.
 * 
 * If there is no action left in the AutoPlan than this will return an
 * {@link EmptyAutonomousAction}.
 * 
 * @author Michael Bentley
 *
 */
public class AutoPlanFollower extends ValueNode<AutoAction> {

	/**
	 * The ValueNode that returns the current auto plan.
	 */
	ValueNode<AutoPath> autoPlanValueNode;

	/**
	 * The array of the current actions for the autoPlan.
	 */
	AutoAction[] currentActionArray;

	/**
	 * The index of the current action.
	 */
	int currentActionIndex = 0;

	EmptyAutonomousAction emptyAutoAction;

	/**
	 * Creates an AutoPlanFollower.
	 * 
	 * @param network
	 *            This' Network.
	 * @param logger
	 *            This' Logger.
	 * @param label
	 *            This' Label.
	 * @param autoPlanValueNode
	 *            The ValueNode that returns the current auto plan.
	 */
	public AutoPlanFollower(Network network, Logger logger, String label, ValueNode<AutoPath> autoPlanValueNode)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, autoPlanValueNode);

		this.autoPlanValueNode = autoPlanValueNode;
		this.emptyAutoAction = new EmptyAutonomousAction(this.getNetwork(), logger, "Empty Auto Action");
	}

	@Override
	protected AutoAction updateValue() {
		// Verifies that the AutoPlan has not changed. This is important because before
		// the match starts the AutoPlanValueNode will not return the proper value.
		if (!autoPlanValueNode.getValue().getActions().equals(this.currentActionArray)) {
			// Updates the actionArray, resets the current index, and then sets up the first
			// action.
			this.currentActionArray = autoPlanValueNode.getValue().getActions();
			this.currentActionArray = appendAutoAction(this.emptyAutoAction);

			this.currentActionIndex = 0;
		}

		AutoAction currentAction = getCurrentAction();

		if (currentAction.getValue()) {
			this.currentActionIndex++;
			return getCurrentAction();
		}

		return currentAction;
	}

	/**
	 * Returns the current action for the AutoPlan. If the currentActionIndex is too
	 * great than this will just return an {@link EmptyAutonomousAction}.
	 * 
	 * @return The current action. If no action is available then it returns an
	 *         {@link EmptyAutonomousAction}.
	 */
	private AutoAction getCurrentAction() {
		if (this.currentActionIndex >= this.currentActionArray.length) {
			// This should never happen because an EmptyAutoAction is already appended to
			// the end earlier but just in case.
			return this.emptyAutoAction;
		}
		return this.currentActionArray[this.currentActionIndex];
	}

	private AutoAction[] appendAutoAction(AutoAction action, AutoAction... actions) {
		AutoAction[] out = new AutoAction[actions.length + 1];
		for (int i = 0; i < actions.length; i++) {
			out[i] = actions[i];
		}
		out[out.length - 1] = action;
		return out;
	}
}

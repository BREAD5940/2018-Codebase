package autonomous;

import org.team5940.pantry.logging.loggers.Logger;
import org.team5940.pantry.processing_network.Network;
import org.team5940.pantry.processing_network.ValueNode;

import autonomous.AutoPathValueNode.Paths;

public class AutoPathValueNode extends ValueNode<Paths> {

	public enum Paths {
		AUTOLINE(AutoStuff.MOVE_FORWARD , AutoStuff.DELIVER_CUBE), CENTERLEFTSWITCH, CENTERRIGHTSWITCH, LEFTSWITCH, RIGHTSWITCH, RIGHTSCALE, LEFTSCALE;
		
		AutoStuff[] plan;
		
		Paths(AutoStuff... plan) {
			this.plan = plan;
		}
		
		public AutoStuff[] getPlan() {
			return plan;
		}
		
		
	}
	
	public enum AutoStuff {
		MOVE_FORWARD, TURN_LEFT, DELIVER_CUBE;
	}

	public AutoPathValueNode(Network network, Logger logger, String label, ValueNode<Paths>[] sourcesArray)
			throws IllegalArgumentException, IllegalStateException {
		super(network, logger, label, sourcesArray);
		
		
	}

	@Override
	protected Paths updateValue() {
		
		return Paths.AUTOLINE; 

	}

}
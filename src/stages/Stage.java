package stages;

import controller.Simulator;

public abstract class Stage {
	
	protected Simulator simulator;
	
	/**
	 * Constructs a new stage
	 * @param simulator the simulator to which this stage is associated
	 */
	public Stage(Simulator simulator)
	{
		this.simulator = simulator;
	}
	
	/**
	 * Runs the stage
	 */
	public abstract void run();
}

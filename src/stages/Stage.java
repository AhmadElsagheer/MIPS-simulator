package stages;

import controller.Simulator;

public abstract class Stage {
	Simulator simulator;
	
	public Stage(Simulator simulator)
	{
		this.simulator = simulator;
	}
	public abstract void run();
	
}

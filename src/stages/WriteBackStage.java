package stages;

import controller.Simulator;

public class WriteBackStage extends Stage{

	/**
	 * Constructs a new write back stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public WriteBackStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	/**
	 * Runs write back stage.
	 */
	public void run() 
	{
		
		// check if source from ALU or memory, then check RegWrite flag and write to the memory accrodingly
		
	}
	
	
	
	
}

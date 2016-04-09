package stages;

import controller.Simulator;

public class WriteBackStage extends Stage{

	public WriteBackStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	public void run() {
		
		// check if source from ALU or memory, then check RegWrite flag and write to the memory accrodingly
		
	}
	
	
	
	
}

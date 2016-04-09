package stages;

import controller.Simulator;

public class InstructionFetchStage extends Stage{

	private int PC;
	public InstructionFetchStage(Simulator simulator)
	{
		super(simulator);
		PC = 0;
	}
	
	@Override
	public void run() {

		//read next instruction from instruction memory
		//increment PC or check PCsrc flag
		//send next instruction to pipeline
		//send new PC to pipeline
		
	}

}

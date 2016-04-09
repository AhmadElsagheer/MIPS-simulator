package stages;

import controller.Simulator;

public class InstructionDecodeStage extends Stage{
	
	
	public InstructionDecodeStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	public void run() {
		
		// get instruction from previous pipeline and decode it
		// get corresponding registers from register file and write them in next pipeline register
		// sign extend
		// write register segments into next pipeline register
		// call writeControlFlags method to write control flags into next pipeline register and pass the opcode to int
		
		
	}
	

	public void writeControlFlags(int opcode)
	{
		
	}
}

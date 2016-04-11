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
	public void run() 
	{
		//read next instruction from instruction memory
		int instruction = simulator.getInstructionMemory().getInstruction(PC);

		//increment PC or check PCsrc flag
		int branch = simulator.getExtoMem().getRegister("Branch").getValue();
		int zero = simulator.getExtoMem().getRegister("Zero").getValue();
		if((branch & zero) == 0)
			PC = PC + 4;
		else
			PC =  simulator.getExtoMem().getRegister("BranchAddress").getValue();
		
		//send next instruction to pipeline
		simulator.getIDtoEx().getRegister("Instruction").setValue(instruction);
		
		//send new PC to pipeline
		simulator.getIDtoEx().getRegister("PC").setValue(PC);
		
	}

}

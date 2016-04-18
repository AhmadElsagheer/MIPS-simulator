package stages;

import controller.Simulator;

public class InstructionFetchStage extends Stage{

	private int PC;
	private int PCWrite;
	
	/**
	 * Constructs a new instruction fetch stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public InstructionFetchStage(Simulator simulator)
	{
		super(simulator);
		PC = 0;
		PCWrite = 1;
	}
	
	public void setPCWrite(int canFetch) 
	{
		this.PCWrite = canFetch;
	}

	@Override
	/**
	 * Runs the instruction fetch stage
	 */
	public void run()
	{
		if (PCWrite == 1)
		{
			//Read next instruction from instruction memory
			int instruction = simulator.getInstructionMemory().getInstruction(PC);

			//Increment PC or check PCsrc flag
			int branch = simulator.getExtoMem().getRegister("Branch").getValue();
			int zero = simulator.getExtoMem().getRegister("Zero").getValue();
			if((branch & zero) == 0)
				PC = PC + 4;
			else
				PC =  simulator.getExtoMem().getRegister("BranchAddress").getValue();
			
			//Send next instruction to pipeline
			simulator.getIDtoEx().getRegister("Instruction").setValue(instruction);
			
			//Send new PC to pipeline
			simulator.getIDtoEx().getRegister("PC").setValue(PC);
		}
	}

}

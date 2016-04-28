package stages;

import controller.Simulator;

public class InstructionFetchStage extends Stage{

	 int PC;
	int tmpPC;
	private int PCWrite;
	private boolean branchNext;

	/**
	 * Constructs a new instruction fetch stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public InstructionFetchStage(Simulator simulator)
	{
		super(simulator);
		PC = 0;
		tmpPC=0;
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
		boolean tmpBranchNext;
		//Read next instruction from instruction memory
		int instruction = simulator.getInstructionMemory().getInstruction(PC);
		//Increment PC or check PCsrc flag
		int branch = simulator.getExtoMem().getRegister("Branch").getValue();
		int zero = simulator.getExtoMem().getRegister("Zero").getValue();
		if((branch & zero) == 0)
			tmpBranchNext = false;
		else{
			tmpBranchNext = true;
			PC = simulator.getExtoMem().getRegister("BranchAddress").getValue();
		}

		if(!branchNext && simulator.getInstructionNumber(0) == Simulator.EMPTY)
		{
			simulator.setInstructionNumber(1, Simulator.EMPTY);
			branchNext = tmpBranchNext;
			return;
		}

		if(branchNext || PCWrite == 1)
		{
			// Set Next Instruction for Instruction Decode
			simulator.setInstructionNumber(1, PC/4);

			//Send next instruction to pipeline
			simulator.getIFtoID().setRegister("Instruction", instruction);

			tmpPC = PC;
			if((branch & zero) == 0)
				PC = PC + 4;
			else
				PC =  simulator.getExtoMem().getRegister("BranchAddress").getValue();


			//Send new PC to pipeline
			simulator.getIFtoID().setRegister("PC", PC);

			// Set Next Instruction for Instruction Fetch (Not Final)
			if(PC/4 < simulator.getInstructionMemory().getNumberOfInstructions())
				simulator.setInstructionNumber(0, PC/4);
			else
				simulator.setInstructionNumber(0, Simulator.EMPTY);
		}
		else
		{
			//PC = PC - 4;
			// Set Next Instruction for Instruction Decode
			simulator.setInstructionNumber(1, PC/4);

			//Send next instruction to pipeline
			simulator.getIFtoID().setRegister("Instruction", instruction);




			//Send new PC to pipeline
			simulator.getIFtoID().setRegister("PC", PC);

			// Set Next Instruction for Instruction Fetch (Not Final)

			PCWrite = 1;
			tmpPC=PC;
			PC = PC + 4;
			if(PC/4 < simulator.getInstructionMemory().getNumberOfInstructions())
				simulator.setInstructionNumber(0, PC/4);
			else
				simulator.setInstructionNumber(0, Simulator.EMPTY);
			
//			simulator.setInstructionNumber(1, simulator.getInstructionNumber(0));
			
		}
		branchNext = tmpBranchNext;
	}
}

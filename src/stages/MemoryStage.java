package stages;

import controller.Simulator;

public class MemoryStage extends Stage{

	/**
	 * Constructs a new memory stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public MemoryStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	/**
	 * Runs memory stage.
	 */
	public void run() 
	{
		if(simulator.getInstructionNumber(3) == Simulator.EMPTY)
		{
			simulator.setInstructionNumber(4, Simulator.EMPTY);
			return;
		}
		
		simulator.getMemtoWb().setRegister("RegWrite", simulator.getExtoMem().getRegister("RegWrite").getValue());
		simulator.getMemtoWb().setRegister("MemToReg", simulator.getExtoMem().getRegister("MemToReg").getValue());
		simulator.getMemtoWb().setRegister("Destination", simulator.getExtoMem().getRegister("Destination").getValue());
		simulator.getMemtoWb().setRegister("ALUResult", simulator.getExtoMem().getRegister("ALUResult").getValue());
		
		if(simulator.getExtoMem().getRegister("MemRead").getValue() == 1)
			simulator.getMemtoWb().setRegister("MemoryOutput", simulator.getDataMemory().read(simulator.getExtoMem().getRegister("ALUResult").getValue()));
	
		if(simulator.getExtoMem().getRegister("MemWrite").getValue() == 1)
			simulator.getDataMemory().write(simulator.getExtoMem().getRegister("ALUResult").getValue(), simulator.getExtoMem().getRegister("ReadData2").getValue());
		
		// Set Next Instruction for Write Back
		simulator.setInstructionNumber(4, simulator.getInstructionNumber(3));

		// Set Next Instruction for Instruction Fetch
		if((simulator.getExtoMem().getRegister("Branch").getValue() & simulator.getExtoMem().getRegister("Zero").getValue()) == 1)
		{
			// Stall in case of branch
			// 1. Clear IF/ID for next instruction
			simulator.getIFtoID().setRegister("Instruction", 0);
			simulator.getIFtoID().setRegister("PC", 0);
			
			// 2. Clear ID/EX for next instruction
			simulator.getIDtoEx().setRegister("MemWrite", 0);
			simulator.getIDtoEx().setRegister("MemRead", 0);
			simulator.getIDtoEx().setRegister("Branch", 0);
			simulator.getIDtoEx().setRegister("RegWrite", 0);
			
			// 3. Clear EX/MEM for next instruction
			simulator.getExtoMem().setRegister("MemWrite", 0);
			simulator.getExtoMem().setRegister("MemRead", 0);
			simulator.getExtoMem().setRegister("Branch", 0);
			simulator.getExtoMem().setRegister("RegWrite", 0);
			
			simulator.setInstructionNumber(1, Simulator.NOP);
			simulator.setInstructionNumber(2, Simulator.NOP);
			simulator.setInstructionNumber(3, Simulator.NOP);			
		}
		
	}	
}

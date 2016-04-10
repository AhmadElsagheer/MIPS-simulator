package stages;

import controller.Simulator;
import units.Register;

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
	
	/**
	 * @param opcode from the instruction to write the appropriate control signals.
	 */
	public void writeControlFlags(int opcode)
	{	
		int RegDst = 0;
		int Branch = 0;
		int MemRead = 0;
		int MemToReg = 0;
		int MemWrite = 0;
		int ALUSrc = 0;
		int RegWrite = 0;
		if (opcode == 0) // R type instruction.
		{
			RegDst = 1;
			RegWrite = 1;

		} else // I type format.
			if (opcode == 35) // lw instruction
		{
			MemRead = 1;
			MemToReg = 1;
			ALUSrc = 1;

		} 
			else if (opcode == 43) // sw instruction
		{
				MemWrite = 1;
				ALUSrc = 1;
		}
			else if (opcode == 4) //beq instruction
		{
				Branch = 1;
		}
			// JUMP To BE HANDELED HERE.
				
		simulator.getIDtoEx().setRegister("RegDst", RegDst);
		simulator.getIDtoEx().setRegister("Branch", Branch);
		simulator.getIDtoEx().setRegister("MemRead", MemRead);
		simulator.getIDtoEx().setRegister("MemToReg", MemToReg);
		simulator.getIDtoEx().setRegister("MemWrite", MemWrite);
		simulator.getIDtoEx().setRegister("ALUSrc", ALUSrc);
		simulator.getIDtoEx().setRegister("RegWrite", RegWrite);
	}
}

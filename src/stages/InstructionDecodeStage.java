package stages;

import controller.Simulator;
import units.Register;
import units.RegisterFile;

public class InstructionDecodeStage extends Stage{
	
	
	public InstructionDecodeStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	/**
	 * get instruction and decode then pass to the registers in the next pipeline.
	 */
	public void run() {
		// get instruction from previous pipeline.
		Register instructionReg = simulator.getIFtoID().getRegister("Instruction");
		RegisterFile regFile = simulator.getRegisterFile();

		// decode it.
		int opcode = instructionReg.getSegment(31, 26);
		int regT = instructionReg.getSegment(20, 16);
		int immValue = instructionReg.getSegment(15, 0);
		int regS = instructionReg.getSegment(25, 21);
		int shamt = instructionReg.getSegment(10, 6);

		// call writeControlFlags method to write control flags into next
		// pipeline register and pass the opcode to int
		writeControlFlags(opcode);
		// write register segments into next pipeline register
		if (opcode == 0) // R - type
		{
			int ALUOp = instructionReg.getSegment(5, 0);
			int regD = instructionReg.getSegment(15, 11);
			simulator.getIDtoEx().setRegister("ALUOp", ALUOp);
			// sign extend
			simulator.getIDtoEx().setRegister("ImmediateValue", shamt);
			simulator.getIDtoEx().setRegister("Destination1", regT);
			simulator.getIDtoEx().setRegister("Destination2", regD);
		}
		simulator.getIDtoEx().setRegister("ImmediateValue", immValue);

		// get corresponding registers from register file and write them in next
		// pipeline register
		simulator.getIDtoEx().setRegister("ReadData1", regFile.readRegister(regS).getValue());
		simulator.getIDtoEx().setRegister("ReadData2", regFile.readRegister(regT).getValue());
		simulator.getIDtoEx().setRegister("Destination1", regT);
		simulator.getIDtoEx().setRegister("Destination2", -1); // do not care in any other instruction
		simulator.getIDtoEx().setRegister("PC",simulator.getIFtoID().getRegister("PC").getValue()); // pass PC
		
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

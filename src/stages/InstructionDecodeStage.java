package stages;

import controller.Simulator;
import units.Register;
import units.RegisterFile;

public class InstructionDecodeStage extends Stage{

	/**
	 * Constructs a new instruction decode stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public InstructionDecodeStage(Simulator simulator)
	{
		super(simulator);
	}

	@Override
	/**
	 * Runs instruction decode stage.
	 */
	public void run() 
	{
		// get instruction from previous pipeline.
		Register instructionReg = simulator.getIFtoID().getRegister("Instruction");
		RegisterFile regFile = simulator.getRegisterFile();

		// decode it.
		int opcode = instructionReg.getSegment(31, 26);
		int readRegister1 = instructionReg.getSegment(25, 21);
		int readRegister2 = instructionReg.getSegment(20, 16);
		int destination2 = instructionReg.getSegment(15, 11);
		int immValue = instructionReg.getSegment(15, 0);
		// sign extend
		int signExtended = signExtend(immValue);

		// call writeControlFlags method to write control flags into next
		// pipeline register and pass the opcode to int
		writeControlFlags(opcode);

		
		// get corresponding registers from register file and write them in next pipeline register
		simulator.getIDtoEx().setRegister("PC",simulator.getIFtoID().getRegister("PC").getValue()); // pass PC
		simulator.getIDtoEx().setRegister("ReadData1", regFile.readRegister(readRegister1).getValue());
		simulator.getIDtoEx().setRegister("ReadData2", regFile.readRegister(readRegister2).getValue());
		simulator.getIDtoEx().setRegister("ImmediateValue", signExtended);
		simulator.getIDtoEx().setRegister("Destination1", readRegister2);
		simulator.getIDtoEx().setRegister("Destination2", destination2); 
		

	}
	
	
	/**
	 * takes the 16 bit immediata value, returns the 32 bit value after sign extension
	 * @param immValue
	 * @return
	 */
	private int signExtend(int immValue) {
		if(((1<<15) & immValue) == 0)
		{
			// positive
			return immValue;
		}
		else
		{
			// negative
			return (((1<<16) - 1) << 16) | immValue;
		}
			
	}

	/**
	 * opcode from the instruction to write the appropriate control signals.
	 * @param 
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
		int ALUOp = 0;
		if (opcode == 0) // R type instruction.
		{
			RegDst = 1;
			RegWrite = 1;
			ALUOp = 2;

		} 
		else // I type format.
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
				ALUOp = 1;
			}
		// TODO <-- JUMP To BE HANDELED HERE.

		// write the flags to the next pipeline register
		simulator.getIDtoEx().setRegister("RegDst", RegDst);
		simulator.getIDtoEx().setRegister("Branch", Branch);
		simulator.getIDtoEx().setRegister("MemRead", MemRead);
		simulator.getIDtoEx().setRegister("MemToReg", MemToReg);
		simulator.getIDtoEx().setRegister("MemWrite", MemWrite);
		simulator.getIDtoEx().setRegister("ALUSrc", ALUSrc);
		simulator.getIDtoEx().setRegister("RegWrite", RegWrite);
		simulator.getIDtoEx().setRegister("ALUOp", ALUOp);
	}
}

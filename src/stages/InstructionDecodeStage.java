package stages;

import controller.Simulator;
import units.Register;
import units.RegisterFile;

public class InstructionDecodeStage extends Stage{

	private boolean isEmptyInstruction;
	
	/**
	 * Constructs a new instruction decode stage.
	 * @param simulator the simulator to which the stage is associated
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
		isEmptyInstruction = simulator.getInstructionNumber(1) == Simulator.EMPTY;
		if(isEmptyInstruction)
		{
			simulator.setInstructionNumber(2, Simulator.EMPTY);
			return;
		}
		
		
		// get instruction from previous pipeline.
		Register instruction = simulator.getIFtoID().getRegister("Instruction");
		RegisterFile registerFile = simulator.getRegisterFile();

		// decode it.
		int opcode = instruction.getSegment(31, 26);
		int readRegister1 = instruction.getSegment(25, 21);
		int readRegister2 = instruction.getSegment(20, 16);
		int destination2 = instruction.getSegment(15, 11);
		int immediateValue = instruction.getSegment(15, 0);
		int signExtend = signExtend(immediateValue);

		if(simulator.getIDtoEx().getRegister("MemRead").getValue() == 1 
			&& (simulator.getIDtoEx().getRegister("Destination1").getValue() == readRegister1 
			|| simulator.getIDtoEx().getRegister("Destination1").getValue() == readRegister2))
		{
			// Stall
			simulator.getIDtoEx().setRegister("MemWrite", 0);
			simulator.getIDtoEx().setRegister("MemRead", 0);
			simulator.getIDtoEx().setRegister("Branch", 0);
			simulator.getIDtoEx().setRegister("RegWrite", 0);
			simulator.getInstructionFetchStage().setPCWrite(0);
			
			// Set Next Instruction for Instruction Fetch/Decode/Execution
			simulator.getIFtoID().selfUpdate();
			simulator.setInstructionNumber(0, simulator.getInstructionNumber(0));
			simulator.setInstructionNumber(1, simulator.getInstructionNumber(1));
			simulator.setInstructionNumber(2, Simulator.NOP);
		}
		else
		{
			writeControlFlags(opcode);

			// write to next pipeline
			simulator.getIDtoEx().setRegister("PC", simulator.getIFtoID().getRegister("PC").getValue());
			simulator.getIDtoEx().setRegister("ReadData1", registerFile.readRegister(readRegister1).getValue());
			simulator.getIDtoEx().setRegister("ReadData2", registerFile.readRegister(readRegister2).getValue());
			simulator.getIDtoEx().setRegister("ImmediateValue", signExtend);
			simulator.getIDtoEx().setRegister("Destination1", readRegister2);
			simulator.getIDtoEx().setRegister("Destination2", destination2);
			simulator.getIDtoEx().setRegister("rs", readRegister1);
			
			// Set Next Instruction for Execution
			simulator.setInstructionNumber(2, simulator.getInstructionNumber(1));
		}
	}
	
	/**
	 * Re-read the values of the registers. Used by WriteBackStage 
	 * to prevent falsy data read
	 */
	public void redoReadRegisters()
	{
		if(isEmptyInstruction)
			return;
		Register instruction = simulator.getIFtoID().getRegister("Instruction");
		RegisterFile registerFile = simulator.getRegisterFile();
		int readRegister1 = instruction.getSegment(25, 21);
		int readRegister2 = instruction.getSegment(20, 16);
		simulator.getIDtoEx().setRegister("ReadData1", registerFile.readRegister(readRegister1).getValue());
		simulator.getIDtoEx().setRegister("ReadData2", registerFile.readRegister(readRegister2).getValue());
	}

	/**
	 * Extends the sign of the 16-bit input value to be a 32-bit value.
	 * @param value the value to be sign-extended
	 * @return the new value after sign extension
	 */
	private int signExtend(int value) 
	{
		if(((1<<15) & value) == 0)
			return value;
		return (((1<<16) - 1) << 16) | value;	
	}

	/**
	 * Generates control signals and writes them to the next pipeline register.
	 * @param opcode the opcode of the instruction
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
		switch(opcode)
		{
			case 0:  ALUOp = 1 + (RegDst = RegWrite = 1); break;				// R-format instructions
			case 35: MemRead = ALUSrc = RegWrite = MemToReg = 1; break;			// LW instruction
			case 43: MemWrite = ALUSrc = 1; break;								// SW instruction
			case 4:  Branch = ALUOp = 1; break;									// BEQ instruction
			//TODO: ALUOp is assumed to be 00 the same as LW/SW, need to find the exact value for ADDI instruction
			case 8:  RegWrite = ALUSrc = 1; break;				// ADDI
			default: ; //TODO: missing instructions
		}

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

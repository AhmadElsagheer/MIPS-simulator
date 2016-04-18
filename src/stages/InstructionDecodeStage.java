package stages;

import controller.Simulator;
import units.Register;
import units.RegisterFile;

public class InstructionDecodeStage extends Stage{

	private int IFtoIDWrite;

	/**
	 * Constructs a new instruction decode stage.
	 * @param simulator the simulator to which the stage is associated
	 */
	public InstructionDecodeStage(Simulator simulator)
	{
		super(simulator);
		IFtoIDWrite=1;
	}

	@Override
	/**
	 * Runs instruction decode stage.
	 */
	public void run() 
	{
		if (IFtoIDWrite == 0)
		{
			IFtoIDWrite = 1;
			simulator.getInstructionFetchStage().setPCWrite(1);
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
			simulator.getIDtoEx().setRegister("MemWrite", 0);
			simulator.getIDtoEx().setRegister("MemRead", 0);
			simulator.getIDtoEx().setRegister("Branch", 0);
			simulator.getIDtoEx().setRegister("RegWrite", 0);
			IFtoIDWrite = 0;
			simulator.getInstructionFetchStage().setPCWrite(0);

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
		}
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
		case 0:  ALUOp = 1 + (RegDst = RegWrite = 1); break;	// R-format instructions
		case 35: MemRead = MemToReg = ALUSrc = 1; break;		// LW instruction
		case 43: MemWrite = ALUSrc = 1; break;					// SW instruction
		case 4:  Branch = ALUOp = 1; break;						// BEQ instruction
		default:	; //TODO: missing instructions
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

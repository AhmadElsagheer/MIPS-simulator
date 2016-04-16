package stages;

import controller.Simulator;
import units.PipelineRegister;

public class ExecutionStage extends Stage{

	/**
	 * Constructs a new execution stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public ExecutionStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	/**
	 * Runs execution stage.
	 */
	public void run() 
	{	
		PipelineRegister IDtoEx = simulator.getIDtoEx(), ExtoMem = simulator.getExtoMem();
		
		// 1. Read from the previous pipeline register
		int funct 			= IDtoEx.getRegister("ImmediateValue").getSegment(5, 0);
		int PC 				= IDtoEx.getRegister("PC").getValue();
		int ALUOp			= IDtoEx.getRegister("ALUOp").getValue();
		int regDst 			= IDtoEx.getRegister("RegDst").getValue();
		int ALUSrc 			= IDtoEx.getRegister("ALUSrc").getValue();
		int readData1		= IDtoEx.getRegister("ReadData1").getValue();
		int readData2		= IDtoEx.getRegister("ReadData2").getValue();
		int immediateValue 	= IDtoEx.getRegister("ImmediateValue").getValue();
		int destination1 	= IDtoEx.getRegister("Destination1").getValue();
		int destination2 	= IDtoEx.getRegister("Destination2").getValue();
		
		// 2. ALU Execution
		ALU(readData1, ALUSrc == 1 ? immediateValue : readData2, ALUControl(funct, ALUOp));		
	
		// 3. Write to next pipeline register	
		ExtoMem.setRegister("BranchAddress", PC + (immediateValue << 2));
		ExtoMem.getRegister("Destination").setValue(regDst == 0 ? destination1 : destination2);	
		ExtoMem.getRegister("RegWrite").setValue(IDtoEx.getRegister("RegWrite").getValue());
		ExtoMem.getRegister("MemToReg").setValue(IDtoEx.getRegister("MemToReg").getValue());
		ExtoMem.getRegister("Branch").setValue(IDtoEx.getRegister("Branch").getValue());
		ExtoMem.getRegister("MemRead").setValue(IDtoEx.getRegister("MemRead").getValue());
		ExtoMem.getRegister("MemWrite").setValue(IDtoEx.getRegister("MemWrite").getValue());
	}
	
	
	/**
	 * Computes the selector for the ALU unit
	 * @param funct function code
	 * @param ALUOp ALU operation code
	 * @return the ALU selector
	 */
	public int ALUControl(int funct, int ALUOp)
	{
		int ALUSelector = 0;
		if(ALUOp == 2)	// R-format instruction
		{		
			switch (funct)
			{
			case 32: ALUSelector = 2; break;
			case 34: ALUSelector = 6; break;
			case 36: ALUSelector = 0; break;
			case 37: ALUSelector = 1; break;
			case 42: ALUSelector = 7; break;
			}
		}
		else			// I-format instruction
		{
			if(ALUOp == 0)
				ALUSelector = 2;
			else if(ALUOp == 1)
				ALUSelector = 6;
		}
		return ALUSelector;
	}
	
	/**
	 * Computes an arithmetic/logical operation and writes the result to EXtoMem pipeline register
	 * @param source1 the first source to ALU unit
	 * @param source2 the second source to ALU unit
	 * @param control the ALU selector
	 */
	public void ALU(int source1, int source2, int control)
	{
		int ALUResult = 0, zeroFlag = 0;
		
		// 1. Perform the operation
		switch (control)
		{
		case 0: ALUResult = source1 & source2; break;
		case 1: ALUResult = source1 | source2; break;
		case 2: ALUResult = source1 + source2; break;
		case 6: ALUResult = source1 - source2; break;
		case 7: ALUResult = (source1 < source2) ? 1 : 0; break;
		}
		
		// 2. Assign the value of the zero flag
		if(ALUResult == 0)
			zeroFlag = 1;
		
		// 3. Set the registers values in the next pipeline register
		simulator.getExtoMem().getRegister("ALUResult").setValue(ALUResult);
		simulator.getExtoMem().getRegister("Zero").setValue(zeroFlag);
	}		
}

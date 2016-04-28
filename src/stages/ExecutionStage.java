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
		if(simulator.getInstructionNumber(2) == Simulator.EMPTY)
		{
			simulator.setInstructionNumber(3, Simulator.EMPTY);
			return;
		}
		
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
		int rs 				= IDtoEx.getRegister("rs").getValue();
		int destination1 	= IDtoEx.getRegister("Destination1").getValue();
		int destination2 	= IDtoEx.getRegister("Destination2").getValue();


		// handling forwarding for readData1
		if(simulator.getExtoMem().getRegister("RegWrite").getValue() == 1 && rs != 0
		   && simulator.getExtoMem().getRegister("Destination").getValue() == rs)
			readData1 = simulator.getExtoMem().getRegister("ALUResult").getValue();		
		else if (simulator.getMemtoWb().getRegister("RegWrite").getValue() == 1 && rs != 0
				&& simulator.getMemtoWb().getRegister("Destination").getValue() == rs)	
					readData1 	= simulator.getMemtoWb().getRegister("MemToReg").getValue() == 0 
								? simulator.getMemtoWb().getRegister("ALUResult").getValue() 
								: simulator.getMemtoWb().getRegister("MemoryOutput").getValue();
			
		// handling forwarding for readData2
		if(simulator.getExtoMem().getRegister("RegWrite").getValue() == 1 && destination1 != 0
			&& simulator.getExtoMem().getRegister("Destination").getValue() == destination1)
						readData2 = simulator.getExtoMem().getRegister("ALUResult").getValue();	
		else if (simulator.getMemtoWb().getRegister("RegWrite").getValue() == 1 && destination1 != 0 
				&& simulator.getMemtoWb().getRegister("Destination").getValue() == destination1)
					readData2 	= simulator.getMemtoWb().getRegister("MemToReg").getValue() == 0 
								? simulator.getMemtoWb().getRegister("ALUResult").getValue() 
								: simulator.getMemtoWb().getRegister("MemoryOutput").getValue();
		// 2. ALU Execution
		ALU(readData1, ALUSrc == 1 ? immediateValue : readData2, ALUControl(funct, ALUOp));		
		// 3. Write to next pipeline register	
		ExtoMem.setRegister("BranchAddress", PC + (immediateValue << 2));
		ExtoMem.setRegister("Destination", regDst == 0 ? destination1 : destination2);	
		ExtoMem.setRegister("RegWrite", IDtoEx.getRegister("RegWrite").getValue());
		ExtoMem.setRegister("MemToReg", IDtoEx.getRegister("MemToReg").getValue());
		ExtoMem.setRegister("Branch", IDtoEx.getRegister("Branch").getValue());
		ExtoMem.setRegister("MemRead", IDtoEx.getRegister("MemRead").getValue());
		ExtoMem.setRegister("MemWrite", IDtoEx.getRegister("MemWrite").getValue());
		ExtoMem.setRegister("ReadData2", readData2);
		
		
		// Set Next Instruction for Memory
		simulator.setInstructionNumber(3, simulator.getInstructionNumber(2));
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
		simulator.getExtoMem().setRegister("ALUResult", ALUResult);
		simulator.getExtoMem().setRegister("Zero", zeroFlag);
		
	}		
}

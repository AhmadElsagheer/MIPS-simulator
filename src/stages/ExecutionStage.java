package stages;

import controller.Simulator;

public class ExecutionStage extends Stage{

	public ExecutionStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	public void run() {
		
		// Get all the needed registers from the previous pipeline register
		int funct = simulator.getIDtoEx()
				.getRegister("ImmediateValue")
				.getSegment(5, 0);
		int PC = simulator.getIDtoEx()
				.getRegister("PC")
				.getValue();
		int ALUOp = simulator.getIDtoEx()
				.getRegister("ALUOp")
				.getValue();
		int regDst = simulator.getIDtoEx()
				.getRegister("RegDst")
				.getValue();
		int ALUSrc = simulator.getIDtoEx()
				.getRegister("ALUSrc")
				.getValue();
		int readData1 = simulator.getIDtoEx()
				.getRegister("readData1")
				.getValue();
		int readData2 = simulator.getIDtoEx()
				.getRegister("readData2")
				.getValue();
		int immediateValue = simulator.getIDtoEx()
				.getRegister("immediateValue")
				.getValue();
		int destination1 = simulator.getIDtoEx()
				.getRegister("Destination1")
				.getValue();
		int destination2 = simulator.getIDtoEx()
				.getRegister("Destination1")
				.getValue();
		
		// Get the ALU Selector
		int ALUSelector = ALUControl(funct, ALUOp);
		
		
		// Assign ALU input sources
		int source1 = readData1;
		int source2 = readData2;
		if(ALUSrc == 1)
			source2 = immediateValue;
		
		// Perform the ALU Operation
		ALU(source1, source2, ALUSelector);
		
		
		// Write the branch address in the next pipeline register
		writeBranchAddress(PC, immediateValue);
		
		// determine register destination 
		writeRegisterDestination(regDst, destination1, destination2);
		
		// write the remaining flags to the next pipeline
		simulator.getExtoMem().getRegister("RegWrite").setValue(
				simulator.getIDtoEx().getRegister("RegWrite").getValue() 
				);
		simulator.getExtoMem().getRegister("MemToReg").setValue(
				simulator.getIDtoEx().getRegister("MemToReg").getValue() 
				);
		simulator.getExtoMem().getRegister("Branch").setValue(
				simulator.getIDtoEx().getRegister("Branch").getValue() 
				);
		simulator.getExtoMem().getRegister("MemRead").setValue(
				simulator.getIDtoEx().getRegister("MemRead").getValue() 
				);
		simulator.getExtoMem().getRegister("MemWrite").setValue(
				simulator.getIDtoEx().getRegister("MemWrite").getValue() 
				);
	}
	
	
	/**
	 * Takes the function code and the ALUOp and returns the selector for the ALU Unit
	 * @param funct
	 * @param ALUOp
	 * @return
	 */
	public int ALUControl(int funct, int ALUOp)
	{
		int ALUSelector = 0;
		if(ALUOp == 2)
		{
			//R-type instruction
			switch (funct)
			{
			case 32: ALUSelector = 2; break;
			case 34: ALUSelector = 6; break;
			case 36: ALUSelector = 0; break;
			case 37: ALUSelector = 1; break;
			case 42: ALUSelector = 7; break;
			}
		}
		else
		{
			//I-type instruction
			if(ALUOp == 0)
				ALUSelector = 2;
			else if(ALUOp == 1)
				ALUSelector = 6;
		}
		return ALUSelector;
	}
	
	/**
	 * Takes the ALU sources and the selector, performs arithmetic/logic operation, and writes
	 * into next pipeline register
	 * @param source1
	 * @param source2
	 * @param control
	 */
	public void ALU(int source1, int source2, int control)
	{
		int ALUResult = 0;
		int zeroFlag = 0;
		
		// Perform the operation
		switch (control)
		{
		case 0: ALUResult = source1 & source2; break;
		case 1: ALUResult = source1 | source2; break;
		case 2: ALUResult = source1 + source2; break;
		case 6: ALUResult = source1 - source2; break;
		case 7: ALUResult = (source1 < source2) ? 1 : 0; break;
		}
		
		// Assign the value of the zero flag
		if(ALUResult == 0)
			zeroFlag = 1;
		
		// Set the registers values in the next pipeline register
		simulator.getExtoMem().getRegister("ALUResult").setValue(ALUResult);
		simulator.getExtoMem().getRegister("Zero").setValue(zeroFlag);
	}
	
	
	/**
	 * Takes the new PC and the offset to add to, calculates the branch address and
	 * writes it into EX/MEM pipeline register
	 * @param PC
	 * @param offset
	 */
	public void writeBranchAddress(int PC, int offset)
	{
		// Multiply offset by 4
		offset <<= 2;
		
		// Add offset to PC
		int branchAddress = PC + offset;
		
		// Write branch address to the next pipeline register
		simulator.getExtoMem().setRegister("BranchAddress", branchAddress);
		
	}
	
	
	/**
	 * Determines the destination register to write in, according to RegDst flag
	 * @param regDst
	 * @param destination1
	 * @param destination2
	 */
	private void writeRegisterDestination(int regDst, int destination1, int destination2) {
		if(regDst == 0)
		{
			simulator.getExtoMem().getRegister("Destination").setValue(destination1);
		}
		else
		{
			simulator.getExtoMem().getRegister("Destination").setValue(destination2);
		}
	}

	
	
}

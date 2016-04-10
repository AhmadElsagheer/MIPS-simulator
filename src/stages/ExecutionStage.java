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
		
		// determine register destination according to the flag from the previous pipeline and write to next pipeline
		
		// check the source of 2nd ALU input according to ALUSrc the flag
		// call ALU function according to the controls returned from the ALUControl function and write to the next pipeline
		// shift [15-0] by 2 and add to PC from the previous pipeline and write to the next pipeline
		// write the remaining flags to the next pipeline
		
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
	
	public void ALU(int source1, int source2, int control)
	{
		
	}
	
	
	
}

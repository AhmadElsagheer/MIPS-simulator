package stages;

import controller.Simulator;

public class ExecutionStage extends Stage{

	public ExecutionStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	public void run() {
		
		// separate 6 bits from previous pipeline to be used in ALU control
		// determine register destination according to the flag from the previous pipeline and write to next pipeline
		// check the source of 2nd ALU input according to ALUSrc the flag
		// call ALU function according to the controls returned from the ALUControl function and write to the next pipeline
		// shift [15-0] by 2 and add to PC from the previous pipeline and write to the next pipeline
		// write the remaining flags to the next pipeline
		
	}
	
	
	public int ALUControl(int funct, int ALUOp)
	{
		
		return 0;
	}
	
	public void ALU(int source1, int source2, int control)
	{
		
	}
	
	
	
}

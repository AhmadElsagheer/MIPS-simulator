package stages;

import controller.Simulator;

public class WriteBackStage extends Stage{

	/**
	 * Constructs a new write back stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public WriteBackStage(Simulator simulator)
	{
		super(simulator);
	}

	@Override
	/**
	 * Runs write back stage.
	 */
	public void run() 
	{
		if(simulator.getInstructionNumber(4) == Simulator.EMPTY)
			return;

		if(simulator.getMemtoWb().getRegister("RegWrite").getValue() == 1)
		{
			int value = 0;
			if(simulator.getMemtoWb().getRegister("MemToReg").getValue() == 1)
				value = simulator.getMemtoWb().getRegister("MemoryOutput").getValue();
			else
				value = simulator.getMemtoWb().getRegister("ALUResult").getValue();
			simulator.getRegisterFile().writeRegister(simulator.getMemtoWb().getRegister("Destination").getValue(), value);

			// Handle RAW where read and write occur in the same clock cycle
			// Fix any falsy read data that should be equal to the value already calculated right away
			simulator.getInstructionDecodeStage().redoReadRegisters();
			
		}
	}

}

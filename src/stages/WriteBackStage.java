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
			if(simulator.getMemtoWb().getRegister("MemToReg").getValue() == 0)
				value = simulator.getMemtoWb().getRegister("MemoryOutput").getValue();
			else
				value = simulator.getMemtoWb().getRegister("ALUResult").getValue();
			simulator.getRegisterFile().writeRegister(simulator.getMemtoWb().getRegister("Destination").getValue(), value);
		}
	}

}

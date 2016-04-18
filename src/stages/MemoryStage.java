package stages;

import controller.Simulator;

public class MemoryStage extends Stage{

	/**
	 * Constructs a new memory stage.
	 * @param simulator the simulator to which the stage is associated.
	 */
	public MemoryStage(Simulator simulator)
	{
		super(simulator);
	}
	
	@Override
	/**
	 * Runs memory stage.
	 */
	public void run() 
	{
		simulator.getMemtoWb().setRegister("RegWrite", simulator.getExtoMem().getRegister("RegWrite").getValue());
		
		simulator.getMemtoWb().setRegister("MemToReg", simulator.getExtoMem().getRegister("MemToReg").getValue());
		
		simulator.getMemtoWb().setRegister("Destination", simulator.getExtoMem().getRegister("Destination").getValue());
		
		if(simulator.getMemtoWb().getRegister("MemRead").getValue()==0)
			simulator.getMemtoWb().setRegister("MemoryOutput", simulator.getDataMemory().read(simulator.getExtoMem().getRegister("ALUResult").getValue()));
	
		if(simulator.getMemtoWb().getRegister("MemWrite").getValue()==0)
			simulator.getDataMemory().write(simulator.getDataMemory().read(simulator.getExtoMem().getRegister("ALUResult").getValue()), simulator.getDataMemory().read(simulator.getExtoMem().getRegister("ReadData2").getValue()));		
	}	
}

package units;


public class InstructionMemory {
	
	int[] instructions;
	
	/**
	 * Constructs a new instruction memory with the specified size
	 * @param size the maximum number of instructions the memory can hold
	 */
	public InstructionMemory(int size)
	{
		instructions = new int[size];
	}
	
	
	public int getInstruction(int index)
	{
		return instructions[index/4];
	}
	
	public void setInstruction(int index, int value)
	{
		instructions[index/4] = value;
	}
	
	//who needs it?
	public void setInstructions(int[] instructions)
	{
		this.instructions = instructions;
	}
	
	
}
	

package units;


public class InstructionMemory {
	
	int[] instructions;
	int numberOfInstructions;
	
	/**
	 * Constructs a new instruction memory with the specified size
	 * @param size the maximum number of instructions the memory can hold
	 */
	public InstructionMemory(int size)
	{
		instructions = new int[size];
	}
	
	/**
	 * Gets the instruction at the specified index (byte addressing)
	 * @param index the base address of the instruction
	 * @return a 32-bit instruction retrieved from index
	 */
	public int getInstruction(int index)
	{
		return instructions[index/4];
	}
	
	/**
	 * Sets the instruction at the specified index (byte addressing)
	 * @param index the base address of the instruction
	 * @param a 32-bit instruction to be stored at index
	 */
	public void setInstruction(int index, int value)
	{
		instructions[index/4] = value;
		numberOfInstructions++;
	}
	
	/**
	 * Gets the number of instructions stored in the instruction memory
	 * which is equivalent to the number of used bytes times four
	 * @return the number of instructions in the memory
	 */
	public int getNumberOfInstructions()
	{
		return numberOfInstructions;
	}
	
	
}
	

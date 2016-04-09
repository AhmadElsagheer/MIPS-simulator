package units;

public class InstructionMemory {
	int[] instructions;
	
	public InstructionMemory(int size)
	{
		instructions = new int[size];
	}
	
	
	public int getInstruction(int index)
	{
		return instructions[index];
	}
	
	public void setInstruction(int index, int value)
	{
		instructions[index] = value;
	}
	
	
	public void setInstructions(int[] instructions)
	{
		this.instructions = instructions;
	}
	
	
}
	

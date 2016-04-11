package units;

public class DataMemory {
	private int[] data;
	
	/**
	 * Constructs a new memory for Data with the specified size
	 * @param size the maximum number of words the memory can hold
	 */
	public DataMemory(int size)
	{
		data = new int[size];
	}
	
	/**
	 * Reads the word from the memory at the specified address
	 * @param address the address of the word to be retrieved (byte addressing)
	 * @return the word at the specified address
	 */
	public int read(int address)
	{
		return data[address/4];
	}
	
	/**
	 * Write a word to the memory at the specified address
	 * @param address the address at which the word will be stored (byte addressing)
	 * @param value the word to be stored in the memory
	 */
	public void write(int address, int value)
	{
		data[address/4] = value;
	}
}

package units;

public class DataMemory {
	private int[] data;
	
	public DataMemory(int size)
	{
		data = new int[size];
	}
	
	public int read(int address)
	{
		return data[address/4];
	}
	
	public void write(int address, int value)
	{
		data[address/4] = value;
	}
}

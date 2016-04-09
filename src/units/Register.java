package units;

public class Register {
	private int size;
	private int value;
	
	
	public Register(int size)
	{
		this.size = size;
	}
	
	//Get part of the register between left and right bits numbers (inclusive)
	public int getSegment(int left, int right)
	{
		return (value >> right) & ((1 << left - right + 1)-1); 
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public int getSize()
	{
		return size;
	}
	
	
	
	
}

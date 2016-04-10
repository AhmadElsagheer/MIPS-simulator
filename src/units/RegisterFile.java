package units;

public class RegisterFile {

	Register[] registers;
	
	/**
	 * Constructs a new 32-register file
	 */
	public RegisterFile()
	{
		registers = new Register[32];
	}
	
	
	public Register readRegister(int index)
	{
		return registers[index];
	}
	
	public void writeRegister(int index, int value)
	{
		registers[index].setValue(value);
	}
	
}

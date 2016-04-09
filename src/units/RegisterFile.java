package units;

public class RegisterFile {

	Register[] registers;
	
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

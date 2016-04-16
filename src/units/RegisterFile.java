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
	
	/**
	 * Read the register at the specified index
	 * @param index the index of the register to be read
	 * @return the register at the given index
	 */
	public Register readRegister(int index)
	{
		return registers[index];
	}
	
	/**
	 * Write the passed value to the register at the specified index
	 * @param index the index of the register to write at
	 * @param value the value to be written
	 */
	public void writeRegister(int index, int value)
	{
		registers[index].setValue(value);
	}
	
}

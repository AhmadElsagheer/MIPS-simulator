package units;

import java.util.HashMap;
import java.util.Map.Entry;

public class PipelineRegister {
	
	private HashMap<String, Register> registers;
	private PipelineRegister tmpRegister;
	
	/**
	 * Constructs a new pipeline register. This register will have a temporary pipeline register
	 * to avoid any overlappping within the simulation process.
	 * @param type the type of the pipeline register
	 */
	public PipelineRegister(int type)
	{
		this(type, false);
	}
	
	/**
	 * Constructs a new pipeline register. 
	 * It populates the pipeline register with mini registers according to its type.
	 * @param type the type of the pipeline register
	 * @param fake an indicator whether the constructed register is the original one
	 * or a temporary (fake) pipeline register.
	 */
	public PipelineRegister(int type, boolean fake)
	{
		registers = new HashMap<String, Register>();
		
		if(type == 0)											// IFtoID
		{
			registers.put("PC", new Register(32));
			registers.put("Instruction", new Register(32));
		}
		else if(type == 1)										// IDtoEx
		{
			registers.put("RegWrite", new Register(1));
			registers.put("MemToReg", new Register(1));
			registers.put("Branch", new Register(1));
			registers.put("MemRead", new Register(1));
			registers.put("MemWrite", new Register(1));
			registers.put("RegDst", new Register(1));
			registers.put("ALUOp", new Register(2));
			registers.put("ALUSrc", new Register(1));
			registers.put("PC", new Register(32));
			registers.put("ReadData1", new Register(32));
			registers.put("ReadData2", new Register(32));
			registers.put("ImmediateValue", new Register(32));
			registers.put("rs", new Register(5));
			registers.put("Destination1", new Register(5));
			registers.put("Destination2", new Register(5));
		}
		else if(type == 2)										// ExToMem
		{
			registers.put("RegWrite", new Register(1));
			registers.put("MemToReg", new Register(1));
			registers.put("Branch", new Register(1));
			registers.put("MemRead", new Register(1));
			registers.put("MemWrite", new Register(1));
			registers.put("BranchAddress", new Register(32));
			registers.put("Zero", new Register(1));
			registers.put("ALUResult", new Register(32));
			registers.put("ReadData2", new Register(32));
			registers.put("Destination", new Register(5));
		}
		else													// MemToWB
		{
			registers.put("RegWrite", new Register(1));
			registers.put("MemToReg", new Register(1));
			registers.put("MemoryOutput", new Register(32));
			registers.put("ALUResult", new Register(32));
			registers.put("Destination", new Register(5));
		}
		
		if(!fake)
			tmpRegister = new PipelineRegister(type, true);
	}
	
	/**
	 * Gets the mini register in the pipeline register with the passed name.
	 * @param registerName the name of the mini register
	 * @return the mini register with the passed name
	 */
	public Register getRegister(String registerName)
	{
		return registers.get(registerName);
	}

	/**
	 * Sets the mini register with the specified name in the pipeline register with the passed value.
	 * This value will be temporarily stored in a temporary register until pipeline register is updated.
	 * @param registerName the name of the mini register
	 * @param value the new value of the mini register
	 */
	public void setRegister(String registerName, int value)
	{
		tmpRegister.getRegister(registerName).setValue(value);
	}
	
	/**
	 * Updates the pipeline register by moving the contents of the mini registers of the temporary
	 * pipeline register to those of the current pipeline register.
	 */
	public void update()
	{
		for(Entry<String, Register> entry: registers.entrySet())
		{
			String registerName = entry.getKey();
			registers.get(registerName).setValue(tmpRegister.getRegister(registerName).getValue());
			tmpRegister.getRegister(registerName).clear();
		}
	}
	
	public void selfUpdate()
	{
		for(Entry<String, Register> entry: registers.entrySet())
			setRegister(entry.getKey(), entry.getValue().getValue());
	}
	
	public String toString()
	{
		String r = "";
		for(Entry<String, Register> entry: registers.entrySet())
			r += String.format("%s %s\n", entry.getKey(), entry.getValue());
		return r;
	}
	
}

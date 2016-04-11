package units;

import java.util.HashMap;
import java.util.Map.Entry;

public class PipelineRegister {
	
	private HashMap<String, Register> registers;
	private PipelineRegister tmpRegister;
	
	public PipelineRegister(int type)
	{
		this(type, false);
	}
	
	public PipelineRegister(int type, boolean fake)
	{
		registers = new HashMap<String, Register>();
		//initialize registers according to type
		if(type == 0)
		{
			registers.put("PC", new Register(32));
			registers.put("Instruction", new Register(32));
		}
		else if(type == 1)
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
			registers.put("Destination1", new Register(5));
			registers.put("Destination2", new Register(5));
		}
		else if(type == 2)
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
		else
		{
			registers.put("RegWrite", new Register(1));
			registers.put("MemToReg", new Register(1));
			registers.put("MemoryOutput", new Register(32));
			registers.put("ALUResult", new Register(32));
			registers.put("Destination", new Register(5));
		}

		registers = new HashMap<String, Register>();
		
		if(!fake)
			tmpRegister = new PipelineRegister(type, true);
	}
	
	public Register getRegister(String registerName)
	{
		return registers.get(registerName);
	}
	
	public void setRegister(String registerName, int value)
	{
		tmpRegister.setRegister(registerName, value);
	}
	
	public void update()
	{
		for(Entry<String, Register> entry: registers.entrySet())
		{
			String registerName = entry.getKey();
			registers.get(registerName).setValue(tmpRegister.getRegister(registerName).getValue());
			tmpRegister.getRegister(registerName).clear();
		}
	}
	
}

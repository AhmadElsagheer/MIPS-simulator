package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class Parser {

	private HashMap<String, Integer> registerMap;
	private HashMap<String, Character> instructionFormat;
	private HashMap<String, Integer> commandMap;
	
	/**
	 * Constructs a new Parser (Assembler)
	 * @throws FileNotFoundException if the program file or instruction set are not found
	 */
	public Parser() throws FileNotFoundException
	{
		initializeRegisterMap();
		initializeCommands();
	}
	
	/**
	 * Initializes the register map which maps register names to their index in the register file
	 */
	private void initializeRegisterMap()
	{
		registerMap = new HashMap<String, Integer>();
		registerMap.put("$0", 0); registerMap.put("$zero", 0); registerMap.put("$at", 1);
		registerMap.put("$gp", 28); registerMap.put("$sp", 29); registerMap.put("$fp", 30);
		registerMap.put("$ra", 31);
		initializeRegisterMap('v', 2, 0, 2); initializeRegisterMap('a', 4, 0, 4);
		initializeRegisterMap('t', 8, 0, 8); initializeRegisterMap('s', 8, 0, 16);
		initializeRegisterMap('t', 2, 8, 24); initializeRegisterMap('k', 2, 0, 26);
	}
	
	/**
	 * Sets the names of the registers in the register file to be used in printing.
	 * @param simulator the simulator containing the register file
	 */
	public void setRegistersNames(Simulator simulator)
	{
		for(Entry<String, Integer> entry: registerMap.entrySet())
			simulator.getRegisterFile().readRegister(entry.getValue()).setName(entry.getKey());
	}
	
	/**
	 * Gets the index of the register in the register file.
	 * @param name the name of the register to get its index
	 * @return the index of the register
	 */
	public int getRegisterNumber(String name)
	{
		return registerMap.get(name);
	}
	
	/**
	 * Initializes the register map with registers sharing the same symbol
	 * @param symbol the common symbol among registers
	 * @param count the number of registers to be mapped
	 * @param start the number of the first register
	 * @param base the base index in the register file to map at
	 */
	private void initializeRegisterMap(char symbol, int count, int start, int base)
	{
		for(int i = 0; i < count; ++i)
			registerMap.put("$"+symbol+(start+i), base+i);
	}
	
	/**
	 * Initializes the command map which maps the assembly commands to their opcodes
	 * (and function in case of R-format instructions)
	 * @throws FileNotFoundException if the instruction set is not found
	 */
	private void initializeCommands() throws FileNotFoundException
	{
		instructionFormat = new HashMap<String, Character>();
		commandMap = new HashMap<String, Integer>();
		Scanner sc = new Scanner(new FileReader("resources/instructionSet.csv"));
		sc.nextLine();		//consume header
		while(sc.hasNextLine())
		{
			String[] instruction = sc.nextLine().split(",");
			instructionFormat.put(instruction[0], instruction[1].charAt(0));
			int command = Integer.parseInt(instruction[2], 16)<<26;
			if(instruction[1].charAt(0) == 'R')
				command |= Integer.parseInt(instruction[3], 16);
			commandMap.put(instruction[0], command);
		}	
		sc.close();
	}
	
	/**
	 * Parses the passed register to its binary representation
	 * @param register the name of the register to be parsed
	 * @param shift left shift amout to place it in the correct position in the instruction
	 * @return partial machine instruction containing the parsed register in the specified position
	 * @throws Exception if the register is not valid
	 */
	private int parseRegister(String register, int shift) throws Exception
	{
		if(!registerMap.containsKey(register))
			throw new Exception("Register " + register + " is not valid.");
		return registerMap.get(register) << shift;
	}
	
	/**
	 * Converts a constant or an address to binary
	 * @param constant the constant or address to be converted
	 * @param shift left shift amout to place it in the correct position in the instruction
	 * @return the binary representation of the constant or address placed in its position within the instruction
	 */
	private int parseConstant(String constant, int shift, int size)
	{
		if(constant.length() > 2 && constant.charAt(1) == 'x')
			return (Integer.parseInt(constant.substring(2), 16) & ((1<<size) - 1)) << shift;
		return (Integer.parseInt(constant) & ((1<<size) - 1)) << shift;
	}
	
	/**
	 * Splits an address in memory to an offset and base address
	 * @param rsAndOffset the assembly representation of the combination
	 * @return an array of two strings containing the base address and the offset, respectively
	 * @throws Exception if the combination is not in the correct format
	 */
	private String[] splitAddress(String rsAndOffset) throws Exception
	{
		String offset = "";
		for(int i = 0; i < rsAndOffset.length(); ++i)
			if(rsAndOffset.charAt(i) == '(')
				return new String[] { rsAndOffset.substring(i+1, rsAndOffset.length()-1),  offset};
			else
				offset += rsAndOffset.charAt(i);
		throw new Exception("Bad I-Format");
	}
	
	/**
	 * Remove any unnecessary tokens which result from splitting with blanks and commas
	 * @param in the input array
	 * @return the filtered array
	 */
	private String[] filterTokens(String[] in)
	{
		int i = 0, j = 0;
		while(i < in.length)
		{
			if(in[i] != null && !in[i].trim().isEmpty())
				in[j++] = in[i];
			++i;
		}
		return Arrays.copyOf(in, j);
	}
	
	/**
	 * Parses a single instruction from assembly to machine language
	 * @param assembly the assembly instruction as tokens
	 * @return the machine(binary) instruction
	 * @throws Exception if the instruction has bad command/format
	 */
	private int parse(String[] assembly) throws Exception
	{
		Character format = instructionFormat.get(assembly[0]);
		if(format == null)
			throw new Exception("Bad command: "+assembly[0]);
		
		int instruction = 0;
		if(format == 'R')
		{
			if(assembly.length != 4)
				throw new Exception("Bad R-format instruction");
			
			instruction |= commandMap.get(assembly[0]);					//opcode and funct
			instruction |= parseRegister(assembly[2], 21); 				//rs - source1
			instruction |= parseRegister(assembly[1], 11);		 		//rd - destination
			if(assembly[0].equals("SLL") || assembly[0].equals("SRL"))
				instruction |= parseConstant(assembly[3], 6, 5);		//shamt - shift amount
			else
				instruction |= parseRegister(assembly[3], 16); 			//rt - source2
		}
		else if(format == 'I')
		{
			instruction |= commandMap.get(assembly[0]);					//opcode
			if(assembly[0].equals("LW") || assembly[0].equals("SW") || assembly[0].equals("LUI"))
			{
				if(assembly.length != 3)
					throw new Exception("Bad I-format instruction: " + assembly[0]);
				if(assembly[0].equals("LUI"))
				{
					instruction |= parseConstant(assembly[1], 0, 16);	//constant
				}
				else
				{
					instruction |= parseRegister(assembly[1], 16);		//rt - source/destination
					String[] rsAndOffset = splitAddress(assembly[2]);
					instruction |= parseRegister(rsAndOffset[0], 21);	//rs - base address
					instruction |= parseConstant(rsAndOffset[1], 0, 16);//constant - offset
				}
				
			}
			else
			{
				if(assembly.length != 4)
					throw new Exception("Bad I-format instruction: " + assembly[0]);
				int rs = assembly[0].equals("BEQ") || assembly[0].equals("BNE") ? 1 : 2, rt = (rs-1^1)+1;			
				instruction |= parseRegister(assembly[rs], 21);			//rs
				instruction |= parseRegister(assembly[rt], 16);			//rt
				instruction |= parseConstant(assembly[3], 0, 16);		//address or constant
			}
		}
		else if(format == 'J')
		{
			if(assembly.length != 2)
				throw new Exception("Bad J-format instruction");
			instruction |= commandMap.get(assembly[0]);					//opcode
			instruction |= parseConstant(assembly[1], 0, 16);			//address
		}
			
		return instruction;
	}
	
	/**
	 * Parses a program from assembly to machine language
	 * @param filePath the path to the file containing the program
	 * @param simulator the simulator to whose instruction memory the parsed instructions will be stored
	 * @throws Exception if the program is not found or has bad instructions
	 */
	public void parse(String filePath, Simulator simulator) throws Exception
	{
		Scanner sc = new Scanner(new FileReader(filePath));
		int counter = 0;
		while(sc.hasNextLine())
		{		
			String[] assembly = filterTokens(sc.nextLine().trim().split(" |,"));
			if(assembly.length == 0)
				continue;
			assembly[0] = assembly[0].toUpperCase();
			simulator.getInstructionMemory().setInstruction(counter, parse(assembly));
			counter += 4;
		}
		
		sc.close();
	}
	
}

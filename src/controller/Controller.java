package controller;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller {
	
	private Simulator simulator;
	private Parser parser;
	
	/**
	 * Constructs a new controller
	 * @throws FileNotFoundException if parser can't find program file or instruction set
	 */
	public Controller() throws FileNotFoundException
	{
		simulator = new Simulator();
		parser = new Parser();
	}
	
	/**
	 * Gets the simulator instance associated with the controller
	 * @return the simulator
	 */
	public Simulator getSimulator() { return simulator; }
	
	/**
	 * Gets the parser instance associated with the controller
	 * @return the parser
	 */
	public Parser getParser() { return parser; }
	
	/**
	 * Initializes the data memory with inital values taken from the input
	 * @param sc the scanner from which the input is taken
	 */
	private void initializeDataMemory(Scanner sc)
	{
		while(true)
		{
			String s = sc.next();
			if(s.equals("finished"))
				return;
			this.getSimulator().getDataMemory().write(Integer.parseInt(s), sc.nextInt());
		}
	}
	
	/**
	 * Initializes the register file with inital values taken from the input
	 * @param sc the scanner from which the input is taken
	 */
	public void initializeRegisterFile(Scanner sc)
	{
		while(true)
		{
			String s = sc.next();
			if(s.equals("finished"))
				return;
			this.getSimulator().getRegisterFile().writeRegister(this.getParser().getRegisterNumber(s), sc.nextInt());
		}
	}
	
	/**
	 * Main program. 
	 * It parses the assembly code to machine language and runs the MIPS simulator
	 * @param args
	 * @throws Exception if required files are not found or program has bad instructions
	 */
	public static void main(String[] args) throws Exception 
	{
		Scanner sc = new Scanner(System.in);
		
		Controller controller = new Controller();
		
		while(true)
		{	
			System.out.print("Enter the path to the program to run: ");
			String fileName = sc.nextLine();
			System.out.println("Compiling...");
			
			try{
				controller.getParser().parse(fileName, controller.getSimulator());
				controller.getParser().setRegistersNames(controller.getSimulator());
			}
			catch(Exception e)
			{
				System.out.println("Error: " + e.getMessage());
				continue;
			}
			
			System.out.println("Initial data memory values.");
			System.out.println("===========================");
			System.out.println("Enter the memory address and the value separated by a space on a single line.\nWhen you finish, write \'finished\' on a single line");
			controller.initializeDataMemory(sc);
			
			System.out.println("Initial register file values.");
			System.out.println("===========================");
			System.out.println("Enter the register name and the value separated by a space on a single line.\nWhen you finish, write \'finished\' on a single line");
			controller.initializeRegisterFile(sc);
			
			
			System.out.print("Choose one of the following options => R (run), E (exit): ");
			if(sc.next().toUpperCase().charAt(0) == 'R')
				controller.getSimulator().run();
			break;
		}		
		
		sc.close();
	}

}

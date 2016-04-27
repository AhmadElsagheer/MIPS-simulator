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
			System.out.print("Choose one of the following options => R (run), E (exit): ");
			if(sc.next().toUpperCase().charAt(0) == 'R')
				controller.getSimulator().run();
			break;
		}		
		
		sc.close();
	}

}

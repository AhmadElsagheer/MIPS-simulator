package controller;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Controller {
	
	private Simulator simulator;
	private Parser parser;
	
	public Controller() throws FileNotFoundException
	{
		simulator = new Simulator();
		parser = new Parser();
	}
	
	public Simulator getSimulator() { return simulator; }
	
	public Parser getParser() { return parser; }
	
	public static void main(String[] args) throws Exception 
	{
		Scanner sc = new Scanner(System.in);
		
		Controller controller = new Controller();
		
		while(true)
		{	
			System.out.print("Enter the path to the program to run: ");
			String fileName = sc.nextLine();
			System.out.println("Compiling...");
			if(!controller.getParser().parse(fileName, controller.getSimulator()))
			{
				//better handled with exceptions
				System.out.println("File contains bad commands");
				continue;
			}
			System.out.println("Choose one of the following options => R (run), E (exit): ");
			if(sc.next().charAt(0) == 'E')
				break;
			//Initial simulation
			while(true)
			{
				controller.getSimulator().run();
				System.out.print("Restart? (Y/N): ");
				if(sc.next().charAt(0) != 'Y')
					break;
			}
			
		}		
		
		sc.close();
	}

}

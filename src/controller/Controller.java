package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Controller {
	
	private Simulator simulator;
	
	public Controller(String fileName) throws FileNotFoundException
	{
		simulator = new Simulator();
		this.parse(fileName);
	}
	
	private void parse(String fileName) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new FileReader(fileName));
	}
	
	public void simulate()
	{
		simulator.run();
	}
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		Scanner sc = new Scanner(System.in);
		
		while(true)
		{
			System.out.print("Enter the path to the file to run: ");
			String fileName = sc.nextLine();
			System.out.println("Compiling...");
			Controller controller = new Controller(fileName);
			System.out.println("Choose one of the following options => R (run), E (exit): ");
			if(sc.next().charAt(0) == 'E')
				break;
			controller.simulate();
		}		
	}

}

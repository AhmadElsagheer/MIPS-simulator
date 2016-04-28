package controller;

import java.util.Arrays;

import stages.ExecutionStage;
import stages.InstructionDecodeStage;
import stages.InstructionFetchStage;
import stages.MemoryStage;
import stages.WriteBackStage;
import units.DataMemory;
import units.InstructionMemory;
import units.PipelineRegister;
import units.RegisterFile;

public class Simulator {
	
	public static final int NOP = -1, EMPTY = -2;
	
	PipelineRegister IFtoID;
	PipelineRegister IDtoEx;
	PipelineRegister ExtoMem;
	PipelineRegister MemtoWb;
	
	InstructionMemory instructionMemory;
	RegisterFile registerFile;
	DataMemory dataMemory;
	
	InstructionFetchStage instructionFetchStage;
	InstructionDecodeStage instructionDecodeStage;
	ExecutionStage executionStage;
	MemoryStage memoryStage;
	WriteBackStage writeBackStage;
	
	int[] instructionsNumbers = new int[5];
	int[] tmpInstructionsNumbers = new int[5];
	String[] instructionAction = new String[] { "fetched", "decoded", "executed", "in memory stage", "in write back stage" };

	/**
	 * Constructs a new simulator that has four pipeline registers, three data units
	 * and five stages (helper simulators for each pipeline stage)
	 */
	public Simulator()
	{
		IFtoID = new PipelineRegister(0);
		IDtoEx = new PipelineRegister(1);
		ExtoMem = new PipelineRegister(2);
		MemtoWb = new PipelineRegister(3);
		
		instructionMemory = new InstructionMemory(1000);	//1000 instructions, 32 KB
		registerFile = new RegisterFile();
		dataMemory = new DataMemory(16000);					//64 KB
		
		instructionFetchStage = new InstructionFetchStage(this);
		instructionDecodeStage = new InstructionDecodeStage(this);
		executionStage = new ExecutionStage(this);
		memoryStage = new MemoryStage(this);
		writeBackStage = new WriteBackStage(this);
		
		Arrays.fill(instructionsNumbers, EMPTY);
		Arrays.fill(tmpInstructionsNumbers, EMPTY);
		instructionsNumbers[0] = 0;
	}
	
	/**
	 * Runs the simulator
	 */
	void run()
	{
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("                Start of Program");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		int clockCycle = 1;

		while(isBusy())
		{
			//Run Stages
			instructionFetchStage.run();
			instructionDecodeStage.run();
			executionStage.run();
			memoryStage.run();
			writeBackStage.run();
			
			// Update Pipleine registers
			updatePipelines();
			
			// Print
			print(clockCycle++);
			
			// Update instructions numbers
			updateInstructionNumbers();
		}
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("               End of Program");
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
	}
	
	public PipelineRegister getIFtoID() { return IFtoID; }

	public PipelineRegister getIDtoEx() { return IDtoEx; }

	public PipelineRegister getExtoMem() { return ExtoMem;}

	public PipelineRegister getMemtoWb() { return MemtoWb; }

	public InstructionMemory getInstructionMemory() { return instructionMemory; }

	public RegisterFile getRegisterFile() {	return registerFile; }

	public DataMemory getDataMemory() {	return dataMemory; }

	public InstructionFetchStage getInstructionFetchStage() { return instructionFetchStage; }

	public InstructionDecodeStage getInstructionDecodeStage() {	return instructionDecodeStage; }

	public ExecutionStage getExecutionStage() {	return executionStage; }

	public MemoryStage getMemoryStage() { return memoryStage; }

	public WriteBackStage getWriteBackStage() { return writeBackStage; }
	
	public int getInstructionNumber(int stageID)
	{
		return instructionsNumbers[stageID];
	}
	
	public void setInstructionNumber(int stageID, int instructionNumber)
	{
		tmpInstructionsNumbers[stageID] = instructionNumber;
	}
	
	public void updateInstructionNumbers()
	{
		for(int i = 0; i < 5; ++i)
		{
			instructionsNumbers[i] = tmpInstructionsNumbers[i];
			tmpInstructionsNumbers[i] = EMPTY;
		}
	}
	
	private void updatePipelines()
	{
		IFtoID.update();
		IDtoEx.update();
		ExtoMem.update();
		MemtoWb.update();
	}
	
	private boolean isBusy()
	{
		for(int instructionNumber: instructionsNumbers)
			if(instructionNumber != EMPTY)
				return true;
		return false;
	}
	
	private void print(int clockCycle)
	{
		if(clockCycle > 1)
		System.out.println("##############################################\n");
		System.out.printf("Clock Cycle %2d\n^^^^^^^^^^^^^^\n", clockCycle);
		for(int i = 0; i < 5; ++i)
			if(instructionsNumbers[i] >= 0)
				System.out.printf("Instruction %d %s\n", instructionsNumbers[i] + 1, instructionAction[i]);
		System.out.println("\nPipeline Registers\n^^^^^^^^^^^^^^^^^^");
		System.out.printf("Pipepline IF/ID\n***************\n%s================================================\n", IFtoID);
		System.out.printf("Pipepline ID/EX\n***************\n%s================================================\n", IDtoEx);
		System.out.printf("Pipepline EX/MEM\n****************\n%s================================================\n", ExtoMem);
		System.out.printf("Pipepline MEM/WB\n****************\n%s================================================\n", MemtoWb);
//		
		System.out.printf("Register File\n^^^^^^^^^^^^^\n%s==============================================\n", registerFile);
		System.out.printf("Data Memory\n^^^^^^^^^^^\n%s\n", dataMemory);
	}
}
package controller;

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
	
	public Simulator()
	{
		IFtoID = new PipelineRegister(0);
		IDtoEx = new PipelineRegister(1);
		ExtoMem = new PipelineRegister(2);
		MemtoWb = new PipelineRegister(3);
		
		// TODO initialize all instance variables
	}
	
	
	
	
	void runSimulation(String pathToFile)
	{
		
	}

//	In main method
//	1. Read user's program (assembly)
//	2. instantiate instruction memory with user's program
//	3. instantiate RegiserFile 
//	4. instantiate DataMemory
//	5. instantiate Pipleline registers
//	6. instantiate all stages
//	simulate
//	
//	
}

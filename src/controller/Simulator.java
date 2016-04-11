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
		
		instructionMemory = new InstructionMemory(100);
		registerFile = new RegisterFile();
		dataMemory = new DataMemory(16000);		//64 KB memory
		
		instructionFetchStage = new InstructionFetchStage(this);
		instructionDecodeStage = new InstructionDecodeStage(this);
		executionStage = new ExecutionStage(this);
		memoryStage = new MemoryStage(this);
		writeBackStage = new WriteBackStage(this);
	}

	void run() 
	{

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
}

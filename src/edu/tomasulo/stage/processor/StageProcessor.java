package edu.tomasulo.stage.processor;

/**
 * The following class provides the structure for executor for a particular
 * stage.
 * 
 * @author Nikhil
 *
 */
public class StageProcessor {

	private boolean occupied = false;

	private boolean executionCompleted = true;

	private int instructionId = -1;

	private int executionTime;

	private int timeElapsed;

	public StageProcessor(int executionTime) {
		this.executionTime = executionTime;
	}

	public boolean isOccupied() {
		return occupied;
	}

	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}

	public boolean isExecutionCompleted() {
		return executionCompleted;
	}

	public void setExecutionCompleted(boolean executionCompleted) {
		this.executionCompleted = executionCompleted;
	}

	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public int getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(int timeElapsed) {
		this.timeElapsed = timeElapsed;
	}
}

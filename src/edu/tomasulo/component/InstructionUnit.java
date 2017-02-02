package edu.tomasulo.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.tomasulo.entity.Instruction;
import edu.tomasulo.entity.Interval;

public class InstructionUnit {

	int instructionId;

	int pc;

	String loopId;

	ArrayList<String> array = new ArrayList<>();

	Map<String, Interval> loopMap = new HashMap<String, Interval>();

	public void addInstructionString(String instructionString) {
		instructionId++;
		array.add(instructionString);
	}

	public void beginLoop(String loopId) {
		this.loopId = loopId;
		Interval interval = new Interval();
		interval.setStartCycleIndex(instructionId);
		loopMap.put(loopId, interval);
	}

	public void endLoop() {
		loopMap.get(loopId).setEndCycleIndex(instructionId);
		loopId = null;
	}

	public Instruction fetchNextInstruction() {
		if (hasNext()) {
			return new Instruction(array.get(pc));
		}
		return null;
	}

	public void incrementPC() {
		pc++;
	}

	public boolean hasNext() {
		if (pc >= array.size()) {
			return false;
		}
		return true;
	}

	public void jumpPCToLoop(String loopId) {
		pc = loopMap.get(loopId).getStartCycleIndex();
	}
}

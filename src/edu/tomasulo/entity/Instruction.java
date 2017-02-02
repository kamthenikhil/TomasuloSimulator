package edu.tomasulo.entity;

import edu.tomasulo.constant.TomasuloConstants;

/**
 * The following class is used to store fields corresponding an instruction.
 * 
 * @author Nikhil
 *
 */
public class Instruction {

	private int id;

	private OpCode opCode;

	private Register src1;

	private Register src2;

	private Register dest;

	private Interval issueInterval = new Interval();

	private Interval execInterval = new Interval();

	private Interval wbInterval = new Interval();

	private Interval cdbInterval = new Interval();

	private String instruction;

	private int branchInstructionId = -1;

	private String loopId;

	public Instruction(String instruction) {
		try {
			parse(instruction);
		} catch (Throwable e) {
			System.err.println("Error while parsing instruction: " + instruction);
			System.err.println("Terminating simulator...");
			System.exit(1);
		}
	}

	private void parse(String instruction) {

		this.instruction = instruction;
		String[] tokens = instruction.split(TomasuloConstants.REGEX_SPACE);
		switch (tokens[0]) {
		case TomasuloConstants.ADD:
			opCode = OpCode.ADD;
			dest = new Register(tokens[1]);
			src1 = new Register(tokens[2]);
			src2 = new Register(tokens[3]);
			break;
		case TomasuloConstants.SUB:
			opCode = OpCode.SUB;
			dest = new Register(tokens[1]);
			src1 = new Register(tokens[2]);
			src2 = new Register(tokens[3]);
			break;
		case TomasuloConstants.MULT:
			opCode = OpCode.MULT;
			dest = new Register(tokens[1]);
			src1 = new Register(tokens[2]);
			src2 = new Register(tokens[3]);
			break;
		case TomasuloConstants.DIV:
			opCode = OpCode.DIV;
			dest = new Register(tokens[1]);
			src1 = new Register(tokens[2]);
			src2 = new Register(tokens[3]);
			break;
		case TomasuloConstants.LW:
			opCode = OpCode.LW;
			dest = new Register(tokens[1]);
			src1 = new Register(tokens[2]);
			break;
		case TomasuloConstants.SW:
			opCode = OpCode.SW;
			src1 = new Register(tokens[1]);
			src2 = new Register(tokens[2]);
			break;
		case TomasuloConstants.BNEZ:
			opCode = OpCode.BNEZ;
			src1 = new Register(tokens[1]);
			src2 = new Register(tokens[2]);
			loopId = tokens[3];
			break;
		default:
			break;
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OpCode getOpCode() {
		return opCode;
	}

	public void setOpCode(OpCode opCode) {
		this.opCode = opCode;
	}

	public Register getSrc1() {
		return src1;
	}

	public void setSrc1(Register src1) {
		this.src1 = src1;
	}

	public Register getSrc2() {
		return src2;
	}

	public void setSrc2(Register src2) {
		this.src2 = src2;
	}

	public Register getDest() {
		return dest;
	}

	public void setDest(Register dest) {
		this.dest = dest;
	}

	public Interval getIssueInterval() {
		return issueInterval;
	}

	public void setIssueInterval(Interval issueInterval) {
		this.issueInterval = issueInterval;
	}

	public Interval getExecInterval() {
		return execInterval;
	}

	public void setExecInterval(Interval execInterval) {
		this.execInterval = execInterval;
	}

	public Interval getWbInterval() {
		return wbInterval;
	}

	public void setWbInterval(Interval wbInterval) {
		this.wbInterval = wbInterval;
	}

	public Interval getCdbInterval() {
		return cdbInterval;
	}

	public void setCdbInterval(Interval cdbInterval) {
		this.cdbInterval = cdbInterval;
	}

	public String getInstruction() {
		return instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}

	public int getBranchInstructionId() {
		return branchInstructionId;
	}

	public void setBranchInstructionId(int branchInstructionId) {
		this.branchInstructionId = branchInstructionId;
	}

	public String getLoopId() {
		return loopId;
	}

	public void setLoopId(String loopId) {
		this.loopId = loopId;
	}

	public String toString() {
		StringBuilder instructionDesc = new StringBuilder();
		instructionDesc.append(TomasuloConstants.HASH);
		instructionDesc.append(TomasuloConstants.TAB);
		instructionDesc.append(instruction);
		instructionDesc.append(TomasuloConstants.TAB);
		instructionDesc
				.append(issueInterval.toString().equals("0") ? TomasuloConstants.HYPHEN : issueInterval.toString());
		instructionDesc.append(TomasuloConstants.TAB);
		instructionDesc
				.append(execInterval.toString().equals("0") ? TomasuloConstants.HYPHEN : execInterval.toString());
		instructionDesc.append(TomasuloConstants.TAB);
		instructionDesc.append(wbInterval.toString().equals("0") ? TomasuloConstants.HYPHEN : wbInterval.toString());
		instructionDesc.append(TomasuloConstants.TAB);
		instructionDesc.append(cdbInterval.toString().equals("0") ? TomasuloConstants.HYPHEN : cdbInterval.toString());
		instructionDesc.append(TomasuloConstants.TAB);
		instructionDesc.append(TomasuloConstants.HASH);
		return instructionDesc.toString();
	}
}

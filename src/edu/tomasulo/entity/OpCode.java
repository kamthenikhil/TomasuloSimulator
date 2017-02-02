package edu.tomasulo.entity;

/**
 * The following enum is used to store the opcodes supported by the simulator.
 * 
 * @author Nikhil
 *
 */
public enum OpCode {

	ADD("ADD"), SUB("SUB"), MULT("MULT"), DIV("DIV"), LW("LW"), SW("SW"), BNEZ("BNEZ");

	private String value;

	OpCode(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
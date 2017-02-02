package edu.tomasulo.entity;

import edu.tomasulo.constant.TomasuloConstants;

/**
 * The following class is used to store the attributes corresponding to a
 * register.
 * 
 * @author Nikhil
 *
 */
public class Register {

	private int index;

	private int value;

	public Register(String register) {
		parse(register);
	}

	private void parse(String register) {
		register = register.replace(TomasuloConstants.REGISTER_PREFIX, TomasuloConstants.EMPTY_STRING);
		try {
			index = Integer.parseInt(register);
		} catch (NumberFormatException e) {
			System.err.println("Invalid register name in the instruction file.");
			System.err.println("Terminating...");
			System.exit(1);
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}

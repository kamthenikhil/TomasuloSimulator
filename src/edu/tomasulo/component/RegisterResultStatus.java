package edu.tomasulo.component;

/**
 * The following class simulates register result status array. It is used to
 * store the index of the instruction which is going to write in that particular
 * register.
 * 
 * @author Nikhil
 *
 */
public class RegisterResultStatus {

	private int[] array;

	public RegisterResultStatus(int numberOfRegisters) {
		init(numberOfRegisters);
	}

	private void init(int numberOfRegisters) {
		array = new int[numberOfRegisters];
		for (int i = 0; i < numberOfRegisters; i++) {
			array[i] = -1;
		}
	}

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

}

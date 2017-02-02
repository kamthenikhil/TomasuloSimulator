package edu.tomasulo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.tomasulo.component.InstructionUnit;
import edu.tomasulo.constant.TomasuloConstants;

public class FileHandlerUtil {

	/**
	 * The following method reads instructions from a text file. It converts and
	 * returns the text data read from the file to a list of instructions.
	 * 
	 * @param filepath
	 * @return instructionList
	 */
	public static InstructionUnit readInstructions(String filepath) {

		File file = new File(filepath);
		BufferedReader reader = null;
		InstructionUnit instructionUnit = new InstructionUnit();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				if (!line.isEmpty()) {
					if (line.contains(TomasuloConstants.COLON)) {
						instructionUnit.beginLoop(line.split(TomasuloConstants.COLON)[0]);
					} else if (line.contains(TomasuloConstants.END)) {
						instructionUnit.endLoop();
					} else {
						instructionUnit.addInstructionString(line);
					}
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			System.err.println("Instruction file not found at given path. Kindly check the path of instructions file.");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Error while closing file reader.");
				}
			}
		}
		return instructionUnit;
	}
}

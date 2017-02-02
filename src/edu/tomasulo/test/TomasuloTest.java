package edu.tomasulo.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.tomasulo.constant.TomasuloConstants;

public class TomasuloTest {

	public static void main(String[] args) {
		BufferedReader reader = null;
		String filepath = null;
		int loadExecutionTime = 0;
		int addExecutionTime = 0;
		int multiplyExecutionTime = 0;
		int branchExecutionTime = 0;
		int memoryRSSize = 0;
		int addRSSize = 0;
		int multiplyRSSize = 0;
		int branchRSSize = 0;
		boolean isTaken = false;
		int loopCount = 0;
		try {
			reader = new BufferedReader(new FileReader(new File("config")));
			String line = reader.readLine();
			while (line != null) {
				if (!line.isEmpty() && !line.startsWith(TomasuloConstants.HASH)) {
					String[] tokens = line.trim().split("=");
					switch (tokens[0].trim()) {
					case TomasuloConstants.INSTRUCTION_FILEPATH:
						filepath = tokens[1].trim();
						break;
					case TomasuloConstants.LOAD_EXECUTION_TIME:
						loadExecutionTime = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.ADD_EXECUTION_TIME:
						addExecutionTime = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.MULTIPLY_EXECUTION_TIME:
						multiplyExecutionTime = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.BRANCH_EXECUTION_TIME:
						branchExecutionTime = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.MEMORY_RS_SIZE:
						memoryRSSize = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.ADD_RS_SIZE:
						addRSSize = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.MULTIPLY_RS_SIZE:
						multiplyRSSize = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.BRANCH_RS_SIZE:
						branchRSSize = Integer.parseInt(tokens[1].trim());
						break;
					case TomasuloConstants.BRANCH_PREDICTION:
						if (tokens[1].trim().equals(TomasuloConstants.BRANCH_TAKEN)) {
							isTaken = true;
						}
						break;
					case TomasuloConstants.TAKEN_COUNT:
						loopCount = Integer.parseInt(tokens[1].trim());
						break;
					default:
						System.err.println("Error while parsing the instruction file");
						System.err.println("Terminating..");
						System.exit(1);
						break;
					}
				}
				line = reader.readLine();
			}
			Tomasulo tomasulo = new Tomasulo(filepath, loadExecutionTime, addExecutionTime, multiplyExecutionTime,
					branchExecutionTime, memoryRSSize, addRSSize, multiplyRSSize, branchRSSize, isTaken, loopCount);
			tomasulo.run();
		} catch (IOException e) {
			System.err.println("Kindly check path and format of config file.");
		} catch (Throwable e) {
			System.err.println("Something went wrong. Kindly check the config and instruction file.");
			System.err.println("Terminating..");
			System.exit(1);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					System.err.println("Error while closing the file reader.");
				}
			}
		}
	}
}
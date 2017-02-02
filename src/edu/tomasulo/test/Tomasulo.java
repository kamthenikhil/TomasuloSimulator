package edu.tomasulo.test;

import java.util.ArrayList;

import edu.tomasulo.component.InstructionUnit;
import edu.tomasulo.component.RegisterResultStatus;
import edu.tomasulo.component.ReservationStation;
import edu.tomasulo.constant.TomasuloConstants;
import edu.tomasulo.entity.Instruction;
import edu.tomasulo.entity.OpCode;
import edu.tomasulo.entity.Register;
import edu.tomasulo.entity.ReservationStationEntry;
import edu.tomasulo.stage.processor.StageProcessor;
import edu.tomasulo.util.FileHandlerUtil;

/**
 * This class provides a configurable Tomasulo algorithm simulator. The
 * following parameters are configurable:
 * <ul>
 * <li>Execution time of execution units</li>
 * <li>Number of reservation stations for each execution unit resource</li>
 * <li>Static branch predictor (Always T or Always NT)</li>
 * </ul>
 * 
 * @author Nikhil
 *
 */
public class Tomasulo {

	private int cycle = 1;
	private int instructionId = 0;
	private StageProcessor commonDataBus;
	private StageProcessor memoryAccess;
	private StageProcessor memoryUnit;
	private StageProcessor adderUnit;
	private StageProcessor multiplierUnit;
	private StageProcessor branchUnit;
	private ReservationStation memoryReservationStation;
	private ReservationStation addReservationStation;
	private ReservationStation multReservationStation;
	private ReservationStation branchReservationStation;
	private RegisterResultStatus registerResultStatus;
	private ArrayList<Instruction> instructions;
	private InstructionUnit instructionUnit;
	private int branchInstructionId = -1;
	private int loopExecutionCount = 3;
	private boolean isTaken = false;

	/**
	 * Parameterized Constructor
	 * 
	 * @param loadExecutionTime
	 * @param adderExecutionTime
	 * @param multiplierExecutionTime
	 * @param branchExecutionTime
	 * @param memoryRSSize
	 * @param addRSSize
	 * @param multRSSize
	 * @param branchRSSize
	 */
	public Tomasulo(String filepath, int loadExecutionTime, int adderExecutionTime, int multiplierExecutionTime,
			int branchExecutionTime, int memoryRSSize, int addRSSize, int multRSSize, int branchRSSize, boolean isTaken,
			int loopCount) {
		init(filepath, loadExecutionTime, adderExecutionTime, multiplierExecutionTime, branchExecutionTime,
				memoryRSSize, addRSSize, multRSSize, branchRSSize, isTaken, loopCount);
	}

	/**
	 * The following method initializes the parameters.
	 * 
	 * @param loadExecutionTime
	 * @param adderExecutionTime
	 * @param multiplierExecutionTime
	 * @param branchExecutionTime
	 * @param memoryRSSize
	 * @param addRSSize
	 * @param multRSSize
	 * @param branchRSSize
	 */
	private void init(String filepath, int loadExecutionTime, int adderExecutionTime, int multiplierExecutionTime,
			int branchExecutionTime, int memoryRSSize, int addRSSize, int multRSSize, int branchRSSize, boolean isTaken,
			int loopCount) {
		commonDataBus = new StageProcessor(1);
		memoryAccess = new StageProcessor(1);
		memoryUnit = new StageProcessor(loadExecutionTime);
		adderUnit = new StageProcessor(adderExecutionTime);
		multiplierUnit = new StageProcessor(multiplierExecutionTime);
		branchUnit = new StageProcessor(branchExecutionTime);
		memoryReservationStation = new ReservationStation(memoryRSSize);
		addReservationStation = new ReservationStation(addRSSize);
		multReservationStation = new ReservationStation(multRSSize);
		branchReservationStation = new ReservationStation(branchRSSize);
		registerResultStatus = new RegisterResultStatus(10);
		instructions = new ArrayList<Instruction>();
		instructionUnit = FileHandlerUtil.readInstructions(filepath);
		this.isTaken = isTaken;
		loopExecutionCount = loopCount;
	}

	/**
	 * The following method is the entry point for the simulator.
	 */
	public void run() {
		while (instructionUnit.hasNext() || !memoryReservationStation.isEmpty() || !addReservationStation.isEmpty()
				|| !multReservationStation.isEmpty() || !branchReservationStation.isEmpty()) {
			cleanupRSAndRegisterResults();
			updateCommonDataBusStage();
			updateMemoryAccessStage();
			updateExecutorStage();
			updateReservationStation();
			cycle++;
		}
		printOutput();
	}

	/**
	 * The following method prints the output in table form the solution for the
	 * Tomasulo Algorithm.
	 */
	private void printOutput() {
		System.out.println("#########################################################");
		System.out.println(TomasuloConstants.HASH + TomasuloConstants.TAB + "Instruction" + TomasuloConstants.TAB
				+ "Issue" + TomasuloConstants.TAB + "Exec" + TomasuloConstants.TAB + "Mem" + TomasuloConstants.TAB
				+ "CDB" + TomasuloConstants.TAB + TomasuloConstants.HASH);
		System.out.println("#########################################################");
		for (Instruction instruction : instructions) {
			System.out.println(instruction.toString());
		}
		System.out.println("#########################################################");
	}

	/**
	 * The following method performs cleanup before with start with the current
	 * cycle. It first removes the non-busy instructions from reservation
	 * tables. It then updates the dependent entries in the reservation table.
	 * Finally, it updates the register result status to clear the entry
	 * corresponding to the computed register.
	 */
	private void cleanupRSAndRegisterResults() {
		ReservationStationEntry entry = fetchNonBusyRSEntry(memoryReservationStation);
		if (entry == null) {
			entry = fetchNonBusyRSEntry(addReservationStation);
			if (entry == null) {
				entry = fetchNonBusyRSEntry(multReservationStation);
				if (entry == null) {
					entry = fetchNonBusyRSEntry(branchReservationStation);
				}
			}
		}

		if (entry != null) {
			updateDependentRSEntries(entry, memoryReservationStation);
			updateDependentRSEntries(entry, addReservationStation);
			updateDependentRSEntries(entry, multReservationStation);
			updateDependentRSEntries(entry, branchReservationStation);
			updateRegisterResultStatus(entry);
		}
	}

	/**
	 * The following method clears the entry in register result status array
	 * corresponding to the instruction executed in the previous cycle.
	 * 
	 * @param entry
	 */
	private void updateRegisterResultStatus(ReservationStationEntry entry) {
		Register lastComputedRegister = instructions.get(entry.getId()).getDest();
		if (lastComputedRegister != null && lastComputedRegister.getIndex() != -1) {
			for (int i = 0; i < registerResultStatus.getArray().length; i++) {
				if (registerResultStatus.getArray()[i] != -1) {
					if (entry.getId() == registerResultStatus.getArray()[i]) {
						registerResultStatus.getArray()[i] = -1;
						break;
					}
				}
			}
		}
	}

	/**
	 * The following method updates the dependent entries in the reservation
	 * table.
	 * 
	 * @param lastRemoved
	 * @param rs
	 */
	private void updateDependentRSEntries(ReservationStationEntry lastRemoved, ReservationStation rs) {
		Register lastComputedRegister = instructions.get(lastRemoved.getId()).getDest();
		if (lastComputedRegister != null && lastComputedRegister != null) {
			int lastComputedInstructionId = lastRemoved.getId();
			for (ReservationStationEntry entry : rs.getTable()) {
				if (entry != null) {
					if (entry.getRsSource1Id() == lastComputedInstructionId) {
						entry.setRsSource1Id(-1);
					}
					if (entry.getRsSource2Id() == lastComputedInstructionId) {
						entry.setRsSource2Id(-1);
					}
				}
			}
		}
	}

	/**
	 * The following method fetches the non-busy instruction from the
	 * reservation table. The non-busy instruction is the one which finished its
	 * execution in the previous cycle.
	 * 
	 * @param rs
	 * @return
	 */
	private ReservationStationEntry fetchNonBusyRSEntry(ReservationStation rs) {
		int counter = 0;
		ReservationStationEntry lastRemoved = null;
		for (ReservationStationEntry entry : rs.getTable()) {
			if (entry != null && !entry.isBusy()) {
				lastRemoved = rs.getTable()[counter];
				rs.getTable()[counter] = null;
				rs.setAvailableEntries(rs.getAvailableEntries() + 1);
				break;
			}
			counter++;
		}
		return lastRemoved;
	}

	/**
	 * The following method polls the instruction from the instruction queue and
	 * processes it if there is space in the corresponding reservation table.
	 */
	private void updateReservationStation() {
		boolean isProcessed = false;
		Instruction instruction = instructionUnit.fetchNextInstruction();
		if (instruction != null) {
			instruction.setId(instructionId);
			switch (instruction.getOpCode()) {
			case ADD:
			case SUB:
				isProcessed |= moveToRSAndUpdateRegisterStatus(instruction, addReservationStation);
				break;
			case MULT:
			case DIV:
				isProcessed |= moveToRSAndUpdateRegisterStatus(instruction, multReservationStation);
				break;
			case LW:
			case SW:
				isProcessed |= moveToRSAndUpdateRegisterStatus(instruction, memoryReservationStation);
				break;
			case BNEZ:
				isProcessed |= moveToRSAndUpdateRegisterStatus(instruction, branchReservationStation);
				break;
			default:
				break;
			}
		}
		if (isProcessed) {
			instruction.setBranchInstructionId(branchInstructionId);
			if (instruction.getOpCode().equals(OpCode.BNEZ)) {
				branchInstructionId = instructionId;
			}
			instructions.add(instruction);
			if (instruction.getOpCode().equals(OpCode.BNEZ) && loopExecutionCount > 0 && isTaken) {
				instructionUnit.jumpPCToLoop(instruction.getLoopId());
				loopExecutionCount--;
			} else {
				instructionUnit.incrementPC();
			}
			instructionId++;
		}
	}

	/**
	 * The following method checks if there is space available in the
	 * reservation table and start processing it. It also returns a boolean
	 * indicating if the instruction was processed. This flag is later used to
	 * poll the instruction from the instruction queue.
	 * 
	 * @param instruction
	 * @param rs
	 * @return
	 */
	private boolean moveToRSAndUpdateRegisterStatus(Instruction instruction, ReservationStation rs) {
		boolean isProcessed = false;
		if (rs.getAvailableEntries() > 0) {
			isProcessed = true;
			for (int i = 0; i < rs.getTable().length; i++) {
				ReservationStationEntry entry = rs.getTable()[i];
				if (entry == null) {
					ReservationStationEntry newEntry = new ReservationStationEntry();
					newEntry.setBusy(true);
					newEntry.setId(instruction.getId());
					int src1Index = instruction.getSrc1().getIndex();
					if (registerResultStatus.getArray()[src1Index] != -1) {
						newEntry.setRsSource1Id(registerResultStatus.getArray()[src1Index]);
					}
					if (instruction.getOpCode() != OpCode.LW) {
						int src2Index = instruction.getSrc2().getIndex();
						if (registerResultStatus.getArray()[src2Index] != -1) {
							newEntry.setRsSource2Id(registerResultStatus.getArray()[src2Index]);
						}
					}
					rs.getTable()[i] = newEntry;
					rs.setAvailableEntries(rs.getAvailableEntries() - 1);
					if (instruction.getOpCode() != OpCode.SW && instruction.getOpCode() != OpCode.BNEZ) {
						int destIndex = instruction.getDest().getIndex();
						registerResultStatus.getArray()[destIndex] = instruction.getId();
					}
					instruction.getIssueInterval().setStartCycleIndex(cycle);
					instruction.getIssueInterval().setEndCycleIndex(cycle);
					break;
				}
			}
		}
		return isProcessed;
	}

	/**
	 * The following method updates different executors to pull instructions
	 * from the reservation station and starts executing them.
	 */
	private void updateExecutorStage() {
		fetchEntryFromRS(memoryUnit, memoryReservationStation);
		fetchEntryFromRS(adderUnit, addReservationStation);
		fetchEntryFromRS(multiplierUnit, multReservationStation);
		fetchEntryFromRS(branchUnit, branchReservationStation);
	}

	/**
	 * The following method fetches the entry from the reservation table and
	 * forwards it to the corresponding executor.
	 * 
	 * @param processor
	 * @param rs
	 */
	private void fetchEntryFromRS(StageProcessor processor, ReservationStation rs) {
		if (!processor.isOccupied()) {
			if (rs.getTable().length != rs.getAvailableEntries()) {
				int minId = Integer.MAX_VALUE;
				int rowId = -1;
				int counter = 0;
				for (ReservationStationEntry entry : rs.getTable()) {
					if (entry != null) {
						if (checkIfParentBrachInstructionExecuted(entry.getId())) {
							if (instructions.get(entry.getId()).getExecInterval().getStartCycleIndex() == 0) {
								if (instructions.get(entry.getId()).getOpCode().equals(OpCode.LW)
										&& entry.getRsSource1Id() == -1
										|| (entry.getRsSource1Id() == -1 && entry.getRsSource2Id() == -1)) {
									if (entry.getRsSource1Id() == -1) {
										if (entry.getId() < minId) {
											minId = entry.getId();
											rowId = counter;
										}
									}
								}
							}
						}
					}
					counter++;
				}
				if (rowId != -1) {
					int instructionId = rs.getTable()[rowId].getId();
					processor.setInstructionId(instructionId);
					processor.setExecutionCompleted(false);
					processor.setOccupied(true);
					processor.setTimeElapsed(1);
					instructions.get(processor.getInstructionId()).getExecInterval().setStartCycleIndex(cycle);
				}
			}
		} else {
			if (!processor.isExecutionCompleted()) {
				processor.setTimeElapsed(processor.getTimeElapsed() + 1);
			}
		}
		if (processor.getTimeElapsed() == processor.getExecutionTime()) {
			processor.setExecutionCompleted(true);
			processor.setTimeElapsed(0);
			instructions.get(processor.getInstructionId()).getExecInterval().setEndCycleIndex(cycle);
			if (instructions.get(processor.getInstructionId()).getOpCode().equals(OpCode.BNEZ)) {
				updateRSStatus(processor.getInstructionId(), branchReservationStation);
				processor.setOccupied(false);
			}
		}
	}

	private boolean checkIfParentBrachInstructionExecuted(int instructionId) {
		if (instructions.get(instructionId).getBranchInstructionId() == -1) {
			return true;
		} else {
			if (instructions.get(instructions.get(instructionId).getBranchInstructionId()).getExecInterval()
					.getEndCycleIndex() != 0) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * The following method updates memory executor to pull instructions from
	 * memory executor and pushes them to write back stage.
	 */
	private void updateMemoryAccessStage() {
		if (!memoryAccess.isOccupied()) {
			if (memoryUnit.isOccupied()) {
				if (memoryUnit.isExecutionCompleted()) {
					memoryAccess.setInstructionId(memoryUnit.getInstructionId());
					memoryAccess.setExecutionCompleted(false);
					memoryAccess.setOccupied(true);
					memoryAccess.setTimeElapsed(1);
					instructions.get(memoryAccess.getInstructionId()).getWbInterval().setStartCycleIndex(cycle);
					memoryUnit.setInstructionId(-1);
					memoryUnit.setOccupied(false);
				}
			}
		} else {
			if (!memoryAccess.isExecutionCompleted()) {
				memoryAccess.setTimeElapsed(memoryAccess.getTimeElapsed() + 1);
			}
		}
		if (memoryAccess.getTimeElapsed() == memoryAccess.getExecutionTime()) {
			memoryAccess.setExecutionCompleted(true);
			memoryAccess.setTimeElapsed(0);
			memoryAccess.setOccupied(false);
			instructions.get(memoryAccess.getInstructionId()).getWbInterval().setEndCycleIndex(cycle);
			if (instructions.get(memoryAccess.getInstructionId()).getOpCode().equals(OpCode.SW)) {
				updateRSStatus(memoryAccess.getInstructionId(), memoryReservationStation);
				memoryAccess.setOccupied(false);
			}
		}
	}

	/**
	 * The following method pushes the instructions to common data bus. In case
	 * of memory instructions the instructions are fetched from the write back
	 * stag. In case of other instructions the instruction are fetched from the
	 * execution stage.
	 */
	private void updateCommonDataBusStage() {
		if (!commonDataBus.isOccupied()) {
			int minId = Integer.MAX_VALUE;
			int executorId = -1;
			if (memoryAccess.isExecutionCompleted()) {
				if (memoryAccess.getInstructionId() != -1) {
					Instruction memoryInstruction = instructions.get(memoryAccess.getInstructionId());
					if (memoryInstruction.getOpCode() != OpCode.SW) {
						if (memoryInstruction.getId() < minId) {
							minId = memoryInstruction.getId();
							executorId = 1;
						}
					}
				}
			}
			if (adderUnit.isExecutionCompleted()) {
				if (adderUnit.getInstructionId() != -1) {
					Instruction addInstruction = instructions.get(adderUnit.getInstructionId());
					if (addInstruction.getId() < minId) {
						minId = addInstruction.getId();
						executorId = 2;
					}
				}
			}
			if (multiplierUnit.isExecutionCompleted()) {
				if (multiplierUnit.getInstructionId() != -1) {
					Instruction multInstruction = instructions.get(multiplierUnit.getInstructionId());
					if (multInstruction.getId() < minId) {
						minId = multInstruction.getId();
						executorId = 3;
					}
				}
			}
			if (executorId != -1) {
				switch (executorId) {
				case 1:
					commonDataBus.setInstructionId(memoryAccess.getInstructionId());
					memoryAccess.setInstructionId(-1);
					memoryAccess.setOccupied(false);
					break;
				case 2:
					commonDataBus.setInstructionId(adderUnit.getInstructionId());
					adderUnit.setInstructionId(-1);
					adderUnit.setOccupied(false);
					break;
				case 3:
					commonDataBus.setInstructionId(multiplierUnit.getInstructionId());
					multiplierUnit.setInstructionId(-1);
					multiplierUnit.setOccupied(false);
					break;
				default:
					break;
				}
				commonDataBus.setOccupied(true);
				commonDataBus.setExecutionCompleted(false);
				commonDataBus.setTimeElapsed(1);
				instructions.get(commonDataBus.getInstructionId()).getCdbInterval().setStartCycleIndex(cycle);
			}
		} else {
			if (!commonDataBus.isExecutionCompleted()) {
				commonDataBus.setTimeElapsed(commonDataBus.getTimeElapsed() + 1);
			}
		}

		if (commonDataBus.getTimeElapsed() == commonDataBus.getExecutionTime()) {
			commonDataBus.setExecutionCompleted(true);
			commonDataBus.setTimeElapsed(0);
			commonDataBus.setOccupied(false);
			instructions.get(commonDataBus.getInstructionId()).getCdbInterval().setEndCycleIndex(cycle);
			int currentInstructionId = commonDataBus.getInstructionId();
			switch (instructions.get(currentInstructionId).getOpCode()) {
			case ADD:
			case SUB:
				updateRSStatus(currentInstructionId, addReservationStation);
				break;
			case MULT:
			case DIV:
				updateRSStatus(currentInstructionId, multReservationStation);
				break;
			case LW:
			case SW:
				updateRSStatus(currentInstructionId, memoryReservationStation);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * The following method updates the status of the instruction in the
	 * reservation table.
	 * 
	 * @param currentInstructionId
	 * @param rs
	 */
	private void updateRSStatus(int currentInstructionId, ReservationStation rs) {
		for (int i = 0; i < rs.getTable().length; i++) {
			ReservationStationEntry entry = rs.getTable()[i];
			if (entry != null) {
				if (currentInstructionId == entry.getId()) {
					entry.setBusy(false);
				}
			}
		}
	}
}
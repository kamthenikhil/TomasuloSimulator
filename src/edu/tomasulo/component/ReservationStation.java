package edu.tomasulo.component;

import edu.tomasulo.entity.ReservationStationEntry;

/**
 * The following class is used to simulate the reservation table.
 * 
 * @author Nikhil
 *
 */
public class ReservationStation {

	private ReservationStationEntry[] table;

	private int availableEntries;

	private void init(int size) {
		table = new ReservationStationEntry[size];
		availableEntries = size;
	}

	public ReservationStationEntry[] getTable() {
		return table;
	}

	public void setTable(ReservationStationEntry[] table) {
		this.table = table;
	}

	public int getAvailableEntries() {
		return availableEntries;
	}

	public void setAvailableEntries(int availableEntries) {
		this.availableEntries = availableEntries;
	}

	public ReservationStation(int size) {
		init(size);
	}

	public boolean isEmpty() {
		if (table.length == availableEntries) {
			return true;
		} else {
			return false;
		}
	}

}

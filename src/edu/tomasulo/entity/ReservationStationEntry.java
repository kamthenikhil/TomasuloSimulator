package edu.tomasulo.entity;

/**
 * The following class is used to store attributes corresponding to each entry
 * in reservation station.
 * 
 * @author Nikhil
 *
 */
public class ReservationStationEntry {

	private boolean busy;

	private int id;

	private int rsSource1Id = -1;

	private int rsSource2Id = -1;

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRsSource1Id() {
		return rsSource1Id;
	}

	public void setRsSource1Id(int rsSource1Id) {
		this.rsSource1Id = rsSource1Id;
	}

	public int getRsSource2Id() {
		return rsSource2Id;
	}

	public void setRsSource2Id(int rsSource2Id) {
		this.rsSource2Id = rsSource2Id;
	}

}

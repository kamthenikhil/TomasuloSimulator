package edu.tomasulo.entity;

import edu.tomasulo.constant.TomasuloConstants;

/**
 * The following class is used to store the interval for each stage in terms of
 * the start cycle index and the end cycle index.
 * 
 * @author Nikhil
 *
 */
public class Interval {

	private int startCycleIndex;

	private int endCycleIndex;

	public int getStartCycleIndex() {
		return startCycleIndex;
	}

	public void setStartCycleIndex(int startCycleIndex) {
		this.startCycleIndex = startCycleIndex;
	}

	public int getEndCycleIndex() {
		return endCycleIndex;
	}

	public void setEndCycleIndex(int endCycleIndex) {
		this.endCycleIndex = endCycleIndex;
	}

	public String toString() {
		if (startCycleIndex == endCycleIndex) {
			return startCycleIndex + TomasuloConstants.EMPTY_STRING;
		} else {
			return startCycleIndex + TomasuloConstants.HYPHEN + endCycleIndex;
		}
	}
}

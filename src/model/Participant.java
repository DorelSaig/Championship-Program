package model;

public class Participant {

	private String name;
	private int lastGameScore;
	private int lastIndex;
	private int thePersonHeWonIndex;

	public Participant(String name) {
		this.name = name;
		lastGameScore = 0;

	}

	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	public int getThePersonHeWonIndex() {
		return thePersonHeWonIndex;
	}

	public void setThePersonHeWonIndex(int thePersonHeWonIndex) {
		this.thePersonHeWonIndex = thePersonHeWonIndex;
	}

	public int getTotalGameScore() {
		return lastGameScore;
	}

	public void setTotalGameScore(int lastGameScore) {
		this.lastGameScore = lastGameScore;
	}

	public String getName() {
		return name;
	}
}

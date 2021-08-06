package model;

public abstract class Championship {

	protected Participant[] partList;
	protected Participant winner;

	public Championship(Participant[] participants) {
		this.partList = participants;

	}

	public abstract Participant playGame(Participant player1, Participant player2, int[] player1Results,
			int[] player2Results) throws Exception;

}

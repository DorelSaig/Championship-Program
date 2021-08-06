package model;

public class BasketChampionship extends Championship {
	private final int NUM_OF_QUARTERS = 4;
	private int team1TotalScore, team2TotalScore;

	public BasketChampionship(Participant[] participants) {
		super(participants);

	}

	public Participant playGame(Participant team1, Participant team2, int[] team1Results, int[] team2Results) {
		Participant matchWinner = null;
		team1TotalScore = 0;
		team2TotalScore = 0;
		for (int i = 0; i < NUM_OF_QUARTERS; i++) {
			team1TotalScore = team1TotalScore + team1Results[i];
			team2TotalScore = team2TotalScore + team2Results[i];
		}
		if (team1TotalScore > team2TotalScore)
			matchWinner = team1;
		else if (team2TotalScore > team1TotalScore)
			matchWinner = team2;
		else
			matchWinner = null;

		team1.setTotalGameScore(team1TotalScore);
		team2.setTotalGameScore(team2TotalScore);

		return matchWinner;
	}

}

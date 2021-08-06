package model;

public class TennisChampionship extends Championship {
	private final int NUM_OF_DEFAULT_SETS = 3;
	private int player1MatchScore, player2MatchScore;

	public TennisChampionship(Participant[] participants) {
		super(participants);

	}

	@Override
	public Participant playGame(Participant player1, Participant player2, int[] player1Results, int[] player2Results)
			throws Exception {
		Participant matchWinner = null;
		player1MatchScore = 0;
		player2MatchScore = 0;

		for (int i = 0; i < NUM_OF_DEFAULT_SETS; i++) {
			if ((player1Results[i] != 6 && player2Results[i] != 6) || (player1Results[i] > 6 || player2Results[i] > 6)
					|| (player1Results[i] == 6 && player2Results[i] == 6)) {
				throw new Exception("One of the following issues occurred: \n- Input must be up to 6  \n- Winner have to reach 6 \n- Cannot be two-way tie");
			}
			if (player1Results[i] > player2Results[i])
				player1MatchScore++;
			else if (player2Results[i] > player1Results[i])
				player2MatchScore++;
		}
		if (player1MatchScore - player2MatchScore == 3)
			matchWinner = player1;
		else if (player2MatchScore - player1MatchScore == 3)
			matchWinner = player2;
		else
			matchWinner = null;

		player1.setTotalGameScore(player1MatchScore);
		player2.setTotalGameScore(player2MatchScore);

		return matchWinner;
	}

}

package listeners;

public interface TournamentUIEventListener {

	void addParticipantToModel(String name) throws Exception;

	void startChampionship(String text);

	void playGame(int player1Index, int player2Index, int[] parti1, int[] parti2, int buttonNumber);

	boolean viewAskIfSemiIsReady(int player1Index, int player2Index);


}

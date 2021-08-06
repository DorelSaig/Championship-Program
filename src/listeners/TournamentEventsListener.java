package listeners;

import model.Participant;

public interface TournamentEventsListener {

	void participantAddedToModelEvent(String name, int position);

	void newChampionshipCreated(String sportType);

	void matchWinnerUpdate(Participant matchWinner, int buttonNumber, int indexInNextStage2);

	int[] overTime(int i);

	void tournamentisFull();

}

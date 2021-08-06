package model;

import java.util.Vector;

import listeners.TournamentEventsListener;

public class TournamentManager {
	private Participant[] participants;
	private Championship theChamp;
	private Vector<TournamentEventsListener> listeners;
	private final int MAX_PARTICIPANT = 8;
	private int participantsCounter;
	private Participant player1, player2;

	public TournamentManager() {
		participants = new Participant[15];
		listeners = new Vector<TournamentEventsListener>();
	}

	public Participant addParticipant(String name) throws Exception {
		if (participantsCounter == MAX_PARTICIPANT)
			throw new Exception("Tournament is Full");

		Participant newPlayer = new Participant(name);
		participants[participantsCounter] = newPlayer;
		fireAddParticipantEvent(newPlayer);
		participantsCounter++;
		if (participantsCounter == MAX_PARTICIPANT)
			fireTournamentIsFull();
		return newPlayer;
	}

	public Participant[] getParticipants() {
		return participants;
	}

	public void startChampionship(String sportType) {
		if (sportType == "Basketball") {
			theChamp = new BasketChampionship(participants);
		} else if (sportType == "Soccer") {
			theChamp = new SoccerChampionship(participants);
		} else if (sportType == "Tennis") {
			theChamp = new TennisChampionship(participants);
		}
		fireNewChampionshipCreated(sportType);
	}

	public void playGame(int player1Index, int player2Index, int[] player1Results, int[] player2Results,
			int buttonNumber) throws Exception {

		int indicationNum = 0;
		int[] oTResult;
		player1 = participants[player1Index];
		player2 = participants[player2Index];
		Participant matchWinner = theChamp.playGame(player1, player2, player1Results, player2Results);
		
		// ------------ No Winner Situation: ------------------
		
		while (matchWinner == null) {
			if (theChamp instanceof BasketChampionship) {
				oTResult = fireOverTime(++indicationNum);
				if (oTResult[0] > oTResult[1])
					matchWinner = player1;

				else if (oTResult[1] > oTResult[0])
					matchWinner = player2;
			}
			if (theChamp instanceof SoccerChampionship) {
				while (matchWinner == null) {
					oTResult = fireOverTime(indicationNum);
					if (oTResult[0] > oTResult[1])
						matchWinner = player1;
					else if (oTResult[1] > oTResult[0])
						matchWinner = player2;
					else
						indicationNum++;
				}

			}
			if (theChamp instanceof TennisChampionship) {
				indicationNum = 3; // indication to what set number are we in.
				for (int i = 0; i < 2; i++) {
					oTResult = fireOverTime(++indicationNum);
					if (indicationNum == 5) {
						if ((oTResult[0] != 6 && oTResult[1] != 6) || (oTResult[0] > 7 || oTResult[1] > 7)
								|| (oTResult[0] == 7 && oTResult[1] == 7) || (oTResult[0] == 7 && oTResult[1] != 6)
								|| (oTResult[0] != 6 && oTResult[1] == 7))
							throw new Exception(
									"One of the following issues occurred: \n- Input must be up to 6 or 7 (just in case the opponent scored 6) \n- Winner have to reach 6 or 7 (just in case the opponent scored 6) \n- Cannot be two-way tie");

					} else if ((oTResult[0] != 6 && oTResult[1] != 6) || (oTResult[0] > 6 || oTResult[1] > 6)
							|| (oTResult[0] == 6 && oTResult[1] == 6))
						throw new Exception(
								"One of the following issues occurred: \n- Input must be up to 6  \n- Winner have to reach 6 \n- Cannot be two-way tie");

					if (oTResult[0] > oTResult[1])
						player1.setTotalGameScore(player1.getTotalGameScore() + 1);
					else
						player2.setTotalGameScore(player2.getTotalGameScore() + 1);
				}

				if (player1.getTotalGameScore() > player2.getTotalGameScore())
					matchWinner = player1;
				else if (player1.getTotalGameScore() > player2.getTotalGameScore()) // check if can be only - else
					matchWinner = player2;

			}
		}
		if (matchWinner.equals(player1)) {
			matchWinner.setLastIndex(player1Index);
			matchWinner.setThePersonHeWonIndex(player2Index);
		} else {
			matchWinner.setLastIndex(player2Index);
			matchWinner.setThePersonHeWonIndex(player1Index);
		}

		int indexInNextStage = buttonNumber + 8;
		participants[indexInNextStage] = matchWinner;
		fireMatchWinner(matchWinner, buttonNumber, indexInNextStage);
	}

	// ---------------------------------------------- Fires & Methods ------------------------------------------------

	private void fireNewChampionshipCreated(String sportType) {
		for (TournamentEventsListener l : listeners)
			l.newChampionshipCreated(sportType);

	}

	private void fireTournamentIsFull() {
		for (TournamentEventsListener l : listeners) {
			l.tournamentisFull();
		}
	}

	private void fireAddParticipantEvent(Participant player) {
		for (TournamentEventsListener l : listeners) {
			l.participantAddedToModelEvent(player.getName(), participantsCounter);
		}

	}

	private int[] fireOverTime(int i) {
		int[] oTResult = null;
		for (TournamentEventsListener l : listeners) {
			oTResult = l.overTime(i);

		}
		return oTResult;
	}

	private void fireMatchWinner(Participant matchWinner, int buttonNumber, int indexInNextStage) {
		for (TournamentEventsListener l : listeners)
			l.matchWinnerUpdate(matchWinner, buttonNumber, indexInNextStage);

	}

	public void registerListener(TournamentEventsListener newListener) {
		listeners.add(newListener);

	}

	public boolean isReady(int player1Index, int player2Index) {
		if (participants[player1Index] != null && participants[player2Index] != null)
			return true;
		else
			return false;

	}

}

package controller;

import listeners.TournamentEventsListener;
import listeners.TournamentUIEventListener;
import model.Participant;
import model.TournamentManager;
import view.MainView;

public class Controller implements TournamentEventsListener, TournamentUIEventListener {
	private TournamentManager theModel;
	private MainView theView;

	public Controller(TournamentManager theModel, MainView theView) {
		this.theModel = theModel;
		this.theView = theView;

		theModel.registerListener(this);
		theView.registerListener(this);
	}

	public void addParticipantToModel(String name) throws Exception {
		if (name.isEmpty()) {
			throw new Exception("Name Cannot Be Empty");
		}
		try {
			theModel.addParticipant(name);
		} catch (Exception e) {
			theView.revealStartBtn();
			theView.TournamentIsFullMessage(e.getMessage());
			theView.disableAddBtn();

		}
	}

	@Override
	public void participantAddedToModelEvent(String name, int position) {
		theView.addParticipantToUI(name, position);

	}

	@Override
	public void tournamentisFull() {
		theView.revealStartBtn();
		theView.TournamentIsFullMessage("The Tournament is Full- Start Championship");
		theView.disableAddBtn();
	}

	@Override
	public void startChampionship(String sportType) {
		theModel.startChampionship(sportType);

	}

	@Override
	public void newChampionshipCreated(String sportType) {
		theView.createNewChampionshipInUI(sportType);

	}

	@Override
	public void playGame(int player1Index, int player2Index, int[] parti1, int[] parti2, int buttonNumber) {
		try {
			theModel.playGame(player1Index, player2Index, parti1, parti2, buttonNumber);
		} catch (Exception e) {
			fireErrorWithMatchScores(e.getMessage());
		}

	}

	private void fireErrorWithMatchScores(String message) {
		theView.errorWithMatchScores(message);

	}

	@Override
	public void matchWinnerUpdate(Participant matchWinner, int buttoNumber, int indexInNextStage) {
		theView.updateWinner(matchWinner.getName(), buttoNumber, indexInNextStage);
		theView.updateLabelsBackground(matchWinner.getLastIndex(), matchWinner.getThePersonHeWonIndex());

	}

	@Override
	public boolean viewAskIfSemiIsReady(int player1Index, int player2Index) {
		return theModel.isReady(player1Index, player2Index);
	}

	@Override
	public int[] overTime(int indicationNum) {

		return theView.itsOverTime(indicationNum);
	}

}

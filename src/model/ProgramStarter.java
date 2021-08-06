package model;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import view.MainView;

public class ProgramStarter extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TournamentManager tournament1 = new TournamentManager();
		MainView theView = new MainView(primaryStage);
		Controller controller = new Controller(tournament1, theView);
	}

}

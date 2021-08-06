package view;

import java.util.Vector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import listeners.TournamentUIEventListener;

public class MainView {

	private final int NUM_OF_QUARTERS = 4;
	private final int NUM_OF_HALVES = 2;
	private final int DEFAULT_NUM_OF_SETS = 3;
	private Vector<TournamentUIEventListener> allListeners = new Vector<TournamentUIEventListener>();
	private Label Caption;
	private Label[] theLabels = new Label[14];
	private Label winner;
	private Button[] playButtons = new Button[7];
	private RadioButton selected;
	private Button btnAddParti, btnStrtChmpshp, btnDone, btnUpdate;
	private HBox hboxMatchLbls, hboxParti1, hboxParti2;
	private Stage oTStage;
	private Stage matchDialog = new Stage();
	private VBox matchVBox, oTVbox;
	private Label overTimeCaption;

	public MainView(Stage primaryStage) {
		primaryStage.setTitle("The Tournaments Manager");
		Caption = new Label("Championship");
		start(primaryStage);

	}

	private void start(Stage primaryStage) {
		BorderPane bpRoot = new BorderPane();
		bpRoot.setPadding(new Insets(40));
		bpRoot.setBackground(setBackgroundColor(Color.LIGHTGRAY));

		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Top BorderPane Editor $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ 
		Caption.setUnderline(true);
		Caption.setFont(Font.font("Eras Demi ITC", FontWeight.EXTRA_BOLD, 40));
		DropShadow CaptionShad = new DropShadow(20, Color.LIGHTSKYBLUE);
		Caption.setEffect(CaptionShad);

		VBox topVbox = new VBox();
		topVbox.setPrefWidth(200);
		topVbox.setSpacing(10);
		topVbox.setBackground(setBackgroundColor(Color.LIGHTGRAY));
		topVbox.setAlignment(Pos.CENTER);

		ToggleGroup tglSport = new ToggleGroup();
		RadioButton rdoBasketBall = new RadioButton("Basketball");
		RadioButton rdoTennis = new RadioButton("Tennis");
		RadioButton rdoSoccer = new RadioButton("Soccer");
		rdoBasketBall.setToggleGroup(tglSport);
		rdoTennis.setToggleGroup(tglSport);
		rdoSoccer.setToggleGroup(tglSport);
		rdoBasketBall.setFont(Font.font("Eras Demi ITC", FontWeight.BOLD, 20));
		rdoTennis.setFont(Font.font("Eras Demi ITC", FontWeight.BOLD, 20));
		rdoSoccer.setFont(Font.font("Eras Demi ITC", FontWeight.BOLD, 20));

		HBox rdoButtons = new HBox();
		rdoButtons.setSpacing(10);
		rdoButtons.setPadding(new Insets(10));
		rdoButtons.setAlignment(Pos.CENTER);
		rdoButtons.getChildren().addAll(rdoBasketBall, rdoTennis, rdoSoccer);

		topVbox.getChildren().addAll(Caption, rdoButtons);
		bpRoot.setTop(topVbox);

		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Left BorderPane Editor $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ 
		VBox leftVbox = new VBox();
		leftVbox.setPrefWidth(100);
		leftVbox.setSpacing(52);
		leftVbox.setBackground(setBackgroundColor(Color.LIGHTGRAY));
		leftVbox.setAlignment(Pos.CENTER_LEFT);

		for (int i = 0; i < theLabels.length; i++) {
			theLabels[i] = createPartiLbl(" ", 20, false, FontWeight.BOLD);
			theLabels[i].setBorder(new Border(
					new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(4.0))));
			DropShadow LabelShad = new DropShadow(20, Color.LIGHTSKYBLUE);
			theLabels[i].setEffect(LabelShad);
			leftVbox.getChildren().add(theLabels[i]);
		}
		bpRoot.setLeft(leftVbox);

		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Center BorderPane Editor $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

		GridPane gpRootCenter = new GridPane();
		gpRootCenter.setPadding(new Insets(10));
		gpRootCenter.setAlignment(Pos.CENTER);
		gpRootCenter.setGridLinesVisible(false);
		gpRootCenter.setHgap(10);
		gpRootCenter.setVgap(10);

		// ########################################## AddParticipantPane ######################################
		Label addPartiLbl = new Label("New Participant Name: ");
		gpRootCenter.add(addPartiLbl, 0, 1);
		TextField tfAddingPar = new TextField();
		gpRootCenter.add(tfAddingPar, 1, 1);
		btnAddParti = new Button("Add Participant");
		gpRootCenter.add(btnAddParti, 0, 2);

		btnAddParti.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				for (TournamentUIEventListener l : allListeners) {
					String name = tfAddingPar.getText();
					try {
						l.addParticipantToModel(name);
						tfAddingPar.clear();
					} catch (Exception e) {
						alertCannotBeEmpty(e.getMessage());
					}
				}
			}
		});

		tfAddingPar.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.ENTER) {
					for (TournamentUIEventListener l : allListeners) {
						String name = tfAddingPar.getText();
						tfAddingPar.clear();
						try {
							l.addParticipantToModel(name);
						} catch (Exception e) {
							alertCannotBeEmpty(e.getMessage());
						}
					}
				}

			}
		});

		// ############################################### TheTournamentPane ###################################

		GridPane gpTournamentRoot = new GridPane();
		gpTournamentRoot.setAlignment(Pos.TOP_CENTER);
		gpTournamentRoot.setGridLinesVisible(false);
		gpTournamentRoot.setVgap(25.0);
		gpTournamentRoot.setHgap(200.0);

		for (int i = 0; i < playButtons.length; i++) {
			playButtons[i] = new Button("Play A Game");
			playButtons[i].minWidth(150);
		}

		StackPane stkRootCenter = new StackPane();

		// ---------------------------------------------------- LinesHBox --------------------------------------
		HBox centerHbox = new HBox(150);
		centerHbox.setAlignment(Pos.CENTER_LEFT);

		VBox qLinesBox = new VBox(84);
		qLinesBox.setAlignment(Pos.CENTER_LEFT);
		for (int i = 0; i < 4; i++) {
			Polyline poly1 = new Polyline(0.0, 80.0, 110.0, 80.0, 110.0, 40, 200.0, 40, 110, 40, 110.0, 0.0, 0.0, 0.0);
			poly1.setStrokeWidth(5);
			qLinesBox.getChildren().add(poly1);
		}

		VBox sLinesBox = new VBox(170);
		sLinesBox.setAlignment(Pos.CENTER_LEFT);
		for (int i = 0; i < 2; i++) {
			Polyline poly1 = new Polyline(0.0, 170.0, 110.0, 170.0, 110.0, 85, 200.0, 85, 110, 85, 110.0, 0.0, 0.0,
					0.0);
			poly1.setStrokeWidth(5);
			sLinesBox.getChildren().add(poly1);
		}

		VBox fLinesBox = new VBox();
		fLinesBox.setAlignment(Pos.CENTER_LEFT);
		Polyline poly1 = new Polyline(0.0, 350.0, 110.0, 350.0, 110.0, 175, 400.0, 175, 110, 175, 110.0, 0.0, 0.0, 0.0);
		poly1.setStrokeWidth(5);
		fLinesBox.getChildren().add(poly1);

		// ---------------------------------------------------- ButtonsHBox ------------------------------------
		HBox buttonsHbox = new HBox(267);
		buttonsHbox.setPadding(new Insets(80.0));

		VBox qButtons = new VBox(145);
		qButtons.setAlignment(Pos.CENTER_LEFT);
		for (int i = 0; i < 4; i++) {
			qButtons.getChildren().add(playButtons[i]);
			int player1Index = i * 2;
			int player2Index = (i * 2) + 1;
			int buttonNumber = i;

			playButtons[i].setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (matchDialog.isShowing())
						alertGameInProcess();
					else
						playGameClicked(player1Index, player2Index, buttonNumber);
				}
			});

		}

		VBox sButtons = new VBox(320);
		sButtons.setAlignment(Pos.CENTER_LEFT);
		for (int i = 4; i < 6; i++) {
			sButtons.getChildren().add(playButtons[i]);
			int player1Index = i * 2;
			int player2Index = (i * 2) + 1;
			int buttonNumber = i;

			playButtons[i].setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					if (matchDialog.isShowing())
						alertGameInProcess();
					else {
						for (TournamentUIEventListener l : allListeners)
							if (l.viewAskIfSemiIsReady(player1Index, player2Index)) {
								playGameClicked(player1Index, player2Index, buttonNumber);
							} else
								alertGameNotReady();
					}
				}
			});
		}

		VBox fButton = new VBox();
		int fButtonIndex = 6;
		fButton.setAlignment(Pos.CENTER_LEFT);
		fButton.getChildren().add(playButtons[6]);
		playButtons[fButtonIndex].setOnAction(new EventHandler<ActionEvent>() {
			int player1Index = fButtonIndex * 2;
			int player2Index = (fButtonIndex * 2) + 1;

			@Override
			public void handle(ActionEvent event) {
				if (matchDialog.isShowing())
					alertGameInProcess();
				else {
					for (TournamentUIEventListener l : allListeners)
						if (l.viewAskIfSemiIsReady(player1Index, player2Index)) {
							playGameClicked(player1Index, player2Index, fButtonIndex);
						} else {
							alertGameNotReady();
						}
				}
			}
		});

		// ---------------------------------------------------- LabelsHBox ------------------------------------
		HBox labelsHbox = new HBox(-50);
		labelsHbox.setAlignment(Pos.CENTER_LEFT);

		VBox sLabels = new VBox(140);
		sLabels.setAlignment(Pos.CENTER_LEFT);
		sLabels.setPadding(new Insets(195));
		for (int i = 8; i < 12; i++) {
			VBox sGroupLabels = new VBox(135);
			sGroupLabels.getChildren().addAll(theLabels[i], theLabels[++i]);
			sLabels.getChildren().add(sGroupLabels);
		}

		VBox fLabels = new VBox(315);
		fLabels.setAlignment(Pos.CENTER_LEFT);
		fLabels.getChildren().addAll(theLabels[12], theLabels[13]);

		// ---------------------------------------------------- WinnerLabel ----------------------------------------------
		winner = createPartiLbl(" ", 40, false, FontWeight.BOLD);
		winner.setAlignment(Pos.CENTER);
		winner.setMaxSize(750, 50);
		winner.setMinSize(250, 50);
		winner.setTranslateX(260);
		winner.setBorder(new Border(
				new BorderStroke(Color.GOLD, BorderStrokeStyle.DASHED, CornerRadii.EMPTY, new BorderWidths(2.0))));
		DropShadow winnerShad = new DropShadow(30, Color.GOLDENROD);
		winner.setEffect(winnerShad);
		winner.setVisible(false);

		bpRoot.setCenter(gpRootCenter);
		centerHbox.getChildren().addAll(qLinesBox, sLinesBox, fLinesBox);
		buttonsHbox.getChildren().addAll(qButtons, sButtons, fButton);
		labelsHbox.getChildren().addAll(sLabels, fLabels, winner);
		stkRootCenter.getChildren().addAll(gpTournamentRoot, centerHbox, labelsHbox, buttonsHbox);

		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ Right BorderPane Editor $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		btnStrtChmpshp = new Button("Start ChampionShip");
		btnStrtChmpshp.setVisible(false);

		BorderPane.setAlignment(btnStrtChmpshp, Pos.BOTTOM_RIGHT);
		bpRoot.setRight(btnStrtChmpshp);
		btnStrtChmpshp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					selected = (RadioButton) tglSport.getSelectedToggle();
					if (selected == null)
						throw new Exception("ChampionShip Type Was Not Selected");

					btnStrtChmpshp.setVisible(false);
					rdoButtons.setVisible(false);
					bpRoot.setCenter(stkRootCenter);

					for (TournamentUIEventListener l : allListeners)
						l.startChampionship(selected.getText());
				} catch (Exception e) {
					alertCannotBeEmpty(e.getMessage());
				}
			}
		});

		// Scene Initial
		primaryStage.setScene(new Scene(bpRoot, 1600, 1000));
		primaryStage.show();
	}

	// ------------------------------------------------ AddParticipantTools -----------------------------------------------
	public void TournamentIsFullMessage(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(message);
		alert.setContentText("Start Championship is on the Right Bottom --->");
		alert.showAndWait();
	}

	public void disableAddBtn() {
		btnAddParti.disarm();
	}

	public void revealStartBtn() {
		btnStrtChmpshp.setVisible(true);
	}

	public void addParticipantToUI(String name, int position) {
		theLabels[position].setText(name);
	}

	public void registerListener(TournamentUIEventListener newListener) {
		allListeners.add(newListener);
	}

	public void createNewChampionshipInUI(String sportType) {
		Caption.setText(sportType + " Championship");
	}

	// -------------------------------------------------------- GameTime --------------------------------------------------

	private void playGameClicked(int player1Index, int player2Index, int buttonNumber) {

		matchDialog.setTitle("Match Update Center");
		int[] parti1, parti2;
		Label[] matchLbls;
		TextField[] tfParti1, tfParti2;
		Label instructionLbl = new Label();
		Label matchUpdateCenterCaption = createPartiLbl(selected.getText(), 26, true, FontWeight.EXTRA_BOLD);
		matchUpdateCenterCaption.setBackground(setBackgroundColor(Color.TRANSPARENT));
		matchUpdateCenterCaption.setAlignment(Pos.CENTER);
		Label competitor1 = createPartiLbl(theLabels[player1Index].getText(), 16, false, FontWeight.NORMAL);
		Label competitor2 = createPartiLbl(theLabels[player2Index].getText(), 16, false, FontWeight.NORMAL);
		competitor1.setBackground(setBackgroundColor(Color.TRANSPARENT));
		competitor2.setBackground(setBackgroundColor(Color.TRANSPARENT));
		competitor1.setMaxWidth(100);
		competitor1.setMinWidth(100);
		competitor2.setMaxWidth(100);
		competitor2.setMinWidth(100);
		hboxParti1 = new HBox(20);
		hboxParti2 = new HBox(20);
		hboxMatchLbls = new HBox(30);
		hboxMatchLbls.setTranslateX(65);
		hboxMatchLbls.setAlignment(Pos.CENTER);
		hboxParti1.setAlignment(Pos.CENTER);
		hboxParti2.setAlignment(Pos.CENTER);
		hboxParti1.getChildren().add(competitor1);
		hboxParti2.getChildren().add(competitor2);
		btnDone = new Button("Done");

		if (selected.getText() == "Basketball") {
			instructionLbl.setText("Please Enter The Score Of Each Team At The End Of Each Quarter:");
			matchLbls = new Label[NUM_OF_QUARTERS];
			parti1 = new int[NUM_OF_QUARTERS];
			tfParti1 = new TextField[NUM_OF_QUARTERS];
			for (int k = 0; k < NUM_OF_QUARTERS; k++) {
				matchLbls[k] = createPartiLbl("Q #" + (k + 1), 10, false, FontWeight.BOLD);
				matchLbls[k].setBackground(setBackgroundColor(Color.TRANSPARENT));
				matchLbls[k].setMaxSize(40, 16);
				matchLbls[k].setMinSize(40, 16);
				hboxMatchLbls.getChildren().add(matchLbls[k]);
				tfParti1[k] = createNewTextField(50, "0");
				hboxParti1.getChildren().add(tfParti1[k]);
			}
			parti2 = new int[NUM_OF_QUARTERS];
			tfParti2 = new TextField[NUM_OF_QUARTERS];
			for (int l = 0; l < NUM_OF_QUARTERS; l++) {
				tfParti2[l] = createNewTextField(50, "0");
				hboxParti2.getChildren().add(tfParti2[l]);
			}

		} else if (selected.getText() == "Soccer") {
			instructionLbl.setText("Please Enter The Outcome Of The Game At The End Of Each Half:");
			matchLbls = new Label[NUM_OF_HALVES];
			parti1 = new int[NUM_OF_HALVES];
			tfParti1 = new TextField[NUM_OF_HALVES];
			for (int k = 0; k < NUM_OF_HALVES; k++) {
				matchLbls[k] = createPartiLbl("H #" + (k + 1), 10, false, FontWeight.BOLD);
				matchLbls[k].setBackground(setBackgroundColor(Color.TRANSPARENT));
				matchLbls[k].setMaxSize(40, 16);
				matchLbls[k].setMinSize(40, 16);
				hboxMatchLbls.getChildren().add(matchLbls[k]);
				tfParti1[k] = createNewTextField(50, "0");
				hboxParti1.getChildren().add(tfParti1[k]);
			}
			parti2 = new int[NUM_OF_HALVES];
			tfParti2 = new TextField[NUM_OF_HALVES];
			for (int l = 0; l < NUM_OF_HALVES; l++) {
				tfParti2[l] = createNewTextField(50, "0");
				hboxParti2.getChildren().add(tfParti2[l]);
			}
		} else {
			instructionLbl.setText("Please Enter The Outcome Of Each Player At The End Of Every Set:");
			matchLbls = new Label[DEFAULT_NUM_OF_SETS];
			parti1 = new int[DEFAULT_NUM_OF_SETS];
			tfParti1 = new TextField[DEFAULT_NUM_OF_SETS];
			for (int k = 0; k < DEFAULT_NUM_OF_SETS; k++) {
				matchLbls[k] = createPartiLbl("S #" + (k + 1), 10, false, FontWeight.BOLD);
				matchLbls[k].setBackground(setBackgroundColor(Color.TRANSPARENT));
				matchLbls[k].setMaxSize(40, 16);
				matchLbls[k].setMinSize(40, 16);
				hboxMatchLbls.getChildren().add(matchLbls[k]);
				tfParti1[k] = createNewTextField(50, "0");
				hboxParti1.getChildren().add(tfParti1[k]);
			}
			parti2 = new int[DEFAULT_NUM_OF_SETS];
			tfParti2 = new TextField[DEFAULT_NUM_OF_SETS];
			for (int l = 0; l < DEFAULT_NUM_OF_SETS; l++) {
				tfParti2[l] = createNewTextField(50, "0");
				hboxParti2.getChildren().add(tfParti2[l]);
			}
		}

		btnDone.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					for (int i = 0; i < tfParti2.length; i++) {
						if (tfParti1[i].getText().isEmpty() || tfParti2[i].getText().isEmpty()) {
							throw new Exception("Input Cannot Be Empty");
						}
						parti1[i] = Integer.parseInt(tfParti1[i].getText());
						parti2[i] = Integer.parseInt(tfParti2[i].getText());
						if (parti1[i] < 0 || parti2[i] < 0) {
							throw new Exception("Input Cannot Be Negative Integer");
						}
					}

					for (TournamentUIEventListener l : allListeners) {
						l.playGame(player1Index, player2Index, parti1, parti2, buttonNumber);
					}
					matchDialog.close();
				} catch (Exception e) {
					alertInvalidInput(e.getMessage());

				}
			}
		});
		matchVBox = new VBox(20);
		matchVBox.setAlignment(Pos.CENTER);
		matchVBox.getChildren().addAll(matchUpdateCenterCaption, instructionLbl, hboxMatchLbls, hboxParti1, hboxParti2,btnDone);
		matchDialog.setScene(new Scene(matchVBox, 600, 300));
		matchDialog.show();

	}

	// -------------------------------------------------------- OverTime --------------------------------------------------
	public int[] itsOverTime(int indicationNum) {
		oTStage = new Stage();
		oTStage.initStyle(StageStyle.TRANSPARENT);
		oTVbox = new VBox(20);
		oTVbox.setAlignment(Pos.CENTER);
		oTVbox.setMinWidth(300);
		int[] oTResult = new int[2];
		overTimeCaption = createPartiLbl(selected.getText() + " Over Time", 26, true, FontWeight.EXTRA_BOLD);
		overTimeCaption.setBackground(setBackgroundColor(Color.TRANSPARENT));
		overTimeCaption.setAlignment(Pos.CENTER);
		overTimeCaption.setMinWidth(400);
		overTimeCaption.maxWidth(500);
		DropShadow oTShad = new DropShadow(6, Color.INDIANRED);
		overTimeCaption.setEffect(oTShad);
		btnUpdate = new Button("Update");
		oTVbox.getChildren().add(overTimeCaption);
		if (selected.getText() == "Basketball")
			oTResult = viewOverTime(indicationNum);
		else if (selected.getText() == "Soccer") {
			if (indicationNum == 0)
				oTResult = viewOverTime(indicationNum);
			else if (indicationNum == 1)
				oTResult = viewSoccerPenalties(5);
			else
				oTResult = viewSoccerPenalties(1);
		} else
			oTResult = viewOverTime(indicationNum);

		oTStage.setScene(new Scene(oTVbox, 600, 300));
		oTStage.showAndWait();
		oTStage.close();

		return oTResult;
	}

	public int[] viewOverTime(int indicationNum) {
		int[] oTResult = new int[2];

		TextField tfOT1 = createNewTextField(50, "0");
		TextField tfOT2 = createNewTextField(50, "0");
		Label lblOTNum = createPartiLbl("OT/Set No. " + indicationNum, 16, true, FontWeight.BOLD);
		lblOTNum.setAlignment(Pos.CENTER);
		lblOTNum.setBackground(setBackgroundColor(Color.TRANSPARENT));

		oTVbox.getChildren().addAll(lblOTNum, tfOT1, tfOT2, btnUpdate);
		btnDone.setDisable(true);

		btnUpdate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				try {
					if (tfOT1.getText().isEmpty() || tfOT2.getText().isEmpty()) {
						throw new Exception("Input Cannot Be Empty");
					}
					oTResult[0] = Integer.parseInt(tfOT1.getText());
					oTResult[1] = Integer.parseInt(tfOT2.getText());
					if (oTResult[0] < 0 || oTResult[1] < 0) {
						throw new Exception("Input Cannot Be Negative Integer");
					}

					oTStage.hide();

				} catch (Exception e) {
					alertInvalidInput(e.getMessage());

				}
			}
		});

		return oTResult;
	}

	private int[] viewSoccerPenalties(int numOfPenalties) {
		int[] oTResult = new int[2];
		HBox penaltyNum = new HBox(35);
		HBox hboxPenalties1 = new HBox(20);
		HBox hboxPenalties2 = new HBox(20);
		penaltyNum.setAlignment(Pos.CENTER);
		hboxPenalties1.setAlignment(Pos.CENTER);
		hboxPenalties2.setAlignment(Pos.CENTER);

		CheckBox[] cBPenalties1 = new CheckBox[5];
		for (int k = 0; k < numOfPenalties; k++) {
			penaltyNum.getChildren().add(new Label(Integer.toString(k + 1)));
			cBPenalties1[k] = new CheckBox();
			hboxPenalties1.getChildren().add(cBPenalties1[k]);
		}

		CheckBox[] cBPenalties2 = new CheckBox[5];
		for (int l = 0; l < numOfPenalties; l++) {
			cBPenalties2[l] = new CheckBox();
			hboxPenalties2.getChildren().add(cBPenalties2[l]);
		}

		oTVbox.getChildren().addAll(penaltyNum, hboxPenalties1, hboxPenalties2, btnUpdate);
		btnDone.setDisable(true);

		btnUpdate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < numOfPenalties; i++) {
					if (cBPenalties1[i].isSelected())
						oTResult[0]++;
					if (cBPenalties2[i].isSelected())
						oTResult[1]++;
					;
				}
				oTStage.hide();
			}
		});

		return oTResult;
	}

	// -------------------------------------------------------- Winner ----------------------------------------------------
	public void updateWinner(String name, int buttonNumber, int labelNumber) {
		if (labelNumber != 14) {
			theLabels[labelNumber].setText(name);

		} else {
			winner.setText(name);
			winner.setBackground(setBackgroundColor(Color.CORNSILK));
			winner.setVisible(true);
		}

		playButtons[buttonNumber].setDisable(true);
	}

	public void updateLabelsBackground(int winnerLastIndex, int loserIndex) {
		theLabels[winnerLastIndex].setBackground(setBackgroundColor(Color.POWDERBLUE));
		theLabels[loserIndex].setBackground(setBackgroundColor(Color.ROSYBROWN));

	}

	// -------------------------------------------------------- Alerts ----------------------------------------------------
	protected void alertCannotBeEmpty(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initStyle(StageStyle.TRANSPARENT);
		alert.setHeaderText(message);
		alert.setContentText("Please Fill The Designated Fields");
		alert.showAndWait();

	}

	protected void alertGameInProcess() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initStyle(StageStyle.TRANSPARENT);
		alert.setHeaderText("Game In Process");
		alert.setContentText("Please Finish It");
		alert.showAndWait();

	}

	protected void alertGameNotReady() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initStyle(StageStyle.TRANSPARENT);
		alert.setHeaderText("Game Is Not Ready Yet");
		alert.setContentText("First, Please Play Earlier Games");
		alert.showAndWait();

	}

	protected void alertInvalidInput(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initStyle(StageStyle.TRANSPARENT);
		alert.setHeaderText(message);
		alert.setContentText("Please Try Another Input");
		alert.showAndWait();
	}

	// -------------------------------------------------------- DesignTools -----------------------------------------------
	private Label createPartiLbl(String text, double size, boolean underline, FontWeight fontWeight) {
		Label theLabel = new Label(text);
		theLabel.setMinWidth(200);
		theLabel.setMaxWidth(200);
		theLabel.setFont(Font.font("Verdana", fontWeight, size));
		theLabel.setUnderline(underline);
		theLabel.setBackground(setBackgroundColor(Color.WHITE));
		return theLabel;
	}

	private TextField createNewTextField(int width, String initialText) {
		TextField theTF = new TextField(initialText);
		theTF.setMinWidth(width);
		theTF.setMaxWidth(width);
		return theTF;

	}

	private Background setBackgroundColor(Color color) {
		Background theBackground = new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
		return theBackground;

	}

	public void errorWithMatchScores(String message) {
		alertInvalidInput(message);
		matchDialog.close();

	}

}

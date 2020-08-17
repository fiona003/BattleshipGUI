import java.util.Scanner;
import java.io.FileNotFoundException;
import javafx.scene.layout.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*; 
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.geometry.Insets;
import javafx.stage.FileChooser;
import java.io.*;
import javafx.scene.text.*;
import java.util.Arrays;
import javafx.geometry.Pos;

public class BattleshipGUI extends Application {
	
	public static Button[][] boardCPU = new Button[10][10];
	public static Button[][] boardPlayer = new Button[10][10];
	public static boolean canMove = true;
    
    public static void main(String[] args) {
        launch(args);
    }

    public static void loadFile(char[][] board, String fileName, Label text) {
		try {
        	Scanner file = new Scanner(new File(fileName));
        	while (file.hasNextLine()) {
				for (int row = 0; row < board.length; row++) {
					for (int col = 0; col < board.length; col++ ) {
         				board[row][col] = file.next().charAt(0);
     				}
				}
			}
			text.setText("File loaded!");
		}
		catch (FileNotFoundException exception) {
			System.out.println("Error opening file");
		}
	}
	
	public static void makeButton(GridPane buttons, Button[][] buttonArray, char[][] textBoard, Label words, Button[][] buttonArray2, char[][] textBoard2, Label words2, int identifier, int[] fleets, int[] fleetNumCPU, int[] fleetNumPLAYER) {
        int y = 1;
 
        for (int row = 0; row < buttonArray.length; row++) {
	        for (int col = 0; col < buttonArray[row].length; col++) {
				buttonArray[row][col] = new Button("");
				buttonArray[row][col].setDisable(true);
				int x1 = row;
				int y1 = col;
				if (identifier == 0) {
				 	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
		            public void handle(ActionEvent e) 
			            {
			            	whenWin(textBoard2, textBoard, fleets);
			            	if (canMove) playerMove(textBoard, words, x1, y1, fleets, fleetNumCPU, textBoard2);
			            	whenWin(textBoard2, textBoard, fleets);
			            	if (canMove) cpuMove(textBoard2, words2, fleets, fleetNumPLAYER, textBoard);
			            	whenWin(textBoard2, textBoard, fleets);	
			            }
			        };
			        buttonArray[row][col].setOnAction(event);
	       		}
			 	buttonArray[row][col].setStyle("-fx-background-radius: 0");
			 	buttonArray[row][col].setMinHeight(43);
		        buttonArray[row][col].setMaxHeight(43);
		        buttonArray[row][col].setMinWidth(43);
		        buttonArray[row][col].setMaxWidth(43);
		        buttons.add(buttonArray[row][col], col, y);
		        if (col == 9) y++;
        	}
		}
	}
	
	public static void playerMove(char[][] refBoard, Label text, int letter, int num, int[] fleets, int[] fleetNum, char[][] cpuTxt) {
		if (refBoard[letter][num] == '*') {
			refBoard[letter][num] = 'M';
			text.setText("You have missed sir!");
			boardCPU[letter][num].setDisable(true);
			boardCPU[letter][num].setStyle("-fx-base: #ff6666;");
		} else if (refBoard[letter][num] != '*') {
			fleets[1]--;
			text.setText("Direct hit, nice shot sir!");
			checkBoat(text, refBoard, fleetNum, letter, num, "Direct hit, nice shot sir!\nYou have sunk the computer's ");
			boardCPU[letter][num].setDisable(true);
			boardCPU[letter][num].setStyle("-fx-base: #99ff66;");	
		}
	}
	
	public static void cpuMove(char[][] refBoard, Label text, int[] fleets, int[] fleetArr, char[][] playerTxt) {
		while (true) {
			String inputCPU = "" + (int) Math.floor(Math.random() * 10) + (int) Math.floor(Math.random() * 10);
			int letter = Integer.parseInt(inputCPU.substring(0, 1));
			int asciiLetter = letter + 65; //+ 65 to get actual ascii value
			int x = Integer.parseInt(inputCPU.substring(1, 2));	
			
			if (refBoard[letter][x] != 'M' || refBoard[letter][x] != 'H') {
				if (refBoard[letter][x] == '*') {
					refBoard[letter][x] = 'M';
					boardPlayer[letter][x].setStyle("-fx-base: #ff6666;");
					text.setText("The computer has attacked " + (char) asciiLetter + x + " and missed!");
					boardPlayer[letter][x].setDisable(true);
					break;
				} else if (refBoard[letter][x] != '*' && refBoard[letter][x] != 'H' && refBoard[letter][x] != 'M') {
					fleets[0]--;
					String boatHit = (refBoard[letter][x] == 'C' ? "Carrier!" : refBoard[letter][x] == 'B' ? "Battleship!" : refBoard[letter][x] == 'S' ? "Submarine!" : refBoard[letter][x] == 'D' ? "Destroyer!" : "Patrol Boat!");
					text.setText("The computer has attacked " + (char) asciiLetter + x + " and hit your " + boatHit + "\n"); // get what ship the computer hit
					checkBoat(text, refBoard, fleetArr, letter, x, "The computer has sunk your ");
					boardPlayer[letter][x].setStyle("-fx-base: #99ff66;");
					boardPlayer[letter][x].setDisable(true);
					break;
				}
			}
		}
	}
	
	public static void checkBoat(Label text, char[][] refBoard, int[] fleetArr, int y, int x, String phrase) {
		for (int row = 0; row < refBoard.length; row++) {
			for (int col = 0; col < refBoard[row].length; col++) {
				if (refBoard[row][col] == refBoard[y][x]) {
					switch (refBoard[y][x]) {
						case 'C':
							fleetArr[0]--;
							if (fleetArr[0] == 0) text.setText(phrase + "Carrier!");
							break;
						case 'B':
							fleetArr[1]--;
							if (fleetArr[1] == 0) text.setText(phrase + "Battleship!");
							break;
						case 'S':
							fleetArr[2]--;
							if (fleetArr[2] == 0) text.setText(phrase + "Submarine!");
							break;
						case 'D':
							fleetArr[3]--;
							if (fleetArr[3] == 0) text.setText(phrase + "Destroyer!");
							break;
						case 'P':
							fleetArr[4]--;
							if (fleetArr[4] == 0) text.setText(phrase + "Patrol Boat!");
							break;
					}
				refBoard[y][x] = 'H';
				}
			}
		}
	}

	public static void whenWin(char[][] cpuTxt, char[][] playerTxt, int[] fleets) {
		if (fleets[0] == 0 || fleets[1] == 0) {
			canMove = false;
			Stage newWindow = new Stage();
	        newWindow.setTitle("Message");
	        Label text = new Label(fleets[0] == 0 ? "The computer has won the game!" : "The player has won the game!");
	        Button okBtn = new Button("OK");
	        text.setPadding(new Insets(10, 10, 10, 10));
	        BorderPane layout = new BorderPane();
	        layout.setCenter(text);
	        layout.setBottom(okBtn);
	        layout.setAlignment(okBtn, Pos.BOTTOM_CENTER);
	        Scene secondScene = new Scene(layout, 230, 70);
	        
			for (int row = 0; row < boardCPU.length; row++) {
				for (int col = 0; col < boardCPU[row].length; col++) {
					boardCPU[row][col].setStyle("");
					boardPlayer[row][col].setStyle("");
					if (playerTxt[row][col] == 'H' || playerTxt[row][col] == 'M') boardCPU[row][col].setStyle("-fx-base: #cccccc;");
					if (cpuTxt[row][col] == 'H' || cpuTxt[row][col] == 'M') boardPlayer[row][col].setStyle("-fx-base: #cccccc;");
					boardCPU[row][col].setDisable(true);
					boardPlayer[row][col].setDisable(true);
				}
			}
			
			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
				public void handle(ActionEvent e) 
		        {
	    				newWindow.close();
				}
			};
		
			okBtn.setOnAction(event);
	        newWindow.setScene(secondScene);
	        newWindow.show();
		}
	}

	public void start(Stage primaryStage) {
		primaryStage.setTitle("Battleship GUI");
		GridPane mainPane = new GridPane();
        Scene scene = new Scene(mainPane, 980, 650);
		char[][] cpuBord = new char[10][10];
		char[][] playerBord = new char[10][10];
		char[][] cpuBord2 = new char[10][10]; //board with the *
		int[] fleets = {17, 17}; //first is total user's fleet units, second is total CPU's fleet units
		int[] fleetPlayer = {5, 4, 3, 3, 2};
		int[] fleetCPU = {5, 4, 3, 3, 2};
//////////////////  Pictures  //////////////////
		Label rows = new Label("");
        Label cols = new Label("");
        Label rows2 = new Label("");
        Label cols2 = new Label("");
        rows.setGraphic(new ImageView(new Image("file:rows.png", 44.0, 0.0, true, true)));
        rows2.setGraphic(new ImageView(new Image("file:rows.png", 44.0, 0.0, true, true)));
        cols.setGraphic(new ImageView(new Image("file:cols.png", 430.0, 0.0, true, true)));
        cols2.setGraphic(new ImageView(new Image("file:cols.png", 430.0, 0.0, true, true)));
        rows2.setPadding(new Insets(0, 0, 10, 15));
        HBox hbox = new HBox(70, cols);
        HBox hbox2 = new HBox(70, cols2);
        VBox vbox = new VBox(70, rows);
        VBox vbox2 = new VBox(70, rows2);
//////////////////  Text msgs  //////////////////
		Label player = new Label("Player message:");
		player.setStyle("-fx-font: 32 arial; -fx-font-weight: bold; -fx-text-fill: #7070db;");
		player.setPadding(new Insets(0, 0, 6, 0));
        Label cpu = new Label("CPU message:");
        cpu.setStyle("-fx-font: 32 arial; -fx-font-weight: bold; -fx-text-fill: #ff5050");
        cpu.setPadding(new Insets(0, 0, 6, 0));
        Label playerText = new Label("Please open the Player.txt file!");
		playerText.setStyle("-fx-font: 16.5 arial; -fx-text-fill: #7070db");
		Label cpuText = new Label("Please open the CPU.txt file!");
		cpuText.setStyle("-fx-font: 16.5 arial; -fx-text-fill: #ff5050");
		StackPane label = new StackPane(player);
//////////////////  Buttons  //////////////////
		GridPane buttonsGrid_player = new GridPane();
		GridPane buttonsGrid_cpu = new GridPane();
		makeButton(buttonsGrid_cpu, boardPlayer, playerBord, cpuText, boardCPU,  cpuBord, playerText, 1, fleets, fleetCPU, fleetPlayer); //Player board
		makeButton(buttonsGrid_player, boardCPU,  cpuBord, playerText, boardPlayer, playerBord, cpuText, 0, fleets, fleetCPU, fleetPlayer); //CPU board
//////////////////  Menu & menu events  //////////////////
        EventHandler<ActionEvent> event1 = new EventHandler<ActionEvent>() { 
            int times = 0;
            public void handle(ActionEvent e) 
            {
            	FileChooser fileChooser = new FileChooser();
            	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
            	File file = fileChooser.showOpenDialog(primaryStage); 
                if (file != null) {
                	String fileName = file.getName();
                	if (fileName.equals("PLAYER.txt")) {
                		times++;
                		loadFile(playerBord, fileName, playerText);
                		for (int row = 0; row < boardPlayer.length; row++) {
                			for (int col = 0; col < boardPlayer[row].length; col++) {
                				boardPlayer[row][col].setText(" " + playerBord[row][col] + " ");
                				if (times == 2) {
                					boardPlayer[row][col].setDisable(false);
                					boardCPU[row][col].setDisable(false);
                				}
                			}	
                		}
                	}
                	if (fileName.equals("CPU.txt")) {
                		times++;
                		loadFile(cpuBord, fileName, cpuText);
                		for (int row = 0; row < boardCPU.length; row++) {
					        for (int col = 0; col < boardCPU[row].length; col++) {
					        	boardCPU[row][col].setText(" * ");
                				if (times == 2) {
                		    		boardPlayer[row][col].setDisable(false);
                					boardCPU[row][col].setDisable(false);
                				}
							}
                		}
                	}
            	}
            }
        };
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            {
    			start(primaryStage);
    			canMove = true;
            } 
        };
        EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
                System.exit(0); 
            } 
        };

        MenuBar mb = new MenuBar();
        Menu m = new Menu("File"); 
        MenuItem m1 = new MenuItem("Open"); 
        MenuItem m2 = new MenuItem("Restart"); 
        MenuItem m3 = new MenuItem("Exit");
        m.setStyle("-fx-padding: 4 4 4 4");
        m.getItems().add(m1); 
        m1.setOnAction(event1);
        m.getItems().add(m2);
        m2.setOnAction(event2); 
        m.getItems().add(m3);
        m3.setOnAction(event3);
        mb.getMenus().add(m); 
//////////////////  Adding stuff to mainpane  //////////////////
        mainPane.add(mb, 0, 0);
        mainPane.add(vbox, 0, 2);
        mainPane.add(vbox2, 2, 2);
        mainPane.add(hbox, 1, 1);
        mainPane.add(hbox2, 3, 1);
     	mainPane.add(buttonsGrid_player, 3, 2);
        mainPane.add(buttonsGrid_cpu, 1, 2);
        mainPane.add(player, 1, 3);
        mainPane.add(playerText, 1, 4);
        mainPane.add(cpu, 3, 3);
        mainPane.add(cpuText, 3, 4);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
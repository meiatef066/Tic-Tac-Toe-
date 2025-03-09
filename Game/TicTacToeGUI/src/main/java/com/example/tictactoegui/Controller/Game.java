package com.example.tictactoegui.Controller;

import com.example.tictactoegui.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Game {
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label winner;
    @FXML
    private GridPane Board;

    private Button[][] buttons = new Button[3][3];
    private boolean xTurn = true;

    @FXML
    public void initialize() {
        createBoard();
    }

    private void createBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setMinSize(100, 100);
              button.setStyle("-fx-background-color: rgb(85,74,149);");
                button.setOnAction(this::getMove);
                Board.add(button, i, j);
                buttons[i][j] = button;
            }
        }
    }

    @FXML
    public void getMove(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getUserData() != null) {
            return;
        }

        Image xImage = new Image(getClass().getResourceAsStream("/assert/x.png"));
        Image oImage = new Image(getClass().getResourceAsStream("/assert/o.png"));

        ImageView imageView = new ImageView(xTurn ? xImage : oImage);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        clickedButton.setGraphic(imageView);
        clickedButton.setUserData(xTurn ? "X" : "O");

        xTurn = !xTurn;
        check_winner();
    }

    private void check_winner() {
        String winnerSymbol = null;

        for (int i = 0; i < 3; i++) {
            if (isSame(buttons[i][0], buttons[i][1], buttons[i][2])) {
                winnerSymbol = (String) buttons[i][0].getUserData();
            }
            if (isSame(buttons[0][i], buttons[1][i], buttons[2][i])) {
                winnerSymbol = (String) buttons[0][i].getUserData();
            }
        }

        if (isSame(buttons[0][0], buttons[1][1], buttons[2][2])) {
            winnerSymbol = (String) buttons[0][0].getUserData();
        }

        if (isSame(buttons[0][2], buttons[1][1], buttons[2][0])) {
            winnerSymbol = (String) buttons[0][2].getUserData();
        }

        if (winnerSymbol != null) {
            winner.setText("Winner: " + winnerSymbol);
            disableBoard();
        } else {
            check_tie();
        }
    }

    private boolean isSame(Button b1, Button b2, Button b3) {
        return (b1.getUserData() != null &&
                b1.getUserData().equals(b2.getUserData()) &&
                b2.getUserData().equals(b3.getUserData()));
    }

    private void check_tie() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                if (button.getUserData() == null) {
                    return;
                }
            }
        }
        winner.setText("It's a Tie!");
        disableBoard();
    }

    private void disableBoard() {
        for (Button[] row : buttons) {
            for (Button button : row) {
                button.setDisable(true);
            }
        }
        ShowAlert();
    }

    private void ShowAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION); // Use CONFIRMATION for Yes/No options
        alert.setTitle("Tic Tac Toe");
        alert.setHeaderText(null);  // No header text
        alert.setContentText("Would you like to play again or back to home?");

        ButtonType playAgain = new ButtonType("Play Again");
        ButtonType backHome = new ButtonType("Back to Home");
        alert.getButtonTypes().setAll(playAgain, backHome);

        alert.showAndWait().ifPresent(response -> {
            if (response == playAgain) {
                ResetBoard(); // Call the reset function
            } else if (response == backHome) {
                // Handle going back to home (if you have a scene switch logic)
                System.out.println("Back to home clicked");
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FXML/Main.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());

                    // Get the current stage from the button
                    Stage stage = (Stage) (Stage) Board.getScene().getWindow();
                    stage.setScene(scene);
                    stage.setTitle("Tic Tac Toe - Multiplayer");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Exit clicked");
            }
        });
    }


    @FXML
    public void ResetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setGraphic(null);
                buttons[i][j].setUserData(null);
                buttons[i][j].setDisable(false);
            }
        }
        winner.setText("");
        xTurn = true;
    }
}

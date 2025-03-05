package com.example.tictactoegui.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class Game {
    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label winner;
    @FXML
    private GridPane Board;

    public Button[][] buttons=new Button[3][3];

    private boolean xTurn = true;

    @FXML
    public void initialize() {
        createBoard();
    }

    private void createBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = new Button();
                button.setMinSize(100, 100);
                button.setOnAction(this::getMove); // Assign click event
                Board.add(button,i,j);
                buttons[i][j]=button;
            }
        }
    }

    @FXML
    public void getMove( ActionEvent event){
            Button clickedButton = (Button) event.getSource();
            if(!clickedButton.getText().isEmpty()){
                return;
            }

            clickedButton.setText(xTurn ? "X" : "O");


            xTurn = !xTurn;   // Toggle turn
            check_winner();
    }


    private void check_winner(){
        String winnerSymbol =null;
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
                    buttons[i][1].getText().equals(buttons[i][2].getText()) &&
                    !buttons[i][0].getText().isEmpty()) {
                winnerSymbol = buttons[i][0].getText();
            }
            if (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
                    buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                    !buttons[0][i].getText().isEmpty()) {
                winnerSymbol = buttons[0][i].getText();
            }
        }
        if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][2].getText()) &&
                !buttons[0][0].getText().isEmpty()) {
            winnerSymbol = buttons[0][0].getText();
        }

        if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
                buttons[1][1].getText().equals(buttons[2][0].getText()) &&
                !buttons[0][2].getText().isEmpty()) {
            winnerSymbol = buttons[0][2].getText();
        }
        if (winnerSymbol != null) {
            winner.setText("Winner: " + winnerSymbol);
            disableBoard();
        }

    }
    private void disableBoard(){
           for(Button[] row : buttons){
               for(Button button : row){
                   button.setDisable(true);
               }
           }
    }

    private void check_tie(){}

    @FXML
    public void ResetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
            }
        }
    }
}

package com.example.tictactoegui.Controller;

import com.example.tictactoegui.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Main {

    private Stage stage;
    private Scene scene;

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void Computer( ActionEvent event) {
    }

    @FXML
    void MultiPlayer( ActionEvent event) {
        // multiplayer page
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FXML/Game.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                // Get the current stage from the button
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("Tic Tac Toe - Multiplayer");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }



    @FXML
    void Online( ActionEvent event) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FXML/Users.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                // Get the current stage from the button
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.setTitle("Tic Tac Toe - Multiplayer");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    @FXML
    void Exit( ActionEvent event) {

    }

}

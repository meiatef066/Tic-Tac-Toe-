package com.example.tictactoegui.Controller;

import com.example.tictactoegui.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Users {
    @FXML
    private ListView<User> listView;
    @FXML
    private TextField searchField;
    @FXML
    private TextField playerName;

    @FXML
    public void initialize() {
        fetchUserFromServer();
    }

    private void fetchUserFromServer() {
        new Thread(() -> {
            try {
                URL url=new URL("http://localhost:8080/v1/api/users");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    System.out.println("Failed : HTTP error code : " + conn.getResponseCode());
                    return;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }
                conn.disconnect();
                // Parse JSON response using Jackson ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();
                List<User> users = objectMapper.readValue(response.toString(), new TypeReference<List<User>>() {});

                // Update UI on JavaFX thread
                Platform.runLater(() -> {
                    updateViewlist(users);
                });

            }catch (Exception e){
                e.printStackTrace();
            }
                  }).start();
          }

    private void updateViewlist( List<User> users ) {
        listView.setCellFactory(param ->new ListCell<User>(){
            private final Button onlineButton = new Button("Invite");

            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                }else {
                    setText(user.getUsername() + (user.getUserStatus()=="ONLINE" ? " (Online)" : " (Offline)"));
                    if (user.getUserStatus()=="ONLINE") {
                        onlineButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        onlineButton.setOnAction(e -> inviteUser(user));
                        setGraphic(onlineButton);
                    }else{
                        setGraphic(null); // Hide button for offline users
                    }
                }
            }
                }

        );
        listView.setItems(FXCollections.observableArrayList(users));

    }

    private void inviteUser( User user ) {
        // api(api/invite?from?to?)
        new Thread(() -> {
            try {
                URL url=new URL("http://localhost:8080/v1/api/invites/send?sender="+playerName.getText()+"&receiver="+user.getUsername());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() == 200) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Invitation Sent");
                        alert.setHeaderText(null);
                        alert.setContentText("Invitation sent to " + user.getUsername());
                        alert.showAndWait();
                    });
                }

                conn.disconnect();

            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

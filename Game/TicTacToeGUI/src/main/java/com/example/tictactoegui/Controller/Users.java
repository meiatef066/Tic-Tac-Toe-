package com.example.tictactoegui.Controller;

import com.example.tictactoegui.model.User;
import com.example.tictactoegui.socket.WebSocketClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Users {
    @FXML
    private ListView<User> listView;
    @FXML
    private TextField userInput;

    private String playerName = "Alice";
    private List<User> users = new ArrayList<>();
    private WebSocketClient webSocket;

    @FXML
    public void initialize() {
        fetchUsersFromServer();
        webSocket = new WebSocketClient("ws://localhost:8080/ws", this::handleIncomingMessage);
    }

    private void fetchUsersFromServer() {
        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/v1/api/users"))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                this.users = mapper.readValue(response.body(), mapper.getTypeFactory().constructCollectionType(List.class, User.class));

                Platform.runLater(() -> updateListView(users));

            } catch (Exception e) {
                Platform.runLater(() -> showError("Error fetching users: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void searchByName(MouseEvent event) {
        searchByNameHelper();
    }

    @FXML
    private void searchByName(ActionEvent event) {
        searchByNameHelper();
    }

    private void searchByNameHelper() {
        String username = userInput.getText().trim();
        if (username.isEmpty()) {
            showError("Please enter a user name");
            return;
        }

        new Thread(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/v1/api/users/" + username))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(response.body(), User.class);

                List<User> filteredUsers = new ArrayList<>();
                if (user != null) {
                    filteredUsers.add(user);
                }

                Platform.runLater(() -> updateListView(filteredUsers));

            } catch (Exception e) {
                Platform.runLater(() -> showError("User not found: " + e.getMessage()));
            }
        }).start();
    }

    private void updateListView(List<User> users) {
        ObservableList<User> observableUsers = FXCollections.observableArrayList(users);
        listView.setItems(observableUsers);

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Label userLabel = new Label(user.getUsername() + " (" + user.getUserStatus() + ")");
                    Button inviteButton = new Button("Invite");

                    if ("ONLINE".equals(user.getUserStatus())) {
                        inviteButton.setOnAction(event -> sendInvitation(user));
                        hbox.getChildren().addAll(userLabel, inviteButton);
                    } else {
                        hbox.getChildren().add(userLabel);
                    }

                    setGraphic(hbox);
                }
            }
        });
    }

    private void sendInvitation(User user) {
        System.out.println("Invitation sent to: " + user.getUsername());
        if (webSocket == null) {
            showError("WebSocket not connected.");
            return;
        }

        String jsonMessage = "{\"type\": \"invite\", \"from\": \"" + playerName + "\", \"to\": \"" + user.getUsername() + "\"}";
        webSocket.sendMessage(jsonMessage);
    }

    private void handleIncomingMessage(String message) {
        System.out.println("Received message: " + message);
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(message);
            String type = jsonNode.get("type").asText();
            String fromUser = jsonNode.get("from").asText();

            if ("invite".equals(type)) {
                Platform.runLater(() -> showInviteDialog(fromUser));
            }
        } catch (Exception e) {
            System.out.println("Error parsing WebSocket message: " + e.getMessage());
        }
    }

    private void showInviteDialog(String fromUser) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Invitation");
        alert.setHeaderText(fromUser + " invited you to a game!");
        alert.setContentText("Do you accept the invitation?");

        ButtonType acceptButton = new ButtonType("Accept", ButtonBar.ButtonData.OK_DONE);
        ButtonType declineButton = new ButtonType("Decline", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(acceptButton, declineButton);

        alert.showAndWait().ifPresent(response -> {
            String jsonMessage;
            if (response == acceptButton) {
                jsonMessage = "{\"type\": \"accept\", \"from\": \"" + playerName + "\", \"to\": \"" + fromUser + "\"}";
            } else {
                jsonMessage = "{\"type\": \"reject\", \"from\": \"" + playerName + "\", \"to\": \"" + fromUser + "\"}";
            }
            webSocket.sendMessage(jsonMessage);
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

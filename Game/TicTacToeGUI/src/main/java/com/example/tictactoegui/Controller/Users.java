package com.example.tictactoegui.Controller;

import com.example.tictactoegui.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.*;
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

    private List<User> users = new ArrayList<>();

    @FXML
    public void initialize() {
        fetchUsersFromServer();
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
                } else {
                    setText(user.getUsername() + " (" + user.getUserStatus() + ")");
                }
            }
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

package com.example.tictactoegui.Controller;

import com.example.tictactoegui.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Users {
    @FXML
    private ListView<User> listView;  // ✅ Change ListView<Text> → ListView<User>

    @FXML
    private TextField searchField;

    private List<User> users = new ArrayList<>();

    @FXML
    public void initialize() {
        fetchUsersFromServer();
    }

    private void fetchUsersFromServer() {
        new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080/v1/api/users");
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(url.toURI()).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                this.users = mapper.readValue(response.body(), new TypeReference<List<User>>() {});

                Platform.runLater(this::updateListView);

            } catch (Exception e) {
                Platform.runLater(() -> showError("Error fetching users: " + e.getMessage()));
            }
        }).start();
    }

    private void updateListView() {
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

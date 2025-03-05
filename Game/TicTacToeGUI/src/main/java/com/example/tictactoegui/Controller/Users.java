package com.example.tictactoegui.Controller;

import com.example.tictactoegui.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Users {
    @FXML
    private ListView<Text> listView;
    @FXML
    private TextField searchField;

    private List<User> users = new ArrayList<>();

    @FXML
    public void initialize() {
        System.out.println("Initializing Users Controller...");
        fetchUsersFromServer();
    }
    private void fetchUsersFromServer() {
        new Thread( ()->{
            try {

                URL url = new URL("http://localhost:8080/v1/api/users");
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request=HttpRequest.newBuilder().uri(url.toURI()).GET().build();

                HttpResponse<String> response= client.send(request,HttpResponse.BodyHandlers.ofString());

                System.out.println("Response Code: " + response.statusCode());
                System.out.println("Response Body: " + response.body());

//                ObjectMapper mapper = new ObjectMapper();
//                try {
//                    this.users = mapper.readValue(response.body(), new TypeReference<List<User>>() {});
//                } catch (Exception e) {
//                    System.out.println("Error parsing JSON: " + e.getMessage());
//                }

//                for (User user : users) {
//                    System.out.println(user);
//                }
                if (response.body().isEmpty()) {
                    System.out.println("❌ Empty response from server!");
                } else {
                    System.out.println("✅ Received Data: " + response.body());
                }

//                Platform.runLater(() -> {
//                    updateListView(users); // Update UI
//                });

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    private void updateListView(List<User> users) {
        ObservableList<Text> items = FXCollections.observableArrayList();

    }

    private void WaitUntillConnect() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Game Update");
        alert.setContentText("Wait Untill connect to server");
        alert.showAndWait(); // Wait until user closes
    }

}

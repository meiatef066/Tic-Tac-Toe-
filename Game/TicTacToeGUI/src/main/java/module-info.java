module com.example.tictactoegui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires Java.WebSocket;
    requires spring.websocket;

    opens com.example.tictactoegui to javafx.fxml;
    exports com.example.tictactoegui;
    exports com.example.tictactoegui.Controller;
    opens com.example.tictactoegui.Controller to javafx.fxml;
    opens com.example.tictactoegui.model to com.fasterxml.jackson.databind;
    exports com.example.tictactoegui.socket;
    opens com.example.tictactoegui.socket to javafx.fxml;

}

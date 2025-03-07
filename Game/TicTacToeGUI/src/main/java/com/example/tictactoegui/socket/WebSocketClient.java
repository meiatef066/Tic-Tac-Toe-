package com.example.tictactoegui.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class WebSocketClient {
    private WebSocket webSocket;
    private Consumer<String> messageHandler;

    public WebSocketClient(String serverUri, Consumer<String> messageHandler) {
        this.messageHandler = messageHandler;
        HttpClient client = HttpClient.newHttpClient();
        this.webSocket = client.newWebSocketBuilder()
                .buildAsync(URI.create(serverUri), new WebSocketListener())
                .join();
    }

    public void sendMessage(String message) {
        if (webSocket != null) {
            webSocket.sendText(message, true);
        } else {
            System.out.println("WebSocket is not connected.");
        }
    }

    private class WebSocketListener implements WebSocket.Listener {
        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("Connected to WebSocket server");
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            String message = data.toString();
            System.out.println("Received: " + message);

            if (messageHandler != null) {
                messageHandler.accept(message);
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("WebSocket error: " + error.getMessage());
        }
    }
}

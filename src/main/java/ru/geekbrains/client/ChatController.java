package ru.geekbrains.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import ru.geekbrains.server.ServerCommandConstants;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField messageField, loginField;
    @FXML
    private HBox messagePanel, authPanel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ListView<String> clientList;

    private final Network network;

    public ChatController() {
        this.network = new Network(this);
    }

    public void setAuthenticated(boolean authenticated) {
        authPanel.setVisible(!authenticated);
        authPanel.setManaged(!authenticated);
        messagePanel.setVisible(authenticated);
        messagePanel.setManaged(authenticated);
        clientList.setVisible(authenticated);
        clientList.setManaged(authenticated);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setAuthenticated(false);
    }

    public void displayMessage(String text) {
        if (chatArea.getText().isEmpty()) {
            chatArea.setText(text);
        } else {
            chatArea.setText(chatArea.getText() + "\n" + text);
        }
    }

    public void displayClient(String nickName) {
        Platform.runLater(() -> clientList.getItems().add(nickName));
    }

    public void removeClient(String nickName) {
        Platform.runLater(() -> clientList.getItems().remove(nickName));
    }

    public void sendAuth() {
        boolean authenticated = network.sendAuth(loginField.getText(), passwordField.getText());
        if(authenticated) {
            loginField.clear();
            passwordField.clear();
            setAuthenticated(true);
        }
    }

    public void sendMessage() {
        String message = messageField.getText();
        if (message.isBlank()) {
            return;
        }

        if (message.startsWith(ServerCommandConstants.CLIENT_PRIVATE_MASSAGE)) {
            String[] parts = message.split(" ", 3);
            if (parts.length != 3) {
                return;
            }
        }

        network.sendMessage(message);
        messageField.clear();
    }

    public void close() {
        network.closeConnection();
    }

    public void itemClicked() {
        String nickname = clientList.getSelectionModel().getSelectedItem();
        if (nickname != null) {
            messageField.setText(String.format("/private %s ", nickname));
        }
    }
}

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

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    private File messageFile;

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

            messageFile = new File(String.format("%s.txt", network.getCurrentLogin()));
            if (messageFile.exists()) {
                ReadMessagesFromFile();
            } else {
                try {
                    if (messageFile.createNewFile()) {
                        System.out.println("File created successfully");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            setAuthenticated(true);
        }
    }

    private void ReadMessagesFromFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(messageFile))) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                displayMessage(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WriteMessagesToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(messageFile))) {
            List<String> messages = getLastMessages();
            for (String message : messages) {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
            }

            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getLastMessages() {
        List<String> messages = Arrays.asList(chatArea.getText().split("\n"));
        return messages
                .stream()
                .skip(Math.max(0, messages.size() - 100))
                .collect(Collectors.toList());
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
        WriteMessagesToFile();
        network.closeConnection();
    }

    public void itemClicked() {
        String nickname = clientList.getSelectionModel().getSelectedItem();
        if (nickname != null) {
            messageField.setText(String.format("/private %s ", nickname));
        }
    }
}

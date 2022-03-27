package ru.geekbrains.client;

import ru.geekbrains.CommonConstants;
import ru.geekbrains.server.ServerCommandConstants;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Network {
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String currentLogin;

    private final ChatController controller;

    private BufferedWriter fileWriter;

    public Network(ChatController chatController) {
        this.controller = chatController;
    }

    private void startReadServerMessages() throws IOException {
        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    String messageFromServer = inputStream.readUTF();
                    System.out.println(messageFromServer);

                    if (messageFromServer.startsWith(ServerCommandConstants.CLIENT_ENTER)) {
                        String[] client = messageFromServer.split(" ");
                        controller.displayClient(client[1]);
                        controller.displayMessage(client[1] + " зашел в чат");
                    } else if (messageFromServer.startsWith(ServerCommandConstants.CLIENT_EXIT)) {
                        String[] client = messageFromServer.split(" ");
                        controller.removeClient(client[1]);
                        controller.displayMessage(client[1] + " покинул чат");
                    } else if (messageFromServer.startsWith(ServerCommandConstants.CLIENTS)) {
                        String[] client = messageFromServer.split(" ");
                        for (int i = 1; i < client.length; i++) {
                            controller.displayClient(client[i]);
                        }
                    } else {
                        controller.displayMessage(messageFromServer);
                        writeMessageToFile(messageFromServer);
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }).start();
    }

    private void initializeNetwork() throws IOException {
        socket = new Socket(CommonConstants.SERVER_ADDRESS, CommonConstants.SERVER_PORT);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean sendAuth(String login, String password) {
        try {
            if (socket == null || socket.isClosed()) {
                initializeNetwork();
            }

            outputStream.writeUTF(String.format("%s %s %s", ServerCommandConstants.AUTHENTICATION, login, password));

            boolean authenticated = inputStream.readBoolean();
            if (authenticated) {
                currentLogin = login;
                String fileName = String.format("%s.txt", currentLogin);
                fileWriter = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8,true));
                startReadServerMessages();
            }
            return authenticated;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<String> readMessagesFromFile() {
        int lines = 100;
        int readLines = 0;
        List<String> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        File file = new File(String.format("%s.txt", currentLogin));
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            long fileLength = file.length() - 1;
            randomAccessFile.seek(fileLength);

            for (long pointer = fileLength; pointer >= 0; pointer--) {
                randomAccessFile.seek(pointer);
                char c;
                c = (char) randomAccessFile.read();
                if (c == '\n') {
                    readLines++;
                    if (readLines == lines)
                        break;
                }
                builder.append(c);
                fileLength = fileLength - pointer;
            }
            builder.reverse();
            result.addAll(Arrays.stream(builder.toString().split("\n")).toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void writeMessageToFile(String message) {
        try {
            fileWriter.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCurrentLogin() {
        return this.currentLogin;
    }

    public void closeConnection() {
        try {
            outputStream.writeUTF(ServerCommandConstants.CLIENT_EXIT);
            outputStream.close();
            inputStream.close();
            fileWriter.flush();
            fileWriter.close();
            socket.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}

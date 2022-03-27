package ru.geekbrains.server;

import ru.geekbrains.CommonConstants;
import ru.geekbrains.server.authorization.AuthService;
import ru.geekbrains.database.DbHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final AuthService authService;
    private List<ClientHandler> connectedUsers;

    public Server() {
        authService = new DbHandler();
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(CommonConstants.SERVER_PORT)) {
            authService.start();
            connectedUsers = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                executorService.submit(new ClientHandler(this, socket));
            }
        } catch (IOException exception) {
            System.out.println("Ошибка в работе сервера");
            exception.printStackTrace();
        } finally {
            authService.end();
            executorService.shutdown();
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNickNameBusy(String nickName) {
        for (ClientHandler handler : connectedUsers) {
            if (handler.getNickName().equals(nickName)) {
                return true;
            }
        }

        return false;
    }

    public synchronized void broadcastMessage(String message) {
        for (ClientHandler handler : connectedUsers) {
            handler.sendMessage(message);
        }
    }

    public synchronized boolean sendPrivateMessage(String from, String to, String message) {
        for (ClientHandler handler : connectedUsers) {
            if (handler.getNickName().equals(to)) {
                handler.sendMessage(String.format("[%s]: %s", from, message));
                return true;
            }
        }

        return false;
    }

    public synchronized void addConnectedUser(ClientHandler handler) {
        connectedUsers.add(handler);
    }

    public synchronized void disconnectUser(ClientHandler handler) {
        connectedUsers.remove(handler);
    }

    public String getClients() {
        StringBuilder builder = new StringBuilder("/clients ");
        for (ClientHandler user : connectedUsers) {
            builder.append(user.getNickName()).append(" ");
        }

        return builder.toString();
    }
}

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    static final Logger logger = LogManager.getLogger(Server.class.getName());
    private final AuthService authService;
    private List<ClientHandler> connectedUsers;

    public Server() {
        authService = new DbHandler();
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(CommonConstants.SERVER_PORT)) {
            logger.info("Сервер успешно запущен");
            authService.start();
            connectedUsers = new ArrayList<>();
            while (true) {
                logger.info("Сервер ожидает подключения");
                Socket socket = server.accept();
                logger.info("Клиент подключился");
                executorService.submit(new ClientHandler(this, socket));
            }
        } catch (IOException exception) {
            logger.error("Ошибка в работе сервера", exception);
        } finally {
            authService.end();
            executorService.shutdown();
            logger.info("Сервер завершил работу");
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
        logger.info(String.format("%s вошел в чат", handler.getNickName()));
    }

    public synchronized void disconnectUser(ClientHandler handler) {
        connectedUsers.remove(handler);
        logger.info(String.format("%s вышел из чата", handler.getNickName()));
    }

    public String getClients() {
        StringBuilder builder = new StringBuilder("/clients ");
        for (ClientHandler user : connectedUsers) {
            builder.append(user.getNickName()).append(" ");
        }

        return builder.toString();
    }
}

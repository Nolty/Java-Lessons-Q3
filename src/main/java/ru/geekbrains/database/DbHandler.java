package ru.geekbrains.database;

import ru.geekbrains.server.authorization.AuthService;

import java.sql.*;

public class DbHandler implements AuthService {
    private Connection connection;

    @Override
    public void start() {
        try {
            String connectionString = "jdbc:sqlite:sample.sqlite";
            connection = DriverManager.getConnection(connectionString);
            System.out.println("Сервис аутентификации инициализирован");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNickNameByLoginAndPassword(String login, String password) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "SELECT nickname, password FROM USERS WHERE login = ?")) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(2).equals(password)) {
                    return resultSet.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void end() {
        try {
            if (connection != null) {
                connection.close();
            }
            System.out.println("Сервис аутентификации отключен");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

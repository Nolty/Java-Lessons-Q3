package ru.geekbrains.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.geekbrains.server.authorization.AuthService;

import java.sql.*;

public class DbHandler implements AuthService {
    private Connection connection;
    static final Logger logger = LogManager.getLogger(AuthService.class.getName());

    @Override
    public void start() {
        try {
            String connectionString = "jdbc:sqlite:sample.sqlite";
            connection = DriverManager.getConnection(connectionString);
            logger.info("Сервис аутентификации инициализирован");
        } catch (SQLException e) {
            logger.error(e);
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
            logger.error(e);
        }

        return null;
    }

    @Override
    public void end() {
        try {
            if (connection != null) {
                connection.close();
            }
            logger.info("Сервис аутентификации отключен");
        } catch (SQLException e) {
            logger.error(e);
        }
    }
}

package quickshop.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import quickshop.entity.User;
import quickshop.util.PasswordUtil;

public class UsersService {
    public enum UserType {
        ADMIN("admin"),
        CASHIER("cashier");

        public final String type;

        private UserType(String type) {
            this.type = type;
        }
    }

    public enum LoginStatus {
        INVALID_USERNAME,
        INVALID_PASSWORD,
        SUCCESS
    }

    private java.sql.Connection connection;
    private Logger logger;

    public UsersService(java.sql.Connection connection, Logger logger) {
        this.connection = connection;
        this.logger = logger;
    }

    public User createUser(String username, String password, UserType userType) throws SQLException {
        System.out.println(username + " : " + password + " : " + userType.type);
        
        // Check if username already exists
        var preExistingUser = getUserByUsername(username);
        if (preExistingUser.isPresent()) {
            return preExistingUser.get();
        }
        
        String sql = "INSERT INTO USERS (ID, USERNAME, PASSWORD, TYPE) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        var userId = UUID.randomUUID().toString();

        statement.setString(1, userId);
        statement.setString(2, username);
        statement.setString(3, PasswordUtil.hashPass(password));
        statement.setString(4, userType.type);
        statement.executeUpdate();

        logger.info("Created 'User' of type "+ userType.type +" with ID " + userId);

        return new User(userId, username, userType);
    }

    public Optional<User> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT ID, USERNAME, TYPE FROM USERS WHERE USERNAME = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String id = resultSet.getString("ID");
            String foundUsername = resultSet.getString("USERNAME");
            String type = resultSet.getString("TYPE");

            return Optional.of(new User(id, foundUsername, type.equals("admin") ? UserType.ADMIN : UserType.CASHIER));
        }

        return Optional.empty();
    }

    public List<User> getAllUsers() throws SQLException {
        Statement statement = connection.createStatement();

        String sql = "SELECT * FROM USERS";
        ResultSet resultSet = statement.executeQuery(sql);

        var allUsers = new ArrayList<User>();

        while (resultSet.next()) {
            allUsers.add(new User(
                    resultSet.getString("ID"),
                    resultSet.getString("USERNAME"),
                    resultSet.getString("TYPE").equals("admin") ? UserType.ADMIN : UserType.CASHIER));
        }

        return allUsers;
    }

    public User updateUser(String id, String newUsername, String newPassword, UserType userType) throws SQLException {
        String updateQuery = "UPDATE USERS SET USERNAME = ?, PASSWORD = ?, TYPE = ? WHERE ID = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

        updateStatement.setString(1, newUsername);
        updateStatement.setString(2, PasswordUtil.hashPass(newPassword));
        updateStatement.setString(3, userType.type);
        updateStatement.setString(4, id);

        updateStatement.executeUpdate();

        logger.info("Updated 'User' with ID " + id);

        return new User(id, newUsername, userType);
    }

    public User updateUser(String id, String newUsername, UserType userType) throws SQLException {
        String updateQuery = "UPDATE USERS SET USERNAME = ?, TYPE = ? WHERE ID = ?";
        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);

        updateStatement.setString(1, newUsername);
        updateStatement.setString(2, userType.type);
        updateStatement.setString(3, id);

        updateStatement.executeUpdate();

        logger.info("Updated 'User' with ID " + id);

        return new User(id, newUsername, userType);
    }

    public void deleteUser(String id) throws SQLException {
        String deleteQuery = "DELETE FROM USERS WHERE ID = ?";
        PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
        
        deleteStatement.setString(1, id);
        deleteStatement.executeUpdate();

        logger.info("Deleted 'User' with ID " + id);
    }

    public LoginStatus login(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT PASSWORD FROM USERS WHERE USERNAME = ?");

        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            if (PasswordUtil.verifyPass(password, resultSet.getString("PASSWORD"))) {
                logger.info("User with username " + username + " logged in.");
                return LoginStatus.SUCCESS;
            }
            logger.info("User with username " + username + " tried to login with a wrong password.");
            return LoginStatus.INVALID_PASSWORD;
        }

        logger.info("User with username " + username + " tried to login and failed.");
        return LoginStatus.INVALID_USERNAME;
    }

    public boolean usernameExists(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM USERS WHERE USERNAME = ?");
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }

        return false;
    }
}

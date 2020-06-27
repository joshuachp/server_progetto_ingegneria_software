package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe utente nel database
 */
public class User {

    private final Integer id;
    private String username;
    private String password;
    private boolean manager;

    private User(Integer id, String username, String password, boolean manager) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.manager = manager;
    }

    /**
     * Select a user with the given username and returns the information
     *
     * @param username Username in the database
     * @return User instance or null on error
     */
    public static User getUser(String username) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("SELECT id, username, password, manager FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getBoolean(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new user
     *
     * @return The user created, null on error
     */
    public static User createUser(String username, String password, boolean manager) {
        Database database = Database.getInstance();
        if (User.getUser(username) == null) {
            try {
                PreparedStatement statement = database.getConnection()
                        .prepareStatement("INSERT INTO users(username, password, manager) VALUES(?, ?, ?)");
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setBoolean(3, manager);
                statement.executeUpdate();
                return User.getUser(username);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Update the database with the current user information
     *
     * @return True se aggiornato con successo
     */
    public boolean updateUser() {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("UPDATE users SET username = ?, password = ?, manager = ? WHERE id = ?");
            statement.setString(1, this.username);
            statement.setString(2, this.password);
            statement.setBoolean(3, this.manager);
            statement.setInt(4, this.id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getManager() {
        return manager;
    }

    public void setManager(boolean manager) {
        this.manager = manager;
    }

}

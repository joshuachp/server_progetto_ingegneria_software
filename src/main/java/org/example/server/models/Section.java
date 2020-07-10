package org.example.server.models;

import org.example.server.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Section {

    private final Integer id;
    private final String name;

    public Section(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return ArrayList of products, null on error
     */
    public static @NotNull ArrayList<Section> getAll() throws SQLException {
        Database database = Database.getInstance();
        ArrayList<Section> list = new ArrayList<>();
        Statement statement = database.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT id, name FROM sections");
        while (resultSet.next()) {
            list.add(new Section(resultSet.getInt(1), resultSet.getString(2)));
        }
        return list;
    }

    /**
     * Get a section with the given name
     *
     * @param name Section name to search
     * @return Section information, null if doest exists
     */
    public static @Nullable Section getSection(String name) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("SELECT id, name FROM sections WHERE name = ?");
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Section(resultSet.getInt(1), resultSet.getString(2));
        }
        return null;
    }

    /**
     * Create a new session with the given name
     *
     * @param name Name of the session
     * @return The new session id
     */
    public static @NotNull Integer createSection(String name) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("INSERT INTO sections(name) VALUES(?)");
        statement.setString(1, name);
        if (statement.executeUpdate() == 1) {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next())
                return keys.getInt(1);
        }
        throw new SQLException("Wrong number of modified rows");
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}

package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Section {

    private final Integer id;
    private final String name;

    public Section(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Get a section with the given name
     *
     * @param name Section name to search
     * @return Section information, null on error
     */
    public static Section getSection(String name) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("SELECT id, name FROM sections WHERE name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Section(resultSet.getInt(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new session with the given name
     *
     * @param name Name of the session
     * @return The session created, null on error
     */
    public static Section createSection(String name) {
        Database database = Database.getInstance();
        if (Section.getSection(name) == null) {
            try {
                PreparedStatement statement = database.getConnection()
                        .prepareStatement("INSERT INTO sections(name) VALUES(?)");
                statement.setString(1, name);
                statement.executeUpdate();
                return Section.getSection(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }
}

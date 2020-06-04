package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Manager {
    private final Integer id;
    private final String badge;
    private final String name;
    private final String surname;
    private final String address;
    private final Integer cap;
    private final String city;
    private final String telephone;
    private final String role;
    private final Integer user_id;

    public Manager(Integer id, String badge, String name, String surname, String address, Integer cap, String city,
                   String telephone, String role, Integer user_id) {
        this.id = id;
        this.badge = badge;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.cap = cap;
        this.city = city;
        this.telephone = telephone;
        this.role = role;
        this.user_id = user_id;
    }

    /**
     * Select a manager associated to the given user id.
     *
     * @param userId The user id fo the manager
     * @return Client instance or null on error
     */
    public static Manager getManager(Integer userId) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("SELECT id, badge, name, surname, address, cap, city, telephone, role, user_id " +
                            "FROM managers WHERE user_id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Manager(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7),
                        resultSet.getString(8), resultSet.getString(9), resultSet.getInt(10));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Create a new manager
     *
     * @param badge     Badge
     * @param name      Name
     * @param surname   Surname
     * @param address   Address
     * @param cap       CAP
     * @param city      City
     * @param telephone Telephone
     * @param role      Role
     * @param user_id   User id
     * @return The created Manager, null on error
     */
    public static Manager createManager(String badge, String name, String surname, String address, Integer cap,
                                        String city, String telephone, String role, Integer user_id) {
        Database database = Database.getInstance();
        if (Client.getClient(user_id) == null) {
            try {
                PreparedStatement statement = database.getConnection()
                        .prepareStatement("INSERT INTO managers " +
                                "(badge, name, surname, address, cap, city, telephone, role, user_id) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, badge);
                statement.setString(2, name);
                statement.setString(3, surname);
                statement.setString(4, address);
                statement.setInt(5, cap);
                statement.setString(6, city);
                statement.setString(7, telephone);
                statement.setString(8, role);
                statement.setInt(9, user_id);
                statement.executeUpdate();
                return Manager.getManager(user_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public String getBadge() {
        return badge;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public Integer getCap() {
        return cap;
    }

    public String getCity() {
        return city;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getRole() {
        return role;
    }

    public Integer getUserId() {
        return user_id;
    }

}

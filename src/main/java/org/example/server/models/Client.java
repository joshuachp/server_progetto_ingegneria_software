package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    private final Integer id;
    private final String name;
    private final String surname;
    private final String address;
    private final Integer cap;
    private final String city;
    private final String telephone;
    private final Integer payment;
    private final Integer user_id;

    public Client(Integer id, String name, String surname, String address, Integer cap, String city,
                  String telephone, Integer payment, Integer user_id) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.cap = cap;
        this.city = city;
        this.telephone = telephone;
        this.user_id = user_id;
        this.payment = payment;
    }

    /**
     * Select a client associated to the given user id.
     *
     * @param userId The user id fo the client
     * @return Client instance or null on error
     */
    public static Client getClient(Integer userId) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("SELECT id, name, surname, address, cap, city, telephone, payment, user_id " +
                            "FROM clients WHERE user_id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Client(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6),
                        resultSet.getString(7), resultSet.getInt(8), resultSet.getInt(9));
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
    public static Client createClient(String name, String surname, String address, Integer cap, String city,
                                      String telephone, Integer payment, Integer user_id) {
        Database database = Database.getInstance();
        if (Client.getClient(user_id) == null) {
            try {
                PreparedStatement statement = database.getConnection()
                        .prepareStatement("INSERT INTO clients " +
                                "(name, surname, address, cap, city, telephone, payment, user_id) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, surname);
                statement.setString(3, address);
                statement.setInt(4, cap);
                statement.setString(5, city);
                statement.setString(6, telephone);
                statement.setInt(7, payment);
                statement.setInt(8, user_id);
                statement.executeUpdate();
                return Client.getClient(user_id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
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

    public Integer getUserId() {
        return user_id;
    }

    public Integer getPayment() {
        return payment;
    }
}

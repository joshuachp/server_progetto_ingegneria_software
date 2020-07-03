package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Client {

    private final Integer id;
    private final Integer user_id;
    private String name;
    private String surname;
    private String address;
    private Integer cap;
    private String city;
    private String telephone;
    private Integer payment;
    private Integer loyalty_card_number;

    public Client(Integer id, String name, String surname, String address, Integer cap, String city,
                  String telephone, Integer payment, Integer user_id, Integer loyalty_card_number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.cap = cap;
        this.city = city;
        this.telephone = telephone;
        this.payment = payment;
        this.user_id = user_id;
        this.loyalty_card_number = loyalty_card_number;
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
                    .prepareStatement("SELECT id, name, surname, address, cap, city, telephone, payment, user_id, " +
                            "loyalty_card_number FROM clients WHERE user_id = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Client(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6),
                        resultSet.getString(7), resultSet.getInt(8), resultSet.getInt(9),
                        resultSet.getInt(10));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create a new client
     *
     * @param name      Name
     * @param surname   Surname
     * @param address   Address
     * @param cap       CAP
     * @param city      City
     * @param telephone Telephone
     * @param payment   Payment
     * @param user_id   User id
     * @return The new created Client, null on error
     */
    public static Client createClient(String name, String surname, String address, Integer cap, String city,
                                      String telephone, Integer payment, Integer user_id, Integer loyalty_card_number) {
        Database database = Database.getInstance();
        if (Client.getClient(user_id) == null) {
            try {
                PreparedStatement statement = database.getConnection()
                        .prepareStatement("INSERT INTO clients " +
                                "(name, surname, address, cap, city, telephone, payment, user_id, " +
                                "loyalty_card_number) " +
                                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, surname);
                statement.setString(3, address);
                statement.setInt(4, cap);
                statement.setString(5, city);
                statement.setString(6, telephone);
                statement.setInt(7, payment);
                statement.setInt(8, user_id);
                if (loyalty_card_number != null)
                    statement.setInt(9, loyalty_card_number);
                else
                    statement.setNull(9, Types.INTEGER);
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

    /**
     * Update the database with the current client information
     *
     * @return True se aggiornato con successo
     */
    public boolean updateClient() {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("UPDATE clients SET name = ?, surname = ?, address = ?, cap = ?, city = ?, " +
                            "telephone = ?,payment = ?, loyalty_card_number = ? WHERE id = ?");
            statement.setString(1, this.name);
            statement.setString(2, this.surname);
            statement.setString(3, this.address);
            statement.setInt(4, this.cap);
            statement.setString(5, this.city);
            statement.setString(6, this.telephone);
            statement.setInt(7, this.payment);
            statement.setInt(8, this.loyalty_card_number);
            statement.setInt(9, this.id);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getUserId() {
        return user_id;
    }

    public Integer getPayment() {
        return payment;
    }

    public void setPayment(Integer payment) {
        this.payment = payment;
    }

    public Integer getLoyaltyCardNumber() {
        return loyalty_card_number;
    }

    public void setLoyaltyCardNumber(Integer loyalty_card_number) {
        this.loyalty_card_number = loyalty_card_number;
    }
}

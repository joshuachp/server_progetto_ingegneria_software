package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class LoyaltyCard {

    private final Integer id;
    private final Integer card_number;
    private final Date emission_date;
    private final Integer points;

    public LoyaltyCard(Integer id, Integer card_number, Date emission_date, Integer points) {
        this.id = id;
        this.card_number = card_number;
        this.emission_date = emission_date;
        this.points = points;
    }


    /**
     * Select a loyalty card with the given card number
     *
     * @param card_number The card number to select
     * @return Loyalty card instance or null on error
     */
    public static LoyaltyCard getLoyaltyCard(Integer card_number) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("SELECT id, card_number, emission_date, points FROM loyalty_cards " +
                            "WHERE card_number = ?");
            statement.setInt(1, card_number);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new LoyaltyCard(resultSet.getInt(1), resultSet.getInt(2), resultSet.getDate(3),
                        resultSet.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public Integer getCardNumber() {
        return card_number;
    }

    public Date getEmissionDate() {
        return emission_date;
    }

    public Integer getPoints() {
        return points;
    }


}

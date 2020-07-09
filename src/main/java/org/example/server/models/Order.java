package org.example.server.models;

import org.example.server.database.Database;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {
    private final Integer id;
    private final Float total;
    private final Integer payment;
    private final Date delivery_start;
    private final Date delivery_end;
    private final Integer state;
    private final Integer user_id;

    public Order(Integer id, Float total, Integer payment, Date delivery_start, Date delivery_end,
                 Integer state, Integer user_id) {
        this.id = id;
        this.total = total;
        this.payment = payment;
        this.delivery_start = delivery_start;
        this.delivery_end = delivery_end;
        this.state = state;
        this.user_id = user_id;
    }

    /**
     * Create a new order in the database with the given data
     *
     * @param payment        Payment
     * @param delivery_start Delivery start
     * @param delivery_end   Delivery end
     * @param state          State
     * @param user_id        User id
     * @return The id of the new order
     * @throws SQLException On error
     */
    public static Integer createOrder(Integer payment, Date delivery_start, Date delivery_end, Integer state,
                                      Integer user_id) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("INSERT INTO orders(total, payment, delivery_start, delivery_end, state, user_id) "
                        + "VALUES(0, ?, ?, ?, ?, ?)");
        statement.setInt(1, payment);
        statement.setDate(2, delivery_start);
        statement.setDate(3, delivery_end);
        statement.setInt(4, state);
        statement.setInt(5, user_id);
        if (statement.executeUpdate() == 1) {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next())
                return keys.getInt(1);
        }
        throw new SQLException("Wrong number of modified rows");
    }

    public static @Nullable Order getOrder(Integer id) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("SELECT id, total, payment, delivery_start, delivery_end, state, user_id " +
                        "FROM orders WHERE id = ?");
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        if (result.next())
            return new Order(result.getInt(1), result.getFloat(2), result.getInt(3), result.getDate(4),
                    result.getDate(5), result.getInt(6), result.getInt(7));
        return null;
    }

    public Integer getId() {
        return id;
    }

    public Float getTotal() {
        return total;
    }

    public Integer getPayment() {
        return payment;
    }

    public Date getDeliveryStart() {
        return delivery_start;
    }

    public Date getDeliveryEnd() {
        return delivery_end;
    }

    public Integer getState() {
        return state;
    }

    public Integer getUserId() {
        return user_id;
    }

}

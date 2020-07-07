package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Order {
    private final Integer id;
    private final Integer total;
    private final Integer payment;
    private final Date delivery_start;
    private final Date delivery_end;
    private final Integer state;
    private final Integer user_id;

    public Order(Integer id, Integer total, Integer payment, Date delivery_start, Date delivery_end,
                 Integer state, Integer user_id) {
        this.id = id;
        this.total = total;
        this.payment = payment;
        this.delivery_start = delivery_start;
        this.delivery_end = delivery_end;
        this.state = state;
        this.user_id = user_id;
    }

    public static boolean createOrder(Integer payment, Date delivery_start, Date delivery_end, Integer state,
                                      Integer user_id) throws SQLException {

        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("INSERT INTO orders(total, payment, delivery_start, delivery_end, state, user_id) "
                        + "VALUES(0, ?, ?, ?, ?, ?)");
        statement.setInt(1, payment);
        statement.setDate(2, (java.sql.Date) delivery_start);
        statement.setDate(3, (java.sql.Date) delivery_end);
        statement.setInt(4, state);
        statement.setInt(5, user_id);
        return statement.executeUpdate() == 1;
    }

    public Integer getId() {
        return id;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPayment() {
        return payment;
    }

    public Date getDelivery_start() {
        return delivery_start;
    }

    public Date getDelivery_end() {
        return delivery_end;
    }

    public Integer getState() {
        return state;
    }

    public Integer getUser_id() {
        return user_id;
    }

}

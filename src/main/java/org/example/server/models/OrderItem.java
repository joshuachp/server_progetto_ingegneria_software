package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderItem {
    private final Integer id;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final Integer product_id;
    private final Integer order_id;

    public OrderItem(Integer id, String name, Integer price, Integer quantity, Integer product_id, Integer order_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.product_id = product_id;
        this.order_id = order_id;
    }

    /**
     * Create an OrderItem, it will save the state of a product at the time of the
     * order.
     *
     * @param productId ID of the product
     * @param orderId   ID of the order
     * @return True on success
     */
    public static boolean createOrderItem(Integer productId, Integer orderId, Integer quantity) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("INSERT INTO order_items(name, price, quantity, product_id, order_id) "
                        + "SELECT products.name, products.price, ?, products.id, ? FROM products " +
                        "WHERE products.id = ?");
        statement.setInt(1, quantity);
        statement.setInt(2, orderId);
        statement.setInt(3, productId);
        return statement.executeUpdate() == 1;

    }

    public static ArrayList<OrderItem> getOrderItems(Integer orderId) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("SELECT id, name, price, quantity, product_id, order_id FROM order_items " +
                        "WHERE order_id = ?");
        statement.setInt(1, orderId);
        ResultSet resultSet = statement.executeQuery();
        // Get length of result set for generating the array list
        ArrayList<OrderItem> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new OrderItem(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3),
                    resultSet.getInt(4), resultSet.getInt(5), resultSet.getInt(6)));
        }
        return list;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getProductId() {
        return product_id;
    }

    public Integer getOrderId() {
        return order_id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

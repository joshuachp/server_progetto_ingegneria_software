package org.example.server.models;

import org.example.server.database.Database;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

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
     * Create an OrderItem, it will save the state of a product at the time of the order. It will update the total of
     * the specified order the products is in.
     *
     * @param orderId   ID of the order
     * @param productId ID of the product
     * @param quantity  Quantity of the product
     * @return True on success
     * @throws SQLException On error
     */
    public static boolean createOrderItem(Integer orderId, Integer productId, Integer quantity) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement insertStatement = database.getConnection()
                .prepareStatement("INSERT INTO order_items(name, price, quantity, product_id, order_id) "
                        + "SELECT products.name, products.price, ?, products.id, ? FROM products " +
                        "WHERE products.id = ?");
        insertStatement.setInt(1, quantity);
        insertStatement.setInt(2, orderId);
        insertStatement.setInt(3, productId);
        PreparedStatement updateStatement = database.getConnection()
                .prepareStatement("UPDATE orders SET total = (total + ? * " +
                        "(SELECT products.price FROM products WHERE products.id = ? )) WHERE id = ?");
        updateStatement.setInt(1, quantity);
        updateStatement.setInt(2, productId);
        updateStatement.setInt(3, orderId);
        return insertStatement.executeUpdate() == 1 && updateStatement.executeUpdate() == 1;
    }

    /**
     * Create a batch of OrderItem, it will save the state of the product at the time of the order. It will update the
     * total of the specified order the products is in.
     *
     * @param orderId  ID of the order
     * @param products Map of product id and product quantity
     * @return True on success
     * @throws SQLException On error
     */
    public static boolean batchCreateOrderItems(Integer orderId, Map<Integer, Integer> products) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement insertStatement = database.getConnection()
                .prepareStatement("INSERT INTO order_items(name, price, quantity, product_id, order_id) "
                        + "SELECT products.name, products.price, ?, products.id, ? FROM products " +
                        "WHERE products.id = ?");
        PreparedStatement updateStatement = database.getConnection()
                .prepareStatement("UPDATE orders SET total = (total + ? * " +
                        "(SELECT products.price FROM products WHERE products.id = ? )) WHERE id = ?");
        for (Integer productId : products.keySet()) {
            // Insert
            insertStatement.setInt(1, products.get(productId));
            insertStatement.setInt(2, orderId);
            insertStatement.setInt(3, productId);
            insertStatement.addBatch();
            // Update
            updateStatement.setInt(1, products.get(productId));
            updateStatement.setInt(2, productId);
            updateStatement.setInt(3, orderId);
            updateStatement.addBatch();
        }
        // Check that the changes of both batches are the same as the products size
        return Arrays.stream(insertStatement.executeBatch()).sum() == products.size() &&
                Arrays.stream(insertStatement.executeBatch()).sum() == products.size();
    }

    /**
     * Returns a list of all the items for a specific order
     *
     * @param orderId Id of the order the items are from
     * @return ArrayList of order items
     * @throws SQLException On error
     */
    public static @NotNull ArrayList<OrderItem> getOrderItems(Integer orderId) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("SELECT id, name, price, quantity, product_id, order_id FROM order_items " +
                        "WHERE order_id = ?");
        statement.setInt(1, orderId);
        ResultSet resultSet = statement.executeQuery();
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

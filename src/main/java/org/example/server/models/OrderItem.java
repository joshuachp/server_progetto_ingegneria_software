package org.example.server.models;

import org.example.server.database.Database;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class OrderItem {
    private final Integer id;
    private final String name;
    private final Float price;
    private final Integer quantity;
    private final Integer product_id;
    private final Integer order_id;

    public OrderItem(Integer id, String name, Float price, Integer quantity, Integer product_id, Integer order_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.product_id = product_id;
        this.order_id = order_id;
    }

    /**
     * Create an OrderItem, it will save the state of a product at the time of the order. It will update the total of
     * the specified order the products is in. Te transaction need to be verified first.
     *
     * @param orderId   ID of the order
     * @param productId ID of the product
     * @param quantity  Quantity of the product
     * @return True on success
     * @throws SQLException On error
     */
    public static boolean createOrderItem(Integer orderId, Integer productId, Integer quantity) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement insert = database.getConnection()
                .prepareStatement("INSERT INTO order_items(name, price, quantity, product_id, order_id) "
                        + "SELECT products.name, products.price, ?, products.id, ? FROM products " +
                        "WHERE products.id = ?");
        insert.setInt(1, quantity);
        insert.setInt(2, orderId);
        insert.setInt(3, productId);
        PreparedStatement updateOrder = database.getConnection()
                .prepareStatement("UPDATE orders SET total = (total + ? * " +
                        "(SELECT products.price FROM products WHERE products.id = ? )) WHERE id = ?");
        updateOrder.setInt(1, quantity);
        updateOrder.setInt(2, productId);
        updateOrder.setInt(3, orderId);
        PreparedStatement updateProduct = database.getConnection()
                .prepareStatement("UPDATE products SET availability = (availability - ?) WHERE id = ?");
        updateProduct.setInt(1, quantity);
        updateProduct.setInt(2, productId);
        return insert.executeUpdate() == 1 && updateOrder.executeUpdate() == 1 && updateProduct.executeUpdate() == 1;
    }

    /**
     * Create a batch of OrderItem, it will save the state of the product at the time of the order. It will update the
     * total of the specified order the products is in. Te transaction need to be verified first.
     *
     * @param orderId  ID of the order
     * @param products Map of product id and product quantity
     * @return True on success
     * @throws SQLException On error
     */
    public static boolean batchCreateOrderItems(Integer orderId, @NotNull Map<Integer, Integer> products) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement insert = database.getConnection()
                .prepareStatement("INSERT INTO order_items(name, price, quantity, product_id, order_id) "
                        + "SELECT products.name, products.price, ?, products.id, ? FROM products " +
                        "WHERE products.id = ?");
        PreparedStatement updateOrder = database.getConnection()
                .prepareStatement("UPDATE orders SET total = (total + ? * " +
                        "(SELECT products.price FROM products WHERE products.id = ? )) WHERE id = ?");
        PreparedStatement updateProduct = database.getConnection()
                .prepareStatement("UPDATE products SET availability = (availability - ?) WHERE id = ?");
        for (Integer productId : products.keySet()) {
            // Insert
            insert.setInt(1, products.get(productId));
            insert.setInt(2, orderId);
            insert.setInt(3, productId);
            insert.addBatch();
            // Order
            updateOrder.setInt(1, products.get(productId));
            updateOrder.setInt(2, productId);
            updateOrder.setInt(3, orderId);
            updateOrder.addBatch();
            // Product
            updateProduct.setInt(1, products.get(productId));
            updateProduct.setInt(2, productId);
            updateProduct.addBatch();
        }
        // Check that the changes of both batches are the same as the products size
        return Arrays.stream(insert.executeBatch()).sum() == products.size() &&
                Arrays.stream(updateOrder.executeBatch()).sum() == products.size() &&
                Arrays.stream(updateProduct.executeBatch()).sum() == products.size();
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
            list.add(new OrderItem(resultSet.getInt(1), resultSet.getString(2), resultSet.getFloat(3),
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

    public Float getPrice() {
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

    /**
     * Return the JSON of the order item instance.
     *
     * @return JSON object
     */
    public JSONObject toJson() {
        return new JSONObject()
                .put("name", this.name)
                .put("price", this.price)
                .put("quantity", this.quantity)
                .put("productId", this.product_id);
    }
}

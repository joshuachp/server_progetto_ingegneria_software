package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    private static final Float[] PRICES = {1.50f, 2.30f};

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void createOrderItem() throws SQLException {
        // Get available product
        Order order = Order.getOrder(1);
        assertNotNull(order);
        Float total = order.getTotal();
        assertTrue(OrderItem.createOrderItem(1, 1, 1));
        order = Order.getOrder(1);
        assertNotNull(order);
        Product product = Product.getProduct(1);
        assertNotNull(product);
        assertEquals(total + product.getPrice(), order.getTotal());
        assertEquals(0, product.getAvailability());
    }

    @Test
    void getOrderItems() throws SQLException {
        List<OrderItem> orderItems = OrderItem.getOrderItems(1);
        assertEquals(2, orderItems.size());
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem item = orderItems.get(i);
            assertEquals(i + 1, item.getId());
            assertEquals("Product", item.getName());
            assertTrue(Arrays.asList(PRICES).contains(item.getPrice()));
            assertEquals(i + 1, item.getQuantity());
            assertEquals(i + 1, item.getProductId());
            assertEquals(1, item.getOrderId());
        }
    }

    @Test
    void createOrderItems() throws SQLException {
        Integer orderId = Order.createOrder(0, new Date(0), new Date(0), 0, "Test", 2);
        Map<Integer, Integer> products = new HashMap<>();
        products.put(1, 1);
        products.put(3, 2);
        assertTrue(OrderItem.batchCreateOrderItems(orderId, products));
        Order order = Order.getOrder(orderId);
        assertNotNull(order);
        assertEquals(11.90f, order.getTotal());
        Product product = Product.getProduct(1);
        assertNotNull(product);
        assertEquals(0, product.getAvailability());
        product = Product.getProduct(3);
        assertNotNull(product);
        assertEquals(1, product.getAvailability());
    }

    @Test
    void batchCreateOrderItems() throws SQLException {
        // Get available product
        Order order = Order.getOrder(1);
        assertNotNull(order);
        Float total = order.getTotal();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        assertTrue(OrderItem.batchCreateOrderItems(1, map));
        order = Order.getOrder(1);
        assertNotNull(order);
        Product product = Product.getProduct(1);
        assertNotNull(product);
        assertEquals(total + product.getPrice(), order.getTotal());
        assertEquals(0, product.getAvailability());
    }

    @Test
    void toJson() {
        OrderItem item = new OrderItem(1, "Name", 1.0f, 1, 1, 1);
        JSONObject json = item.toJson();

        assertTrue(json.has("name"));
        assertTrue(json.has("price"));
        assertTrue(json.has("quantity"));
        assertTrue(json.has("productId"));

        assertEquals("Name", json.getString("name"));
        assertEquals(1.0f, json.getFloat("price"));
        assertEquals(1, json.getInt("quantity"));
        assertEquals(1, json.getInt("productId"));
    }
}

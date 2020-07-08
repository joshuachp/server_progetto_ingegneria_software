package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void createOrderItem() throws SQLException {
        // Get available product
        Order order = Order.getOrder(1);
        assertNotNull(order);
        int total = order.getTotal();
        assertTrue(OrderItem.createOrderItem(1, 1, 1));
        order = Order.getOrder(1);
        assertNotNull(order);
        assertEquals(total + 1, order.getTotal());
        Product product = Product.getProduct(1);
        assertNotNull(product);
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
            assertEquals(1, item.getPrice());
            assertEquals(i + 1, item.getQuantity());
            assertEquals(i + 1, item.getProductId());
            assertEquals(1, item.getOrderId());
        }
    }

    @Test
    void createOrderItems() throws SQLException {
        Integer orderId = Order.createOrder(0, new Date(0), new Date(0), 0, 2);
        Map<Integer, Integer> products = new HashMap<>();
        products.put(1, 1);
        products.put(3, 2);
        assertTrue(OrderItem.batchCreateOrderItems(orderId, products));
        Order order = Order.getOrder(orderId);
        assertNotNull(order);
        assertEquals(3, order.getTotal());
        Product product = Product.getProduct(1);
        assertNotNull(product);
        assertEquals(0, product.getAvailability());
        product = Product.getProduct(3);
        assertNotNull(product);
        assertEquals(1, product.getAvailability());
    }
}

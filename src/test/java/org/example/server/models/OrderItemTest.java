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
        Order order = Order.getOrder(1);
        assertNotNull(order);
        int total = order.getTotal();
        assertTrue(OrderItem.createOrderItem(1, 1, 10));
        order = Order.getOrder(1);
        assertNotNull(order);
        assertEquals(total + 10, order.getTotal());
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
        assertTrue(Order.createOrder(0, new Date(0), new Date(0), 0, 2));
        int total = 0;
        Map<Integer, Integer> products = new HashMap<>();
        for (int i = 1; i < 4; i++) {
            products.put(i, i);
            total += i;
        }
        assertTrue(OrderItem.batchCreateOrderItems(2, products));
        Order order = Order.getOrder(2);
        assertNotNull(order);
        assertEquals(total, order.getTotal());
    }
}

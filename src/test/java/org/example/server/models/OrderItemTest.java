package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    void getOrderItems() {
        AtomicReference<List<OrderItem>> orderItems = new AtomicReference<>();
        assertDoesNotThrow(() -> orderItems.set(OrderItem.getOrderItems(1)));
        assertEquals(2, orderItems.get().size());
        for (int i = 0; i < orderItems.get().size(); i++) {
            OrderItem item = orderItems.get().get(i);
            assertEquals(i + 1, item.getId());
            assertEquals("Product", item.getName());
            assertEquals(1, item.getPrice());
            assertEquals(i + 1, item.getQuantity());
            assertEquals(i + 1, item.getProductId());
            assertEquals(1, item.getOrderId());
        }
    }
}

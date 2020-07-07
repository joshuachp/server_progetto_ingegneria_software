package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void createOrderItem() {
        // TODO: Create order
        assertDoesNotThrow(() ->
                assertTrue(OrderItem.createOrderItem(1, 1, 10))
        );
    }

    @Test
    void getOrderItems() {
        AtomicReference<List<OrderItem>> orderItems = new AtomicReference<>();
        assertDoesNotThrow(() -> orderItems.set(OrderItem.getOrderItems(1)));
        assertEquals(2, orderItems.get().size());
        for (int i = 0; i < orderItems.get().size(); i++) {
            OrderItem item = orderItems.get().get(i);
            assertEquals(i, item.getId());
            assertEquals("Product", item.getName());
            assertEquals(1, item.getPrice());
            assertEquals(1, item.getQuantity());
            assertEquals(i, item.getProductId());
            assertEquals(1, item.getOrderId());
        }
    }
}

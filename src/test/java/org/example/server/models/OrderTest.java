package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void createOrder() throws SQLException {
        assertTrue(Order.createOrder(0, new Date(0), new Date(0), 0, 2));
        Order order = Order.getOrder(2);
        assertNotNull(order);
        assertEquals(2, order.getId());
        assertEquals(0, order.getTotal());
        assertEquals(0, order.getPayment());
        assertEquals(new Date(0), order.getDeliveryStart());
        assertEquals(new Date(0), order.getDeliveryEnd());
        assertEquals(0, order.getState());
        assertEquals(2, order.getUserId());
    }

    @Test
    void getOrder() throws SQLException {
        Order order = Order.getOrder(1);
        assertNotNull(order);
        assertEquals(1, order.getId());
        assertEquals(3, order.getTotal());
        assertEquals(0, order.getPayment());
        assertEquals(new Date(0), order.getDeliveryStart());
        assertEquals(new Date(0), order.getDeliveryEnd());
        assertEquals(0, order.getState());
        assertEquals(2, order.getUserId());


    }
}
package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void createOrder() throws SQLException {
        Integer id = Order.createOrder(0, new Date(0), new Date(0), 0, 2);
        assertEquals(2, id);
        Order order = Order.getOrder(id);
        assertNotNull(order);
        assertEquals(id, order.getId());
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
        assertEquals(3.80f, order.getTotal());
        assertEquals(0, order.getPayment());
        assertEquals(new Date(0), order.getDeliveryStart());
        assertEquals(new Date(0), order.getDeliveryEnd());
        assertEquals(0, order.getState());
        assertEquals(2, order.getUserId());
    }

    @Test
    void getOrders() throws SQLException {
        List<Order> list = Order.getOrders(2);
        assertEquals(1, list.size());
        Order order = list.get(0);
        assertEquals(1, order.getId());
        assertEquals(3.80f, order.getTotal());
        assertEquals(0, order.getPayment());
        assertEquals(new Date(0), order.getDeliveryStart());
        assertEquals(new Date(0), order.getDeliveryEnd());
        assertEquals(0, order.getState());
        assertEquals(2, order.getUserId());
    }

    @Test
    void toJson() throws SQLException {
        Order order = Order.getOrder(1);
        assertNotNull(order);

        JSONObject json = order.toJson();

        assertTrue(json.has("id"));
        assertTrue(json.has("total"));
        assertTrue(json.has("payment"));
        assertTrue(json.has("deliveryStart"));
        assertTrue(json.has("deliveryEnd"));
        assertTrue(json.has("state"));

        assertEquals(1, json.getInt("id"));
        assertEquals(3.80f, json.getFloat("total"));
        assertEquals(0, json.getInt("payment"));
        assertEquals(new Date(0), new Date(json.getLong("deliveryStart")));
        assertEquals(new Date(0), new Date(json.getLong("deliveryEnd")));
        assertEquals(0, json.getInt("state"));
    }
}
package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClienteTest {

    /**
     * Test client is get properly
     */
    @Test
    void getClient() {
        MockDatabase.createMockDatabase();
        Cliente cliente = Cliente.getClient(2);
        assertNotNull(cliente);
        assertEquals(1, cliente.getId());
        assertEquals("Name", cliente.getName());
        assertEquals("Surname", cliente.getSurname());
        assertEquals("Via Viale 1", cliente.getAddress());
        assertEquals(3333, cliente.getCap());
        assertEquals("City", cliente.getCity());
        assertEquals("3334445555", cliente.getTelephone());
        assertEquals(2, cliente.getUserId());
    }
}
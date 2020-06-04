package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    /**
     * Test client is get properly
     */
    @Test
    void getClient() {
        Client client = Client.getClient(2);
        assertNotNull(client);
        assertEquals(1, client.getId());
        assertEquals("Name", client.getName());
        assertEquals("Surname", client.getSurname());
        assertEquals("Via Viale 1", client.getAddress());
        assertEquals(3333, client.getCap());
        assertEquals("City", client.getCity());
        assertEquals("3334445555", client.getTelephone());
        assertEquals(0, client.getPayment());
        assertEquals(2, client.getUserId());
    }

    @Test
    void createClient() {
        User user = User.createUser("test", "test", false);
        assertNotNull(user);
        Client client = Client.createClient("Name", "Surname", "Via Viale 1", 3333,
                "City", "3334445555", 0, user.getId());
        assertNotNull(client);
        // We don't really know the id
        assertEquals("Name", client.getName());
        assertEquals("Surname", client.getSurname());
        assertEquals("Via Viale 1", client.getAddress());
        assertEquals(3333, client.getCap());
        assertEquals("City", client.getCity());
        assertEquals("3334445555", client.getTelephone());
        assertEquals(0, client.getPayment());
        assertEquals(user.getId(), client.getUserId());
    }
}
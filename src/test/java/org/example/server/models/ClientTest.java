package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(33333, client.getCap());
        assertEquals("City", client.getCity());
        assertEquals("3334445555", client.getTelephone());
        assertEquals(0, client.getPayment());
        assertEquals(2, client.getUserId());
        assertEquals(1234, client.getLoyaltyCardNumber());
    }

    @Test
    void createClient() {
        User user = User.createUser("test", "test", false);
        assertNotNull(user);
        Client client = Client.createClient("Name", "Surname", "Via Viale 1", 33333,
                "City", "3334445555", 0, user.getId(), 1234);
        assertNotNull(client);
        // We don't really know the id
        assertEquals("Name", client.getName());
        assertEquals("Surname", client.getSurname());
        assertEquals("Via Viale 1", client.getAddress());
        assertEquals(33333, client.getCap());
        assertEquals("City", client.getCity());
        assertEquals("3334445555", client.getTelephone());
        assertEquals(0, client.getPayment());
        assertEquals(user.getId(), client.getUserId());
        assertEquals(1234, client.getLoyaltyCardNumber());
    }

    @Test
    public void updateUser() {
        User user = User.getUser("guest");
        assertNotNull(user);
        Client client = Client.getClient(user.getId());
        assertNotNull(client);

        client.setName("Test Name");
        client.setSurname("Test Surname");
        client.setAddress("Test Address");
        client.setCap(0);
        client.setCity("Test City");
        client.setTelephone("Test Telephone");
        client.setPayment(1);
        client.setLoyaltyCardNumber(1234);

        assertTrue(client.updateClient());

        client = Client.getClient(user.getId());
        assertNotNull(client);

        assertEquals("Test Name", client.getName());
        assertEquals("Test Surname", client.getSurname());
        assertEquals("Test Address", client.getAddress());
        assertEquals(0, client.getCap());
        assertEquals("Test City", client.getCity());
        assertEquals("Test Telephone", client.getTelephone());
        assertEquals(1, client.getPayment());
        assertEquals(1234, client.getLoyaltyCardNumber());
    }
}
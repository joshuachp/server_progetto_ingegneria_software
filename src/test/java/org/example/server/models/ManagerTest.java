package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManagerTest {


    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    /**
     * Test manager is get properly
     */
    @Test
    void getManager() {
        Manager manager = Manager.getManager(1);
        assertNotNull(manager);
        assertEquals(1, manager.getId());
        assertEquals("D34DB33F", manager.getBadge());
        assertEquals("Name", manager.getName());
        assertEquals("Surname", manager.getSurname());
        assertEquals("Via Viale 1", manager.getAddress());
        assertEquals(3333, manager.getCap());
        assertEquals("City", manager.getCity());
        assertEquals("3334445555", manager.getTelephone());
        assertEquals("Admin", manager.getRole());
        assertEquals(1, manager.getUserId());
    }
}
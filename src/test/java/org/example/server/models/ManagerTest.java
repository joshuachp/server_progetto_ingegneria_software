package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(33333, manager.getCap());
        assertEquals("City", manager.getCity());
        assertEquals("3334445555", manager.getTelephone());
        assertEquals("Admin", manager.getRole());
        assertEquals(1, manager.getUserId());
    }

    @Test
    void createManager() {
        User user = User.createUser("test", "test", false);
        assertNotNull(user);
        Manager manager = Manager.createManager("D34DB33F", "Name", "Surname", "Via Viale 1", 33333,
                "City", "3334445555", "Test", user.getId());
        assertNotNull(manager);
        // We don't really know the id
        assertEquals("D34DB33F", manager.getBadge());
        assertEquals("Name", manager.getName());
        assertEquals("Surname", manager.getSurname());
        assertEquals("Via Viale 1", manager.getAddress());
        assertEquals(33333, manager.getCap());
        assertEquals("City", manager.getCity());
        assertEquals("3334445555", manager.getTelephone());
        assertEquals("Test", manager.getRole());
        assertEquals(user.getId(), manager.getUserId());
    }

    @Test
    public void updateManager() {
        User user = User.getUser("admin");
        assertNotNull(user);
        Manager manager = Manager.getManager(user.getId());
        assertNotNull(manager);

        manager.setBadge("Test Badge");
        manager.setName("Test Name");
        manager.setSurname("Test Surname");
        manager.setAddress("Test Address");
        manager.setCap(0);
        manager.setCity("Test City");
        manager.setTelephone("Test Telephone");
        manager.setRole("Test Role");

        assertTrue(manager.updateManager());

        manager = Manager.getManager(user.getId());
        assertNotNull(manager);

        assertEquals("Test Badge", manager.getBadge());
        assertEquals("Test Name", manager.getName());
        assertEquals("Test Surname", manager.getSurname());
        assertEquals("Test Address", manager.getAddress());
        assertEquals(0, manager.getCap());
        assertEquals("Test City", manager.getCity());
        assertEquals("Test Telephone", manager.getTelephone());
        assertEquals("Test Role", manager.getRole());
    }
}
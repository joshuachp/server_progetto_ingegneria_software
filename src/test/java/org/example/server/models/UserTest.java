package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suit per la classe User
 */
public class UserTest {

    /**
     * Controlla che getUser sia eseguito correttamente
     */
    @Test
    public void getUser() {
        MockDatabase.createMockDatabase();
        User user = User.getUser("admin");
        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("admin", user.getUsername());
        assertEquals("$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS", user.getPassword());
        assertTrue(user.getManager());
    }

    /**
     * Controlla che updateUser sia eseguito correttamente
     */
    @Test
    public void updateUser() {
        MockDatabase.createMockDatabase();
        User admin = User.getUser("admin");
        assertNotNull(admin);
        admin.setUsername("username");
        admin.setPassword("password");
        admin.setManager(false);
        assertTrue(admin.updateUser());
        User user = User.getUser("username");
        assertNotNull(user);
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertFalse(user.getManager());
    }
}
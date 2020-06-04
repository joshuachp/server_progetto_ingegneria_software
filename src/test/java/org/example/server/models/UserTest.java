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
        assertEquals("admin", user.getUsername());
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
        admin.setResponsabile(false);
        assertTrue(admin.updateUser());
        User user = User.getUser("username");
        assertNotNull(user);
        assertEquals("username", user.getUsername());
        assertEquals("password", user.getPassword());
        assertFalse(user.getResponsabile());
    }
}
package org.example.server.utils;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void checkPassword() {
        @SuppressWarnings("SpellCheckingInspection")
        String hash = "$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS";
        assertTrue(Utils.checkPassword("password", hash));
        assertFalse(Utils.checkPassword("test", hash));
    }

    @Test
    void hashPassword() {
        String hash = Utils.hashPassword("password");
        assertTrue(Utils.checkPassword("password", hash));
    }

    @Test
    void createSession() {
        String session = Utils.createSession();
        byte[] bytes = Base64.getDecoder().decode(session);
        assertEquals(256, bytes.length);
    }
}
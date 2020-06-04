package org.example.server.routes;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class RouterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void registerClient() throws Exception {
        MockDatabase.createMockDatabase();
        this.mockMvc.perform(post("/api/client/register")
                .param("username", "test")
                .param("password", "password")
                .param("name", "Name")
                .param("surname", "Surname")
                .param("address", "Via Viale 1")
                .param("cap", "3333")
                .param("city", "City")
                .param("telephone", "3334445555"))
                .andExpect(status().isOk());
    }
}
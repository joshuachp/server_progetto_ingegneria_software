package org.example.server.routes;

import org.example.server.database.MockDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class RouterTest {

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void registerClient() throws Exception {
        this.mockMvc.perform(post("/api/client/register")
                .param("username", "test")
                .param("password", "password")
                .param("name", "Name")
                .param("surname", "Surname")
                .param("address", "Via Viale 1")
                .param("cap", "3333")
                .param("city", "City")
                .param("telephone", "3334445555")
                .param("payment", "1"))
                .andExpect(status().isOk());
    }


    @Test
    void registerManager() throws Exception {
        this.mockMvc.perform(post("/api/manager/register")
                .param("username", "test")
                .param("password", "password")
                .param("badge", "D34DB33F")
                .param("name", "Name")
                .param("surname", "Surname")
                .param("address", "Via Viale 1")
                .param("cap", "3333")
                .param("city", "City")
                .param("telephone", "3334445555")
                .param("role", "Test"))
                .andExpect(status().isOk());
    }

    @Test
    void createProducts() throws Exception {
        JSONObject product = new JSONObject()
                .put("name", "Name")
                .put("brand", "Brand")
                .put("package_size", 1)
                .put("price", 1)
                .put("availability", 1)
                .put("characteristics", "Characteristic")
                .put("section", "Section");
        JSONObject json = new JSONObject()
                .accumulate("products", product)
                .accumulate("products", product);
        this.mockMvc.perform(post("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json.toString()))
                .andExpect(status().isOk());
    }
}
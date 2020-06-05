package org.example.server.routes;

import org.example.server.database.MockDatabase;
import org.example.server.models.Product;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
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
    void testAutenticateUser() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("username"));
        assertTrue(json.has("responsabile"));
        assertTrue(json.has("session"));
        assertTrue(json.has("badge"));
        assertTrue(json.has("name"));
        assertTrue(json.has("surname"));
        assertTrue(json.has("address"));
        assertTrue(json.has("cap"));
        assertTrue(json.has("city"));
        assertTrue(json.has("telephone"));
        assertTrue(json.has("role"));

        assertEquals("admin", json.getString("username"));
        assertTrue(json.getBoolean("responsabile"));
        // Session not verified
        assertEquals("D34DB33F", json.getString("badge"));
        assertEquals("Name", json.getString("name"));
        assertEquals("Surname", json.getString("surname"));
        assertEquals("Via Viale 1", json.getString("address"));
        assertEquals(3333, json.getInt("cap"));
        assertEquals("City", json.getString("city"));
        assertEquals("3334445555", json.getString("telephone"));
        assertEquals("Admin", json.getString("role"));
    }

    @Test
    void testAutenticateUserSession() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/autenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
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
        // No image for null
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
        Product product1 = Product.getProduct("Name", "Brand", 1, 1, null, 1, "Characteristic", "Section");
        assertNotNull(product1);
    }

    @Test
    void getAllProducts() throws Exception {
        this.mockMvc.perform(post("/api/user/autenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk());
        this.mockMvc.perform(post("/api/product/all").param("session", "session"))
                .andExpect(status().isOk());
    }

}
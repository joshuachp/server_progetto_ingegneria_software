package org.example.server.routes;

import org.example.server.database.MockDatabase;
import org.example.server.models.Client;
import org.example.server.models.Manager;
import org.example.server.models.Product;
import org.example.server.models.User;
import org.example.server.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Date;

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
        assertTrue(json.has("session"));
        assertEquals("D34DB33F", json.getString("badge"));
        assertEquals("Name", json.getString("name"));
        assertEquals("Surname", json.getString("surname"));
        assertEquals("Via Viale 1", json.getString("address"));
        assertEquals(33333, json.getInt("cap"));
        assertEquals("City", json.getString("city"));
        assertEquals("3334445555", json.getString("telephone"));
        assertEquals("Admin", json.getString("role"));
    }

    @Test
    void testAutenticateUserClient() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("username"));
        assertTrue(json.has("responsabile"));
        assertTrue(json.has("session"));
        assertTrue(json.has("name"));
        assertTrue(json.has("surname"));
        assertTrue(json.has("address"));
        assertTrue(json.has("cap"));
        assertTrue(json.has("city"));
        assertTrue(json.has("telephone"));
        assertTrue(json.has("payment"));
        assertTrue(json.has("loyalty_card_number"));

        assertEquals("guest", json.getString("username"));
        assertFalse(json.getBoolean("responsabile"));
        assertTrue(json.has("session"));
        assertEquals("Name", json.getString("name"));
        assertEquals("Surname", json.getString("surname"));
        assertEquals("Via Viale 1", json.getString("address"));
        assertEquals(33333, json.getInt("cap"));
        assertEquals("City", json.getString("city"));
        assertEquals("3334445555", json.getString("telephone"));
        assertEquals(0, json.getInt("payment"));
        assertEquals(1234, json.getInt("loyalty_card_number"));
    }

    @Test
    void testAutenticateUserSession() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/user/session")
                .param("session", session))
                .andExpect(status().isOk()).andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
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
        assertEquals(session, json.getString("session"));
        assertEquals("D34DB33F", json.getString("badge"));
        assertEquals("Name", json.getString("name"));
        assertEquals("Surname", json.getString("surname"));
        assertEquals("Via Viale 1", json.getString("address"));
        assertEquals(33333, json.getInt("cap"));
        assertEquals("City", json.getString("city"));
        assertEquals("3334445555", json.getString("telephone"));
        assertEquals("Admin", json.getString("role"));
    }

    @Test
    void testAutenticateUserSessionClient() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/user/session")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("username"));
        assertTrue(json.has("responsabile"));
        assertTrue(json.has("session"));
        assertTrue(json.has("name"));
        assertTrue(json.has("surname"));
        assertTrue(json.has("address"));
        assertTrue(json.has("cap"));
        assertTrue(json.has("city"));
        assertTrue(json.has("telephone"));
        assertTrue(json.has("payment"));
        assertTrue(json.has("loyalty_card_number"));

        assertEquals("guest", json.getString("username"));
        assertFalse(json.getBoolean("responsabile"));
        // Session not verified
        assertEquals("Name", json.getString("name"));
        assertEquals("Surname", json.getString("surname"));
        assertEquals("Via Viale 1", json.getString("address"));
        assertEquals(33333, json.getInt("cap"));
        assertEquals("City", json.getString("city"));
        assertEquals("3334445555", json.getString("telephone"));
        assertEquals(0, json.getInt("payment"));
        assertEquals(1234, json.getInt("loyalty_card_number"));
    }

    @Test
    void registerClient() throws Exception {
        this.mockMvc.perform(post("/api/client/register")
                .param("username", "test")
                .param("password", "password")
                .param("name", "Name")
                .param("surname", "Surname")
                .param("address", "Via Viale 1")
                .param("cap", "33333")
                .param("city", "City")
                .param("telephone", "3334445555")
                .param("payment", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void registerClientCardNumber() throws Exception {
        this.mockMvc.perform(post("/api/client/register")
                .param("username", "test")
                .param("password", "password")
                .param("name", "Name")
                .param("surname", "Surname")
                .param("address", "Via Viale 1")
                .param("cap", "33333")
                .param("city", "City")
                .param("telephone", "3334445555")
                .param("payment", "1")
                .param("card_number", "1234"))
                .andExpect(status().isOk());
    }

    @Test
    void updateClient() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject user = new JSONObject(result.getResponse().getContentAsString());
        this.mockMvc.perform(post("/api/client/update")
                .param("session", user.getString("session"))
                .param("password", "Test Password")
                .param("name", "Test Name")
                .param("surname", "Test Surname")
                .param("address", "Test Address")
                .param("cap", "0")
                .param("city", "Test City")
                .param("telephone", "Test Telephone")
                .param("payment", "1")
                .param("card_number", "1234"))
                .andExpect(status().isOk());

        User guest = User.getUser("guest");
        assertNotNull(guest);
        assertTrue(Utils.checkPassword("Test Password", guest.getPassword()));

        Client client = Client.getClient(guest.getId());
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

    @Test
    void registerManager() throws Exception {
        this.mockMvc.perform(post("/api/manager/register")
                .param("username", "test")
                .param("password", "password")
                .param("badge", "D34DB33F")
                .param("name", "Name")
                .param("surname", "Surname")
                .param("address", "Via Viale 1")
                .param("cap", "33333")
                .param("city", "City")
                .param("telephone", "3334445555")
                .param("role", "Test"))
                .andExpect(status().isOk());
    }

    @Test
    void updateManager() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject user = new JSONObject(result.getResponse().getContentAsString());
        this.mockMvc.perform(post("/api/manager/update")
                .param("session", user.getString("session"))
                .param("password", "Test Password")
                .param("badge", "Test Badge")
                .param("name", "Test Name")
                .param("surname", "Test Surname")
                .param("address", "Test Address")
                .param("cap", "0")
                .param("city", "Test City")
                .param("telephone", "Test Telephone")
                .param("role", "Test Role"))
                .andExpect(status().isOk());

        User admin = User.getUser("admin");
        assertNotNull(admin);
        assertTrue(Utils.checkPassword("Test Password", admin.getPassword()));

        Manager manager = Manager.getManager(admin.getId());
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

    @Test
    void createProducts() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject user = new JSONObject(result.getResponse().getContentAsString());
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
                .accumulate("products", product)
                .put("session", user.get("session"));
        this.mockMvc.perform(post("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json.toString()))
                .andExpect(status().isOk());
        Product product1 = Product.getProduct("Name", "Brand", 1, 1, null, 1, "Characteristic", "Section");
        assertNotNull(product1);
    }

    @Test
    void getAllProducts() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/product/all")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("products"));
        JSONArray products = json.getJSONArray("products");
        assertEquals(3, products.length());
    }

    @Test
    void getAllSections() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/section/all")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("sections"));
        JSONArray sections = json.getJSONArray("sections");
        assertEquals(1, sections.length());
    }

    @Test
    void getLoyaltyCard() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/card/1234")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("card_number"));
        assertTrue(json.has("emission_date"));
        assertTrue(json.has("points"));


        assertEquals(1234, json.getInt("card_number"));
        assertEquals(new Date(0), new Date(json.getLong("emission_date")));
        assertEquals(500, json.getInt("points"));
    }
}
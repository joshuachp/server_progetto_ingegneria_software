package org.example.server.routes;

import org.example.server.database.MockDatabase;
import org.example.server.models.*;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class RouterTest {

    private static final Float[] PRICES = {1.50f, 2.30f};

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void authUser() throws Exception {
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
    void authUserClient() throws Exception {
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
    void authUserSession() throws Exception {
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
    void authUserSessionClient() throws Exception {
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
    void logoutUser() throws Exception {
        // Authenticate
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        // Get session
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        String session = json.getString("session");
        // Logout
        this.mockMvc.perform(post("/api/user/logout")
                .param("session", session))
                .andExpect(status().isOk());
        // Check if session is invalid
        this.mockMvc.perform(post("/api/user/session")
                .param("session", session))
                .andExpect(status().isUnauthorized());
        // Check logout with invalid session
        this.mockMvc.perform(post("/api/user/logout")
                .param("session", session))
                .andExpect(status().isUnauthorized());
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
        JSONObject productJson = new JSONObject()
                .put("name", "Name")
                .put("brand", "Brand")
                .put("package_size", 1)
                .put("price", 1)
                .put("availability", 1)
                .put("characteristics", "Characteristic")
                .put("section", "Section");
        JSONObject json = new JSONObject()
                .accumulate("products", productJson)
                .accumulate("products", productJson)
                .put("session", user.get("session"));
        this.mockMvc.perform(post("/api/product/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json.toString()))
                .andExpect(status().isOk());
        Product product = Product.getProduct(4);
        assertNotNull(product);
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
    void getProduct() throws Exception {
        // Auth
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        // Get product id 1
        result = this.mockMvc.perform(post("/api/product/1")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());

        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("brand"));
        assertTrue(json.has("package_size"));
        assertTrue(json.has("price"));
        assertTrue(json.has("image"));
        assertTrue(json.has("availability"));
        assertTrue(json.has("characteristics"));
        assertTrue(json.has("section"));

        assertEquals(1, json.getInt("id"));
        assertEquals("Product", json.getString("name"));
        assertEquals("Brand", json.getString("brand"));
        assertEquals(1, json.getInt("package_size"));
        assertEquals(1, json.getInt("price"));
        assertEquals("http://localhost:8080/images/mascara.jpg", json.get("image"));
        assertEquals(1, json.getInt("availability"));
        assertEquals("Characteristics", json.getString("characteristics"));
        assertEquals("Section 1", json.getString("section"));

        // Error session
        this.mockMvc.perform(post("/api/product/1")
                .param("session", "test"))
                .andExpect(status().isUnauthorized());
        // Error product id
        this.mockMvc.perform(post("/api/product/42")
                .param("session", session))
                .andExpect(status().isBadRequest());
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
        assertEquals(2, sections.length());
        assertEquals("Section 1", sections.get(0));
        assertEquals("Section 2", sections.get(1));
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

    @Test
    void createOrder() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        String session = json.getString("session");
        // Request
        Map<Integer, Integer> products = new HashMap<>();
        products.put(1, 1);
        products.put(3, 2);
        json = new JSONObject()
                .put("session", session)
                .put("products", products)
                .put("deliveryEnd", new Date(0).getTime())
                .put("deliveryStart", new Date(0).getTime());
        this.mockMvc.perform(
                post("/api/order/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(json.toString()))
                .andExpect(status().isOk());
        // Check order
        Order order = Order.getOrder(2);
        assertNotNull(order);
        assertEquals(11.90f, order.getTotal());
        assertEquals(0, order.getPayment());
        assertEquals(new Date(0), order.getDeliveryStart());
        assertEquals(new Date(0), order.getDeliveryEnd());
        assertEquals(0, order.getState());
        assertEquals(2, order.getUserId());
        // Check order items
        List<OrderItem> orderItems = OrderItem.getOrderItems(2);
        assertEquals(products.size(), orderItems.size());
        assertEquals(1, orderItems.get(0).getProductId());
        assertEquals(1, orderItems.get(0).getQuantity());
        assertEquals(3, orderItems.get(1).getProductId());
        assertEquals(2, orderItems.get(1).getQuantity());
    }

    @Test
    void getUserOrders() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/order/user")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("orders"));

        JSONArray orders = json.getJSONArray("orders");
        assertEquals(1, orders.length());

        json = orders.getJSONObject(0);
        assertEquals(1, json.getInt("id"));
        assertEquals(3.80f, json.getFloat("total"));
        assertEquals(0, json.getInt("payment"));
        assertEquals(new Date(0), new Date(json.getLong("deliveryStart")));
        assertEquals(new Date(0), new Date(json.getLong("deliveryEnd")));
        assertEquals(0, json.getInt("state"));
    }

    @Test
    void getOrderItems() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "guest")
                .param("password", "guest"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/order-item/all/1")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("orderItems"));

        JSONArray orderItems = json.getJSONArray("orderItems");
        assertEquals(2, orderItems.length());

        json = orderItems.getJSONObject(0);

        assertEquals(2, orderItems.length());
        for (int i = 0; i < orderItems.length(); i++) {
            JSONObject item = orderItems.getJSONObject(i);
            assertEquals("Product", item.getString("name"));
            assertTrue(Arrays.asList(PRICES).contains(item.getFloat("price")));
            assertEquals(i + 1, item.getInt("quantity"));
            assertEquals(i + 1, item.getInt("productId"));
        }
    }

    @Test
    void getAllOrders() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");
        result = this.mockMvc.perform(post("/api/order/all")
                .param("session", session))
                .andExpect(status().isOk())
                .andReturn();
        json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("orders"));

        JSONArray orders = json.getJSONArray("orders");
        assertEquals(1, orders.length());

        json = orders.getJSONObject(0);
        assertEquals(1, json.getInt("id"));
        assertEquals(3.80f, json.getFloat("total"));
        assertEquals(0, json.getInt("payment"));
        assertEquals(new Date(0), new Date(json.getLong("deliveryStart")));
        assertEquals(new Date(0), new Date(json.getLong("deliveryEnd")));
        assertEquals(0, json.getInt("state"));
    }

    @Test
    void updateOrderState() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/api/user/authenticate")
                .param("username", "admin")
                .param("password", "password"))
                .andExpect(status().isOk()).andReturn();
        JSONObject json = new JSONObject(result.getResponse().getContentAsString());
        assertTrue(json.has("session"));
        String session = json.getString("session");

        this.mockMvc.perform(post("/api/order/1/update")
                .param("session", session)
                .param("newState", "1"))
                .andExpect(status().isOk());

        Order order = Order.getOrder(1);
        assertNotNull(order);
        assertEquals(1, order.getState());
    }
}
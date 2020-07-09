package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    public static final Float[] PRICES = {1.50f, 2.30f, 5.20f};
    public static final String[] IMAGES = {
            "http://localhost:8080/images/broccoli.jpg",
            "http://localhost:8080/images/mascara.jpg"
    };

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void getProduct() throws SQLException {
        Product product = Product.getProduct(1);
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("Product", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1.50f, product.getPrice());
        assertEquals("http://localhost:8080/images/mascara.jpg", product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals("Section 1", product.getSection());
        // Error
        product = Product.getProduct(42);
        assertNull(product);
    }

    @Test
    void createProduct() throws SQLException {
        Integer id = Product.createProduct("Test", "Brand", 1, 1.50f, null, 1, "Characteristics", "Section");
        assertEquals(id, 4);
        Product product = Product.getProduct(id);
        assertNotNull(product);
        assertEquals("Test", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1.50f, product.getPrice());
        assertNull(product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals("Section", product.getSection());
    }

    @Test
    void getAll() throws SQLException {
        List<Product> products = Product.getAll();
        assertNotNull(products);
        for (Product product : products) {
            assertNotNull(product);
            assertEquals("Product", product.getName());
            assertEquals("Brand", product.getBrand());
            assertEquals(1, product.getPackageSize());
            assertTrue(Arrays.asList(PRICES).contains(product.getPrice()));
            if (product.getImage() != null)
                assertTrue(Arrays.asList(IMAGES).contains(product.getImage()));
            assertTrue(1 == product.getAvailability() || 3 == product.getAvailability());
            assertEquals("Characteristics", product.getCharacteristics());
            assertTrue(product.getSection().equals("Section 1") || product.getSection().equals("Section 2"));
        }
    }

    @Test
    void toJSON() throws SQLException {
        Product product = Product.getProduct(1);
        assertNotNull(product);
        JSONObject json = product.toJSON();
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
        assertEquals("http://localhost:8080/images/mascara.jpg", json.getString("image"));
        assertEquals(1, json.getInt("availability"));
        assertEquals("Characteristics", json.getString("characteristics"));
        assertEquals("Section 1", json.getString("section"));
    }
}
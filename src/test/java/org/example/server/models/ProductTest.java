package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void getProduct() {
        // Image null
        Product product = Product.getProduct("Product", "Brand", 1, 1, null,
                1, "Characteristics", "Section");
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("Product", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1, product.getPrice());
        assertNull(product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals("Section", product.getSectionId());
        // Image not null
        product = Product.getProduct("Product", "Brand", 1, 1,
                "http://localhost:8080/images/broccoli.jpg", 1, "Characteristics",
                "Section");
        assertNotNull(product);
        assertEquals(3, product.getId());
        assertEquals("Product", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1, product.getPrice());
        assertEquals("http://localhost:8080/images/broccoli.jpg", product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
    }

    @Test
    void getProductById() {
        Product product = Product.getProduct(1);
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("Product", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1, product.getPrice());
        assertNull(product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals("Section", product.getSectionId());
        // Error
        product = Product.getProduct(42);
        assertNull(product);
    }

    @Test
    void createProduct() {
        Product product = Product.createProduct("Test", "Brand", 1, 1, null,
                1, "Characteristics", "Section");
        assertNotNull(product);
        assertEquals("Test", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1, product.getPrice());
        assertNull(product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals("Section", product.getSectionId());
    }

    @Test
    void getAll() {
        List<Product> products = Product.getAll();
        assertNotNull(products);
        for (Product product : products) {
            assertNotNull(product);
            assertEquals("Product", product.getName());
            assertEquals("Brand", product.getBrand());
            assertEquals(1, product.getPackageSize());
            assertEquals(1, product.getPrice());
            if (product.getImage() != null)
                assertEquals("http://localhost:8080/images/broccoli.jpg", product.getImage());
            assertEquals(1, product.getAvailability());
            assertEquals("Characteristics", product.getCharacteristics());
            assertEquals("Section", product.getSectionId());
        }
    }

    @Test
    void toJSON() {
        Product product = Product.getProduct(1);
        assertNotNull(product);
        JSONObject json = product.toJSON();
        assertTrue(json.has("id"));
        assertTrue(json.has("name"));
        assertTrue(json.has("brand"));
        assertTrue(json.has("package_size"));
        assertTrue(json.has("price"));
        assertFalse(json.has("image"));
        assertTrue(json.has("availability"));
        assertTrue(json.has("characteristics"));
        assertTrue(json.has("section"));

        assertEquals(1, json.getInt("id"));
        assertEquals("Product", json.getString("name"));
        assertEquals("Brand", json.getString("brand"));
        assertEquals(1, json.getInt("package_size"));
        assertEquals(1, json.getInt("price"));
        assertEquals(1, json.getInt("availability"));
        assertEquals("Characteristics", json.getString("characteristics"));
        assertEquals("Section", json.getString("section"));
    }
}
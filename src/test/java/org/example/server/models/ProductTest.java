package org.example.server.models;

import org.example.server.database.MockDatabase;
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
        Product product = Product.getProduct("Product", "Brand", 1, 1, null,
                1, "Characteristics", 1);
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("Product", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1, product.getPrice());
        assertNull(product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals(1, product.getSectionId());
    }

    @Test
    void createProduct() {
        Section section = Section.getSection("Section");
        assertNotNull(section);
        Product product = Product.createProduct("Test", "Brand", 1, 1, null,
                1, "Characteristics", section.getId());
        assertNotNull(product);
        assertEquals("Test", product.getName());
        assertEquals("Brand", product.getBrand());
        assertEquals(1, product.getPackageSize());
        assertEquals(1, product.getPrice());
        assertNull(product.getImage());
        assertEquals(1, product.getAvailability());
        assertEquals("Characteristics", product.getCharacteristics());
        assertEquals(section.getId(), product.getSectionId());
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
            assertNull(product.getImage());
            assertEquals(1, product.getAvailability());
            assertEquals("Characteristics", product.getCharacteristics());
            assertEquals(1, product.getSectionId());
        }
    }
}
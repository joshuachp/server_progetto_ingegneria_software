package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class SectionTest {


    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void getSection() {
        Section section = Section.getSection("Section");
        assertNotNull(section);
        assertEquals(1, section.getId());
        assertEquals("Section", section.getName());
    }

    @Test
    void createSection() {
        Section section = Section.createSection("Test");
        assertNotNull(section);
        assertEquals("Test", section.getName());
    }

    @Test
    void getAll() {
        List<Section> sections = Section.getAll();
        assertNotNull(sections);
        for (Section section : sections) {
            assertNotNull(section);
            assertEquals("Section", section.getName());
        }
    }
}
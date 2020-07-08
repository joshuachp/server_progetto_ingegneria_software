package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class SectionTest {


    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void getSection() {
        Section section = Section.getSection("Section 1");
        assertNotNull(section);
        assertEquals(1, section.getId());
        assertEquals("Section 1", section.getName());
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
        assertEquals(2, sections.size());
        assertEquals("Section 1", sections.get(0).getName());
        assertEquals("Section 2", sections.get(1).getName());
    }
}
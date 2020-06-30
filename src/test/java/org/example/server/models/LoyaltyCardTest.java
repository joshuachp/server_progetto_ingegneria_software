package org.example.server.models;

import org.example.server.database.MockDatabase;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoyaltyCardTest {

    @BeforeEach
    void setUp() {
        MockDatabase.createMockDatabase();
    }

    @Test
    void getLoyaltyCard() {
        LoyaltyCard loyaltyCard = LoyaltyCard.getLoyaltyCard(1234);
        assertNotNull(loyaltyCard);
        assertEquals(1, loyaltyCard.getId());
        assertEquals(1234, loyaltyCard.getCardNumber());
        assertEquals(new Date(0), loyaltyCard.getEmissionDate());
        assertEquals(500, loyaltyCard.getPoints());
    }

    @Test
    void toJSON() {
        LoyaltyCard loyaltyCard = new LoyaltyCard(1, 1234, new Date(0), 500);
        JSONObject json = loyaltyCard.toJSON();
        assertEquals(1234, json.getInt("card_number"));
        assertEquals(new Date(0), new Date(json.getLong("emission_date")));
        assertEquals(500, json.getInt("points"));
    }
}
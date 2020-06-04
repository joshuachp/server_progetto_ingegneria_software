package org.example.server.models;

import java.util.Date;

public class LoyaltyCard {

    private Integer id;
    private Integer card_number;
    // TODO: Check correct sql return type
    private Date emission_date;
    private Integer points;
    private Integer client_id;

    public LoyaltyCard(Integer id, Integer card_number, Date emission_date, Integer points, Integer client_id) {
        this.id = id;
        this.card_number = card_number;
        this.emission_date = emission_date;
        this.points = points;
        this.client_id = client_id;
    }
}

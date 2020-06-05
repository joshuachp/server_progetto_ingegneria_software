package org.example.server.models;

public class Order {
    private final Integer id;
    private final Integer total;
    private final Integer payment;
    private final Integer delivery_start;
    private final Integer delivery_end;
    private final Integer state;
    private final Integer user_id;

    public Order(Integer id, Integer total, Integer payment, Integer delivery_start, Integer delivery_end,
                 Integer state, Integer user_id) {
        this.id = id;
        this.total = total;
        this.payment = payment;
        this.delivery_start = delivery_start;
        this.delivery_end = delivery_end;
        this.state = state;
        this.user_id = user_id;
    }
}

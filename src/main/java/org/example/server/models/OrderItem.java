package org.example.server.models;

public class OrderItem {
    private final Integer id;
    private final String name;
    private final Integer price;
    private final Integer quantity;
    private final Integer total;
    private final Integer order_id;

    public OrderItem(Integer id, String name, Integer price, Integer quantity, Integer total, Integer order_id) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.order_id = order_id;
    }
}

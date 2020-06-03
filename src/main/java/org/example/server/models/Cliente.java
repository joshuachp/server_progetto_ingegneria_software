package org.example.server.models;

public class Cliente {

    private final Integer id;
    private final String name;
    private final String surname;
    private final String address;
    private final Integer cap;
    private final String city;
    private final String telephone;
    private final Integer user_id;

    public Cliente(Integer id, String name, String surname, String address, Integer cap, String city,
                   String telephone, Integer user_id) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.cap = cap;
        this.city = city;
        this.telephone = telephone;
        this.user_id = user_id;
    }
}

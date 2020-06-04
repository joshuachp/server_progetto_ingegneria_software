package org.example.server.models;

public class Product {
    private final Integer id;
    private final String name;
    private final String brand;
    private final Integer package_size;
    private final Integer price;
    private final String image;
    private final Integer availability;
    private final String characteristics;
    private final Integer section_id;

    public Product(Integer id, String name, String brand, Integer package_size, Integer price, String image,
                   Integer availability, String characteristics, Integer section_id) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.package_size = package_size;
        this.price = price;
        this.image = image;
        this.availability = availability;
        this.characteristics = characteristics;
        this.section_id = section_id;
    }
}

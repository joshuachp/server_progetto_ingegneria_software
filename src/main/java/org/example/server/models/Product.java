package org.example.server.models;

import org.example.server.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public static Product createProduct(String name, String brand, Integer package_size, Integer price, String image,
                                        Integer availability, String characteristics, Integer section_id) {
        Database database = Database.getInstance();
        if (Section.getSection(name) == null) {
            try {
                PreparedStatement statement = database.getConnection()
                        .prepareStatement("INSERT INTO products(name, brand, package_size, price, image, " +
                                "availability, characteristics, section_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, brand);
                statement.setInt(3, package_size);
                statement.setInt(4, price);
                statement.setString(5, image);
                statement.setInt(6, availability);
                statement.setString(7, characteristics);
                statement.setInt(8, section_id);
                statement.executeUpdate();
                return Product.getProduct(name);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // TODO: How to get product, using only name now
    public static Product getProduct(String name) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement = database.getConnection()
                    .prepareStatement("SELECT id, name, brand, package_size, price, image, availability, " +
                            "characteristics, section_id FROM products WHERE name = ?");
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getInt(7),
                        resultSet.getString(8), resultSet.getInt(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getPackageSize() {
        return package_size;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public Integer getAvailability() {
        return availability;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public Integer getSectionId() {
        return section_id;
    }

    public Integer getId() {
        return id;
    }
}

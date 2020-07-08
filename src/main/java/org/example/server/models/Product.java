package org.example.server.models;

import org.example.server.database.Database;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Product {

    private final Integer id;
    private final String name;
    private final String brand;
    private final Integer package_size;
    private final Integer price;
    private final String image;
    private final Integer availability;
    private final String characteristics;
    private final String section;

    public Product(Integer id, String name, String brand, Integer package_size, Integer price, String image,
                   Integer availability, String characteristics, String section) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.package_size = package_size;
        this.price = price;
        this.image = image;
        this.availability = availability;
        this.characteristics = characteristics;
        this.section = section;
    }

    /**
     * Get all the products in the database
     *
     * @return ArrayList of products, null on error
     */
    public static @NotNull ArrayList<Product> getAll() throws SQLException {
        ArrayList<Product> list = new ArrayList<>();
        Database database = Database.getInstance();
        Statement statement = database.getConnection().createStatement();
        ResultSet resultSet = statement
                .executeQuery("SELECT id, name, brand, package_size, price, image, availability, characteristics, " +
                        "section FROM products");
        while (resultSet.next()) {
            list.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getInt(7),
                    resultSet.getString(8), resultSet.getString(9)));
        }
        return list;
    }

    /**
     * Create a new product and return it
     *
     * @param name            Name
     * @param brand           Brand
     * @param package_size    Package size
     * @param price           Price
     * @param image           Image
     * @param availability    Availability
     * @param characteristics Characteristics
     * @param section         Section id
     * @return Return the new product id
     */
    public static @NotNull Integer createProduct(String name, String brand, Integer package_size, Integer price,
                                                 String image, Integer availability, String characteristics,
                                                 String section) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("INSERT INTO products(name, brand, package_size, price, image, " +
                        "availability, characteristics, section) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, name);
        statement.setString(2, brand);
        statement.setInt(3, package_size);
        statement.setInt(4, price);
        statement.setString(5, image);
        statement.setInt(6, availability);
        statement.setString(7, characteristics);
        statement.setString(8, section);
        if (statement.executeUpdate() == 1) {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next())
                return keys.getInt(1);
        }
        throw new SQLException("Wrong number of modified rows");
    }

    /**
     * Get a product in hte database throw its id.
     *
     * @param id The product id in the database
     * @return Product information or null on erro
     */
    public static @Nullable Product getProduct(Integer id) throws SQLException {
        Database database = Database.getInstance();
        PreparedStatement statement = database.getConnection()
                .prepareStatement("SELECT id, name, brand, package_size, price, image, availability, " +
                        "characteristics, section FROM products WHERE id = ?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getInt(7),
                    resultSet.getString(8), resultSet.getString(9));
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

    public String getSection() {
        return section;
    }

    public Integer getId() {
        return id;
    }

    /**
     * Return JSON of product with fields: name, brand, package_size, price, availability, characteristics, section.
     *
     * @return JSON of the product
     */
    public JSONObject toJSON() {
        JSONObject json = new JSONObject()
                .put("id", this.id)
                .put("name", this.name)
                .put("brand", this.brand)
                .put("package_size", this.package_size)
                .put("price", this.price)
                .put("availability", this.availability)
                .put("characteristics", this.characteristics)
                .put("section", this.section);
        if (this.image != null)
            json.put("image", this.image);
        return json;
    }
}

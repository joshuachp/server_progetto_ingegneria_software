package org.example.server.models;

import org.example.server.database.Database;
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
     * @return ArrayList of products, null on error
     */
    public static @Nullable ArrayList<Product> getAll() {
        Database database = Database.getInstance();
        ArrayList<Product> list = new ArrayList<>();
        try {
            Statement statement = database.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id, name, brand, " +
                    "package_size, price, image, availability, characteristics, section FROM products");
            while (resultSet.next()) {
                list.add(new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getInt(7),
                        resultSet.getString(8), resultSet.getString(9)));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
     * @return Return the product, null on error
     */
    public static @Nullable Product createProduct(String name, String brand, Integer package_size, Integer price, String
            image,
                                                  Integer availability, String characteristics, String section) {
        Database database = Database.getInstance();
        if (Section.getSection(name) == null) {
            try {
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
                statement.executeUpdate();
                return Product.getProduct(name, brand, package_size, price, image, availability, characteristics,
                        section);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * Get a product having all the information.
     * NOTE: This isn't unique
     *
     * @param name            Name
     * @param brand           Brand
     * @param package_size    Package size
     * @param price           Price
     * @param image           Image
     * @param availability    Availability
     * @param characteristics Characteristics
     * @param section         Section Id
     * @return The product information, null on error
     */
    public static @Nullable Product getProduct(String name, String brand, Integer package_size, Integer price,
                                               String image,
                                               Integer availability, String characteristics, String section) {
        Database database = Database.getInstance();
        try {
            PreparedStatement statement;
            // Use IS NULL if null
            if (image != null) {
                statement = database.getConnection()
                        .prepareStatement("SELECT id, name, brand, package_size, price, image, availability, " +
                                "characteristics, section FROM products WHERE name = ? AND brand = ? AND " +
                                "package_size " +
                                "= ? AND price = ? AND image = ? AND availability = ? AND characteristics = ? AND" +
                                " " +
                                "section = ?");
                statement.setString(1, name);
                statement.setString(2, brand);
                statement.setInt(3, package_size);
                statement.setInt(4, price);
                statement.setString(5, image);
                statement.setInt(6, availability);
                statement.setString(7, characteristics);
                statement.setString(8, section);
            } else {
                statement = database.getConnection().prepareStatement("SELECT id, name, brand, package_size, " +
                        "price, " +
                        "image, availability, characteristics, section FROM products WHERE name = ? AND brand " +
                        "= ? " +
                        "AND " + "package_size = ? AND price = ? AND image IS NULL AND availability = ? AND " +
                        "characteristics = ? AND section = ?");
                statement.setString(1, name);
                statement.setString(2, brand);
                statement.setInt(3, package_size);
                statement.setInt(4, price);
                statement.setInt(5, availability);
                statement.setString(6, characteristics);
                statement.setString(7, section);
            }
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Product(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6), resultSet.getInt(7),
                        resultSet.getString(8), resultSet.getString(9));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Get a product in hte database throw its id.
     *
     * @param id The product id in the database
     * @return Product information or null on erro
     */
    public static @Nullable Product getProduct(Integer id) {
        Database database = Database.getInstance();
        try {
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

    public String getSectionId() {
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

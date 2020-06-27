package org.example.server.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe per la creazione di un database per i test
 */
public class MockDatabase {

    private MockDatabase() {
    }

    /**
     * Crea il database per i test
     */
    public static void createMockDatabase() {
        Database database = Database.getInstance();
        try {
            // Si connette in un database in memoria (":memory:") non su file
            Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            database.setConnection(connection);
            setUpDatabase(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserisce le informazioni nel database di test
     *
     * @param connection Connessione al database di test
     * @throws SQLException Connessione al database fallita
     */
    private static void setUpDatabase(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        // Create user table
        statement.addBatch("CREATE TABLE users ( " +
                "id INTEGER PRIMARY KEY, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "manager BOOLEAN NOT NULL)");
        // Create manager table
        statement.addBatch("CREATE TABLE managers (" +
                "id INTEGER PRIMARY KEY, " +
                "badge TEXT NOT NULL, " +
                "name TEXT NOT NULL, " +
                "surname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "cap INT NOT NULL, " +
                "city TEXT NOT NULL, " +
                "telephone TEXT NOT NULL, " +
                "role TEXT NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE)");
        // Creates clients table
        statement.addBatch("CREATE TABLE clients (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "surname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "cap INT NOT NULL, " +
                "city TEXT NOT NULL, " +
                "telephone TEXT NOT NULL, " +
                "payment INTEGER DEFAULT 0 NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "loyalty_card_number INTEGER , " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                "FOREIGN KEY(loyalty_card_number) REFERENCES loyalty_cards(card_number) ON DELETE CASCADE ON UPDATE " +
                "CASCADE)");
        // Create loyalty_cards table
        statement.addBatch(" CREATE TABLE loyalty_cards (" +
                "id INTEGER PRIMARY KEY, " +
                "card_number INTEGER UNIQUE NOT NULL, " +
                "emission_date INTEGER NOT NULL, " +
                "points INTEGER NOT NULL)");
        // Create table sections
        statement.addBatch("CREATE TABLE sections (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL UNIQUE)");
        // Create table product
        statement.addBatch("CREATE TABLE products (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "brand TEXT NOT NULL, " +
                "package_size INTEGER NOT NULL, " +
                "price INTEGER NOT NULL, " +
                "image TEXT, " +
                "availability INTEGER NOT NULL DEFAULT 0, " +
                "characteristics TEXT, " +
                "section INTEGER NOT NULL, " +
                "FOREIGN KEY(section) REFERENCES sections(name) ON DELETE CASCADE ON UPDATE CASCADE)");
        // Create section table
        // Add user responsabile admin:password
        statement.addBatch("INSERT INTO users (username, password, manager) " +
                "VALUES('admin', '$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS', 1)");
        // Add user cliente guest:guest
        statement.addBatch("INSERT INTO users (username, password, manager) " +
                "VALUES('guest', '$2y$12$34AOvePv2yzpQN9aN0ixD.DGmVUaBjWOLq5PImEo0wCfD3iB89HwK', 0)");
        // Add manager data for admin user
        statement.addBatch("INSERT INTO managers (badge, name, surname, address, cap, city, telephone, role, user_id)" +
                "VALUES ('D34DB33F', 'Name', 'Surname', 'Via Viale 1', 33333, 'City', '3334445555', 'Admin', 1)");
        // Add loyalty card
        statement.addBatch("INSERT INTO loyalty_cards (card_number, emission_date, points)" +
                "VALUES (1234, 0, 500)");
        // Add client data for guest user
        statement.addBatch("INSERT INTO clients (name, surname, address, cap, city, telephone, payment, user_id, " +
                "loyalty_card_number) VALUES ('Name', 'Surname', 'Via Viale 1', 33333, 'City', '3334445555', 0, 2, " +
                "1234)");
        // Add section
        statement.addBatch("INSERT INTO sections(name) VALUES('Section')");
        // Add product
        statement.addBatch("INSERT INTO products(name, brand, package_size, price, image, availability, " +
                "characteristics, section) VALUES('Product', 'Brand', 1, 1, NULL, 1, 'Characteristics', 'Section')");
        statement.addBatch("INSERT INTO products(name, brand, package_size, price, image, availability, " +
                "characteristics, section) VALUES('Product', 'Brand', 1, 1, NULL, 1, 'Characteristics', 'Section')");
        statement.addBatch("INSERT INTO products(name, brand, package_size, price, image, availability, " +
                "characteristics, section) VALUES('Product', 'Brand', 1, 1, NULL, 1, 'Characteristics', 'Section')");
        // Execute
        statement.executeBatch();
    }
}

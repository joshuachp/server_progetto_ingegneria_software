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
        // Creates clients table
        statement.addBatch("CREATE TABLE clients (" +
                "id INTEGER PRIMARY KEY, " +
                "name TEXT NOT NULL, " +
                "surname TEXT NOT NULL, " +
                "address TEXT NOT NULL, " +
                "cap INT NOT NULL, " +
                "city TEXT NOT NULL, " +
                "telephone TEXT NOT NULL, " +
                "user_id INTEGER NOT NULL, " +
                "FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE)");
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
        // Add user responsabile admin:password
        statement.addBatch("INSERT INTO users (username, password, manager) " +
                "VALUES('admin', '$2b$10$swPp91a8qj40VkcBEn704eIFNOQ1Tvwxc2lZlQppIq/VgyLFLfzpS', 1)");
        // Add user cliente guest:guest
        statement.addBatch("INSERT INTO users (username, password, manager) " +
                "VALUES('guest', '$2y$12$34AOvePv2yzpQN9aN0ixD.DGmVUaBjWOLq5PImEo0wCfD3iB89HwK', 0)");
        // Add client data for guest user
        statement.addBatch("INSERT INTO clients (name, surname, address, cap, city, telephone, user_id)" +
                "VALUES ('Name', 'Surname', 'Via Viale 1', 3333, 'City', '3334445555', 2)");
        // Add manager data for admin user
        statement.addBatch("INSERT INTO managers (badge, name, surname, address, cap, city, telephone, role, user_id)" +
                "VALUES ('D34DB33F', 'Name', 'Surname', 'Via Viale 1', 3333, 'City', '3334445555', 'Admin', 1)");
        statement.executeBatch();
    }
}

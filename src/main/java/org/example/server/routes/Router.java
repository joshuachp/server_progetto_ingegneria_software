package org.example.server.routes;

import org.example.server.database.Database;
import org.example.server.models.*;
import org.example.server.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Server for database interaction
 */
@RestController
public class Router {

    /**
     * Map of the authenticated users. It's in memory since we don't have a lot of
     * users.
     */
    private final Map<String, User> userSessions;

    /**
     * Privare server constructor
     */
    private Router() {
        this.userSessions = new HashMap<>();
        Database.getInstance();
    }

    /**
     * Authenticate a user. Check the password with the hash in the database.
     *
     * @param username String User username
     * @param password String User username
     * @return Return JSONObject of user data
     */
    @PostMapping(value = "/api/user/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public String authUser(@RequestParam String username, @RequestParam String password) {
        User user = User.getUser(username);
        if (user != null && Utils.checkPassword(password, user.getPassword())) {
            String session = Utils.createSession();
            userSessions.put(session, user);
            // Check responsabile or client
            if (user.isManager()) {
                Manager manager = Manager.getManager(user.getId());
                if (manager != null) {
                    return new JSONObject().put("username", user.getUsername()).put("responsabile", user.isManager())
                            .put("session", session).put("badge", manager.getBadge()).put("name", manager.getName())
                            .put("surname", manager.getSurname()).put("address", manager.getAddress())
                            .put("cap", manager.getCap()).put("city", manager.getCity())
                            .put("telephone", manager.getTelephone()).put("role", manager.getRole()).toString();
                }
            } else {
                Client client = Client.getClient(user.getId());
                if (client != null) {
                    return new JSONObject().put("username", user.getUsername()).put("responsabile", user.isManager())
                            .put("session", session).put("name", client.getName()).put("surname", client.getSurname())
                            .put("address", client.getAddress()).put("cap", client.getCap())
                            .put("city", client.getCity()).put("telephone", client.getTelephone())
                            .put("payment", client.getPayment())
                            .put("loyalty_card_number", client.getLoyaltyCardNumber()).toString();
                }
            }
            // Found error in some place above
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Autentica un utente with session token.
     *
     * @param session User session
     * @return Return JSONObject of user data
     */
    @PostMapping(value = "/api/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String autenticateUser(@RequestParam String session) {
        if (userSessions.containsKey(session)) {
            User user = User.getUser(userSessions.get(session).getUsername());
            if (user != null) {
                if (user.isManager()) {
                    Manager manager = Manager.getManager(user.getId());
                    if (manager != null) {
                        return new JSONObject().put("username", user.getUsername())
                                .put("responsabile", user.isManager()).put("session", session)
                                .put("badge", manager.getBadge()).put("name", manager.getName())
                                .put("surname", manager.getSurname()).put("address", manager.getAddress())
                                .put("cap", manager.getCap()).put("city", manager.getCity())
                                .put("telephone", manager.getTelephone()).put("role", manager.getRole()).toString();
                    }
                } else {
                    Client client = Client.getClient(user.getId());
                    if (client != null) {
                        return new JSONObject().put("username", user.getUsername())
                                .put("responsabile", user.isManager()).put("session", session)
                                .put("name", client.getName()).put("surname", client.getSurname())
                                .put("address", client.getAddress()).put("cap", client.getCap())
                                .put("city", client.getCity()).put("telephone", client.getTelephone())
                                .put("payment", client.getPayment())
                                .put("loyalty_card_number", client.getLoyaltyCardNumber()).toString();
                    }
                }
            }
            // Found error in some place above
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * De autenticate a user deleting a user session
     *
     * @param session The session to delete
     * @return True for success
     */
    @PostMapping(value = "/api/user/logout")
    private String logoutUser(@RequestParam String session) {
        if (userSessions.containsKey(session)) {
            userSessions.remove(session);
            return "OK";
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Update user information
     *
     * @param session     The session to delete
     * @param password    Password
     * @param name        Name
     * @param surname     Surname
     * @param address     Address
     * @param cap         CAP
     * @param city        City
     * @param telephone   Telephone
     * @param payment     Payment
     * @param card_number Number of the loyalty card can be null
     * @return "OK" on success
     */
    @PostMapping(value = "/api/client/update")
    private String updateClient(@RequestParam String session, @RequestParam(required = false) String password,
                                @RequestParam String name, @RequestParam String surname, @RequestParam String address,
                                @RequestParam Integer cap, @RequestParam String city, @RequestParam String telephone,
                                @RequestParam Integer payment, @RequestParam(required = false) Integer card_number) {
        if (userSessions.containsKey(session)) {
            // If set update user password
            User user = userSessions.get(session);
            if (password != null) {
                user.setPassword(Utils.hashPassword(password));
                if (!user.updateUser())
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Client client = Client.getClient(user.getId());
            if (client != null) {
                // Set data
                client.setName(name);
                client.setSurname(surname);
                client.setAddress(address);
                client.setCap(cap);
                client.setCity(city);
                client.setTelephone(telephone);
                client.setPayment(payment);
                // If set search for loyalty card
                if (card_number != null) {
                    if (LoyaltyCard.getLoyaltyCard(card_number) == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loyalty card number not found");
                    }
                    client.setLoyaltyCardNumber(card_number);
                }
                // Update client
                if (client.updateClient())
                    return "OK";
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Create a new client user
     *
     * @param username    Username
     * @param password    Password
     * @param name        Name
     * @param surname     Surname
     * @param address     Address
     * @param cap         CAP
     * @param city        City
     * @param telephone   Telephone
     * @param payment     Payment
     * @param card_number Number of the loyalty card can be null
     * @return True on success
     */
    @PostMapping(value = "/api/client/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public String registerClient(@RequestParam String username, @RequestParam String password,
                                 @RequestParam String name, @RequestParam String surname, @RequestParam String address,
                                 @RequestParam Integer cap, @RequestParam String city, @RequestParam String telephone,
                                 @RequestParam Integer payment, @RequestParam(required = false) Integer card_number) {
        User user = User.createUser(username, Utils.hashPassword(password), false);
        String session = Utils.createSession();
        userSessions.put(session, user);
        if (user != null) {
            if (card_number != null && LoyaltyCard.getLoyaltyCard(card_number) == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loyalty card number not found");
            }
            Client client = Client.createClient(username, surname, address, cap, city, telephone, payment, user.getId(),
                    card_number);
            if (client != null)
                return "OK";
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Create a new manager user
     *
     * @param username  Username
     * @param password  Password
     * @param badge     Badge
     * @param name      Name
     * @param surname   Surname
     * @param address   Address
     * @param cap       CAP
     * @param city      City
     * @param telephone Telephone
     * @param role      Role
     * @return Ok on success
     */
    @PostMapping(value = "/api/manager/register")
    public String registerManager(@RequestParam String username, @RequestParam String password,
                                  @RequestParam String badge, @RequestParam String name, @RequestParam String surname,
                                  @RequestParam String address, @RequestParam Integer cap, @RequestParam String city,
                                  @RequestParam String telephone, @RequestParam String role) {
        User user = User.createUser(username, Utils.hashPassword(password), false);
        String session = Utils.createSession();
        userSessions.put(session, user);
        if (user != null) {
            Manager manager = Manager.createManager(badge, username, surname, address, cap, city, telephone, role,
                    user.getId());
            if (manager != null)
                return "OK";
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Update user information
     *
     * @param session   The session to delete
     * @param password  Password
     * @param badge     Badge
     * @param name      Name
     * @param surname   Surname
     * @param address   Address
     * @param cap       CAP
     * @param city      City
     * @param telephone Telephone
     * @param role      Role
     * @return "OK" on success
     */
    @PostMapping(value = "/api/manager/update")
    private String updateManager(@RequestParam String session, @RequestParam(required = false) String password,
                                 @RequestParam String badge, @RequestParam String name, @RequestParam String surname,
                                 @RequestParam String address, @RequestParam Integer cap, @RequestParam String city,
                                 @RequestParam String telephone, @RequestParam String role) {
        if (userSessions.containsKey(session)) {
            // If set update user password
            User user = userSessions.get(session);
            if (password != null) {
                user.setPassword(Utils.hashPassword(password));
                if (!user.updateUser())
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Manager manager = Manager.getManager(user.getId());
            if (manager != null) {
                // Set data
                manager.setBadge(badge);
                manager.setName(name);
                manager.setSurname(surname);
                manager.setAddress(address);
                manager.setCap(cap);
                manager.setCity(city);
                manager.setTelephone(telephone);
                manager.setRole(role);
                // Update manager
                if (manager.updateManager())
                    return "OK";
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Create products from a json array
     *
     * @param body A json string, with a products array
     * @return String ok on success
     */
    @PostMapping(value = "/api/product/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes =
            MediaType.APPLICATION_JSON_VALUE)
    public String createProducts(@RequestBody String body) {
        // Check session
        JSONObject json = new JSONObject(body);
        if (!json.has("session") || !userSessions.containsKey(json.getString("session")))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (json.has("products")) {
            // Cycle throw the products
            JSONArray products = json.getJSONArray("products");
            JSONObject product;
            if (!products.isEmpty()) {
                // Reused variables
                String sectionName;
                Section section;
                for (int i = 0; i < products.length(); i++) {
                    product = products.getJSONObject(i);
                    // Get the section or create it if it doesn't exist
                    sectionName = product.getString("section");
                    try {
                        section = Section.getSection(sectionName);
                        if (section == null) {
                            Section.createSection(sectionName);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    // Create the product, this can create a duplicate product with different id but
                    // we leve the manager to manually control this
                    try {
                        Product.createProduct(product.getString("name"), product.getString("brand"),
                                product.getInt("package_size"), product.getFloat("price"),
                                product.isNull("image") ? null : product.getString("image"),
                                product.getInt("availability"), product.getString("characteristics"), sectionName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
                return "OK";
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    /**
     * Get all products, return a json with an array named products.
     *
     * @param session User session
     * @return Json with products array
     */
    @PostMapping(value = "/api/product/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllProducts(@RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JSONObject json = new JSONObject();
        List<Product> products;
        try {
            products = Product.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        products.forEach(product -> json.append("products", product.toJSON()));
        return json.toString();
    }

    /**
     * Get a product with a specific id, return a json with an array named products.
     *
     * @param id      Product id
     * @param session User session
     * @return Json with products array
     */
    @PostMapping(value = "/api/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProduct(@PathVariable(value = "id") Integer id, @RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Product product;
        try {
            product = Product.getProduct(id);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (product == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return product.toJSON().toString();
    }

    /**
     * Get all sections return a json with an array named sections.
     *
     * @param session User session
     * @return Json with products array
     */
    @PostMapping(value = "/api/section/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllSections(@RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JSONObject json = new JSONObject();
        List<Section> sections;
        try {
            sections = Section.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        sections.forEach(section -> json.append("sections", section.getName()));
        return json.toString();
    }

    /**
     * Get the loyalty card
     *
     * @param card_number Loyalty card
     * @param session     User session
     * @return Loyalty card JSON
     */
    @PostMapping(value = "/api/card/{card_number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLoyaltyCard(@PathVariable(value = "card_number") Integer card_number,
                                 @RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        User user = userSessions.get(session);
        if (user.isManager())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a client");
        Client client = Client.getClient(user.getId());
        if (client != null) {
            if (!client.getLoyaltyCardNumber().equals(card_number))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loyalty card not registered to client");
            LoyaltyCard card = LoyaltyCard.getLoyaltyCard(card_number);
            if (card != null)
                return card.toJSON().toString();
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Create a new order for a client. The request should have a JSON as a body and have a session and products
     * elements. The products is a map of product id and quantity.
     *
     * @param body JSON body
     * @return "OK" on success
     */
    @PostMapping(value = "/api/order/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createOrder(@RequestBody String body) {
        // Checks request body
        JSONObject json = new JSONObject(body);
        if (!(json.has("session") && json.has("products") && json.has("deliveryStart") && json.has("deliveryEnd")))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        String session = json.getString("session");
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        User user = userSessions.get(session);
        if (user.isManager())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        Client client = Client.getClient(user.getId());
        if (client == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

        // Map product id and quantity
        Map<Integer, Integer> map = Utils.convertToIntegerMap(json.getJSONObject("products").toMap());
        // Validate order
        map.keySet().forEach(id -> {
            try {
                Product product = Product.getProduct(id);
                if (product == null || product.getAvailability() < map.get(id))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        });
        try {
            // Create order
            Integer orderId = Order.createOrder(client.getPayment(), new Date(json.getLong("deliveryStart")),
                    new Date(json.getLong("deliveryEnd")), 0, user.getId());
            // Creates product items
            if (OrderItem.batchCreateOrderItems(orderId, map))
                return "OK";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Get all orders for a specific user return a json with an array named orders.
     *
     * @param session User session
     * @return JSON with orders array
     */
    @PostMapping(value = "/api/order/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserOrders(@RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        User user = userSessions.get(session);
        JSONObject json = new JSONObject();
        List<Order> orders;
        try {
            orders = Order.getOrders(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        orders.forEach(order -> json.append("orders", order.toJson()));
        return json.toString();
    }

    /**
     * Get all orders for a specific user return a json with an array named orders.
     *
     * @param session User session
     * @return JSON with orders array
     */
    @PostMapping(value = "/api/order/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllOrders(@RequestParam String session) {
        if (!userSessions.containsKey(session) || !userSessions.get(session).isManager())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JSONObject json = new JSONObject();
        List<Order> orders;
        try {
            orders = Order.getAllOrders();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        orders.forEach(order -> json.append("orders", order.toJson()));
        return json.toString();
    }

    /**
     * Get all orders items for a specific order return a json with an array named orderItems.
     *
     * @param orderId Order id
     * @param session User session
     * @return JSON with orders array
     */
    @PostMapping(value = "/api/order-item/all/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getOrderItems(@PathVariable(value = "orderId") Integer orderId, @RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        User user = userSessions.get(session);
        Order order;
        try {
            order = Order.getOrder(orderId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (order == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (!user.isManager() && !order.getUserId().equals(user.getId()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JSONObject json = new JSONObject();
        List<OrderItem> orderItems;
        try {
            orderItems = OrderItem.getOrderItems(orderId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        orderItems.forEach(item -> json.append("orderItems", item.toJson()));
        return json.toString();
    }

    /**
     * Update an order state
     *
     * @param orderId  order id
     * @param session  User session
     * @param newState New state
     * @return OK on success
     */
    @PostMapping(value = "/api/order/{orderId}/update")
    public String updateOrderState(@PathVariable(value = "orderId") Integer orderId, @RequestParam String session,
                                   @RequestParam Integer newState) {
        if (!userSessions.containsKey(session) || !userSessions.get(session).isManager())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        try {
            if (Order.getOrder(orderId) == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            if (!Order.updateOrderState(orderId, newState))
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return "OK";
    }

}

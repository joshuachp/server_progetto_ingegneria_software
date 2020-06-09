package org.example.server.routes;


import org.example.server.database.Database;
import org.example.server.models.*;
import org.example.server.utils.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Server for database interaction
 */
@RestController
public class Router {

    /**
     * Map of the authenticated users. It's in memory since we don't have a lot of users.
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
     * Autentica un utente. Controlla la password con l'hash della password nel
     * database, se coincidono ritorna una token sessione utente.
     *
     * @param username String User username
     * @param password String User username
     * @return Return JSONObject of user data
     */
    @PostMapping(value = "/api/user/authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    public String autenticateUser(@RequestParam String username,
                                  @RequestParam String password) {
        User user = User.getUser(username);
        if (user != null && Utils.checkPassword(password, user.getPassword())) {
            String session = Utils.createSession();
            userSessions.put(session, user);
            // Check responsabile or client
            if (user.getManager()) {
                Manager manager = Manager.getManager(user.getId());
                if (manager != null) {
                    return new JSONObject()
                            .put("username", user.getUsername())
                            .put("responsabile", user.getManager())
                            .put("session", session)
                            .put("badge", manager.getBadge())
                            .put("name", manager.getName())
                            .put("surname", manager.getSurname())
                            .put("address", manager.getAddress())
                            .put("cap", manager.getCap())
                            .put("city", manager.getCity())
                            .put("telephone", manager.getTelephone())
                            .put("role", manager.getRole())
                            .toString();
                }
            } else {
                Client client = Client.getClient(user.getId());
                if (client != null) {
                    return new JSONObject()
                            .put("username", user.getUsername())
                            .put("responsabile", user.getManager())
                            .put("session", session)
                            .put("name", client.getName())
                            .put("surname", client.getSurname())
                            .put("address", client.getAddress())
                            .put("cap", client.getCap())
                            .put("city", client.getCity())
                            .put("telephone", client.getTelephone())
                            .toString();
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Autentica un utente with session token.
     * TODO: session
     *
     * @param session User session
     * @return Return JSONObject of user data
     */
    @PostMapping(value = "/api/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public String autenticateUser(@RequestParam String session) {
        if (userSessions.containsKey(session)) {
            User user = User.getUser(userSessions.get(session).getUsername());
            if (user != null) {
                if (user.getManager()) {
                    Manager manager = Manager.getManager(user.getId());
                    if (manager != null) {
                        return new JSONObject()
                                .put("username", user.getUsername())
                                .put("responsabile", user.getManager())
                                .put("session", session)
                                .put("badge", manager.getBadge())
                                .put("name", manager.getName())
                                .put("surname", manager.getSurname())
                                .put("address", manager.getAddress())
                                .put("cap", manager.getCap())
                                .put("city", manager.getCity())
                                .put("telephone", manager.getTelephone())
                                .put("role", manager.getRole())
                                .toString();
                    }
                } else {
                    Client client = Client.getClient(user.getId());
                    if (client != null) {
                        return new JSONObject()
                                .put("username", user.getUsername())
                                .put("responsabile", user.getManager())
                                .put("session", session)
                                .put("name", client.getName())
                                .put("surname", client.getSurname())
                                .put("address", client.getAddress())
                                .put("cap", client.getCap())
                                .put("city", client.getCity())
                                .put("telephone", client.getTelephone())
                                .toString();
                    }
                }
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    /**
     * De autenticate a user deleting a user session
     *
     * @param session The session to delete
     * @return True for success
     */
    @PostMapping(value = "/api/user/de-authenticate", produces = MediaType.APPLICATION_JSON_VALUE)
    private boolean deAuthenticateUser(@RequestParam String session) {
        if (userSessions.containsKey(session)) {
            userSessions.remove(session);
            return true;
        }
        return false;
    }


    /**
     * Create a new client user
     * TODO: Tessera
     *
     * @param username  Username
     * @param password  Password
     * @param name      Name
     * @param surname   Surname
     * @param address   Address
     * @param cap       CAP
     * @param city      City
     * @param telephone Telephone
     * @param payment   Payment
     * @return True on success
     */
    @PostMapping(value = "/api/client/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean registerClient(@RequestParam String username, @RequestParam String password,
                                  @RequestParam String name, @RequestParam String surname,
                                  @RequestParam String address, @RequestParam Integer cap, @RequestParam String city,
                                  @RequestParam String telephone, @RequestParam Integer payment) {
        User user = User.createUser(username, Utils.hashPassword(password), false);
        String session = Utils.createSession();
        // TODO: Decide what to do for already authenticated user
        userSessions.put(session, user);
        if (user != null) {
            Client client = Client.createClient(username, surname, address, cap, city, telephone, payment,
                    user.getId());
            return client != null;
        }
        return false;
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
     * @return True on success
     */
    @PostMapping(value = "/api/manager/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean registerManager(@RequestParam String username, @RequestParam String password,
                                   @RequestParam String badge, @RequestParam String name, @RequestParam String surname
            , @RequestParam String address, @RequestParam Integer cap, @RequestParam String city,
                                   @RequestParam String telephone, @RequestParam String role) {
        User user = User.createUser(username, Utils.hashPassword(password), false);
        String session = Utils.createSession();
        userSessions.put(session, user);
        if (user != null) {
            Manager manager = Manager.createManager(badge, username, surname, address, cap, city, telephone, role,
                    user.getId());
            return manager != null;
        }
        return false;
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
                for (int i = 0; i < products.length(); i++) {
                    product = products.getJSONObject(i);
                    // Get the section or create it if it doesn't exist
                    String sectionName = product.getString("section");
                    Section section = Section.getSection(sectionName);
                    if (section == null) {
                        section = Section.createSection(sectionName);
                        if (section == null) {
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        }
                    }
                    // Create the product
                    // TODO: Should fail if product already exists?
                    if (Product.createProduct(product.getString("name"), product.getString("brand"),
                            product.getInt("package_size"), product.getInt("price"),
                            product.isNull("image") ? null : product.getString("image"),
                            product.getInt("availability"), product.getString("characteristics"),
                            sectionName) == null)
                        System.out.println("Error project not created");
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
        List<Product> products = Product.getAll();
        if (products == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        products.forEach(product -> json.accumulate("products", product.toJSON()));
        return json.toString();
    }

    /**
     * Get all sections return a json with an array named sections.
     * TODO
     *
     * @param session User session
     * @return Json with products array
     */
    @PostMapping(value = "/api/section/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllSections(@RequestParam String session) {
        if (!userSessions.containsKey(session))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        JSONObject json = new JSONObject();
        List<Product> products = Product.getAll();
        if (products == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        products.forEach(product -> json.accumulate("products", product.toJSON()));
        return json.toString();
    }

}

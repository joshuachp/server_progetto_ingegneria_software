package org.example.server.routes;


import org.example.server.database.Database;
import org.example.server.models.Client;
import org.example.server.models.Manager;
import org.example.server.models.User;
import org.example.server.utils.Utils;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
            // TODO: Decide what to do for already authenticated user
            userSessions.put(session, user);
            // Check responsabile or client
            if (user.getManager()) {
                Manager manager = Manager.getManager(user.getId());
                if (manager == null)
                    return null;
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
            Client client = Client.getClient(user.getId());
            if (client == null)
                return null;
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
        return null;
    }

    /**
     * Autentica un utente with session token.
     *
     * @param session User session
     * @return Return JSONObject of user data
     */
    @PostMapping(value = "/api/user/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject autenticateUser(@RequestParam String session) {
        if (userSessions.containsKey(session)) {
            User user = userSessions.get(session);
            return new JSONObject()
                    .put("username", user.getUsername())
                    .put("responsabile", user.getManager())
                    .put("session", session);
        }
        return null;
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
     *
     * @param username  Username
     * @param password  Password
     * @param name      Name
     * @param surname   Surname
     * @param address   Address
     * @param cap       CAP
     * @param city      city
     * @param telephone telephone
     * @return True on success
     */
    @PostMapping(value = "/api/client/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean registerClient(@RequestParam String username, @RequestParam String password,
                                  @RequestParam String name, @RequestParam String surname,
                                  @RequestParam String address, @RequestParam Integer cap, @RequestParam String city,
                                  @RequestParam Integer payment, @RequestParam String telephone) {
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
}

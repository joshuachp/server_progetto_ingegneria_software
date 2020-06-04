package org.example.server.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.Base64;


/**
 * Utility class to not change the implementation of upper algorithms
 */
public class Utils {

    public static final int BCRYPT_ROUNDS = 12;

    /**
     * Check that the password and the hashed password matches
     *
     * @param password Password to check
     * @param hash     Hash to check
     * @return Match
     */
    public static boolean checkPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash.toCharArray()).verified;
    }

    /**
     * Created the the salted bcrypt hash of the password
     *
     * @param password Password to hash
     * @return Hashed password
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(BCRYPT_ROUNDS, password.toCharArray());
    }

    /**
     * Create a session token
     *
     * @return The session token
     */
    public static String createSession() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[256];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);

    }


}

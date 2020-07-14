package org.example.server.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;


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
    public static boolean checkPassword(@NotNull String password, @NotNull String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash.toCharArray()).verified;
    }

    /**
     * Created the the salted bcrypt hash of the password
     *
     * @param password Password to hash
     * @return Hashed password
     */
    public static @NotNull String hashPassword(@NotNull String password) {
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

    /**
     * Converts a {@link Map}<{@link String},{@link Object}> into a {@link Map}<{@link Integer},{@link Integer}>
     *
     * @param map Starting map
     * @return Converted map
     */
    public static Map<Integer, Integer> convertToIntegerMap(@NotNull Map<String, Object> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> Integer.valueOf(entry.getKey()),
                        entry -> (Integer) entry.getValue()
                ));
    }
}

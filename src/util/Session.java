package util;

import model.User;

/**
 * Session — Holder singleton untuk user yang sedang login.
 * Auto-logout supported via clear().
 */
public class Session {

    private static User currentUser = null;

    public static void set(User user) {
        currentUser = user;
    }

    public static User get() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getRole() {
        return currentUser != null ? currentUser.getRole() : "";
    }
}

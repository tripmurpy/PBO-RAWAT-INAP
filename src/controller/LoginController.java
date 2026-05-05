package controller;

import model.User;
import service.AuthService;
import util.ServiceFactory;
import util.Session;

/**
 * LoginController — Menangani logika autentikasi.
 * UI memanggil controller ini, bukan AuthService langsung.
 */
public class LoginController {

    private AuthService authService;

    public LoginController() {
        this.authService = ServiceFactory.getInstance().getAuthService();
    }

    /**
     * Proses login. Return User jika sukses, throw Exception jika gagal.
     */
    public User login(String username, String password) throws Exception {
        User user = authService.login(username, password);
        Session.set(user);
        return user;
    }

    public void logout() {
        authService.logout();
        Session.clear();
    }
}

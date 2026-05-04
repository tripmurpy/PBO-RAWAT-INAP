package controller;

import model.User;
import service.AuthService;
import util.Validator;

/**
 * AuthController — Menangani aksi login/logout dari UI.
 * Memvalidasi input, lalu meneruskan ke AuthService.
 * 
 * INHERITANCE: Bisa di-extend jika ada role baru di masa depan.
 */
public class AuthController {

    private AuthService authService;

    public AuthController() {
        this.authService = new AuthService();
    }

    /**
     * Proses login dari input UI.
     * @return User jika berhasil
     * @throws Exception jika gagal (pesan error untuk UI)
     */
    public User login(String username, String password) throws Exception {
        // Validasi input
        String error = Validator.validasiLogin(username, password);
        if (error != null) throw new Exception(error);

        return authService.login(username.trim(), password);
    }

    /** Proses logout */
    public void logout() {
        authService.logout();
    }

    /** Cek status login */
    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    /** Ambil user yang login */
    public User getCurrentUser() {
        return authService.getCurrentUser();
    }
}

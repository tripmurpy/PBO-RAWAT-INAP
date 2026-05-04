package service;

import model.User;
import storage.UserDB;

/**
 * AuthService — Logika bisnis autentikasi.
 * Memverifikasi kredensial dan mengelola sesi admin.
 * 
 * ENCAPSULATION: Detail autentikasi tersembunyi dari Controller.
 */
public class AuthService {

    private UserDB userDB;
    private User currentUser; // Sesi user yang login

    public AuthService() {
        this.userDB = new UserDB();
        this.currentUser = null;
    }

    /**
     * Verifikasi login dan simpan sesi.
     * @return User jika berhasil, null jika gagal
     */
    public User login(String username, String password) throws Exception {
        // Inisialisasi akun default jika belum ada
        userDB.inisialisasiDefault();

        User user = userDB.findByUsername(username);
        if (user == null) {
            throw new Exception("Akun tidak ditemukan");
        }
        if (!user.verifikasiPassword(password)) {
            throw new Exception("Password salah");
        }
        this.currentUser = user;
        return user;
    }

    /** Logout — hapus sesi */
    public void logout() {
        this.currentUser = null;
    }

    /** Cek apakah ada user yang sedang login */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /** Mendapatkan user yang sedang login */
    public User getCurrentUser() {
        return currentUser;
    }
}

package service;

import model.User;
import model.repository.IUserRepository;

/**
 * AuthService — Logika bisnis autentikasi.
 * Memverifikasi kredensial dan mengelola sesi admin.
 */
public class AuthService {

    private IUserRepository userRepo;
    private User currentUser; // Sesi user yang login

    public AuthService(IUserRepository userRepo) {
        this.userRepo = userRepo;
        this.currentUser = null;
    }

    /**
     * Verifikasi login dan simpan sesi.
     * @return User jika berhasil, null jika gagal
     */
    public User login(String username, String password) throws Exception {
        // Validasi input
        String error = util.Validator.validasiLogin(username, password);
        if (error != null) throw new Exception(error);

        // Inisialisasi akun default jika belum ada
        userRepo.inisialisasiDefault();

        User user = userRepo.findByUsername(username);
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

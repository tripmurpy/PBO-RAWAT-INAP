package model;

/**
 * User — Entitas data pengguna sistem (Admin).
 * ENCAPSULATION: Password disimpan sebagai hash, bukan plaintext.
 */
public class User {

    public static final String ROLE_ADMIN = "ADMIN";

    private int recordId;
    private String id;
    private String username;
    private String passwordHash;
    private String namaLengkap;
    private String role;

    public User() {
        this.role = ROLE_ADMIN;
    }

    public User(String id, String username, String passwordHash, 
                String namaLengkap, String role) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.namaLengkap = namaLengkap;
        this.role = role;
    }

    public int getRecordId() { return recordId; }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getNamaLengkap() { return namaLengkap; }
    public String getRole() { return role; }

    public void setRecordId(int recordId) { this.recordId = recordId; }
    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    public void setRole(String role) { this.role = role; }

    /**
     * Membuat hash sederhana dari password.
     * J2ME tidak punya MessageDigest standar, gunakan hash custom.
     */
    public static String hashPassword(String password) {
        int hash = 7;
        for (int i = 0; i < password.length(); i++) {
            hash = hash * 31 + password.charAt(i);
        }
        // Konversi ke hex string
        String hex = Integer.toHexString(hash & 0x7FFFFFFF);
        return "H" + hex;
    }

    /**
     * Memverifikasi password terhadap hash yang tersimpan.
     */
    public boolean verifikasiPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    public String toString() {
        return username + " (" + role + ")";
    }
}

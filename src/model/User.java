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
        // Gabungkan password + salt statis + iterasi
        StringBuffer sbSalted = new StringBuffer();
        sbSalted.append("RSI_SALT_v1_").append(password).append("_").append(password.length());
        String salted = sbSalted.toString();
        
        int hash1 = 17;
        int hash2 = 31;
        for (int i = 0; i < salted.length(); i++) {
            char c = salted.charAt(i);
            hash1 = hash1 * 37 + c;
            hash2 = hash2 * 53 + (c ^ (i + 7));
        }
        // Iterasi stretching (1000x)
        for (int round = 0; round < 1000; round++) {
            hash1 = hash1 * 31 ^ hash2;
            hash2 = hash2 * 37 ^ hash1;
        }
        
        StringBuffer res = new StringBuffer();
        res.append("S2$");
        res.append(Integer.toString(hash1, 16));
        res.append(Integer.toString(hash2, 16));
        return res.toString();
    }

    /**
     * Memverifikasi password terhadap hash yang tersimpan.
     */
    public boolean verifikasiPassword(String password) {
        return passwordHash.equals(hashPassword(password));
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(username).append(" (").append(role).append(")");
        return sb.toString();
    }
}

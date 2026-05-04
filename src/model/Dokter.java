package model;

/**
 * Dokter — Entitas data dokter rumah sakit.
 * 
 * ENCAPSULATION: Semua field private, akses via getter/setter.
 */
public class Dokter {

    // ========== FIELD PRIVATE (ENCAPSULATION) ==========
    private int recordId;
    private String id; // ID Dokter (DKT-XXXX)
    private String nama; // Nama lengkap dokter
    private String spesialisasi; // Spesialisasi (Sp.PD, Sp.A, dll)
    private String jadwal; // Jadwal praktik (Senin-Jumat)
    private boolean aktif; // Status aktif/tidak

    // ========== KONSTRUKTOR ==========

    public Dokter() {
        this.aktif = true;
    }

    public Dokter(String id, String nama, String spesialisasi, String jadwal) {
        this.id = id;
        this.nama = nama;
        this.spesialisasi = spesialisasi;
        this.jadwal = jadwal;
        this.aktif = true;
    }

    // ========== GETTER (ENCAPSULATION) ==========

    public int getRecordId() {
        return recordId;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public String getJadwal() {
        return jadwal;
    }

    public boolean isAktif() {
        return aktif;
    }

    // ========== SETTER (ENCAPSULATION) ==========

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    // ========== TO STRING ==========

    public String toString() {
        return nama + " (" + spesialisasi + ")";
    }
}

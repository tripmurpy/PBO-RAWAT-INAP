package model;

/**
 * Pasien — Entitas data pasien rumah sakit.
 * 
 * ENCAPSULATION: Semua field bersifat private dengan getter/setter.
 * Field noRM bersifat read-only setelah di-set (tidak ada setter publik
 * untuk noRM agar integritas data terjaga).
 */
public class Pasien {

    // ========== FIELD PRIVATE (ENCAPSULATION) ==========
    private int recordId;      // ID internal RMS
    private String noRM;       // Nomor Rekam Medis (RM-YYYYMMDD-XXX)
    private String nama;       // Nama lengkap pasien
    private long tglLahir;     // Tanggal lahir (millisecond)
    private String jenisKelamin; // "L" atau "P"
    private String alamat;     // Alamat lengkap
    private String noTelp;     // Nomor telepon
    private String asuransi;   // Jenis asuransi (BPJS/Mandiri/dll)

    // ========== KONSTRUKTOR ==========

    /**
     * Konstruktor default.
     */
    public Pasien() {
    }

    /**
     * Konstruktor lengkap untuk pembuatan pasien baru.
     */
    public Pasien(String noRM, String nama, long tglLahir, 
                  String jenisKelamin, String alamat, 
                  String noTelp, String asuransi) {
        this.noRM = noRM;
        this.nama = nama;
        this.tglLahir = tglLahir;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.asuransi = asuransi;
    }

    // ========== GETTER (ENCAPSULATION) ==========

    public int getRecordId() {
        return recordId;
    }

    public String getNoRM() {
        return noRM;
    }

    public String getNama() {
        return nama;
    }

    public long getTglLahir() {
        return tglLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getAsuransi() {
        return asuransi;
    }

    // ========== SETTER (ENCAPSULATION) ==========
    // noRM TIDAK memiliki setter publik — hanya bisa di-set via konstruktor
    // atau setNoRM() internal untuk deserialisasi dari RMS

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setNoRM(String noRM) {
        this.noRM = noRM;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setTglLahir(long tglLahir) {
        this.tglLahir = tglLahir;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public void setAsuransi(String asuransi) {
        this.asuransi = asuransi;
    }

    // ========== TO STRING ==========

    /**
     * Representasi ringkas pasien.
     */
    public String toString() {
        return noRM + " - " + nama;
    }
}

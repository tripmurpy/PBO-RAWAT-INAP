package model;

/**
 * Pasien — Entitas data pasien rumah sakit.
 *
 * INHERITANCE: Extends Person (nama, tglLahir, jenisKelamin via Entity.id).
 * ENCAPSULATION: Field private, akses via getter/setter.
 * POLYMORPHISM: Override tampilkan() menampilkan status rawat.
 */
public class Pasien extends Person {

    public static final String STATUS_AKTIF = "AKTIF";
    public static final String STATUS_DIRAWAT = "DIRAWAT";
    public static final String STATUS_PULANG = "PULANG";

    private int recordId;
    private String alamat;
    private String noTelp;
    private String asuransi;
    private String status;

    public Pasien() {
        this.status = STATUS_AKTIF;
    }

    public Pasien(String noRM, String nama, long tglLahir,
            String jenisKelamin, String alamat,
            String noTelp, String asuransi) {
        super(noRM, nama, tglLahir, jenisKelamin);
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.asuransi = asuransi;
        this.status = STATUS_AKTIF;
    }

    // noRM is the id field from Entity
    public String getNoRM() {
        return getId();
    }

    public void setNoRM(String noRM) {
        setId(noRM);
    }

    public int getRecordId() {
        return recordId;
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

    public String getStatus() {
        return status;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAktif() {
        return STATUS_AKTIF.equals(status);
    }

    public boolean isDirawat() {
        return STATUS_DIRAWAT.equals(status);
    }

    /** POLYMORPHISM: Override tampilkan() dengan status. */
    public String tampilkan() {
        StringBuffer sb = new StringBuffer();
        sb.append(nama).append(" [").append(status).append("]");
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getNoRM()).append(" - ").append(nama);
        return sb.toString();
    }
}

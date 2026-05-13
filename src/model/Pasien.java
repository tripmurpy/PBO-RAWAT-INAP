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
    private String dokterPenanggungJawab;
    private String kamarRawat;
    private String keluhan;
    private String namaWali;
    private String noTelpWali;

    public Pasien() {
        this.status = STATUS_AKTIF;
    }

    public Pasien(String noRM, String nama, long tglLahir,
            String jenisKelamin, String alamat,
            String noTelp, String asuransi, String namaWali, String noTelpWali) {
        super(noRM, nama, tglLahir, jenisKelamin);
        this.alamat = alamat;
        this.noTelp = noTelp;
        this.asuransi = asuransi;
        this.namaWali = namaWali;
        this.noTelpWali = noTelpWali;
        this.status = STATUS_AKTIF;
        this.dokterPenanggungJawab = "";
        this.kamarRawat = "";
        this.keluhan = "";
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

    public String getDokterPenanggungJawab() {
        return dokterPenanggungJawab;
    }

    public void setDokterPenanggungJawab(String dokterPenanggungJawab) {
        this.dokterPenanggungJawab = dokterPenanggungJawab;
    }

    public String getKamarRawat() {
        return kamarRawat;
    }

    public void setKamarRawat(String kamarRawat) {
        this.kamarRawat = kamarRawat;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public String getNamaWali() {
        return namaWali;
    }

    public void setNamaWali(String namaWali) {
        this.namaWali = namaWali;
    }

    public String getNoTelpWali() {
        return noTelpWali;
    }

    public void setNoTelpWali(String noTelpWali) {
        this.noTelpWali = noTelpWali;
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

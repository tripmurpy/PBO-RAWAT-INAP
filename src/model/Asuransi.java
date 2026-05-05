package model;

/**
 * Asuransi — Entitas data asuransi pasien.
 * 
 * ENCAPSULATION: Field private, getter/setter.
 */
public class Asuransi {

    // ========== KONSTANTA TIPE KLAIM ==========
    public static final String KLAIM_BPJS = "BPJS";
    public static final String KLAIM_MANDIRI = "Mandiri";
    public static final String KLAIM_SWASTA = "Swasta";

    // ========== FIELD PRIVATE (ENCAPSULATION) ==========
    private String id;
    private String namaAsuransi; // Nama perusahaan asuransi
    private String tipeKlaim; // BPJS / Mandiri / Swasta

    // ========== KONSTRUKTOR ==========

    public Asuransi() {
    }

    public Asuransi(String id, String namaAsuransi, String tipeKlaim) {
        this.id = id;
        this.namaAsuransi = namaAsuransi;
        this.tipeKlaim = tipeKlaim;
    }

    // ========== GETTER (ENCAPSULATION) ==========

    public String getId() {
        return id;
    }

    public String getNamaAsuransi() {
        return namaAsuransi;
    }

    public String getTipeKlaim() {
        return tipeKlaim;
    }

    // ========== SETTER (ENCAPSULATION) ==========

    public void setId(String id) {
        this.id = id;
    }

    public void setNamaAsuransi(String namaAsuransi) {
        this.namaAsuransi = namaAsuransi;
    }

    public void setTipeKlaim(String tipeKlaim) {
        this.tipeKlaim = tipeKlaim;
    }

    // ========== TO STRING ==========

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(namaAsuransi).append(" (").append(tipeKlaim).append(")");
        return sb.toString();
    }
}

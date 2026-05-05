package model;

/**
 * Dokter — Entitas data dokter rumah sakit.
 *
 * INHERITANCE: Extends Person (nama, tglLahir, jenisKelamin via Entity.id).
 * ENCAPSULATION: Field private, akses via getter/setter.
 * POLYMORPHISM: Override tampilkan() menampilkan spesialisasi.
 */
public class Dokter extends Person {

    private int recordId;
    private String spesialisasi;
    private String jadwal;
    private boolean aktif;

    public Dokter() {
        this.aktif = true;
    }

    public Dokter(String id, String nama, String spesialisasi, String jadwal) {
        super(id, nama, 0L, "L");
        this.spesialisasi = spesialisasi;
        this.jadwal = jadwal;
        this.aktif = true;
    }

    public int getRecordId() { return recordId; }
    public String getSpesialisasi() { return spesialisasi; }
    public String getJadwal() { return jadwal; }
    public boolean isAktif() { return aktif; }

    public void setRecordId(int recordId) { this.recordId = recordId; }
    public void setSpesialisasi(String spesialisasi) { this.spesialisasi = spesialisasi; }
    public void setJadwal(String jadwal) { this.jadwal = jadwal; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }

    /** POLYMORPHISM: Override tampilkan() dengan spesialisasi. */
    public String tampilkan() {
        StringBuffer sb = new StringBuffer();
        sb.append("dr. ").append(nama).append(" - Sp.").append(spesialisasi);
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(nama).append(" (").append(spesialisasi).append(")");
        return sb.toString();
    }
}

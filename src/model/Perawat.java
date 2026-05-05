package model;

/**
 * Perawat — Entitas data perawat rumah sakit.
 *
 * INHERITANCE: Extends Person.
 * POLYMORPHISM: Override tampilkan() menampilkan shift.
 */
public class Perawat extends Person {

    private int recordId;
    private String shift; // PAGI, SIANG, MALAM
    private boolean aktif;

    public Perawat() {
        this.aktif = true;
        this.shift = "PAGI";
    }

    public Perawat(String id, String nama, long tglLahir,
            String jenisKelamin, String shift) {
        super(id, nama, tglLahir, jenisKelamin);
        this.shift = shift;
        this.aktif = true;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getShift() {
        return shift;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    /** POLYMORPHISM: Override tampilkan() dengan shift. */
    public String tampilkan() {
        StringBuffer sb = new StringBuffer();
        sb.append(nama).append(" [Shift ").append(shift).append("]");
        return sb.toString();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(nama).append(" (Perawat - ").append(shift).append(")");
        return sb.toString();
    }
}

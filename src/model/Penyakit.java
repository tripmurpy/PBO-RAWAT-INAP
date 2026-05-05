package model;

/**
 * Penyakit — Entitas data penyakit dengan kode ICD-10.
 * 
 * ENCAPSULATION: Field private, getter/setter.
 */
public class Penyakit {

    // ========== FIELD PRIVATE (ENCAPSULATION) ==========
    private String kodeICD10; // Kode ICD-10 (A00-Z99)
    private String namaPenyakit; // Nama penyakit

    // ========== KONSTRUKTOR ==========

    public Penyakit() {
    }

    public Penyakit(String kodeICD10, String namaPenyakit) {
        this.kodeICD10 = kodeICD10;
        this.namaPenyakit = namaPenyakit;
    }

    // ========== GETTER (ENCAPSULATION) ==========

    public String getKodeICD10() {
        return kodeICD10;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    // ========== SETTER (ENCAPSULATION) ==========

    public void setKodeICD10(String kodeICD10) {
        this.kodeICD10 = kodeICD10;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    // ========== TO STRING ==========

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(kodeICD10).append(" - ").append(namaPenyakit);
        return sb.toString();
    }
}

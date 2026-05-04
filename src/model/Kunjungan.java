package model;

/**
 * Kunjungan — Entitas riwayat kunjungan/perawatan.
 * ENCAPSULATION: Field private, getter/setter.
 */
public class Kunjungan {

    public static final String STATUS_BERLANGSUNG = "BERLANGSUNG";
    public static final String STATUS_SELESAI = "SELESAI";

    private int recordId;
    private String id;
    private String idAdmisi;
    private String noRMPasien;
    private long tglMasuk;
    private long tglKeluar;
    private String diagnosis;
    private String kodeICD10;
    private String namaDokter;
    private String namaRuangan;
    private String status;
    private int lamaRawat;

    public Kunjungan() {
        this.status = STATUS_BERLANGSUNG;
        this.tglKeluar = 0;
        this.lamaRawat = 0;
    }

    public Kunjungan(String id, String idAdmisi, String noRMPasien,
                     long tglMasuk, String diagnosis, String namaDokter,
                     String namaRuangan) {
        this.id = id;
        this.idAdmisi = idAdmisi;
        this.noRMPasien = noRMPasien;
        this.tglMasuk = tglMasuk;
        this.diagnosis = diagnosis;
        this.namaDokter = namaDokter;
        this.namaRuangan = namaRuangan;
        this.status = STATUS_BERLANGSUNG;
        this.tglKeluar = 0;
        this.kodeICD10 = "";
        this.lamaRawat = 0;
    }

    public int getRecordId() { return recordId; }
    public String getId() { return id; }
    public String getIdAdmisi() { return idAdmisi; }
    public String getNoRMPasien() { return noRMPasien; }
    public long getTglMasuk() { return tglMasuk; }
    public long getTglKeluar() { return tglKeluar; }
    public String getDiagnosis() { return diagnosis; }
    public String getKodeICD10() { return kodeICD10; }
    public String getNamaDokter() { return namaDokter; }
    public String getNamaRuangan() { return namaRuangan; }
    public String getStatus() { return status; }
    public int getLamaRawat() { return lamaRawat; }
    public boolean isBerlangsung() { return STATUS_BERLANGSUNG.equals(status); }

    public void setRecordId(int recordId) { this.recordId = recordId; }
    public void setId(String id) { this.id = id; }
    public void setIdAdmisi(String idAdmisi) { this.idAdmisi = idAdmisi; }
    public void setNoRMPasien(String noRMPasien) { this.noRMPasien = noRMPasien; }
    public void setTglMasuk(long tglMasuk) { this.tglMasuk = tglMasuk; }
    public void setTglKeluar(long tglKeluar) { this.tglKeluar = tglKeluar; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setKodeICD10(String kodeICD10) { this.kodeICD10 = kodeICD10; }
    public void setNamaDokter(String namaDokter) { this.namaDokter = namaDokter; }
    public void setNamaRuangan(String namaRuangan) { this.namaRuangan = namaRuangan; }
    public void setStatus(String status) { this.status = status; }
    public void setLamaRawat(int lamaRawat) { this.lamaRawat = lamaRawat; }

    public String toString() {
        return id + " | " + noRMPasien + " [" + status + "]";
    }
}

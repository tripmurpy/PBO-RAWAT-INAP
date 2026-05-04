package model;

/**
 * Kunjungan — Model untuk menampilkan riwayat kunjungan (read-only view of Admisi).
 */
public class Kunjungan {
    private String idAdmisi;
    private String noRMPasien;
    private long tglMasuk;
    private long tglKeluar;
    private String status;
    private String diagnosis;
    private int lamaRawat;

    public Kunjungan(String idAdmisi, String noRMPasien, long tglMasuk, 
                     long tglKeluar, String status, String diagnosis, int lamaRawat) {
        this.idAdmisi = idAdmisi;
        this.noRMPasien = noRMPasien;
        this.tglMasuk = tglMasuk;
        this.tglKeluar = tglKeluar;
        this.status = status;
        this.diagnosis = diagnosis;
        this.lamaRawat = lamaRawat;
    }

    public String getIdAdmisi() { return idAdmisi; }
    public String getNoRMPasien() { return noRMPasien; }
    public long getTglMasuk() { return tglMasuk; }
    public long getTglKeluar() { return tglKeluar; }
    public String getStatus() { return status; }
    public String getDiagnosis() { return diagnosis; }
    public int getLamaRawat() { return lamaRawat; }
}

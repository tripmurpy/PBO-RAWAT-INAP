package model;

/**
 * Admisi — Entitas data rawat inap (admisi masuk).
 * 
 * Menghubungkan Pasien, Dokter, dan Ruangan dalam satu
 * transaksi rawat inap.
 * 
 * ENCAPSULATION: Field private, getter/setter.
 */
public class Admisi {

    // ========== KONSTANTA STATUS ==========
    public static final String STATUS_AKTIF = "AKTIF";
    public static final String STATUS_SELESAI = "SELESAI";

    // ========== FIELD PRIVATE (ENCAPSULATION) ==========
    private int recordId;
    private String idAdmisi; // ID Admisi (ADM-YYYY-XXXX)
    private String noRMPasien; // No. RM pasien terkait
    private String idDokter; // ID dokter penanggung jawab
    private String idRuangan; // ID ruangan/kamar
    private long tglMasuk; // Tanggal masuk rawat inap (ms)
    private long tglKeluar; // Tanggal keluar (ms), 0 jika belum
    private String diagnosisAwal; // Diagnosis awal saat masuk
    private String diagnosisAkhir; // Diagnosis akhir saat keluar
    private String kodeICD10; // Kode ICD-10 diagnosis
    private String catatan; // Catatan tambahan
    private String status; // AKTIF / SELESAI
    private int biayaTotal; // Total biaya rawat inap

    // ========== KONSTRUKTOR ==========

    public Admisi() {
        this.status = STATUS_AKTIF;
        this.tglKeluar = 0;
        this.diagnosisAkhir = "";
        this.kodeICD10 = "";
        this.catatan = "";
    }

    public Admisi(String idAdmisi, String noRMPasien, String idDokter,
            String idRuangan, long tglMasuk, String diagnosisAwal) {
        this.idAdmisi = idAdmisi;
        this.noRMPasien = noRMPasien;
        this.idDokter = idDokter;
        this.idRuangan = idRuangan;
        this.tglMasuk = tglMasuk;
        this.diagnosisAwal = diagnosisAwal;
        this.status = STATUS_AKTIF;
        this.tglKeluar = 0;
        this.diagnosisAkhir = "";
        this.kodeICD10 = "";
        this.catatan = "";
    }

    // ========== GETTER (ENCAPSULATION) ==========

    public int getRecordId() {
        return recordId;
    }

    public String getIdAdmisi() {
        return idAdmisi;
    }

    public String getNoRMPasien() {
        return noRMPasien;
    }

    public String getIdDokter() {
        return idDokter;
    }

    public String getIdRuangan() {
        return idRuangan;
    }

    public long getTglMasuk() {
        return tglMasuk;
    }

    public long getTglKeluar() {
        return tglKeluar;
    }

    public String getDiagnosisAwal() {
        return diagnosisAwal;
    }

    public String getDiagnosisAkhir() {
        return diagnosisAkhir;
    }

    public String getKodeICD10() {
        return kodeICD10;
    }

    public String getCatatan() {
        return catatan;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Mengecek apakah admisi masih aktif.
     */
    public boolean isAktif() {
        return STATUS_AKTIF.equals(status);
    }

    /**
     * Mengecek apakah admisi sudah selesai.
     */
    public boolean isSelesai() {
        return STATUS_SELESAI.equals(status);
    }

    // ========== SETTER (ENCAPSULATION) ==========

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setIdAdmisi(String idAdmisi) {
        this.idAdmisi = idAdmisi;
    }

    public void setNoRMPasien(String noRMPasien) {
        this.noRMPasien = noRMPasien;
    }

    public void setIdDokter(String idDokter) {
        this.idDokter = idDokter;
    }

    public void setIdRuangan(String idRuangan) {
        this.idRuangan = idRuangan;
    }

    public void setTglMasuk(long tglMasuk) {
        this.tglMasuk = tglMasuk;
    }

    public void setTglKeluar(long tglKeluar) {
        this.tglKeluar = tglKeluar;
    }

    public void setDiagnosisAwal(String diagnosisAwal) {
        this.diagnosisAwal = diagnosisAwal;
    }

    public void setDiagnosisAkhir(String diagnosisAkhir) {
        this.diagnosisAkhir = diagnosisAkhir;
    }

    public void setKodeICD10(String kodeICD10) {
        this.kodeICD10 = kodeICD10;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBiayaTotal() {
        return biayaTotal;
    }

    public void setBiayaTotal(int biayaTotal) {
        this.biayaTotal = biayaTotal;
    }

    // Alias untuk noRMPasien agar kompatibel dengan DB layer
    public String getNoRM() {
        return noRMPasien;
    }

    public void setNoRM(String noRM) {
        this.noRMPasien = noRM;
    }

    // ========== TO STRING ==========

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(idAdmisi);
        sb.append(" [");
        sb.append(status);
        sb.append("]");
        return sb.toString();
    }
}

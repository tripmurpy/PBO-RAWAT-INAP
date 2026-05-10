package model;

/**
 * Kunjungan — Model untuk menampilkan riwayat kunjungan (read-only view of Admisi).
 * Menyimpan data ringkasan termasuk informasi pembayaran.
 */
public class Kunjungan {
    private String idAdmisi;
    private String noRMPasien;
    private long tglMasuk;
    private long tglKeluar;
    private String status;
    private String diagnosis;
    private int lamaRawat;
    private int biayaTotal;
    private int biayaRuangan;
    private int biayaMakanan;
    private int biayaObat;
    private int biayaAdmin;
    private String tipePembayaran;
    private String namaBank;

    public Kunjungan(String idAdmisi, String noRMPasien, long tglMasuk, 
                     long tglKeluar, String status, String diagnosis, int lamaRawat) {
        this.idAdmisi = idAdmisi;
        this.noRMPasien = noRMPasien;
        this.tglMasuk = tglMasuk;
        this.tglKeluar = tglKeluar;
        this.status = status;
        this.diagnosis = diagnosis;
        this.lamaRawat = lamaRawat;
        this.biayaTotal = 0;
        this.biayaRuangan = 0;
        this.biayaMakanan = 0;
        this.biayaObat = 0;
        this.biayaAdmin = 0;
        this.tipePembayaran = "";
        this.namaBank = "";
    }

    public Kunjungan(String idAdmisi, String noRMPasien, long tglMasuk,
                     long tglKeluar, String status, String diagnosis, int lamaRawat,
                     int biayaTotal, int biayaRuangan, int biayaMakanan, int biayaObat, int biayaAdmin,
                     String tipePembayaran, String namaBank) {
        this.idAdmisi = idAdmisi;
        this.noRMPasien = noRMPasien;
        this.tglMasuk = tglMasuk;
        this.tglKeluar = tglKeluar;
        this.status = status;
        this.diagnosis = diagnosis;
        this.lamaRawat = lamaRawat;
        this.biayaTotal = biayaTotal;
        this.biayaRuangan = biayaRuangan;
        this.biayaMakanan = biayaMakanan;
        this.biayaObat = biayaObat;
        this.biayaAdmin = biayaAdmin;
        this.tipePembayaran = tipePembayaran;
        this.namaBank = namaBank;
    }

    public int getBiayaRuangan() { return biayaRuangan; }
    public int getBiayaMakanan() { return biayaMakanan; }
    public int getBiayaObat() { return biayaObat; }
    public int getBiayaAdmin() { return biayaAdmin; }

    public String getIdAdmisi() { return idAdmisi; }
    public String getNoRMPasien() { return noRMPasien; }
    public long getTglMasuk() { return tglMasuk; }
    public long getTglKeluar() { return tglKeluar; }
    public String getStatus() { return status; }
    public String getDiagnosis() { return diagnosis; }
    public int getLamaRawat() { return lamaRawat; }
    public int getBiayaTotal() { return biayaTotal; }
    public String getTipePembayaran() { return tipePembayaran; }
    public String getNamaBank() { return namaBank; }
}

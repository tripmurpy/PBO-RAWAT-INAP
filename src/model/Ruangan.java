package model;

/**
 * Ruangan — Entitas data ruangan/kamar rumah sakit.
 * 
 * ENCAPSULATION: Field private, kontrol akses via getter/setter.
 * Status kamar dikontrol via konstanta statis.
 */
public class Ruangan {

    // ========== KONSTANTA STATUS KAMAR ==========
    public static final String STATUS_KOSONG = "KOSONG";
    public static final String STATUS_TERISI = "TERISI";
    public static final String STATUS_MAINTENANCE = "MAINTENANCE";

    // ========== KONSTANTA TIPE KAMAR ==========
    public static final String TIPE_VIP = "VIP";
    public static final String TIPE_KELAS_1 = "Kelas I";
    public static final String TIPE_KELAS_2 = "Kelas II";
    public static final String TIPE_KELAS_3 = "Kelas III";
    public static final String TIPE_VVIP = "VVIP";

    // ========== FIELD PRIVATE (ENCAPSULATION) ==========
    private int recordId;
    private String id; // ID Ruangan (RNG-XXXX)
    private String namaRuangan; // Nama/nomor kamar (VIP-01, Kls1-02)
    private String tipeKamar; // Tipe (VIP, Kelas I, Kelas II, dll)
    private int kapasitas; // Kapasitas tempat tidur
    private String statusKamar; // KOSONG / TERISI / MAINTENANCE
    private String namaPasien; // Nama pasien jika TERISI (opsional)
    private String noRM; // No. RM pasien jika TERISI
    private double harga; // Harga per malam
    private String fasilitas; // Daftar fasilitas
    private String namaPenanggungJawab; // Nama penanggung jawab jika TERISI

    // ========== KONSTRUKTOR ==========

    public Ruangan() {
        this.statusKamar = STATUS_KOSONG;
        this.namaPasien = "";
        this.noRM = "";
        this.namaPenanggungJawab = "";
        this.fasilitas = "";
    }

    public Ruangan(String id, String namaRuangan, String tipeKamar, int kapasitas) {
        this(id, namaRuangan, tipeKamar, kapasitas, 0, "");
    }

    public Ruangan(String id, String namaRuangan, String tipeKamar, int kapasitas, double harga, String fasilitas) {
        this.id = id;
        this.namaRuangan = namaRuangan;
        this.tipeKamar = tipeKamar;
        this.kapasitas = kapasitas;
        this.harga = harga;
        this.fasilitas = fasilitas;
        this.statusKamar = STATUS_KOSONG;
        this.namaPasien = "";
        this.noRM = "";
        this.namaPenanggungJawab = "";
    }

    // ========== GETTER (ENCAPSULATION) ==========

    public int getRecordId() {
        return recordId;
    }

    public String getId() {
        return id;
    }

    public String getNamaRuangan() {
        return namaRuangan;
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public String getStatusKamar() {
        return statusKamar;
    }

    public String getNamaPasien() {
        return namaPasien;
    }

    public double getHarga() {
        return harga;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public String getNamaPenanggungJawab() {
        return namaPenanggungJawab;
    }

    /**
     * Mengecek apakah kamar sedang kosong.
     */
    public boolean isKosong() {
        return STATUS_KOSONG.equals(statusKamar);
    }

    /**
     * Mengecek apakah kamar sedang terisi.
     */
    public boolean isTerisi() {
        return STATUS_TERISI.equals(statusKamar);
    }

    // ========== SETTER (ENCAPSULATION) ==========

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNamaRuangan(String namaRuangan) {
        this.namaRuangan = namaRuangan;
    }

    public void setTipeKamar(String tipeKamar) {
        this.tipeKamar = tipeKamar;
    }

    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }

    public void setStatusKamar(String statusKamar) {
        this.statusKamar = statusKamar;
    }

    public void setNamaPasien(String namaPasien) {
        this.namaPasien = namaPasien;
    }

    public String getNoRM() {
        return noRM;
    }

    public void setNoRM(String noRM) {
        this.noRM = noRM;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public void setNamaPenanggungJawab(String namaPenanggungJawab) {
        this.namaPenanggungJawab = namaPenanggungJawab;
    }

    public void setKosong(boolean kosong) {
        if (kosong) {
            this.statusKamar = STATUS_KOSONG;
            this.noRM = "";
            this.namaPasien = "";
        } else {
            this.statusKamar = STATUS_TERISI;
        }
    }

    // ========== TO STRING ==========

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(namaRuangan).append(" [").append(statusKamar).append("]");
        return sb.toString();
    }
}

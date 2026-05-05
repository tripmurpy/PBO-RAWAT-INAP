package model;

/**
 * Obat — Entitas data obat/farmasi.
 *
 * ENCAPSULATION: Stok dikendalikan ketat via kurangiStok()/tambahStok().
 */
public class Obat extends Entity {

    public static final String BENTUK_TABLET = "TABLET";
    public static final String BENTUK_SIRUP = "SIRUP";
    public static final String BENTUK_INJEKSI = "INJEKSI";

    private int recordId;
    private String nama;
    private String bentuk;
    private String satuan;
    private int stok;
    private int stokMinimum;
    private int hargaPerUnit;

    public Obat() {
    }

    public Obat(String id, String nama, String bentuk, String satuan,
            int stok, int stokMinimum, int hargaPerUnit) {
        super(id);
        this.nama = nama;
        this.bentuk = bentuk;
        this.satuan = satuan;
        this.stok = stok;
        this.stokMinimum = stokMinimum;
        this.hargaPerUnit = hargaPerUnit;
    }

    public int getRecordId() {
        return recordId;
    }

    public String getNama() {
        return nama;
    }

    public String getBentuk() {
        return bentuk;
    }

    public String getSatuan() {
        return satuan;
    }

    public int getStok() {
        return stok;
    }

    public int getStokMinimum() {
        return stokMinimum;
    }

    public int getHargaPerUnit() {
        return hargaPerUnit;
    }

    public boolean isLowStock() {
        return stok <= stokMinimum;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setBentuk(String bentuk) {
        this.bentuk = bentuk;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void setStokMinimum(int stokMinimum) {
        this.stokMinimum = stokMinimum;
    }

    public void setHargaPerUnit(int hargaPerUnit) {
        this.hargaPerUnit = hargaPerUnit;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(nama).append(" [").append(bentuk).append("] - Stok: ").append(stok);
        return sb.toString();
    }
}

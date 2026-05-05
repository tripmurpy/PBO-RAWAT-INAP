package model;

/**
 * ItemObat — Satu baris obat dalam resep.
 * Disimpan sebagai bagian komposit dari Resep.
 */
public class ItemObat {

    private String obatId;
    private String namaObat;
    private int quantity;
    private String dosis;

    public ItemObat() {}

    public ItemObat(String obatId, String namaObat, int quantity, String dosis) {
        this.obatId = obatId;
        this.namaObat = namaObat;
        this.quantity = quantity;
        this.dosis = dosis;
    }

    public String getObatId() { return obatId; }
    public String getNamaObat() { return namaObat; }
    public int getQuantity() { return quantity; }
    public String getDosis() { return dosis; }

    public void setObatId(String obatId) { this.obatId = obatId; }
    public void setNamaObat(String namaObat) { this.namaObat = namaObat; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(namaObat).append(" x").append(quantity).append(" (").append(dosis).append(")");
        return sb.toString();
    }
}
